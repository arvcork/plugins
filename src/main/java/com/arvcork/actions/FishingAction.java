package com.arvcork.actions;

import net.runelite.api.ItemID;

public class FishingAction extends Action {
    public FishingAction(int requiredAmount)
    {
        super(
                "Catch x Raw Harpoon Fish",
                ItemID.RAW_HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
