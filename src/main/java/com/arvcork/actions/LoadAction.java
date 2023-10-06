package com.arvcork.actions;

import com.arvcork.TemporossSoloHelperPlugin;

public class LoadAction extends Action {
    public LoadAction(int requiredAmount, int ammoCrateId) {
        super(
                "Load %s Harpoonfish into ammunition crate",
                ammoCrateId,
                requiredAmount,
                ActionSearchableType.Inventory
        );
    }
}
