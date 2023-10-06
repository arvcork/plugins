package com.arvcork.events;

import lombok.Getter;

public class TemporossEnergyDepleted {
    @Getter
    private int amount;

    public TemporossEnergyDepleted(int amount)
    {
        this.amount = amount;
    }
}
