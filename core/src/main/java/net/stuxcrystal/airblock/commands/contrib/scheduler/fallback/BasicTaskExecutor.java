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
