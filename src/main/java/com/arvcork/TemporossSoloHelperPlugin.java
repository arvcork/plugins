package com.arvcork;

import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.interrupts.*;
import com.arvcork.managers.TemporossEquipmentManager;
import com.arvcork.managers.TemporossSequenceManager;
import com.arvcork.managers.TemporossStateManager;
import com.arvcork.overlays.TemporossOverlay;
import com.arvcork.overlays.TemporossUnkahOverlay;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
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
public class TemporossSoloHelperPlugin extends Plugin
{
    public static final int TEMPOROSS_ENERGY_WIDGET_ID = 35;

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
            DoubleFishSpotInterrupt.class, TetherInterrupt.class
    };

    private static final Class<?>[] MANAGERS = new Class[]{
            TemporossEquipmentManager.class, TemporossStateManager.class, TemporossSequenceManager.class, TemporossSession.class
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

    @Getter
    private boolean withinTemporossRegion = false;

    @Getter
    private boolean withinUnkahRegion = false;

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
            var currentRegionId = WorldPoint.fromLocalInstance(this.client, client.getLocalPlayer().getLocalLocation()).getRegionID();

            if (this.isInUnkahRegion(currentRegionId))
            {
                this.withinUnkahRegion = true;
                overlayManager.remove(temporossOverlay);
                overlayManager.add(temporossUnkahOverlay);
                return;
            }

            if (this.isInTemporossArea(currentRegionId))
            {
                this.withinTemporossRegion = true;
                overlayManager.add(temporossOverlay);

                return;
            }

            this.withinUnkahRegion = false;
            this.withinTemporossRegion = false;
        }
    }

    /**
     * Determine if the player is within the Tempoross region.
     */
    public boolean isInTemporossArea(int regionId)
    {
        return regionId == TEMPOROSS_REGION_ID;
    }

    /**
     * Determine if the player is within the Unkah region.
     */
    public boolean isInUnkahRegion(int regionId)
    {
        return regionId == UNKAH_REGION_ID;
    }
}
