package com.arvcork;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("temporosssolohelper")
public interface TemporossSoloHelperConfig extends Config {
    @ConfigItem(
            keyName = "showEndWarning",
            name = "Show end game warning",
            description = "Show a warning when the plugin doesn't think you will get enough reward points."
    )
    default boolean endWarning()
    {
        return true;
    }
}
