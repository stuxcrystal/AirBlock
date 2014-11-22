package net.stuxcrystal.airblock;

/**
 * The implementation of a loader.
 */
public abstract class EntryPoint {

    /**
     * Called when the plugin starts.
     */
    public void start() {

    }

    /**
     * Called when the plugin stops.
     */
    public void stop() {

    }

    /**
     * <p>The reload command.</p>
     * <p>
     *     If you override this method, do not call the original implementation.
     * </p>
     */
    public void reload() {
        this.stop();
        this.start();
    }

}
