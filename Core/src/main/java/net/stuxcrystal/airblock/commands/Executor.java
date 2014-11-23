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

package net.stuxcrystal.airblock.commands;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.backend.HandleWrapper;
import net.stuxcrystal.airblock.commands.contrib.Permissions;
import net.stuxcrystal.airblock.commands.contrib.history.History;
import net.stuxcrystal.airblock.commands.contrib.history.HistoryComponent;
import net.stuxcrystal.airblock.commands.contrib.sessions.SessionManager;
import net.stuxcrystal.airblock.commands.contrib.sessions.Sessions;
import net.stuxcrystal.airblock.commands.core.settings.CommandSettings;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Objects;

/**
 * <p>Defines the executor.</p>
 * <p>
 *     The executor is inherently <b>not</b> threadsafe as it automatically performs
 *     context-switches when it is inside a command.
 * </p>
 * <p>
 *     Use the Session-Component to attach data to an executor as it has more features and is
 *     designed to be thread-safe.
 * </p>
 */
public class Executor extends HandleWrapper<ExecutorHandle<?>> {

    /**
     * The stack of the current contexts
     */
    public LinkedList<CommandSettings> ctxStack = new LinkedList<CommandSettings>();

    /**
     * Creates a new executor.
     * @param handle The handle.
     */
    public Executor(ExecutorHandle<?> handle, Environment environment) {
        super(handle, environment);
    }

    /**
     * Pushes an environment to the executor.
     * @param locale The locale.
     */
    public void pushContext(@NonNull CommandSettings locale) {
        this.ctxStack.push(locale);
    }

    /**
     * Returns the current context of the executor.
     * @return The context of the executor.
     */
    @NonNull
    public CommandSettings getContext() {
        CommandSettings settings = this.ctxStack.peek();
        if (settings == null)
            return this.getEnvironment();
        return settings;
    }

    /**
     * Pops the context of the executor.
     */
    public void popContext() {
        this.ctxStack.pop();
    }

    /**
     * Returns the value of the executor.
     * @return The value of the executor.
     */
    @NonNull
    public String getName() {
        return this.getHandle().getName();
    }

    /**
     * <p>Checks if the executor has administrative rights.</p>
     * <p>
     *     This will serve as fallback for the permission subsystem of the
     *     backend.
     * </p>
     * @return {@code true} if the handle has administrative rights.
     */
    public boolean isAdmin() {
        return this.getHandle().isAdmin();
    }

    /**
     * Checks if the executor is a console or command-block object.
     * @return {@code true} if no player is behind the objects.
     */
    public boolean isConsole() {
        return this.getHandle().isConsole();
    }

    /**
     * Sends a message to the executor.
     * @param messages The message that should be sent.
     */
    public void sendMessage(String... messages) {
        for (String message : messages) {
            Objects.requireNonNull(message);
            this.getHandle().sendMessage(message);
        }
    }

    /**
     * <p>Returns the port that the executor uses to connect to the server.</p>
     * <p>
     *     If this a console executor, result of the method is {@code null}.
     * </p>
     * @return The address the user sends to the message.
     */
    @Nullable
    public InetSocketAddress getAddress() {
        return this.getHandle().getAddress();
    }

    /**
     * Checks if the given user has the permission needed to perform the method.
     * @param node  The node to check.
     * @return {@code true} if the executor has the permission needed.
     */
    public boolean hasPermission(@NonNull String node) {
        if (!this.hasComponent(Permissions.class))
            return this.isAdmin();
        return this.getComponent(Permissions.class).hasPermission(node);
    }

    /**
     * Returns a new executor without any context-switches.
     * @return The new executor.
     */
    public Executor getRawExecutor() {
        return new Executor(this.getHandle(), this.getEnvironment());
    }

    /**
     * Returns the session manager.
     * @return The session manager.
     */
    public Sessions getSessions() {
        if (!this.hasComponent(Sessions.class))
            this.getEnvironment().getComponentManager().register(Executor.class, new SessionManager(this.getEnvironment()));
        return this.getComponent(Sessions.class);
    }

    public History getHistory() {
        if (!this.hasComponent(History.class))
            this.getEnvironment().getComponentManager().register(Executor.class, new HistoryComponent(this.getEnvironment()));
        return this.getComponent(History.class);
    }
}
