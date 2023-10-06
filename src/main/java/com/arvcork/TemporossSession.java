package com.arvcork;

import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.utils.NpcUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Slf4j
@Singleton
public class TemporossSession {
    private static final int[] TEMPOROSS_AMMUNITION_CRATES = {
            NpcID.AMMUNITION_CRATE, NpcID.AMMUNITION_CRATE_10577, NpcID.AMMUNITION_CRATE_10578, NpcID.AMMUNITION_CRATE_10579
    };

    @Inject
    private Client client;

    @Inject
    private EventBus eventBus;

    @Getter
    private TemporossActivity currentActivity = TemporossActivity.Idle;

    private TemporossActivity previousActivity = TemporossActivity.Idle;

    /**
     * Parse the chat message to determine what action the player is performing.
     */
    @Subscribe
    private void onChatMessage(ChatMessage chatMessage)
    {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
        {
            return;
        }

        String message = Text.standardize(chatMessage.getMessage());

        log.debug("This is the chat message: " + message);
        log.debug("This is the tether message: " + TemporossMessage.TETHER);
        log.debug("This is the result: " + message.contains(TemporossMessage.TETHER));

        if (message.contains(TemporossMessage.TETHER))
        {
            this.setCurrentActivity(TemporossActivity.TetheringMast);
        }

        if (message.contains(TemporossMessage.UNTETHER) || message.contains(TemporossMessage.WAVE_FAILED) || message.contains(TemporossMessage.TETHER_SUCCESS))
        {
            this.setCurrentActivity(TemporossActivity.Idle);
        }
    }

    /**
     * Determine the action the player is performing based on the current animation.
     */
    @Subscribe
    private void onAnimationChanged(AnimationChanged event)
    {
        if (client.getLocalPlayer() != event.getActor())
        {
            return;
        }

        if (client.getLocalPlayer().getAnimation() == -1 && this.currentActivity != TemporossActivity.TetheringMast)
        {
            // The player is doing nothing, set it to idle.
            this.setCurrentActivity(TemporossActivity.Idle);

            return;
        }

        switch (client.getLocalPlayer().getAnimation())
        {
            case AnimationID.COOKING_RANGE:
                final LocalPoint shrinePoint = new LocalPoint(7360, 9408); // Location of the north shrine, the one that should be used.
                int distance = shrinePoint.distanceTo(client.getLocalPlayer().getLocalLocation());

                if (distance == 0)
                {
                    this.setCurrentActivity(TemporossActivity.Cooking);
                }

                break;
            case AnimationID.CONSTRUCTION:
            case AnimationID.CONSTRUCTION_IMCANDO:
                this.setCurrentActivity(TemporossActivity.Repairing);
            case AnimationID.LOOKING_INTO:
                if (this.previousActivity != TemporossActivity.TetheringMast)
                {
                    this.setCurrentActivity(TemporossActivity.FillingBuckets);
                }
                break;
            default:
        }
    }

    @Subscribe
    private void onInteractingChanged(InteractingChanged event)
    {
        if (event.getSource() != client.getLocalPlayer())
        {
            return;
        }

        if (event.getTarget() == null)
        {
            this.setCurrentActivity(TemporossActivity.Idle);

            return;
        }

        if (!(event.getTarget() instanceof NPC))
        {
            return;
        }

        NPC npc = (NPC) event.getTarget();

        if (NpcUtils.is(npc, NpcID.FISHING_SPOT_10569))
        {
            this.setCurrentActivity(TemporossActivity.FishingDoubleSpot);
        }

        if (NpcUtils.is(npc, NpcID.FISHING_SPOT_10565))
        {
            this.setCurrentActivity(TemporossActivity.Fishing);
        }

        if (NpcUtils.is(npc, NpcID.FIRE_8643))
        {
            this.setCurrentActivity(TemporossActivity.DousingFire);
        }

        if (NpcUtils.isOneOf(npc, TEMPOROSS_AMMUNITION_CRATES))
        {
            this.setCurrentActivity(TemporossActivity.StockingCannon);
        }
    }

    /**
     * Set the current activity and keep a reference to what the player was previously doing.
     */
    private void setCurrentActivity(TemporossActivity activity)
    {
        this.previousActivity = this.currentActivity;
        this.currentActivity = activity;

        eventBus.post(new TemporossActivityChanged(activity));
    }
}
