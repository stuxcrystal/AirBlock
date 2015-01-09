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

package net.stuxcrystal.airblock.commands.contrib.threads;

import net.stuxcrystal.airblock.commands.core.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;
import net.stuxcrystal.airblock.commands.core.hooks.HookHandler;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.ShutdownHook;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The fallback system for asynchronous threads.
 */
@Components(ThreadManager.class)
public class FallbackThreadSystem {

    /**
     * Contains the main thread.
     */
    private long mainThreadId = -1;

    /**
     * Contains the default executor service.
     */
    private ExecutorService service = null;

    /**
     * The internal lock.
     */
    private final Object lock = new Object();

    /**
     * Initializes the fallback-system for threads.
     */
    public FallbackThreadSystem() {
        this.mainThreadId = Thread.currentThread().getId();
    }

    /**
     * <p>Checks if the invoker of the thread is currently in the main-thread.</p>
     * <p>
     *     Please note that there is a serious overhead when the method is first called since
     *     we need to determine the thread id of the main thread.
     * </p>
     * @return {@code true} if so.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.NONE)
    public boolean isInMainThread(BackendHandle handle) {
        synchronized (this.lock) {
            // Retrieve the ID of the main thread...
            if (this.mainThreadId == -1) {
                try {
                    this.mainThreadId = (Long) handle.callInMainThread(new Callable<Long>() {
                        @Override
                        public Long call() throws Exception {
                            return Thread.currentThread().getId();
                        }
                    });
                } catch (Throwable throwable) {
                    throw new RuntimeException("Failed to retrieve main thread id.", throwable);
                }
            }
        }
        return this.mainThreadId == Thread.currentThread().getId();
    }

    /**
     * Runs the given thread asynchronously.
     * @param runnable The runnable that should be executed.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.NONE)
    public void runAsynchronously(BackendHandle handle, Runnable runnable) {
        synchronized (this.lock) {
            if (this.service == null) {
                this.createThreadPool(handle);
            }
        }
        this.service.submit(runnable);
    }

    /**
     * Creates the new thread-pool.
     * @param handle The new thread-pool.
     */
    private void createThreadPool(BackendHandle handle) {
        this.service = Executors.newCachedThreadPool();
        handle.getEnvironment().getHookManager().registerHooks(this);
    }

    /**
     * Shuts the threads down.
     * @param hook The hook that is being attached.
     */
    @HookHandler
    public void shutdown(ShutdownHook hook) {
        this.service.shutdown();
        try {
            this.service.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return;
        }
        if (!this.service.isShutdown() && !this.service.isTerminated())
            hook.getEnvironment().getBackend().getLogger().warning("Failed to stop thread-pool.");
    }
}
