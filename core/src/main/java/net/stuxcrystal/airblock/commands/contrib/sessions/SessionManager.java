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

package net.stuxcrystal.airblock.commands.contrib.sessions;

import net.stuxcrystal.airblock.commands.backend.Handle;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import java.util.*;
import java.util.logging.Level;

/**
 * The manager for sessions.
 */
@Components(Sessions.class)
public class SessionManager {

    /**
     * Map of all recognized sessions handled by the command-executor.<p />
     *
     * The sessions are stored in a static variable because multiple CommandExecutors can access the same
     * handle.<p />
     *
     * The access to the sessions is thread-safe.
     */
    private final Map<UUID, Map<Class<? extends Session>, Session>> sessions = Collections.synchronizedMap(
            new HashMap<UUID, Map<Class<? extends Session>, Session>>()
    );

    /**
     * Contains the environment that will be used.
     */
    private final Environment environment;

    /**
     * The environment.
     * @param environment The environment.
     */
    public SessionManager(Environment environment) {
        this.environment = environment;
    }

    /**
     * Creates a new session.
     *
     * @param cls      The type of ths session
     * @return The newly created session.
     */
    protected <S extends Session> S createSession(Class<S> cls) {

        S session;
        try {
            session = cls.newInstance();
        } catch (InstantiationException e) {
            // Log Exception.
            this.environment.getLogger().log(Level.WARNING, "Failed to create session of " + cls, e);
            return null;
        } catch (IllegalAccessException e) {
            // Log Exception.
            this.environment.getLogger().log(Level.WARNING, "Failed to create session of " + cls, e);
            return null;
        }

        session.initialize(this.environment);
        return session;

    }

    /**
     * Returns the session with the given type.
     * @param sessions The session-container of this command-executor.
     * @param cls      The class that contains the session-container.
     * @param <S>      The type of the session object.
     * @return The session-object or null if the creation of the session failed.
     */
    @SuppressWarnings("unchecked")
    private <S extends Session> S _getSession(Map<Class<? extends Session>, Session> sessions, Class<S> cls) {
        S result = (S) sessions.get(cls);
        if (result == null || result.isSessionExpired()) {
            result = this.createSession(cls);
            if (result == null) return null;
            sessions.put(cls, result);
        }

        return result;
    }

    /**
     * <p>Returns the session using this object.</p>
     *
     * <p>Sessions are stored by a synchronized map are identified by the Class of the implementing type.</p>
     *
     * @param handle The executor that the sessions are attached.
     * @param cls The class object.
     * @param <S> The type of the session.
     * @return null if the creation of the session failed.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public <S extends Session> S getSession(Handle handle, Class<S> cls) {
        this.ensureValidHandle(handle);
        Map<Class<? extends Session>, Session> sessions = this.sessions.get(handle.getUniqueIdentifier());

        if (sessions == null) {
            sessions = new HashMap<Class<? extends Session>, Session>();
            this.sessions.put(handle.getUniqueIdentifier(), sessions);
        }

        return this._getSession(sessions, cls);
    }

    /**
     * Returns an array of all sessions registered for the executor.
     * @param handle The sessions registered for the executor.
     * @return The sessions registered for the executor.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.SYNCHRONIZED_INSTANCE)
    public List<Session> getSessions(Handle handle) {
        this.ensureValidHandle(handle);
        Map<Class<? extends Session>, Session> sessions = this.sessions.get(handle.getUniqueIdentifier());
        if (sessions == null)
            return Collections.emptyList();
        return new ArrayList<Session>(sessions.values());
    }

    /**
     * Ensures you get a supported handle.
     * @param handle The handle.
     */
    private void ensureValidHandle(Handle handle) {
        if (handle.getUniqueIdentifier() == null)
            throw new IllegalArgumentException("Unsupported executor type.");
    }

}
