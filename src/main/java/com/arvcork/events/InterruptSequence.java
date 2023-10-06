package com.arvcork.events;

import com.arvcork.interrupts.InterruptType;
import lombok.Getter;

public class InterruptSequence {
    @Getter
    private InterruptType type;

    public InterruptSequence(InterruptType type)
    {
        this.type = type;
    }
}
