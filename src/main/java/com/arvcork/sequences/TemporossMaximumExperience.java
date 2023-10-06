package com.arvcork.sequences;

import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.actions.*;
import net.runelite.api.ItemID;

/**
 * Max Experience as described: https://oldschool.runescape.wiki/w/Tempoross/Strategies#Solo_no_cooking_(max_XP)
 */
public class TemporossMaximumExperience {
    public static Action[] Actions = {
            new FishingAction(25),
            new LoadAction(25, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new TakeAction(ItemID.BUCKET, 5),
            new FillAction(5),
            new ClearInventoryAction(27),
            new FishingAction(27),
            new LoadAction(2, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new FishingAction(27),
            new LoadAction(2, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new AttackAction(100), // TODO: This is until he resurfaces again, add an option for that.
            new LoadAction(25, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new FishingAction(25),
            new LoadAction(25, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new TakeAction(ItemID.ROPE, 1),
            new TakeAction(ItemID.HAMMER, 1),
            new AttackAction(100),
            new LeaveAction()
    };
}
