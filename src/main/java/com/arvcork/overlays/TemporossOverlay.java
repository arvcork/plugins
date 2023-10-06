package com.arvcork.overlays;

import com.arvcork.TemporossSession;
import com.arvcork.managers.TemporossSequenceManager;
import com.arvcork.utils.OverlayUtils;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

public class TemporossOverlay extends Overlay {
    @Inject
    private TemporossSequenceManager temporossSequenceManager;

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

//        if (this.temporossSequenceManager.getInterrupt() != null)
//        {
//            OverlayUtils.renderErrorMessage(panelComponent, this.temporossSequenceManager.getInterrupt());
//
//            return panelComponent.render(graphics);
//        }

        if (temporossSession.getCurrentActivity() != null)
        {
            OverlayUtils.renderSuccessMessage(panelComponent, temporossSession.getCurrentActivity().toString());
        } else {
            OverlayUtils.renderSuccessMessage(panelComponent, "Waiting for the player to do something.");
        }

        return panelComponent.render(graphics);
    }
}
