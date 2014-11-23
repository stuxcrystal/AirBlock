package net.stuxcrystal.airblock;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.ReloadHook;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.ShutdownHook;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * The implementation of a loader.
 */
public abstract class EntryPoint {

    /**
     * Contains the envi
     */
    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private Environment environment;

    /**
     * Initializes the entry-point.
     * @param environment The initialized entry-point.
     */
    final void initialize(@NonNull Environment environment) {
        this.environment = environment;
    }

    /**
     * Called when the plugin starts.
     */
    public final void start() {
        this.onStart();
    }

    /**
     * Called when the plugin stops.
     */
    public final void stop() {
        this.getEnvironment().getHookManager().call(new ShutdownHook(this.getEnvironment()));
        this.onStop();

        // Clear all hooks that have been registered.
        this.getEnvironment().getHookManager().clear();
    }

    /**
     * The reload command.
     */
    public final void reload() {
        this.getEnvironment().getHookManager().call(new ReloadHook(this.getEnvironment()));
        this.onReload();
    }

    /**
     * Override this method to implement initialization behaviour.
     */
    protected void onStart() {

    }

    /**
     * Override this method to implement de-initialization behaviour.
     */
    protected void onStop() {

    }

    /**
     * Override this method to implement re-initialization behaviour.
     */
    protected void onReload() {
        this.stop();
        this.start();
    }

}
