package com.arvcork.interrupts;

import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.managers.TemporossStateManager;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class StormIntensityInterrupt {
    @Inject
    private TemporossStateManager temporossManager;

    @Inject
    private TemporossSoloHelperPlugin plugin;

    @Inject
    private EventBus eventBus;

    @Inject
    private Client client;

    public boolean hasTriggered;

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (plugin.isInTemporossArea())
        {
            int stormIntensity = this.temporossManager.getStormIntensity();

            if (stormIntensity >= 90 && ! this.hasTriggered)
            {
                this.hasTriggered = true;
                eventBus.post(new Interrupt("Load fish into the Ammunition Crate."));
            }

            if (this.hasTriggered && stormIntensity < 90)
            {
                this.hasTriggered = false;
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (! this.plugin.isInTemporossArea())
        {
            return;
        }

        Player player = this.client.getLocalPlayer();

        if (event.getActor() == player)
        {
            Actor interactingWith = player.getInteracting();

            if (interactingWith instanceof NPC)
            {
                NPC npc = (NPC) interactingWith;

                if (this.hasTriggered && Arrays.binarySearch(TemporossSoloHelperPlugin.TEMPOROSS_AMMUNITION_CRATES, npc.getId()) >= 0)
                {
                    eventBus.post(new ClearInterrupt());
                }
            }
        }
    }
}
