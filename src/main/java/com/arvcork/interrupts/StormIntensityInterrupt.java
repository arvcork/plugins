package com.arvcork.interrupts;

import com.arvcork.TemporossActivity;
import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.events.TemporossEvent;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

public class StormIntensityInterrupt extends BaseInterrupt {
    @Inject
    private Client client;

    @Override
    protected InterruptType getInterruptType() {
        return InterruptType.StormIntensityWarning;
    }

    @Subscribe
    private void onTemporossEvent(TemporossEvent temporossEvent)
    {
        if (temporossEvent.getEventId() == TemporossEvent.STORM_EXCEEDED_MAXIMUM)
        {
            this.interrupt();
        }

        if (temporossEvent.getEventId() == TemporossEvent.STORM_OK)
        {
            this.clear();
        }
    }

    @Subscribe
    private void onTemporossActivityChanged(TemporossActivityChanged event)
    {
        if (event.getActivity() != TemporossActivity.StockingCannon && this.temporossSession.getCurrentTemporossEvent() == TemporossEvent.STORM_EXCEEDED_MAXIMUM)
        {
            this.interrupt();
        }
    }

    @Override
    protected boolean shouldInterrupt() {
        ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

        if (itemContainer != null)
        {
            int fish = itemContainer.count(ItemID.RAW_HARPOONFISH);
            int cooked = itemContainer.count(ItemID.HARPOONFISH);

            // We only want to show this if there's a point (the player has some fish which can do damage).
            if ((fish > 0 || cooked > 0) && this.temporossSession.getCurrentActivity() != TemporossActivity.StockingCannon)
            {
                return true;
            }
        }

        return this.temporossSession.getCurrentActivity() != TemporossActivity.StockingCannon;
    }
}
