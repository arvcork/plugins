package com.arvcork;

import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.interrupts.*;
import com.arvcork.managers.TemporossEquipmentManager;
import com.arvcork.managers.TemporossStateManager;
import com.arvcork.managers.TemporossSequenceManager;
import com.arvcork.overlays.TemporossOverlay;
import com.arvcork.overlays.TemporossUnkahOverlay;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NpcID;
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
@Singleton
public class TemporossSoloHelperPlugin extends Plugin
{
    public static final int TEMPOROSS_ESSENCE_WIDGET_ID = 45;

    public static final int TEMPOROSS_STORM_INTENSITY_WIDGET_ID = 55;

    public static final int TEMPOROSS_REGION_ID = 12078;

    public static final int UNKAH_REGION_ID = 12588;

    public static final int NORTH_AMMUNITION_CRATE_ID = 40968;

    public static final int SOUTH_AMMUNITION_CRATE_ID = 40969;

    public static final int[] TEMPOROSS_AMMUNITION_CRATES = {
            NpcID.AMMUNITION_CRATE, NpcID.AMMUNITION_CRATE_10577, NpcID.AMMUNITION_CRATE_10578, NpcID.AMMUNITION_CRATE_10579
    };

    private static final Class<?>[] INTERRUPTS = new Class[]{
            DoubleFishSpotInterrupt.class, StormIntensityInterrupt.class, TetherInterrupt.class
    };

    private static final Class<?>[] MANAGERS = new Class[]{
            TemporossEquipmentManager.class, TemporossStateManager.class, TemporossSequenceManager.class,
            TemporossSession.class
    };

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private TemporossUnkahOverlay temporossUnkahOverlay;

    @Inject
    private TemporossOverlay temporossOverlay;

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
        for (Class<?> interrupt : INTERRUPTS)
        {
            log.debug("Initialising interrupt class {}", interrupt);
            eventBus.register(this.injector.getInstance(interrupt));
        }
//
        for (Class<?> manager : MANAGERS)
        {
            log.debug("Initialsing manager classes {}", manager);

            eventBus.register(this.injector.getInstance(manager));
        }
    }

    @Override
    protected void shutDown() throws Exception
    {
        // TODO: Unregister all from the event bus.
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() != GameState.LOGGED_IN)
        {
            overlayManager.remove(temporossOverlay);
            overlayManager.remove(temporossUnkahOverlay);
            return;
        }

        if (event.getGameState() == GameState.LOGGED_IN)
        {
            if (this.isInUnkahRegion())
            {
                overlayManager.remove(temporossOverlay);
                overlayManager.add(temporossUnkahOverlay);
                return;
            }

            if (this.isInTemporossArea())
            {
                overlayManager.add(temporossOverlay);
            }
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
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return false;
        }

        int currentRegionId = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID();

        return currentRegionId == regionId;
    }
}
