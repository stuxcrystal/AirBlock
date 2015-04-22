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

package net.stuxcrystal.airblock.commands.core.backend;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Defines the handle for the executor.
 * @param <T> The type of the handle.
 */
public abstract class ExecutorHandle<T> extends Handle<T> {

    /**
     * The basic handle of the executor.
     * @param handle The handle for the executor.
     */
    public ExecutorHandle(T handle) {
        super(handle);
    }

    /**
     * Wraps the executor in the given environment.
     * @param environment The environment that should be used.
     * @return The wrapped executor.
     */
    public Executor wrap(Environment environment) {
        return environment.getBackend().wrap(this);
    }

    /**
     * <p>Returns the value of the executor.</p>
     * <p><b>WARNING: Users may change their names. Make sure you do not use this value as a persistent name.</b></p>
     * @return The value of the executor.
     */
    public abstract String getName();

    /**
     * Sends a message to the executor.
     * @param message The message that should be sent.
     */
    public abstract void sendMessage(@NonNull String message);

    /**
     * <p>Checks if the executor has administrative rights.</p>
     * <p>
     *     This will serve as fallback for the permission subsystem of the
     *     backend.
     * </p>
     * @return {@code true} if the handle has administrative rights.
     */
    public abstract boolean isAdmin();

    /**
     * Checks if the executor is a console or command-block object.
     * @return {@code true} if no player is behind the objects.
     */
    public abstract boolean isConsole();

    /**
     * Returns the address of the executor.
     * @return The address of the executor.
     */
    public abstract InetSocketAddress getAddress();

    /**
     * <p>Returns a unique identifier that is used on all handles to check if they are
     * essentially the same.</p>
     *
     * <p>
     *     The UUID on Console object will be random! Otherwise it will return the global
     *     unique identifier for the player.
     * </p>
     *
     * <p>
     *     If this function returns {@code null} it won't make sense to use the function.
     * </p>
     * @return The unique identifier.
     */
    @Nullable
    public abstract UUID getUniqueIdentifier();
}
