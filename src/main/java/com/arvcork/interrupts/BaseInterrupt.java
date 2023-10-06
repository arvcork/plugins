package com.arvcork.interrupts;

import com.arvcork.TemporossSession;
import com.arvcork.events.InterruptSequence;
import com.arvcork.events.ResumeSequence;
import net.runelite.client.eventbus.EventBus;

import javax.inject.Inject;

public abstract class BaseInterrupt {
    @Inject
    private EventBus eventBus;

    @Inject
    protected TemporossSession temporossSession;

    protected void interrupt()
    {
        if (this.shouldInterrupt())
        {
            // TODO: Check if the notifications are enabled, if so then fire off the to the user.
            this.eventBus.post(new InterruptSequence(this.getInterruptType()));
        }
    }

    protected boolean isInterrupted()
    {
        return this.temporossSession.isInterruptedWith(this.getInterruptType());
    }

    protected void clear()
    {
        if (this.isInterrupted())
        {
            this.eventBus.post(new ResumeSequence());
        }
    }

    protected abstract InterruptType getInterruptType();

    protected abstract boolean shouldInterrupt();
}
