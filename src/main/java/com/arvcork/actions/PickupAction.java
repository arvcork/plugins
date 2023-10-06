package com.arvcork.actions;

import net.runelite.api.ItemID;
import net.runelite.api.coords.LocalPoint;

public class PickupAction extends Action {
    public PickupAction(int requiredAmount, LocalPoint dropLocation) {
        super(
                "Pickup %s Harpoonfish",
                ItemID.HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Inventory,
                dropLocation
        );
    }
}
