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

package moe.encode.airblock.commands.core.backend;

import lombok.NonNull;
import moe.encode.airblock.commands.Backend;
import moe.encode.airblock.commands.core.CommandImplementation;
import moe.encode.airblock.commands.core.settings.Environment;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * The handle for the backend
 */
public abstract class BackendHandle<T, E> extends Handle<T> {

    /**
     * Creates the new backendhandle.
     * @param handle The backend handle.
     */
    public BackendHandle(T handle) {
        super(handle);
    }

    /**
     * Contains the environment.
     */
    private Environment environment = null;

    /**
     * Returns the console object.
     * @return The console object.
     */
    @NonNull
    public abstract ExecutorHandle<E> getConsole();

    /**
     * Returns the logger for this backend.
     * @return The logger for this backend.
     */
    @NonNull
    public abstract Logger getLogger();

    /**
     * Returns all executor entities that are logged in.
     * @return All executor entities that are logged in.
     */
    @NonNull
    public abstract ExecutorHandle<E>[] getExecutors();

    /**
     * Runs the runnable later in the main thread.
     * @param runnable Runs the runnable later.
     */
    public abstract void runLater(@NonNull Runnable runnable);

    /**
     * <p>Runs the runnable in the main thread and holds the calling thread
     * while executing.</p>
     * <p>
     *     It will be executed directly when called in main thread.
     * </p>
     * <p>
     *     Internal function for the system.
     * </p>
     * @param callable The callable to execute.
     * @return The result of the main thread.
     */
    public abstract <R> R callInMainThread(@NonNull Callable<R> callable) throws Throwable;

    /**
     * Wraps the given executor.
     * @param handle The executor.
     * @return The wrapper.
     */
    public abstract ExecutorHandle<E> wrap(E handle);

    /**
     * Sets the environment.
     * @param environment The environment.
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Returns the environment.
     * @return The environment.
     */
    public Environment getEnvironment() {
        return this.environment;
    }

    /**
     * Registers the given command under the given name.
     * @param name             The name of the command.
     * @param implementation   The implementation of the command.
     */
    public abstract void registerCommand(String name, CommandImplementation implementation);

    /**
     * Always return null.
     * @return {@code null}.
     */
    @Override
    public final UUID getUniqueIdentifier() {
        return null;
    }

    /**
     * Returns the current minecraft version.
     * @return The current minecraft version.
     */
    public abstract MinecraftVersion getVersion();

    /**
     * Returns the name of the plugin.
     * @return The name of the plugin.
     */
    public abstract String getName();


    @Override
    @SuppressWarnings("unchecked")
    public final Backend wrap(Environment environment) {
        return new Backend(this, environment);
    }
}
