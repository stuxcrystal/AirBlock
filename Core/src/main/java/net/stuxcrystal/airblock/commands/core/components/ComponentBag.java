package net.stuxcrystal.airblock.commands.core.components;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.backend.Handle;
import net.stuxcrystal.airblock.commands.backend.HandleWrapper;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Holder for components.</p>
 * <p>
 *     Makes sure that the given wrapper has the given component and will use
 *     the default implementations provided by the handle object when no
 *     other component defined their own implementation of the interface.
 * </p>
 */
public class ComponentBag {

    /**
     * The registered method.
     */
    private Map<Class<?>, Object> methods = new HashMap<Class<?>, Object>();

    /**
     * Registers the new component.
     * @param o The object to register.
     */
    public void registerComponent(@NonNull Object o) {
        this.registerComponent(o.getClass(), o);
    }

    /**
     * Register the implemented interfaces of the superclass.
     * @param cls The type of the object.
     * @param obj The object to register.
     */
    private void registerComponent(Class<?> cls, Object obj) {
        Components components = cls.getAnnotation(Components.class);
        if (components == null)
            return;

        for (Class<?> interfaceCls : components.value()) {
            if (!this.methods.containsKey(interfaceCls))
                this.methods.put(interfaceCls, obj);
        }

        this.registerComponent(cls.getSuperclass(), obj);
    }

    /**
     * Checks if the given interface is implemented by the component holder
     * @param interfaceCls The interface that should be implemented.
     * @return {@code true} if the given interface has been implemented.
     */
    public boolean isImplemented(@NonNull Class<?> interfaceCls, @NonNull HandleWrapper<?> wrapper) {
        return interfaceCls.isInstance(wrapper.getHandle()) || this.methods.containsKey(interfaceCls);
    }

    /**
     * <p>Calls the component method.</p>
     * <p>
     *     Please note that external components are prioritized as they may implement better
     *     suited algorithms for the actual plugin.
     * </p>
     * @param cls     The interface class that is implemented.
     * @param method  The method.
     * @param handle  The handle.
     * @param objects The parameters.
     * @param <T> The types.
     * @return The result of the call.
     */
    @Nullable
    public <T> T call(@NonNull Class<?> cls, @NonNull Method method, @NonNull HandleWrapper<?> handle, Object... objects) throws Throwable {
        Handle<?> hObj = handle.getHandle();
        if (this.methods.containsKey(cls))
            return this.callExternal(cls, method, handle.getEnvironment(), hObj, objects);
        else if (cls.isInstance(hObj))
            return this.callInternal(method, handle.getEnvironment(), hObj, objects);
        else
            throw new UnsupportedOperationException("Interface has not been implemented.");
    }

    /**
     * Calls the internal implementation of the method.
     * @param method       The methods of the call.
     * @param environment  The environment that is used by the command.
     * @param handle       The handle that was called.
     * @param parameters   The parameters that should be passed to the function.
     * @param <T> The type of the implementation.
     * @return The result of the function.
     * @throws Throwable If the method throws an exception.
     */
    <T> T callInternal(Method method, Environment environment, Handle<?> handle, Object... parameters) throws Throwable {
        Method aMethod;
        try {
            aMethod = handle.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException("The implementation does not implement the called method.");
        }
        return this.invoke(aMethod, environment, handle, parameters);
    }

    /**
     * Calls the actual component method.
     * @param cls          The type that should be searched.
     * @param method       The methods of the call.
     * @param environment  The environment that is used by the command.
     * @param handle       The handle that was called.
     * @param parameters   The parameters that should be passed to the function.
     * @param <T> The type of the implementation.
     * @return The result of the function.
     * @throws Throwable If the method throws an exception.
     */
    <T> T callExternal(Class<?> cls, Method method, Environment environment, Handle<?> handle, Object... parameters)
            throws Throwable {
        Object container = this.methods.get(cls);
        if (container == null)
            throw new UnsupportedOperationException("Unsupported interface: " + cls.getSimpleName());

        Method aMethod = this.getMethod(container.getClass(), method, handle);
        if (aMethod == null) {
            throw new UnsupportedOperationException(
                    "The implementation of the interface does not implement the called method."
            );
        }

        return this.invoke(aMethod, environment, container, ArrayUtils.add(parameters, 0, handle));
    }

    /**
     * Returns the correct method for the implementation.
     *
     * @param cls     The class which method should be searched.
     * @param method  The method that should be found.
     * @param handle  The handle that is affected by the holder.
     * @return The method or {@code null} if the method was not found.
     */
    @SuppressWarnings("unchecked")
    Method getMethod(Class<?> cls, Method method, Handle<?> handle) {
        Class<?> current = cls;
        while (current.getAnnotation(Components.class) != null) {
            Class<?> handleCls = handle.getClass();
            while (Handle.class.isAssignableFrom(handleCls)) {
                try {
                    return current.getDeclaredMethod(
                            method.getName(),
                            ArrayUtils.add(method.getParameterTypes(), 0, handleCls)
                    );
                } catch (NoSuchMethodException ignored) {
                    handleCls = handleCls.getSuperclass();
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }

    /**
     * Invokes the function in the way described by the function that has been annotated.
     * @param method       The method that should be called.
     * @param instance     The instance that should be implemented.
     * @param parameters   The parameters
     * @return The result of the method or the callable.
     * @throws Throwable If the method throws an exception.
     */
    <T> T invoke(Method method, Environment environment, Object instance, Object... parameters) throws Throwable {
        Component component = method.getAnnotation(Component.class);
        Component.ExecutionThread syncstate = (component!=null)?component.thread(): Component.ExecutionThread.SAME_THREAD;
        return syncstate.invoke(environment, method, instance, parameters);
    }
}
