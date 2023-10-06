package com.arvcork.interrupts;

import lombok.Getter;

import javax.inject.Singleton;

public class Interrupt {
    @Getter
    private final String description;

    public Interrupt(String description) {
        this.description = description;
    }
}
