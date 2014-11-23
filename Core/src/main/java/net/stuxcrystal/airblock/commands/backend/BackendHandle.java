package net.stuxcrystal.airblock.commands.backend;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

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
}
