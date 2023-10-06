package com.arvcork.actions;

import com.arvcork.events.ActionCompleted;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.eventbus.EventBus;

import javax.inject.Inject;
import java.awt.*;

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

    @Getter
    @Setter
    private int currentProgress = 0;

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

    /**
     * Get the formatted string with the required amount.
     */
    public String getFormattedStepString()
    {
        return String.format(this.description, this.requiredAmount);
    }

    public Color getTitleColor()
    {
        if (this.currentProgress == this.requiredAmount)
        {
            return Color.GREEN;
        }

        if (this.currentProgress >= (this.requiredAmount / 2))
        {
            return Color.YELLOW;
        }

        if (this.currentProgress > (this.requiredAmount / 4))
        {
            return Color.ORANGE;
        }

        return Color.RED;
    }

    /**
     * Determine if the action has been completed.
     */
    public boolean isCompleted()
    {
        return this.getCurrentProgress() == this.getRequiredAmount();
    }

    /**
     * Get the current progress string.
     */
    public String getProgressString()
    {
        return this.getCurrentProgress() + "/" + this.getRequiredAmount();
    }
}
