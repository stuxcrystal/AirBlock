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

package net.stuxcrystal.airblock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.stuxcrystal.airblock.annotations.AirBlockLoader;
import net.stuxcrystal.airblock.annotations.ComponentList;
import net.stuxcrystal.airblock.commands.arguments.ArgumentParser;
import net.stuxcrystal.airblock.commands.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.hooks.Hook;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.PlayerLoginHook;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.PlayerLogoffHook;
import net.stuxcrystal.airblock.commands.core.hooks.predefined.ShutdownHook;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import net.stuxcrystal.airblock.configuration.ConfigurationLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The bootstrapper class that will bootstrap the AirBlock-Framework for this plugin.
 */
public final class Bootstrapper {

    /**
     * Contains the data for the bootstrapper.
     */
    @AllArgsConstructor
    private static class BootstrapperInstance {

        @Getter
        @Setter
        private EntryPoint ep;

        @Getter
        @Setter
        private BackendHandle handle;

        @Getter
        @Setter
        private ConfigurationLoader config;
    }

    /**
     * Contains all instances of the bootstrapped classes.
     */
    private static Map<Class<?>, BootstrapperInstance> INSTANCES = new ConcurrentHashMap<Class<?>, BootstrapperInstance>();

    /**
     * Returns the data to the bootstrapper.
     * @param instance The instance of the bootstrapper.
     * @return The desired instance.
     */
    private static BootstrapperInstance getData(Object instance) {
        return Bootstrapper.INSTANCES.get(instance.getClass().getAnnotation(AirBlockLoader.class).value());
    }

    /**
     * Loads the entry-point.
     * @param loader The loader that contains the entry-point.
     * @return The entry-point.
     */
    private static EntryPoint loadEntryPoint(AirBlockLoader loader) {
        try {
            return (EntryPoint) loader.value().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to initiate EntryPoint.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initiate EntryPoint.", e);
        }
    }

    /**
     * Initializes the environment.
     * @param loader  The loader.
     * @param handle  The handle.
     * @return The returned environment.
     */
    private static Environment initializeEnvironment(AirBlockLoader loader, BackendHandle handle) {
        // Update the backend handle.
        Environment environment = new Environment(handle);
        for (ComponentList cl : loader.components()) {
            for (Class<?> componentType : cl.components()) {
                Object component;
                try {
                    component = componentType.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException("Failed to initiate Component.", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to initiate Component.", e);
                }

                environment.getComponentManager().register(cl.type(), component);
            }
        }

        // Update the backend handle.
        for (Class<?> type : loader.argumentTypes()) {
            Object parser;
            try {
                parser = type.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Failed to initiate Parser", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to initiate Parser.", e);
            }

            environment.getArgumentConverter().registerParser((ArgumentParser) parser);
        }
        return environment;
    }

    /**
     * Bootstrap the environment.
     * @param backendEntryPoint The internal entry-point.
     * @param backendHandle     The internal backend-handle.
     */
    public static void begin(BackendEntryPoint backendEntryPoint, BackendHandle backendHandle) {
        // Get the necessary data.
        Class<?> cls = backendEntryPoint.getClass();
        AirBlockLoader loader = cls.getAnnotation(AirBlockLoader.class);

        if (loader == null)
            throw new IllegalArgumentException("The given backend does not have an @AirBlockLoader annotation.");

        // Check if the values are valid.
        if (Bootstrapper.INSTANCES.containsKey(loader.value()))
            throw new IllegalStateException("The entry-point already exists.");
        if (!EntryPoint.class.isAssignableFrom(loader.value()))
            throw new IllegalArgumentException("Given class not an entry-point class.");

        EntryPoint entry = Bootstrapper.loadEntryPoint(loader);

        // Configuration Loader
        ConfigurationLoader cl = new ConfigurationLoader(backendEntryPoint.getBaseConfigurationStorage());

        // Prepare the environment.
        Environment environment = Bootstrapper.initializeEnvironment(loader, backendHandle);
        Environment.setInstance(environment);
        entry.initialize(environment, cl);
        Bootstrapper.INSTANCES.put(loader.value(), new BootstrapperInstance(entry, backendHandle, cl));
    }

    public static void start(BackendEntryPoint backendEntryPoint) {
        EntryPoint entry = Bootstrapper.getData(backendEntryPoint).getEp();

        // Start the entry-point.
        backendEntryPoint.init(entry);
        entry.start();
        backendEntryPoint.begin(entry);
    }

    /**
     * Stops the instance.
     * @param backendEntryPoint The backend-entry.
     */
    public static void end(BackendEntryPoint backendEntryPoint) {
        BootstrapperInstance bi = Bootstrapper.getData(backendEntryPoint);
        EntryPoint entryPoint = bi.getEp();

        backendEntryPoint.end(entryPoint);
        entryPoint.stop();
        backendEntryPoint.deinit(entryPoint);

        Bootstrapper.INSTANCES.remove(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
    }

    /**
     * Reloads the entry-point.
     * @param backendEntryPoint The reloaded entry-point.
     */
    public static void reload(BackendEntryPoint backendEntryPoint) {
        EntryPoint ep = Bootstrapper.getData(backendEntryPoint).getEp();

        backendEntryPoint.reloading(ep);
        ep.reload();
        backendEntryPoint.reloaded(ep);
    }

    /**
     * Wraps the data.
     * @param object The object.
     * @param <T>    The type of the wrapped player.
     * @return The handle.
     */
    @SuppressWarnings("unchecked")
    private static <T> ExecutorHandle<T> wrap(BackendEntryPoint backendEntryPoint, T object) {
        return Bootstrapper.getData(backendEntryPoint).getHandle().wrap(object);
    }

    /**
     * Called when a user is logging in.
     * @param backendEntryPoint  The backend.
     * @param handle             The handle.
     * @param <T> The type of the handle.
     */
    public static <T> void login(BackendEntryPoint backendEntryPoint, T handle) {
        BootstrapperInstance bi = Bootstrapper.getData(backendEntryPoint);
        bi.getHandle().getEnvironment().getHookManager().call(
                new PlayerLoginHook(Bootstrapper.wrap(backendEntryPoint, handle).wrap(bi.getHandle().getEnvironment()))
        );
    }

    /**
     * Called when a user is logging in.
     * @param backendEntryPoint  The backend.
     * @param handle             The handle.
     * @param <T> The type of the handle.
     */
    public static <T> void logoff(BackendEntryPoint backendEntryPoint, T handle) {
        BootstrapperInstance bi = Bootstrapper.getData(backendEntryPoint);
        bi.getHandle().getEnvironment().getHookManager().call(
                new PlayerLogoffHook(Bootstrapper.wrap(backendEntryPoint, handle).wrap(bi.getHandle().getEnvironment()))
        );
    }

    /**
     * Returns the configuration for the entry-point.
     * @param backendEntryPoint The configuration for the entry-point.
     * @return The configuration for the entry-point.
     */
    public static ConfigurationLoader getConfigurationLoader(BackendEntryPoint backendEntryPoint) {
        return Bootstrapper.getData(backendEntryPoint).config;
    }

}
