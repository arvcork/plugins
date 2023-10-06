package com.arvcork.actions;

import net.runelite.api.ItemID;

public class CookingAction extends Action {
    public CookingAction(int requiredAmount)
    {
        super(
                "Cook x Harpoonfish",
                ItemID.HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
