package com.arvcork.events;

import lombok.Getter;

public class TemporossEssenceDepleted {
    @Getter
    private int amount;

    public TemporossEssenceDepleted(int amount)
    {
        this.amount = amount;
    }
}
