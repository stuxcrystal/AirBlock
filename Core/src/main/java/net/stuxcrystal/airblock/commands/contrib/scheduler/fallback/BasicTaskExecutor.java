package net.stuxcrystal.airblock.commands.contrib.scheduler.fallback;

import net.stuxcrystal.airblock.commands.backend.BackendHandle;

/**
 * Fallback implementation of the task.
 */
public class BasicTaskExecutor implements Runnable {

    /**
     * Is the task executed asynchronously.
     */
    private final boolean async;

    /**
     * The backend.
     */
    private final BackendHandle handle;

    /**
     * Contains the runnable.
     */
    private final Runnable runnable;


    public BasicTaskExecutor(boolean async, BackendHandle handle, Runnable runnable) {
        this.async = async;
        this.handle = handle;
        this.runnable = runnable;
    }


    @Override
    public void run() {
        Runnable rn = new Runnable() {
            @Override
            public void run() {
                BasicTaskExecutor.this.runnable.run();
            }
        };

        if (this.async) {
            this.handle.getEnvironment().getBackend().runAsynchronously(rn);
        } else {
            this.handle.runLater(rn);
        }
    }
}
