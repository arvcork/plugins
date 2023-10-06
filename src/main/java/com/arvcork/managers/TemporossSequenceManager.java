package com.arvcork.managers;

import com.arvcork.TemporossActivity;
import com.arvcork.TemporossSession;
import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.actions.*;
import com.arvcork.events.InterruptSequence;
import com.arvcork.events.ResumeSequence;
import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.events.TemporossEnergyDepleted;
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
        log.debug("I AM HERE");
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            int currentRegionId = WorldPoint.fromLocalInstance(this.client, client.getLocalPlayer().getLocalLocation()).getRegionID();

            log.debug("Current region: " + currentRegionId);

            if (currentRegionId == TemporossSoloHelperPlugin.TEMPOROSS_REGION_ID)
            {
                log.debug("Within region.");
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
            if (this.hasCompletedCurrentActionViaInventory())
            {
                this.advance();
            }
        }

        if (this.currentAction instanceof LoadAction)
        {
            if (this.hasCompletedLoadingEvent())
            {
                this.advance();
            }
        }
    }

    @Subscribe
    private void onItemSpawned(ItemSpawned event)
    {
        if (this.hasCompletedActionViaGround(event))
        {
            this.advance();
        }
    }

    @Subscribe
    private void onTemporossEnergyDepleted(TemporossEnergyDepleted event)
    {
        if (this.currentAction instanceof AttackAction)
        {
            if (this.hasCompletedCurrentActionViaDamage())
            {
                this.advance();
            }
        }
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
     * Advance to the next action in the sequence.
     */
    private void advance()
    {
        this.currentProgress = 0;
        this.currentActionIndex++;
        this.currentAction = this.actions[this.currentActionIndex];
    }

    /**
     * Determine if the player has dealt enough damage to Tempoross.
     */
    private boolean hasCompletedCurrentActionViaDamage()
    {
        this.currentProgress = this.getTotalDamageDone();

        return this.getTotalDamageDone() >= this.currentAction.getRequiredAmount();
    }

    /**
     * Get the total damage done to Tempoross.
     */
    private int getTotalDamageDone()
    {
        return 100 - this.temporossStateManager.getEssence();
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
    private boolean hasCompletedCurrentActionViaInventory()
    {
        this.currentProgress = this.getCurrentInventoryQuantity();

        return this.getCurrentInventoryQuantity() == this.currentAction.getRequiredAmount();
    }

    /**
     * Determine if the player has completed the loading event in the cannon.
     */
    private boolean hasCompletedLoadingEvent()
    {
        this.currentProgress = this.temporossSession.getActivityLoadedFish();

        return this.currentProgress == this.currentAction.getRequiredAmount();
    }

    /**
     * Determine if the player has dropped all required items on the expected tile from the current action.
     */
    private boolean hasCompletedActionViaGround(ItemSpawned event)
    {
        if (this.currentAction.getLocalPoint() == null)
        {
            return false;
        }

        int groundAmount = 0;
        final LocalPoint expectedLocation = this.currentAction.getLocalPoint();
        final LocalPoint droppedLocation = event.getTile().getLocalLocation();

        for (TileItem groundItem : event.getTile().getGroundItems())
        {
            if (groundItem.getId() == this.currentAction.getSearchableId() && expectedLocation.equals(droppedLocation))
            {
                groundAmount += groundItem.getQuantity();
            }
        }

        this.currentProgress = groundAmount;

        return groundAmount == this.currentAction.getRequiredAmount();
    }

    public boolean isInterrupted()
    {
        return this.interrupt != null;
    }
}
