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
    }

    /**
     * Contains all instances of the bootstrapped classes.
     */
    private static Map<Class<?>, BootstrapperInstance> INSTANCES = new ConcurrentHashMap<Class<?>, BootstrapperInstance>();

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
        Bootstrapper.INSTANCES.put(abl.value(), new BootstrapperInstance(ep, backendHandle));

        // Bootstrap the values.
        ep.start();
    }

    /**
     * Stops the instance.
     * @param backendEntryPoint The backend-entry.
     */
    public static void end(Object backendEntryPoint) {
        BootstrapperInstance bi = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
        // Inform the others that we are currently shutting down.
        bi.getHandle().getEnvironment().getHookManager().call(new ShutdownHook(bi.getHandle().getEnvironment()));
        bi.getEp().stop();
        Bootstrapper.INSTANCES.remove(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
    }

    /**
     * Reloads the entry-point.
     * @param backendEntryPoint The reloaded entry-point.
     */
    public static void reload(Object backendEntryPoint) {
        EntryPoint ep = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value()).getEp();
        ep.reload();
    }

    /**
     * Wraps the data.
     * @param object The object.
     * @param <T>    The type of the wrapped player.
     * @return The handle.
     */
    @SuppressWarnings("unchecked")
    private static <T> ExecutorHandle<T> wrap(Object backendEntryPoint, T object) {
        return Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value()).getHandle().wrap(object);
    }

    /**
     * Called when a user is logging in.
     * @param backendEntryPoint  The backend.
     * @param handle             The handle.
     * @param <T> The type of the handle.
     */
    public static <T> void login(Object backendEntryPoint, T handle) {
        BootstrapperInstance bi = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
        bi.getHandle().getEnvironment().getHookManager().call(new PlayerLoginHook(Bootstrapper.wrap(backendEntryPoint, handle).wrap(bi.getHandle().getEnvironment())));
    }

    /**
     * Called when a user is logging in.
     * @param backendEntryPoint  The backend.
     * @param handle             The handle.
     * @param <T> The type of the handle.
     */
    public static <T> void logoff(Object backendEntryPoint, T handle) {
        BootstrapperInstance bi = Bootstrapper.INSTANCES.get(backendEntryPoint.getClass().getAnnotation(AirBlockLoader.class).value());
        bi.getHandle().getEnvironment().getHookManager().call(new PlayerLogoffHook(Bootstrapper.wrap(backendEntryPoint, handle).wrap(bi.getHandle().getEnvironment())));
    }

}
