package com.arvcork;

import com.arvcork.events.*;
import com.arvcork.interrupts.InterruptType;
import com.arvcork.utils.NpcUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    @Getter
    private InterruptType currentInterrupt;

    @Getter
    private int currentTemporossEvent;

    @Getter
    private int currentlyCookedFish = 0;

    @Getter
    private int currentlyRawFish = 0;

    @Getter
    private int realLoadedFish = 0;

    @Getter
    private int activityLoadedFish = 0;

    private int loadedFishCount = 0;

    private int firedFishCount = 0;

    private TemporossActivity previousActivity = TemporossActivity.Idle;

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event)
    {
        ItemContainer item = this.client.getItemContainer(InventoryID.INVENTORY);

        if (item == null)
        {
            return;
        }

        this.currentlyRawFish = item.count(ItemID.RAW_HARPOONFISH);
        this.currentlyCookedFish = item.count(ItemID.HARPOONFISH);

        if (this.isPerformingActivity(TemporossActivity.StockingCannon))
        {
            this.loadedFishCount += 1;
        }
    }

    /**
     * When tempoross loses energy, that means that the cannon has fired a fish so lets decrement it.
     */
    @Subscribe
    private void onTemporossEnergyDepleted(TemporossEnergyDepleted event)
    {
        if (this.loadedFishCount > 0)
        {
            this.firedFishCount += 1;
        }
    }

    /**
     * Get the current number of fish that is loaded into the cannon.
     */
    public int getCurrentlyLoadedFish()
    {
        return Math.abs(this.loadedFishCount - this.firedFishCount);
    }

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

        if (message.contains(TemporossMessage.WAVE_INCOMING))
        {
            this.updateTemporossEvent(TemporossEvent.WAVE_INCOMING);
        }

        if (message.contains(TemporossMessage.TETHER))
        {
            this.setCurrentActivity(TemporossActivity.TetheringMast);
        }

        if (message.contains(TemporossMessage.UNTETHER) || message.contains(TemporossMessage.WAVE_FAILED) || message.contains(TemporossMessage.TETHER_SUCCESS))
        {
            this.updateTemporossEvent(TemporossEvent.WAVE_GONE);
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

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
        if (event.getNpc().getId() == NpcID.FISHING_SPOT_10569)
        {
            this.updateTemporossEvent(TemporossEvent.DOUBLE_FISHING_SPOT_SPAWNED);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event)
    {
        if (event.getNpc().getId() == NpcID.FISHING_SPOT_10569)
        {
            this.updateTemporossEvent(TemporossEvent.DOUBLE_FISHING_SPOT_DESPAWNED);
        }
    }

    /**
     * Determine if the players sequence is currently being interrupted by a given type.
     */
    public boolean isInterruptedWith(InterruptType type)
    {
        if (this.currentInterrupt == null)
        {
            return false;
        }

        return this.currentInterrupt == type;
    }

    /**
     * Determine if the player is performing a specific activity.
     */
    public boolean isPerformingActivity(TemporossActivity activity)
    {
        return this.getCurrentActivity() == activity;
    }

    /**
     * Determine if the player was performing an activity.
     */
    public boolean wasPerformingActivity(TemporossActivity activity)
    {
        return this.previousActivity == activity;
    }

    private void updateTemporossEvent(int temporossEvent)
    {
        this.currentTemporossEvent = temporossEvent;
        this.eventBus.post(new TemporossEvent(temporossEvent));
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
