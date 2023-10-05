package com.arvcork;

import com.arvcork.managers.TemporossEquipmentManager;
import com.arvcork.managers.TemporossManager;
import com.arvcork.overlays.TemporossUnkahOverlay;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Tempoross Solo Helper"
)
public class TemporossSoloHelperPlugin extends Plugin
{
    public static final int TEMPOROSS_ESSENCE_WIDGET_ID = 45;

    public static final int TEMPOROSS_STORM_INTENSITY_WIDGET_ID = 55;

    public static final int TEMPOROSS_REGION_ID = 12078;

    public static final int UNKAH_REGION_ID = 12588;

    @Inject
    private Client client;

    @Inject
    private TemporossManager temporossManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private TemporossEquipmentManager temporossEquipmentManager;

    @Inject
    private TemporossUnkahOverlay temporossUnkahOverlay;

    @Inject
    private TemporossSoloHelperConfig config;

    @Inject
    private EventBus eventBus;

    @Provides
    TemporossSoloHelperConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TemporossSoloHelperConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        eventBus.register(temporossManager);
        eventBus.register(temporossEquipmentManager);
    }

    @Override
    protected void shutDown() throws Exception
    {
        eventBus.unregister(temporossManager);
        eventBus.unregister(temporossEquipmentManager);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }

        if (this.isInUnkahRegion())
        {
            overlayManager.add(temporossUnkahOverlay);
        }
    }

    /**
     * Determine if the player is within the Tempoross region.
     */
    public boolean isInTemporossArea()
    {
        return isInRegion(TEMPOROSS_REGION_ID);
    }

    /**
     * Determine if the player is within the Unkah region.
     */
    public boolean isInUnkahRegion()
    {
        return isInRegion(UNKAH_REGION_ID);
    }

    /**
     * Determine if the player is within a specified region.
     */
    private boolean isInRegion(int regionId)
    {
        int currentRegionId = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID();

        return currentRegionId == regionId;
    }
}
