package com.arvcork.events;

import com.arvcork.TemporossActivity;
import lombok.Getter;

public class TemporossActivityChanged {
    @Getter
    private TemporossActivity activity;

    public TemporossActivityChanged(TemporossActivity activity)
    {
        this.activity = activity;
    }
}
