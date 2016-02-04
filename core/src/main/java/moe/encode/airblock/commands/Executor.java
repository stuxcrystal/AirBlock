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

package moe.encode.airblock.commands;

import lombok.NonNull;
import moe.encode.airblock.commands.contrib.sessions.SessionManager;
import moe.encode.airblock.commands.core.backend.ExecutorHandle;
import moe.encode.airblock.commands.core.backend.HandleWrapper;
import moe.encode.airblock.commands.contrib.Permissions;
import moe.encode.airblock.commands.contrib.history.History;
import moe.encode.airblock.commands.contrib.history.HistoryComponent;
import moe.encode.airblock.commands.contrib.sessions.Sessions;
import moe.encode.airblock.commands.core.backend.MinecraftVersion;
import moe.encode.airblock.commands.core.settings.CommandSettings;
import moe.encode.airblock.commands.core.settings.Environment;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

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
            return this.isAdmin() || this.isConsole();
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

    /**
     * Returns the command history of the player.
     * @return The command history.
     */
    public History getHistory() {
        if (!this.hasComponent(History.class))
            this.getEnvironment().getComponentManager().register(Executor.class, new HistoryComponent(this.getEnvironment()));
        return this.getComponent(History.class);
    }

    /**
     * The unique executor identifier.
     * @return The unique executor identifier.
     */
    public String getUniqueExecutorIdentifier() {
        // Since minecraft uses from 1.8 onward the uuid, we will try to use the uuid.
        if (this.getEnvironment().getBackend().getMinecraftVersion().compareTo(new MinecraftVersion(1,7)) == 1) {
            if (this.isConsole())
                // If we use the console:
                // We enforce the console.
                return "CONSOLE";

            UUID uuid = this.getHandle().getUniqueIdentifier();
            if (uuid != null)
                return uuid.toString();
        }

        // Revert to default name.
        return this.getName();
    }
}
