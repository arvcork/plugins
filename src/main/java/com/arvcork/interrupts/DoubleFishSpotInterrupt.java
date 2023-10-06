package com.arvcork.interrupts;

import com.arvcork.TemporossActivity;
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
        return InterruptType.DoubleFishingSpot;
    }

    /**
     * Determine if the interrupt should actually run.
     */
    @Override
    protected boolean shouldInterrupt()
    {
        ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

        // If there's less than 3 inventory spaces left, there's no point in interrupting the player.
        if (itemContainer != null && itemContainer.count() > 25)
        {
            return false;
        }

        return this.temporossSession.getCurrentActivity() != TemporossActivity.FishingDoubleSpot
                && this.temporossSession.getCurrentActivity() != TemporossActivity.TetheringMast
                && this.temporossSession.getCurrentActivity() != TemporossActivity.StockingCannon;
    }
}
