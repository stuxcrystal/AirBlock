package net.stuxcrystal.airblock.commands.contrib.threads;

import lombok.NonNull;

/**
 * <p>The manager for threads.</p>
 * <p>
 *     <b>WARNING:</b> Always use Component.ExecutionThread.SAME_THREAD as other threads may
 *     cause loops.
 * </p>
 */
public interface ThreadManager {

    /**
     * Runs the runnable in a new thread.
     * @param runnable Runs the runnable in a separate thread.
     */
    public abstract void runAsynchronously(@NonNull Runnable runnable);

    /**
     * Are we currently in the main thread.
     * @return {@code true} if we are in main.
     */
    public abstract boolean isInMainThread();

}
