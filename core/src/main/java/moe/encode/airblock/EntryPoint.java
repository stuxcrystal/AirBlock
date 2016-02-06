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

package moe.encode.airblock;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import moe.encode.airblock.commands.core.hooks.predefined.ReloadHook;
import moe.encode.airblock.commands.core.hooks.predefined.ShutdownHook;
import moe.encode.airblock.commands.core.settings.Environment;
import moe.encode.airblock.configuration.ConfigurationLoader;

import java.util.logging.Logger;

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

    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private ConfigurationLoader configurationLoader;

    /**
     * Initializes the entry-point.
     * @param environment The initialized entry-point.
     * @param loader      The loader for the configuration.
     */
    final void initialize(@NonNull Environment environment, @NonNull ConfigurationLoader loader) {
        this.configurationLoader = loader;
        this.environment = environment;
    }

    /**
     * Shorthand for {@code ep.getEnvironment().getLogger();}
     * @return The logger of this entry-point.
     */
    public final Logger getLogger() {
        return this.environment.getLogger();
    }

    /**
     * Called when the plugin starts.
     */
    public final void start() {
        this.environment.getServiceManager().registerServiceRaw(this.getClass(), this);
        this.environment.getServiceManager().registerService(EntryPoint.class, this);
        this.onStart();
    }

    /**
     * Called when the plugin stops.
     */
    public final void stop() {
        // Fire a shutdown event.
        this.getEnvironment().getHookManager().call(new ShutdownHook(this.getEnvironment()));

        // Stop the actual workings.
        this.onStop();

        // Clear all hooks that have been registered.
        this.getEnvironment().getHookManager().clear();

        // Unregister the entry-point.
        this.environment.getServiceManager().unregisterService(EntryPoint.class);
        this.environment.getServiceManager().unregisterService(this.getClass());
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
