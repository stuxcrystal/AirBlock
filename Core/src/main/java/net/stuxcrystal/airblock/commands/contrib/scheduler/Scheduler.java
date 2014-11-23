package net.stuxcrystal.airblock.commands.contrib.scheduler;

/**
 * Defines a scheduler instance.
 */
public interface Scheduler {

    /**
     * Schedules a new task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @param repeat    The repetition period (0 means it won't be repeated)
     * @param async     {@code true} and it runs in it's own thread | {@code false} and it runs in the main thread.
     * @return The task.
     */
    public Task scheduleTask(Runnable runnable, int delay, int repeat, boolean async);

    /**
     * Schedules a new synchronous task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    public Task scheduleTask(Runnable runnable, int delay);

    /**
     * Schedules a new synchronous task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    public Task scheduleTaskAsynchronously(Runnable runnable, int delay);

    /**
     * Schedules a new repetitive task.
     *
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    public Task scheduleRepetitiveTask(Runnable runnable, int delay, int period);

    /**
     * Schedules a new asynchronous task.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    public Task scheduleRepetitiveAsynchronousTask(Runnable runnable, int delay, int period);

    /**
     * Schedules a new repetitive task.
     *
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    public Task scheduleRepetitiveTask(Runnable runnable, int period);

    /**
     * Schedules a new asynchronous task.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    public Task scheduleRepetitiveAsynchronousTask(Runnable runnable, int period);

    /**
     * Just runs a task.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    public Task runTask(Runnable runnable);

    /**
     * Just runs a task in the main thread.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    public Task runTaskAsync(Runnable runnable);
}
