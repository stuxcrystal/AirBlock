package net.stuxcrystal.airblock.commands.core.components;

import net.stuxcrystal.airblock.commands.core.settings.Environment;
import net.stuxcrystal.airblock.commands.core.utils.ReflectionUtils;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Meta-Data for the component, such as how the component should be synchronized.</p>
 *
 * <p></p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Component {

    /**
     * How should the object be synchronized.
     */
    public enum ExecutionStrategy {

        /**
         * Do not synchronize the calls in any way.
         */
        NONE {
            @Override
            public <T> T invoke(Method method, Object instance, Object... parameters) throws Throwable {
                return ReflectionUtils.invoke(method, instance, parameters);
            }
        },

        /**
         * Acts like the synchronized keyword.
         */
        SYNCHRONIZED_METHOD {

            /**
             * Contains all locks.
             */
            private final Map<Method, ReentrantLock> locks = new HashMap<Method, ReentrantLock>();

            @Override
            public <T> T invoke(Method method, Object instance, Object... parameters) throws Throwable {
                // Get the current lock for the component.
                ReentrantLock lock;
                synchronized (this.locks) {
                    if (this.locks.containsKey(method))
                        lock = this.locks.get(method);
                    else
                        this.locks.put(method, lock = new ReentrantLock(true));
                }

                T result;

                // Lock the lock so no thread can call the method.
                lock.lock();
                try {
                    // Invoke the method.
                    result = ReflectionUtils.invoke(method, instance, parameters);
                } finally {
                    // Unlock the method.
                    lock.unlock();
                }

                // Remove the lock if it is not used anymore.
                synchronized (this.locks) {
                    if (lock.isHeldByCurrentThread() && lock.getHoldCount() <= 0) {
                        this.locks.remove(method);
                    }
                }

                return result;
            }
        },

        /**
         * <p>Only one method of the declaring class of the method can be called at once.</p>
         */
        SYNCHRONIZED_CLASS {

            /**
             * Contains all locks.
             */
            private final Map<Class<?>, ReentrantLock> locks = new HashMap<Class<?>, ReentrantLock>();

            @Override
            public <T> T invoke(Method method, Object instance, Object... parameters) throws Throwable {
                Class<?> cls = method.getDeclaringClass();

                // Get the current lock for the component.
                ReentrantLock lock;
                synchronized (this.locks) {
                    if (this.locks.containsKey(cls))
                        lock = this.locks.get(cls);
                    else
                        this.locks.put(cls, lock = new ReentrantLock(true));
                }

                T result;

                // Lock the lock so no thread can call the method.
                lock.lock();
                try {
                    // Invoke the method.
                    result = ReflectionUtils.invoke(method, instance, parameters);
                } finally {
                    // Unlock the method.
                    lock.unlock();
                }

                // Remove the lock if it is not used anymore.
                synchronized (this.locks) {
                    if (lock.isHeldByCurrentThread() && lock.getHoldCount() <= 0) {
                        this.locks.remove(cls);
                    }
                }

                return result;
            }
        },

        /**
         * <p>Only one method of the instance can be called at the same time.</p>
         * <p>
         *
         * </p>
         */
        SYNCHRONIZED_INSTANCE {
            /**
             * Contains all locks.
             */
            private final Map<Object, ReentrantLock> locks = new IdentityHashMap<Object, ReentrantLock>();

            @Override
            public <T> T invoke(Method method, Object instance, Object... parameters) throws Throwable {
                // Get the current lock for the component.
                ReentrantLock lock;
                synchronized (this.locks) {
                    if (this.locks.containsKey(instance))
                        lock = this.locks.get(instance);
                    else
                        this.locks.put(instance, lock = new ReentrantLock(true));
                }

                T result;

                // Lock the lock so no thread can call the method.
                lock.lock();
                try {
                    // Invoke the method.
                    result = ReflectionUtils.invoke(method, instance, parameters);
                } finally {
                    // Unlock the method.
                    lock.unlock();
                }

                // Remove the lock if it is not used anymore.
                synchronized (this.locks) {
                    if (lock.isHeldByCurrentThread() && lock.getHoldCount() <= 0) {
                        this.locks.remove(instance);
                    }
                }

                return result;
            }
        },

        ;

        /**
         * Executes the method.
         *
         * @param method        The method that should be executed.
         * @param instance      The instance on which the method will be executed.
         * @param parameters    The parameters that should be passed to the object.
         * @param <T> The return type.
         * @return The result of the object.
         */
        public abstract <T> T invoke(Method method, Object instance, Object... parameters) throws Throwable;

    }

    /**
     * What states of synchronization is allowed?
     */
    public enum ExecutionThread {

        /**
         * Just execute the function.
         */
        SAME_THREAD {
            @Override
            public <T> T invoke(Environment environment, Method method, Object instance, Object... parameters)
                    throws Throwable {
                return ComponentManager.invoke(method, instance, parameters);
            }
        },

        /**
         * Execute the function in the main server thread.
         */
        MAIN_THREAD {
            @Override
            public <T> T invoke(Environment environment, Method method, Object instance, Object... parameters)
                    throws Throwable {

                // If we are already in the main thread, we will just execute the function.
                if (environment.getBackend().isInMainThread()) {
                    return ExecutionThread.SAME_THREAD.invoke(environment, method, instance, parameters);
                }

                // Create a new future task for the method.
                RunnableFuture<T> future = new FutureTask<T>(new MethodRunner<T>(method, instance, parameters));

                // Make sure the method is being called.
                environment.getBackend().runLater(future);
                try {
                    // Wait until the method has been executed.
                    return future.get();
                } catch (ExecutionException e) {
                    throw e.getCause();
                } catch (InterruptedException e) {
                    // Fire the interrupt in the component, too.
                    future.cancel(true);
                    throw e;
                }
            }
        },

        /**
         * <p>Execute the function asynchronously.</p>
         * <p>Will always return a {@link java.util.concurrent.Future} object.</p>
         */
        OTHER_THREAD {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T invoke(Environment environment, Method method, Object instance, Object... parameters)
                    throws Throwable {
                RunnableFuture future = new FutureTask(new MethodRunner(method, instance, parameters));
                environment.getBackend().runAsynchronously(future);
                return (T)future;
            }
        },

        ;

        /**
         * Invokes the components.
         * @param method       The method that should be invoked.
         * @param instance     The instance that should be used for calling the method.
         * @param parameters   The parameters that are passed to the method.
         * @param <T> The return type of the method.
         * @return Thr return type of the method.
         */
        public abstract <T> T invoke(Environment environment, Method method, Object instance, Object... parameters)
                throws Throwable;
    }

    /**
     * In which thread should the method be executed.
     * @return The thread where the method should be executed.
     */
    public ExecutionThread thread() default ExecutionThread.SAME_THREAD;

    /**
     * How should the method be synchronized?
     * @return How should the method be synchronized.
     */
    public ExecutionStrategy strategy() default ExecutionStrategy.NONE;

}
