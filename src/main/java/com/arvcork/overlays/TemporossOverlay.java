package com.arvcork.overlays;

import com.arvcork.TemporossSession;
import com.arvcork.interrupts.InterruptType;
import com.arvcork.utils.OverlayUtils;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class TemporossOverlay extends Overlay {
    @Inject
    private TemporossSession temporossSession;

    private final PanelComponent panelComponent = new PanelComponent();

    private static final String OVERLAY_TITLE = "Maximum Reward Points";

    public TemporossOverlay()
    {
        setPosition(OverlayPosition.BOTTOM_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();

        panelComponent.getChildren().add(
                TitleComponent.builder().text(OVERLAY_TITLE).color(Color.CYAN).build()
        );

        panelComponent.setPreferredSize(
                new Dimension(graphics.getFontMetrics().stringWidth(OVERLAY_TITLE) + 150, 0)
        );

        if (this.temporossSession.isInterrupted())
        {
            OverlayUtils.renderErrorMessage(panelComponent, this.temporossSession.getCurrentInterrupt().toString());
        } else {
            OverlayUtils.renderSuccessMessage(panelComponent, "Waiting for a interrupt to happen.");
        }

        return panelComponent.render(graphics);
    }
}
