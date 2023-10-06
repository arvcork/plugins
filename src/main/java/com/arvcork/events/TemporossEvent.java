package com.arvcork.events;

import lombok.Getter;

public class TemporossEvent {
    public static final int WAVE_INCOMING = 1;
    public static final int WAVE_GONE = 2;

    public static final int DOUBLE_FISHING_SPOT_SPAWNED = 3;

    public static final int DOUBLE_FISHING_SPOT_DESPAWNED = 4;

    public static final int STORM_EXCEEDED_MAXIMUM = 5;

    public static final int STORM_OK = 6;

    @Getter
    private int eventId;

    public TemporossEvent(int eventId)
    {
        this.eventId = eventId;
    }
}
