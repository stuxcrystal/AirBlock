/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.stuxcrystal.airblock.commands.contrib.scheduler.fallback;

import net.stuxcrystal.airblock.commands.core.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.contrib.scheduler.Scheduler;
import net.stuxcrystal.airblock.commands.contrib.scheduler.Task;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;
import net.stuxcrystal.airblock.commands.core.hooks.HookHandler;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.ShutdownHook;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The fallback-scheduler.
 */
@Components(Scheduler.class)
public class FallbackScheduler {

    /**
     * The service.
     */
    private ScheduledExecutorService service;

    /**
     * The fallback-scheduler.
     * @param environment The environment that will be used.
     */
    public FallbackScheduler(Environment environment) {
        this.service = Executors.newSingleThreadScheduledExecutor();
        environment.getHookManager().registerHooks(this);
    }

    /**
     * Schedules a new task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @param repeat    The repetition period (0 means it won't be repeated)
     * @param async     {@code true} and it runs in it's own thread | {@code false} and it runs in the main thread.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleTask(BackendHandle handle, Runnable runnable, int delay, int repeat, boolean async) {
        if (repeat == 0) {
            return new BasicTask(this.service.schedule(new BasicTaskExecutor(async, handle, runnable), delay, TimeUnit.MILLISECONDS));
        } else {
            return new BasicTask(this.service.scheduleAtFixedRate(new BasicTaskExecutor(async, handle, runnable), delay, repeat, TimeUnit.MICROSECONDS));
        }
    }

    /**
     * Schedules a new synchronous task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleTask(BackendHandle handle, Runnable runnable, int delay){
        return this.scheduleTask(handle, runnable, delay, 0, false);
    }

    /**
     * Schedules a new synchronous task.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleTaskAsynchronously(BackendHandle handle, Runnable runnable, int delay){
        return this.scheduleTask(handle, runnable, delay, 0, true);
    }

    /**
     * Schedules a new repetitive task.
     *
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleRepetitiveTask(BackendHandle handle, Runnable runnable, int delay, int period){
        return this.scheduleTask(handle, runnable, delay, period, false);
    }

    /**
     * Schedules a new asynchronous task.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleRepetitiveAsynchronousTask(BackendHandle handle, Runnable runnable, int delay, int period){
        return this.scheduleTask(handle, runnable, delay, period, true);
    }

    /**
     * Schedules a new repetitive task.
     *
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleRepetitiveTask(BackendHandle handle, Runnable runnable, int period) {
        return this.scheduleTask(handle, runnable, 0, period, false);
    }

    /**
     * Schedules a new asynchronous task.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task scheduleRepetitiveAsynchronousTask(BackendHandle handle, Runnable runnable, int period) {
        return this.scheduleTask(handle, runnable, 0, period, true);
    }

    /**
     * Just runs a task.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task runTask(BackendHandle handle, Runnable runnable) {
        return this.scheduleTask(handle, runnable, 0, 0, false);
    }

    /**
     * Just runs a task in the main thread.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public Task runTaskAsync(BackendHandle handle, Runnable runnable) {
        return this.scheduleTask(handle, runnable, 0, 0, true);
    }

    /**
     * Shutdown the executor when needed.
     * @param hook The hook.
     */
    @HookHandler
    public void onShutdown(ShutdownHook hook) {
        this.service.shutdown();
    }
}
