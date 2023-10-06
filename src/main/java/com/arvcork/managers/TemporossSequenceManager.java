package com.arvcork.managers;

import com.arvcork.TemporossActivity;
import com.arvcork.TemporossSession;
import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.actions.*;
import com.arvcork.events.*;
import com.arvcork.interrupts.InterruptType;
import com.arvcork.sequences.TemporossMaxRewardPoints;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Slf4j
@Singleton
public class TemporossSequenceManager {
    @Inject
    private Client client;

    @Inject
    private TemporossSession temporossSession;

    @Inject
    private TemporossStateManager temporossStateManager;

    private int currentActionIndex = 0;

    private Action[] actions = TemporossMaxRewardPoints.Actions;

    @Getter
    private InterruptType interrupt;

    @Getter
    private Action currentAction;

    @Getter
    private int currentProgress = 0;

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            int currentRegionId = WorldPoint.fromLocalInstance(this.client, client.getLocalPlayer().getLocalLocation()).getRegionID();

            if (currentRegionId == TemporossSoloHelperPlugin.TEMPOROSS_REGION_ID)
            {
                this.initialize();
            }
        }
    }

    @Subscribe
    private void onInterruptSequence(InterruptSequence event)
    {
        this.interrupt = event.getType();
    }

    @Subscribe
    private void onResumeSequence(ResumeSequence event)
    {
        this.interrupt = null;
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
    {
        if (this.currentAction instanceof CookingAction || this.currentAction instanceof FishingAction || this.currentAction instanceof PickupAction)
        {
            this.processInventory();
        }

        if (this.currentAction instanceof LoadAction)
        {
            this.processLoadAction();
        }

        this.checkForCompletion();
    }

    @Subscribe
    private void onItemSpawned(ItemSpawned event)
    {
        if (this.getCurrentAction() == null)
        {
            return;
        }

        if (this.getCurrentAction().getLocalPoint() == null)
        {
            return;
        }

        if (this.getCurrentAction().getSearchableType() != ActionSearchableType.Ground)
        {
            return;
        }

        int groundAmount = 0;
        final LocalPoint expectedLocation = this.getCurrentAction().getLocalPoint();
        final LocalPoint droppedLocation = event.getTile().getLocalLocation();

        for (TileItem groundItem : event.getTile().getGroundItems())
        {
            if (groundItem.getId() == this.getCurrentAction().getSearchableId() && expectedLocation.equals(droppedLocation))
            {
                groundAmount += groundItem.getQuantity();
            }
        }

        this.currentAction.setCurrentProgress(groundAmount);

        this.checkForCompletion();
    }

    @Subscribe
    private void onTemporossEssenceDepleted(TemporossEssenceDepleted event)
    {
        if (this.currentAction instanceof AttackAction)
        {
            this.currentAction.setCurrentProgress(
                this.temporossStateManager.getCurrentDamageDealt()
            );
        }

        this.checkForCompletion();
    }

    public boolean isInterruptedWith(InterruptType type)
    {
        return this.interrupt != null && this.interrupt == type;
    }

    /**
     * Initialize the current sequence of actions.
     */
    private void initialize()
    {
        this.actions = Arrays.copyOf(TemporossMaxRewardPoints.Actions, TemporossMaxRewardPoints.Actions.length);
        this.currentActionIndex = 0;
        this.currentAction = this.actions[this.currentActionIndex];
    }

    /**
     * Check if the current action has been completed.
     */
    private void checkForCompletion()
    {
        if (this.currentAction != null && this.currentAction.isCompleted())
        {
            this.advance();
        }
    }

    /**
     * Advance to the next action in the sequence.
     */
    private void advance()
    {
        this.currentActionIndex++;

        if (this.currentActionIndex <= this.actions.length)
        {
            this.currentAction = this.actions[this.currentActionIndex];

            if (this.currentAction instanceof AttackAction)
            {
                this.currentAction.setCurrentProgress(
                        this.temporossStateManager.getCurrentDamageDealt()
                );
            }
        }
    }

    /**
     * Get the number of items in the inventory based off the current action.
     */
    private int getCurrentInventoryQuantity()
    {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

        if (inventory == null)
        {
            return 0;
        }

        return inventory.count(this.currentAction.getSearchableId());
    }

    /**
     * Determine if the player has completed the current action by looking in the inventory.
     */
    private void processInventory()
    {
        this.currentAction.setCurrentProgress(
            this.getCurrentInventoryQuantity()
        );
    }

    /**
     * Get the number of items in the inventory based off the current action.
     */
    private void processLoadAction()
    {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

        if (inventory == null)
        {
            return;
        }

        if (this.temporossSession.isPerformingActivity(TemporossActivity.StockingCannon))
        {
            int currentProgress = this.currentAction.getCurrentProgress();

            this.currentAction.setCurrentProgress(
                currentProgress + 1
            );
        }
    }

    public boolean isInterrupted()
    {
        return this.interrupt != null;
    }
}
