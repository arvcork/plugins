package com.arvcork.actions;

import net.runelite.api.InventoryID;

public class ClearInventoryAction extends Action {
    public ClearInventoryAction(int requiredAmount) {
        super(
                "Clear x amount of spaces from your inventory",
                InventoryID.INVENTORY.getId(),
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
