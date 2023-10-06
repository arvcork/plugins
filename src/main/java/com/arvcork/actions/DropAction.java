package com.arvcork.actions;

import net.runelite.api.ItemID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public class DropAction extends Action {
    public DropAction(int requiredAmount, LocalPoint dropLocation) {
        super(
                "Drop x Harpoonfish",
                ItemID.HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Ground,
                dropLocation
        );
    }
}
