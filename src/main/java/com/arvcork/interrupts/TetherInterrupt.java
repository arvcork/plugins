package com.arvcork.interrupts;

import com.arvcork.TemporossActivity;
import com.arvcork.events.TemporossActivityChanged;
import com.arvcork.events.TemporossEvent;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Singleton;

@Singleton
public class TetherInterrupt extends BaseInterrupt {

    @Subscribe
    private void onTemporossEvent(TemporossEvent temporossEvent)
    {
        if (temporossEvent.getEventId() == TemporossEvent.WAVE_INCOMING)
        {
            this.interrupt();
        }

        if (temporossEvent.getEventId() == TemporossEvent.WAVE_GONE)
        {
            this.clear();
        }
    }

    @Subscribe
    private void onTemporossActivityChanged(TemporossActivityChanged event)
    {
        if (event.getActivity() != TemporossActivity.TetheringMast && this.temporossSession.getCurrentTemporossEvent() == TemporossEvent.WAVE_INCOMING)
        {
            this.interrupt();
        }
    }

    @Override
    protected InterruptType getInterruptType() {
        return InterruptType.TETHER_TO_TOTEM_POLE;
    }

    /**
     * Determine if the interrupt should actually run.
     */
    @Override
    protected boolean shouldInterrupt()
    {
        return this.temporossSession.getCurrentActivity() != TemporossActivity.TetheringMast;
    }
}
