package com.arvcork.actions;

import net.runelite.api.ItemID;

public class CookingAction extends Action {
    public CookingAction(int requiredAmount)
    {
        super(
                "Cook %s Harpoonfish",
                ItemID.HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
