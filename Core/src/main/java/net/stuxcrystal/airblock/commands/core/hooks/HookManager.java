package net.stuxcrystal.airblock.commands.core.hooks;

import net.stuxcrystal.airblock.commands.core.settings.Environment;
import net.stuxcrystal.airblock.commands.core.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * The manager for hooks.
 */
public class HookManager {

    /**
     * Creates the new hook manager.
     * @param environment The hook manager.
     */
    public HookManager(Environment environment) {
        this.environment = environment;
    }

    /**
     * Contains the actual hooks.
     */
    private class HookData {

        /**
         * The method that should be used.
         */
        private final Method method;

        /**
         * The instance of that should be used.
         */
        private final Object instance;

        /**
         * Contains the handler for the hooks.
         */
        private final HookHandler handler;

        /**
         * The actual hook.
         * @param method    The method.
         * @param instance  The instance.
         */
        private HookData(Method method, Object instance) {
            this.method = method;
            this.instance = instance;
            this.handler = this.method.getAnnotation(HookHandler.class);
        }
    }

    /**
     * The internal environment that is used by the hook-handler.
     */
    private final Environment environment;

    /**
     * The actual registered hooks.
     */
    private Map<Class<?>, List<HookData>> hooks = new ConcurrentHashMap<Class<?>, List<HookData>>();

    /**
     * Registers all hooks.
     * @param object The object that contains the hooks.
     */
    public void registerHooks(Object object) {
        if (object instanceof Class)
            this.registerHooks((Class)object, null);
        else
            this.registerHooks(object.getClass(), object);
    }

    /**
     * Registers all hooks defined the class.
     * @param cls     The current class.
     * @param object  The object.
     */
    void registerHooks(Class<?> cls, Object object) {
        if (cls == null)
            return;

        for (Method method : cls.getDeclaredMethods()) {
            HookHandler handler = method.getAnnotation(HookHandler.class);
            if (handler == null)
                continue;

            if (method.getParameterTypes().length != 1)
                throw new IllegalArgumentException("Invalid handler format: Invalid argument count.");

            if (!Hook.class.isAssignableFrom(method.getParameterTypes()[0])) {
                throw new IllegalArgumentException("Invalid handler format: Argument-Type not a hook.");
            }

            if (!hooks.containsKey(method.getParameterTypes()[0])) {
                hooks.put(method.getParameterTypes()[0], new ArrayList<HookData>());
            }

            hooks.get(method.getParameterTypes()[0]).add(new HookData(method, object));
        }

        this.registerHooks(cls.getSuperclass(), object);
    }

    /**
     * Calls the hook.
     * @param hook The hook that should be called.
     */
    public void call(Hook hook) {
        this.call(hook.getClass(), hook);
    }

    /**
     * Calls the hook.
     * @param type   The type of the hook.
     * @param hook   The hook itself.
     */
    void call(Class<?> type, Hook hook) {
        if (!Hook.class.isAssignableFrom(type))
            return;

        if (hooks.containsKey(type)) {
            List<HookData> hooks = new ArrayList<HookData>(this.hooks.get(type));

            hooks.sort(new Comparator<HookData>() {
                @Override
                public int compare(HookData o1, HookData o2) {
                    return ((Integer)o1.handler.priority().ordinal()).compareTo(o2.handler.priority().ordinal());
                }
            });
            Collections.reverse(hooks);

            for (HookData data : hooks) {
                try {
                    ReflectionUtils.invoke(data.method, data.instance, hook);
                } catch (Throwable e) {
                    e.printStackTrace();
                    this.environment.getBackend().getLogger().log(Level.SEVERE, "Error in hook-handler: " + data.method, e);
                    return;
                }
            }
        }

        this.call(type.getSuperclass(), hook);
    }
}
