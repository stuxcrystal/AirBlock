package net.stuxcrystal.airblock.commands.contrib.scheduler;

/**
 * Base class for a task.
 */
public interface Task {

    /**
     * Cancels the task.
     */
    public void cancel();

    /**
     * Checks if the task has been cancelled.
     * @return {@code true} if the task has been completed.
     */
    public boolean isCancelled();

    /**
     * Checks if the task is currently completed.
     * @return {@code true} if the task is completed.
     */
    public boolean isCompleted();

}
