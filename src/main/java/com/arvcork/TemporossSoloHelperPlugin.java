package com.arvcork;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
        name = "Tempoross Solo Helper"
)
public class TemporossSoloHelperPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private TemporossSoloHelperConfig config;

    @Provides
    TemporossSoloHelperConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TemporossSoloHelperConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("Example started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("Example stopped!");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
            //
        }
    }
}
