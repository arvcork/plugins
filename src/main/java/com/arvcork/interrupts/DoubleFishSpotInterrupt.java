package com.arvcork.interrupts;

import com.arvcork.TemporossActivity;
import com.arvcork.actions.Action;
import com.arvcork.actions.ActionSearchableType;
import com.arvcork.actions.FishingAction;
import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.events.TemporossEvent;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class DoubleFishSpotInterrupt extends BaseInterrupt {
    @Inject
    private Client client;

    private boolean isSpawned;

    @Subscribe
    public void onTemporossEvent(TemporossEvent event)
    {
        if (event.getEventId() == TemporossEvent.DOUBLE_FISHING_SPOT_SPAWNED)
        {
            this.isSpawned = true;
            this.interrupt();
        }

        if (event.getEventId() == TemporossEvent.DOUBLE_FISHING_SPOT_DESPAWNED)
        {
            this.isSpawned = false;
            this.clear();
        }
    }

    @Subscribe
    public void onTemporossActivityChanged(TemporossActivityChanged event)
    {
        if (event.getActivity() == TemporossActivity.FishingDoubleSpot)
        {
            this.clear();
            return;
        }

        if (event.getActivity() == TemporossActivity.Idle && this.isSpawned)
        {
            this.interrupt();
        }
    }

    @Override
    protected InterruptType getInterruptType() {
        return InterruptType.DOUBLE_FISHING_SPOT;
    }

    /**
     * Determine if the interrupt should actually run.
     */
    @Override
    protected boolean shouldInterrupt()
    {
        ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        Action currentAction = this.temporossSequenceManager.getCurrentAction();

        if (currentAction == null)
        {
            return false;
        }

        if (! (currentAction instanceof FishingAction))
        {
            // If the current action does not require fish, do not interrupt the player.
            return false;
        }

        int sourcedItems = itemContainer.count(currentAction.getSearchableId());

        if ((currentAction.getRequiredAmount() - 3) > sourcedItems)
        {
            return false;
        }

        return this.temporossSession.getCurrentActivity() != TemporossActivity.FishingDoubleSpot
                && this.temporossSession.getCurrentActivity() != TemporossActivity.TetheringMast
                && this.temporossSession.getCurrentActivity() != TemporossActivity.StockingCannon;
    }
}
