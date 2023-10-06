package com.arvcork.actions;

import lombok.Getter;
import net.runelite.api.coords.LocalPoint;

public class Action {
    @Getter
    private final int searchableId;

    @Getter
    private final int requiredAmount;

    @Getter
    private final String description;

    @Getter
    private ActionSearchableType searchableType;

    @Getter
    private LocalPoint localPoint;

    public Action(String description, int searchableId, int requiredAmount, ActionSearchableType searchableType) {
        this.searchableId = searchableId;
        this.requiredAmount = requiredAmount;
        this.description = description;
        this.searchableType = searchableType;
    }

    public Action(String description, int searchableId, int requiredAmount, ActionSearchableType searchableType, LocalPoint localPoint) {
        this.searchableId = searchableId;
        this.requiredAmount = requiredAmount;
        this.description = description;
        this.localPoint = localPoint;
        this.searchableType = searchableType;
    }
}
