package net.stuxcrystal.airblock;

import net.stuxcrystal.airblock.annotations.AirBlockLoader;
import net.stuxcrystal.airblock.annotations.ComponentList;
import net.stuxcrystal.airblock.commands.arguments.ArgumentParser;
import net.stuxcrystal.airblock.commands.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The bootstrapper class that will bootstrap the AirBlock-Framework for this plugin.
 */
public final class Bootstrapper {

    /**
     * Contains all instances of the bootstrapped classes.
     */
    private static Map<Class<?>, EntryPoint> INSTANCES = new ConcurrentHashMap<Class<?>, EntryPoint>();

    /**
     * Bootstrap the environment.
     * @param backendEntryPoint The internal entry-point.
     * @param backendHandle     The internal backend-handle.
     */
    public static void begin(Object backendEntryPoint, BackendHandle backendHandle) {
        // Get the necessary data.
        Class<?> loader = backendEntryPoint.getClass();
        AirBlockLoader abl = loader.getAnnotation(AirBlockLoader.class);

        // Check if the values are valid.
        if (Bootstrapper.INSTANCES.containsKey(abl.value()))
            throw new IllegalStateException("The entry-point already exists.");
        if (!EntryPoint.class.isAssignableFrom(abl.value()))
            throw new IllegalArgumentException("Given class not an entry-point class.");

        // Create the new environment factory.
        EntryPoint ep;
        try {
            ep = (EntryPoint) abl.value().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to initiate EntryPoint.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initiate EntryPoint.", e);
        }

        // Update the backend handle.
        Environment environment = new Environment(backendHandle);
        for (ComponentList cl : abl.components()) {
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
        for (Class<?> type : abl.argumentTypes()) {
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

        // Update the default environment.
        Environment.setInstance(environment);

        // Register the instance.
        Bootstrapper.INSTANCES.put(abl.value(), ep);

        // Bootstrap the values.
        ep.start();
    }

    /**
     * Stops the instance.
     * @param backendEntryPoint The backend-entry.
     */
    public static void end(Object backendEntryPoint) {
        EntryPoint ep = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
        ep.stop();
        Bootstrapper.INSTANCES.remove(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
    }

    /**
     * Reloads the entry-point.
     * @param backendEntryPoint The reloaded entry-point.
     */
    public static void reload(Object backendEntryPoint) {
        EntryPoint ep = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
        ep.reload();
    }

}
