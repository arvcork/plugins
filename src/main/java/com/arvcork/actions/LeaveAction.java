package com.arvcork.actions;

import net.runelite.api.NpcID;

public class LeaveAction extends Action {
    public LeaveAction() {
        super(
                "Leave the minigame",
                NpcID.TEMPOROSS,
                1,
                ActionSearchableType.Npc
        );
    }
}
