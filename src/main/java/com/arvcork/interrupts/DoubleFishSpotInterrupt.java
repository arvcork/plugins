package com.arvcork.interrupts;

import com.arvcork.TemporossSoloHelperPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Slf4j
@Singleton
public class DoubleFishSpotInterrupt {
    @Inject
    private TemporossSoloHelperPlugin plugin;

    @Inject
    private Client client;

    @Inject
    private EventBus eventBus;

    private boolean hasTriggered;

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned)
    {
        // TODO: If this is max xp we always want to trigger.

        if (NpcID.FISHING_SPOT_10569 == npcSpawned.getNpc().getId())
        {
            this.hasTriggered = true;
            eventBus.post(new Interrupt("Fish at the double fishing spot."));
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned)
    {
        if (this.hasTriggered && NpcID.FISHING_SPOT_10569 == npcDespawned.getNpc().getId())
        {
            this.hasTriggered = false;
            eventBus.post(new ClearInterrupt());
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event)
    {
        if (! this.plugin.isInTemporossArea())
        {
            return;
        }

        final Actor target = event.getTarget();

        if (target == null && this.hasTriggered)
        {
            eventBus.post(new Interrupt("Fish at the double fishing spot."));
        }

        if (target == null && ! this.hasTriggered)
        {
            return;
        }

        Player player = this.client.getLocalPlayer();

        if (event.getSource() != player)
        {
            return;
        }

        final NPC npc = (NPC) target;

//        log.debug("The distance away to the fishing spot is: " + event.getTarget().getLocalLocation().distanceTo(client.getLocalPlayer().getLocalLocation()));

        if (npc.getId() == NpcID.FISHING_SPOT_10569 && this.hasTriggered)
        {
            eventBus.post(new ClearInterrupt());
        }
    }
}
