package net.stuxcrystal.airblock.commands.contrib.sessions;

/**
 * The interface for the session extensions.
 */
public interface Sessions {

    /**
     * Returns a new session.
     * @param type The type of the session.
     * @param <T>  The type of the session.
     * @return The type of the session.
     */
    public <T> T getSession(Class<T> type);

}
