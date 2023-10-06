package com.arvcork.managers;

import com.arvcork.TemporossActivity;
import com.arvcork.TemporossSoloHelperPlugin;
import com.arvcork.events.TemporossEvent;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class TemporossStateManager {
    @Getter
    private int essence;

    @Getter
    private int stormIntensity;

    @Inject
    public Client client;

    @Inject
    public TemporossSoloHelperPlugin plugin;

    @Inject
    private EventBus eventBus;

    private boolean hasTriggeredStormEvent = false;

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (! plugin.isInTemporossArea())
        {
            return;
        }

        this.essence = parseTemporossStatusValue(TemporossSoloHelperPlugin.TEMPOROSS_ESSENCE_WIDGET_ID);
        this.stormIntensity = parseTemporossStatusValue(TemporossSoloHelperPlugin.TEMPOROSS_STORM_INTENSITY_WIDGET_ID);

        if (this.stormIntensity > 90 && ! this.hasTriggeredStormEvent)
        {
            this.hasTriggeredStormEvent = true;
            this.eventBus.post(new TemporossEvent(TemporossEvent.STORM_EXCEEDED_MAXIMUM));
        }

        if (this.hasTriggeredStormEvent && this.stormIntensity < 90)
        {
            this.hasTriggeredStormEvent = false;
            this.eventBus.post(new TemporossEvent(TemporossEvent.STORM_OK));
        }
    }

    /**
     * Parse the value from the current tempoross group widget.
     */
    private int parseTemporossStatusValue(int widgetId)
    {
        Widget widget = client.getWidget(WidgetID.TEMPOROSS_GROUP_ID, widgetId);

        if (widget == null || widget.getText().isEmpty())
        {
            return 0;
        }

        Pattern regex = Pattern.compile("\\d+|None");
        Matcher matcher = regex.matcher(widget.getText());

        if (matcher.find()) {
            String match = matcher.group(0);

            if (match.equals("None")) {
                return 0;
            }

            return Integer.parseInt(match);
        }

        return 0;
    }
}
