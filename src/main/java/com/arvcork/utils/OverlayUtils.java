package com.arvcork.utils;

import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;

public class OverlayUtils {
    /**
     * Render a error message within a panel component.
     */
    public static void renderSuccessMessage(PanelComponent panelComponent, String message)
    {
        panelComponent.getChildren().add(
                TitleComponent.builder().text(message).color(Color.GREEN).build()
        );
    }
    
    /**
     * Render a error message within a panel component.
     */
    public static void renderErrorMessage(PanelComponent panelComponent, String message)
    {
        panelComponent.getChildren().add(
                TitleComponent.builder().text(message).color(Color.RED).build()
        );

    }
}
