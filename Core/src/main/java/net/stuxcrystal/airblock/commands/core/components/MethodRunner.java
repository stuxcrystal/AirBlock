package net.stuxcrystal.airblock.commands.core.components;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Executes the method.
 * @param <V> The return type.
 */
@RequiredArgsConstructor
public class MethodRunner<V> implements Callable<V> {

    /**
     * The method that should be executed.
     */
    private final Method method;

    /**
     * The instance on which the method will be called.
     */
    private final Object instance;

    /**
     * The parameters that have been passed.
     */
    private final Object[] parameters;

    /**
     * Call the method.
     * @return The result of the method.
     * @throws Exception If the method threw an exception.
     */
    @Override
    public V call() throws Exception {
        try {
            return ComponentManager.invoke(method, instance, parameters);
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
