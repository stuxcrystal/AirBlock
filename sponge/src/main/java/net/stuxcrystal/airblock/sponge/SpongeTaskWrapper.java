package net.stuxcrystal.airblock.sponge;

import net.stuxcrystal.airblock.commands.contrib.scheduler.Task;

/**
 * The task wrapper.
 */
class SpongeTaskWrapper implements Task {

    public static class SpongeTaskRunner implements Runnable {

        private Runnable runnable;

        private SpongeTaskWrapper task;

        private boolean completed = false;
        private boolean repeat = false;

        public SpongeTaskRunner(Runnable runnable, boolean repeat) {
            this.runnable = runnable;
            this.repeat = repeat;
        }

        void setTask(SpongeTaskWrapper task) {
            this.task = task;
            if (this.completed)
                this.setCompleted();
        }

        private void setCompleted() {
            if (this.repeat)
                return;

            if (this.task != null)
                this.task.completed = true;

            this.completed = true;
        }

        public void run() {
            try {
                this.runnable.run();
            } finally {
                this.setCompleted();
            }

        }

    }

    private org.spongepowered.api.scheduler.Task task;
    private boolean cancelled = false;
    private boolean completed = false;

    public SpongeTaskWrapper(org.spongepowered.api.scheduler.Task task) {
        this.task = task;
    }

    public void cancel() {
        this.task.cancel();
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isCompleted() {
        return this.cancelled || this.completed;
    }
}
