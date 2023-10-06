package com.arvcork.sequences;

import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.actions.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TemporossMaxRewardPoints {
    @Inject
    private EventBus eventBus;

    public static Action[] Actions = {
            new FishingAction(16),
            new CookingAction(16),
            new LoadAction(16, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),

            new FishingAction(19),
            new CookingAction(19),
            new LoadAction(19, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new AttackAction(40),

            new FishingAction(19),
            new CookingAction(19),
            new LoadAction(19, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new AttackAction(70),

            new FishingAction(19),
            new CookingAction(19),
            new DropAction(19, new LocalPoint(6848, 6464)),
            new FishingAction(19),
            new CookingAction(19),
            new LoadAction(3, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new LoadAction(16, TemporossSoloHelperPlugin.SOUTH_AMMUNITION_CRATE_ID),
            new PickupAction(19, new LocalPoint(6848, 6464)),
            new LoadAction(19, TemporossSoloHelperPlugin.NORTH_AMMUNITION_CRATE_ID),
            new AttackAction(100),

            new LeaveAction()
    };
}
