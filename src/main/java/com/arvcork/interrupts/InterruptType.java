package com.arvcork.interrupts;

public enum InterruptType {
    DOUBLE_FISHING_SPOT("Fish at the double fishing spot."),
    TETHER_TO_TOTEM_POLE("Tether to the nearest totem pole."),
    STORM_INTENSITY_WARNING("Place all ammunition in the ammunition crate.");

    private String message;

    InterruptType(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return this.message;
    }
}
