package com.arvcork.overlays;

import com.arvcork.managers.TemporossEquipmentManager;
import com.arvcork.utils.OverlayUtils;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;

public class TemporossUnkahOverlay extends Overlay {
    @Inject
    public TemporossEquipmentManager temporossEquipmentManager;

    private final PanelComponent panelComponent = new PanelComponent();

    private static final String OVERLAY_TITLE = "Tempoross Solo Helper";

    public TemporossUnkahOverlay()
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

        ArrayList<String> errors = new ArrayList<String>();

        if (! this.temporossEquipmentManager.doesPlayerHaveBucketsOfWater()) {
            errors.add("Missing buckets of water.");
        }

        if (! this.temporossEquipmentManager.doesPlayerHaveHammer())
        {
            errors.add("Missing hammer.");
        }

        if (! this.temporossEquipmentManager.doesPlayerHaveRope())
        {
            errors.add("Missing rope.");
        }

        if (! this.temporossEquipmentManager.doesPlayerHaveHarpoon())
        {
            errors.add("Missing harpoon.");
        }

        if (! this.temporossEquipmentManager.doesPlayerHaveFreeSpaces())
        {
            errors.add("You need at least 19 free inventory slots.");
        }

        if (! errors.isEmpty())
        {
            errors.forEach(error -> OverlayUtils.renderErrorMessage(panelComponent, error));

            return panelComponent.render(graphics);
        }

        OverlayUtils.renderSuccessMessage(panelComponent, "You are ready to fight Tempoross.");

        return panelComponent.render(graphics);
    }
}
