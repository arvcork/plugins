package com.arvcork.actions;

import net.runelite.api.ItemID;

public class FillAction extends Action {
    public FillAction(int requiredAmount) {
        super(
                "Fill buckets with water",
                ItemID.BUCKET_OF_WATER,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
