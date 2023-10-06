package com.arvcork.managers;

import com.arvcork.interrupts.ClearInterrupt;
import com.arvcork.interrupts.Interrupt;
import lombok.Getter;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Singleton;

@Singleton
public class TemporossSequenceManager {
    @Getter
    private String interrupt;

    @Subscribe
    public void onInterrupt(Interrupt interrupt)
    {
        this.interrupt = interrupt.getDescription();
    }

    @Subscribe
    public void onClearInterrupt(ClearInterrupt clearInterrupt)
    {
        this.interrupt = null;
    }
}
