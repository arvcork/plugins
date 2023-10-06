package com.arvcork.actions;

import com.arvcork.TemporossSoloHelperPlugin;

public class AttackAction extends Action {

    public AttackAction(int requiredAmount) {
        super(
                "Deal damage to Tempoross",
                TemporossSoloHelperPlugin.TEMPOROSS_ESSENCE_WIDGET_ID,
                requiredAmount,
                ActionSearchableType.Widget
        );
    }
}
