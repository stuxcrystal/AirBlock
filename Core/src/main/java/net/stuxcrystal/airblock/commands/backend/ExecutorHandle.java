package net.stuxcrystal.airblock.commands.backend;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import java.net.InetSocketAddress;

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
     * Returns the value of the executor.
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
}
