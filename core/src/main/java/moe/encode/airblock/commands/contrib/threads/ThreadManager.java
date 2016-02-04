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

package moe.encode.airblock.commands.contrib.threads;

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
