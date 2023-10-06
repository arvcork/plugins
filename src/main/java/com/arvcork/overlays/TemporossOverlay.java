package com.arvcork.overlays;

import com.arvcork.TemporossActivity;
import com.arvcork.TemporossSession;
import com.arvcork.actions.Action;
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

        Action currentAction = this.temporossSequenceManager.getCurrentAction();

        if (currentAction == null)
        {
            return panelComponent.render(graphics);
        }

        panelComponent.setPreferredSize(
                new Dimension(graphics.getFontMetrics().stringWidth(OVERLAY_TITLE) + 150, 0)
        );

        if (this.temporossSequenceManager.isInterrupted())
        {
            OverlayUtils.renderErrorMessage(panelComponent, this.temporossSequenceManager.getInterrupt().toString());
        } else {
            boolean isIdle = this.temporossSession.isPerformingActivity(TemporossActivity.Idle);

            panelComponent.getChildren().add(
                    TitleComponent.builder()
                            .text(currentAction.getFormattedStepString())
                            .color(isIdle ? Color.RED : Color.CYAN)
                            .build()
            );
        }

        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Current progress:")
                        .right(currentAction.getProgressString())
                        .rightColor(currentAction.getTitleColor())
                        .build()
        );

//        panelComponent.getChildren().add(
//                LineComponent.builder()
//                        .left("Current raw:")
//                        .right(Integer.toString(this.temporossSession.getCurrentlyRawFish()))
//                        .build()
//        );
//
//        panelComponent.getChildren().add(
//                LineComponent.builder()
//                        .left("Current cooked:")
//                        .right(Integer.toString(this.temporossSession.getCurrentlyCookedFish()))
//                        .build()
//        );
//
//        panelComponent.getChildren().add(
//                LineComponent.builder()
//                        .left("Current loaded this activity:")
//                        .right(Integer.toString(this.temporossSession.getActivityLoadedFish()))
//                        .build()
//        );
//
//        panelComponent.getChildren().add(
//                LineComponent.builder()
//                        .left("Current loaded overall:")
//                        .right(Integer.toString(this.temporossSession.getCurrentlyLoadedFish()))
//                        .build()
//        );
//
//        panelComponent.getChildren().add(
//                LineComponent.builder()
//                        .left("Activity:")
//                        .right(this.temporossSession.getCurrentActivity().toString())
//                        .build()
//        );

        return panelComponent.render(graphics);
    }
}
