package com.arvcork.utils;

import net.runelite.api.NPC;

import java.util.Arrays;

public class NpcUtils {
    /**
     * Determine if the npc is the given NPC.
     */
    public static boolean is(NPC npc, int npcId)
    {
        return npc.getId() == npcId;
    }

    /**
     * Determine if the NPC is one of the given.
     */
    public static boolean isOneOf(NPC npc, int[] npcIds)
    {
        return Arrays.stream(npcIds).anyMatch(id -> npc.getId() == id);
    }
}
