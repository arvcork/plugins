package com.arvcork.interrupts;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TetherInterrupt {
    private static final String WAVE_INCOMING_MESSAGE = "a colossal wave closes in...";
    private static final String TETHER_MESSAGE = "you securely tether yourself";
    private static final String UNTETHER_MESSAGE = "you untether yourself";
    private static final String TETHER_SUCCESS_MESSAGE = "the rope keeps you securely upright";

    @Inject
    private EventBus eventBus;

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage)
    {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE)
        {
            return;
        }

        String message = Text.standardize(chatMessage.getMessage());

        if (message.contains(WAVE_INCOMING_MESSAGE))
        {
            eventBus.post(new Interrupt("Tether to Totem Pole."));
        }

        if (message.contains(TETHER_MESSAGE) || message.contains(UNTETHER_MESSAGE) || message.contains(TETHER_SUCCESS_MESSAGE))
        {
            eventBus.post(new ClearInterrupt());
        }
    }
}
