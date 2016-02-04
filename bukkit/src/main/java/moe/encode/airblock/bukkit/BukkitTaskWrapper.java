package moe.encode.airblock.bukkit;

import moe.encode.airblock.commands.contrib.scheduler.Task;
import org.bukkit.scheduler.BukkitTask;

/**
 * The task wrapper.
 */
class BukkitTaskWrapper implements Task {

    public static class BukkitTaskRunner implements Runnable {

        private Runnable runnable;

        private BukkitTaskWrapper task;

        private boolean completed = false;
        private boolean repeat = false;

        public BukkitTaskRunner(Runnable runnable, boolean repeat) {
            this.runnable = runnable;
            this.repeat = repeat;
        }

        void setTask(BukkitTaskWrapper task) {
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

    private BukkitTask task;
    private boolean cancelled = false;
    private boolean completed = false;

    public BukkitTaskWrapper(BukkitTask task) {
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
