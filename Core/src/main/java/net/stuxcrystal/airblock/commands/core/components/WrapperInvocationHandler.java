package net.stuxcrystal.airblock.commands.core.components;

import net.stuxcrystal.airblock.commands.backend.HandleWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler for the wrapper.
 */
public class WrapperInvocationHandler implements InvocationHandler {

    /**
     * The handle wrapper that will be used to call methods.
     */
    final HandleWrapper wrapper;

    /**
     * The interface that will be implemented.
     */
    final Class<?> interfaceCls;

    /**
     * The invocation handler.
     * @param wrapper        The wrapper.
     * @param interfaceCls   The interface class.
     */
    public WrapperInvocationHandler(HandleWrapper wrapper, Class<?> interfaceCls) {
        this.wrapper = wrapper;
        this.interfaceCls = interfaceCls;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.wrapper.getEnvironment().getComponentManager().call(
                this.wrapper,
                this.interfaceCls,
                method,
                args
        );
    }
}
