package net.stuxcrystal.airblock.commands.contrib.scheduler.fallback;

import net.stuxcrystal.airblock.commands.contrib.scheduler.Task;

import java.util.concurrent.Future;

/**
 * The task.
 */
public class BasicTask implements Task {

    private final Future task;

    public BasicTask(Future task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel(true);
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public boolean isCompleted() {
        return this.task.isDone();
    }
}
