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

package net.stuxcrystal.airblock.commands.core.components;


import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.backend.HandleWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The manager for components.</p>
 * <p>
 *     Components are a central part of the AirBlock-Framework as they provide natural
 *     security agains security-bloats and allow to define new backend functionality.
 * </p>
 */
public class ComponentManager {

    /**
     * Contains all bags.
     */
    private Map<Class<?>, ComponentBag> bags = new HashMap<Class<?>, ComponentBag>();

    /**
     * Registers a new wrapper type.
     *
     * @param wrapperType     The wrapper to implement.
     * @param implementation  The implementation of the given interfaces.
     */
    public void register(@NonNull Class<?> wrapperType, @NonNull Object implementation) {
        if (!HandleWrapper.class.isAssignableFrom(wrapperType))
            throw new IllegalArgumentException("Invalid argument");

        if (!this.bags.containsKey(wrapperType))
            this.bags.put(wrapperType, new ComponentBag());

        this.bags.get(wrapperType).registerComponent(implementation);
    }

    /**
     * Checks if the given handle wrapper can execute the given function.
     * @param interfaceCls The interface class.
     * @param wrapper      The wrapper.
     * @return {@code true} if the component has been implemented.
     */
    public boolean isImplemented(@NonNull Class<?> interfaceCls, @NonNull HandleWrapper<?> wrapper) {
        Class<?> cur = wrapper.getClass();
        while (HandleWrapper.class.isAssignableFrom(cur)) {
            if (this.bags.containsKey(cur))
                if (this.bags.get(cur).isImplemented(interfaceCls, wrapper))
                    return true;
            cur = cur.getSuperclass();
        }
        return false;
    }

    /**
     * Calls the given function of the interface.
     *
     * @param wrapper          The wrapper that calls the method.
     * @param interfaceCls     The interface class that is used.
     * @param method           The method that is used.
     * @param parameters       The parameters that should be passed.
     * @param <T> The return type.
     * @return The result of the function.
     * @throws Throwable If we fail to execute the function.
     */
    public <T> T call(HandleWrapper<?> wrapper, Class<?> interfaceCls, Method method, Object... parameters) throws Throwable {
        ComponentBag bag = null;

        Class<?> cur = wrapper.getClass();
        while (HandleWrapper.class.isAssignableFrom(cur)) {
            if (this.bags.containsKey(cur))
                if (this.bags.get(cur).isImplemented(interfaceCls, wrapper)) {
                    bag = this.bags.get(cur);
                    break;
                }
            cur = cur.getSuperclass();
        }

        if (bag == null)
            throw new UnsupportedOperationException("Interface is not supported.");

        return bag.call(interfaceCls, method, wrapper, parameters);
    }

    /**
     * Returns the interface instance of the given component.
     * @param interfaceCls  The interface class.
     * @param wrapper       The actual wrapper.
     * @param <T> The return type.
     * @return The interface.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInterface(Class<T> interfaceCls, HandleWrapper<?> wrapper) {
        if (!interfaceCls.isInterface())
            throw new IllegalArgumentException("The given class should be an interface.");

        if (!this.isImplemented(interfaceCls, wrapper))
            throw new IllegalArgumentException("Unsupported interface.");

        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceCls}, new WrapperInvocationHandler(
                wrapper, interfaceCls
        ));
    }

    /**
     * Invoke the method.
     *
     * @param method          Invokes the method.
     * @param instance        The instance which should be used.
     * @param parameters      The parameters.
     * @param <T> The return type.
     * @return The result of the function.
     * @throws Throwable If the method throws an exception.
     */
    static <T> T invoke(Method method, Object instance, Object[] parameters) throws Throwable {
        Component.ExecutionStrategy strategy = Component.ExecutionStrategy.NONE;
        Component component = method.getAnnotation(Component.class);
        if (component != null)
            strategy = component.strategy();

        return strategy.invoke(method, instance, parameters);
    }
}
