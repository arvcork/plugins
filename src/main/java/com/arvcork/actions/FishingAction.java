package com.arvcork.actions;

import net.runelite.api.ItemID;

public class FishingAction extends Action {
    public FishingAction(int requiredAmount)
    {
        super(
                "Catch %s Raw Harpoon Fish",
                ItemID.RAW_HARPOONFISH,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
