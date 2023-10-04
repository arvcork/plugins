package com.arvcork.managers;

import com.arvcork.TemporossSoloHelperPlugin;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class TemporossManager {
    @Getter
    private int essence;

    @Getter
    private int stormIntensity;

    @Inject
    public Client client;

    @Inject
    public TemporossSoloHelperPlugin plugin;

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (! plugin.isInTemporossArea())
        {
            return;
        }

        essence = parseTemporossStatusValue(TemporossSoloHelperPlugin.TEMPOROSS_ESSENCE_WIDGET_ID);
        stormIntensity = parseTemporossStatusValue(TemporossSoloHelperPlugin.TEMPOROSS_STORM_INTENSITY_WIDGET_ID);
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
