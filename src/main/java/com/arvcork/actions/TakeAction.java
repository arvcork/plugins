package com.arvcork.actions;

import net.runelite.api.ItemID;

public class TakeAction extends Action {
    public TakeAction(int itemId, int requiredAmount) {
        super(
            "Take x buckets",
            itemId,
            requiredAmount,
            ActionSearchableType.Inventory
        );
    }
}
