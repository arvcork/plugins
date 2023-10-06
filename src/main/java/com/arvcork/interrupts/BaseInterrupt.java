package com.arvcork.interrupts;

import com.arvcork.TemporossSession;
import com.arvcork.events.InterruptSequence;
import com.arvcork.events.ResumeSequence;
import com.arvcork.managers.TemporossSequenceManager;
import net.runelite.client.Notifier;
import net.runelite.client.eventbus.EventBus;

import javax.inject.Inject;

public abstract class BaseInterrupt {
    @Inject
    private EventBus eventBus;

    @Inject
    private Notifier notifier;

    @Inject
    protected TemporossSession temporossSession;

    @Inject
    protected TemporossSequenceManager temporossSequenceManager;

    protected void interrupt()
    {
        if (this.shouldInterrupt())
        {
            InterruptType interruptType = this.getInterruptType();
            this.notifier.notify(interruptType.toString()); // TODO: Only do this if enabled in the configuration.
            this.eventBus.post(new InterruptSequence(interruptType));
        }
    }

    protected boolean isInterrupted()
    {
        return this.temporossSequenceManager.isInterruptedWith(this.getInterruptType());
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
