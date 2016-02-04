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

package moe.encode.airblock.commands.contrib.sessions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import moe.encode.airblock.commands.core.settings.Environment;

/**
 * Represents a session.<p />
 *
 * The session implementation MUST have a constructor that does not have
 * any parameters.
 */
public abstract class Session {

    /**
     * Contains the environment of the session
     */
    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private Environment environment;

    /**
     * The last time of access.
     */
    private long lastAccessTime = -1;

    /**
     * The time for a session to expire (in milliseconds).<br />
     * If the expire-time is 0, the session will never expire.
     */
    private long expireTime = 0;

    /**
     * Was the session forced to be expired?
     */
    private boolean isExpired = false;

    /**
     * Constructs a session object.
     */
    public Session() { }

    /**
     * Initializes the Session-Object.
     */
    final void initialize(@NonNull Environment environment) {
        this.environment = environment;
        this.updateAccessTime();
    }

    /**
     * Updates the last-access-time.
     */
    public final void updateAccessTime() {
        if (this.isSessionExpired()) throw new IllegalStateException("The session is expired");
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * @return The time of the last access.
     */
    public final long getLastAccessTime() {
        return this.lastAccessTime;
    }

    /**
     * Sets the time for the session to expire.
     * @param time The time to expire or 0 if the session should never expire.
     */
    public final void setExpireTime(long time) {
        if (isSessionExpired()) throw new IllegalStateException("The session is expired.");
        this.expireTime = time;
    }

    /**
     * Checks if the session is expired.
     * @return true if the session is expired.
     */
    public final boolean isSessionExpired() {
        if (this.isExpired) return true;
        if (this.expireTime == 0) return false;
        if (this.lastAccessTime == -1) return false; // Forcefully allow the access time to exist
        boolean expired = System.currentTimeMillis() - this.lastAccessTime > this.expireTime;
        if (expired) this.isExpired = true;
        return expired;
    }

    /**
     * Expire the session now.
     */
    public void expire() {
        this.isExpired = true;
    }

}
