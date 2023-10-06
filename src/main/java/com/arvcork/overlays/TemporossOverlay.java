package com.arvcork.overlays;

import com.arvcork.TemporossSession;
import com.arvcork.interrupts.InterruptType;
import com.arvcork.managers.TemporossSequenceManager;
import com.arvcork.utils.OverlayUtils;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class TemporossOverlay extends Overlay {
    @Inject
    private TemporossSession temporossSession;

    @Inject
    private TemporossSequenceManager temporossSequenceManager;

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

        if (this.temporossSequenceManager.getCurrentAction() == null)
        {
            OverlayUtils.renderSuccessMessage(panelComponent, "Loading...");

            return panelComponent.render(graphics);
        }

        if (this.temporossSequenceManager.isInterrupted())
        {
            OverlayUtils.renderErrorMessage(panelComponent, this.temporossSequenceManager.getInterrupt().toString());
        } else {
            OverlayUtils.renderSuccessMessage(panelComponent, this.temporossSequenceManager.getCurrentAction().getDescription());
        }

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current raw:")
                        .right(Integer.toString(this.temporossSession.getCurrentlyRawFish()))
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current cooked:")
                        .right(Integer.toString(this.temporossSession.getCurrentlyCookedFish()))
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current loaded this activity:")
                        .right(Integer.toString(this.temporossSession.getActivityLoadedFish()))
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current loaded overall:")
                        .right(Integer.toString(this.temporossSession.getRealLoadedFish()))
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Activity:")
                        .right(this.temporossSession.getCurrentActivity().toString())
                        .build()
        );

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current progress:")
                        .right(this.temporossSequenceManager.getCurrentProgress() + "/" + this.temporossSequenceManager.getCurrentAction().getRequiredAmount())
                        .build()
        );

        return panelComponent.render(graphics);
    }
}
