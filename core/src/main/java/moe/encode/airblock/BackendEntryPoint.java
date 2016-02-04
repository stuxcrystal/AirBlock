package moe.encode.airblock;

import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;

/**
 * Interface for the most basic functions of the backend entry-point.
 */
public interface BackendEntryPoint {

    /**
     * Called when the environment has been bootstrapped but before the main
     * EntryPoint has been initialized.
     * @param entryPoint  The main entrypoint.
     */
    public void init(EntryPoint entryPoint);

    /**
     * Called after the EntryPoint has been initialized.
     * @param entryPoint  The main entrypoint.
     */
    public void begin(EntryPoint entryPoint);

    /**
     * Called before the entry-point is being reloaded.
     * @param entryPoint  The main entrypoint.
     */
    public void reloading(EntryPoint entryPoint);

    /**
     * Called after the entry-point has been reloaded.
     * @param entryPoint  The main entrypoint.
     */
    public void reloaded(EntryPoint entryPoint);

    /**
     * Called before the EntryPoint is being disabled.
     * @param entryPoint Before the entrypoint is being disabled.
     */
    public void end(EntryPoint entryPoint);

    /**
     * Called before the entry-point is being disabled.
     * @param entryPoint Before the entry-point is being disabled.
     */
    public void deinit(EntryPoint entryPoint);

    /**
     * <p>The configuration-storage that is needed for preparing the
     * ConfigurationLoader.</p>
     *
     * <p>Access the actual configuration-loader using {@link Bootstrapper#getConfigurationLoader(BackendEntryPoint)}</p>
     * @return The configuration loader.
     */
    public ConfigurationStorage getBaseConfigurationStorage();
}
