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

package moe.encode.airblock.commands.core.components;

import moe.encode.airblock.utils.ReflectionUtils;
import moe.encode.airblock.commands.core.settings.Environment;

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
                return call(this.locks, method, method, instance, parameters);
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
                return call(this.locks, cls, method, instance, parameters);
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
                return call(this.locks, instance, method, instance, parameters);
            }
        },

        ;

        /**
         * Synchronized implementation.
         *
         * @param locks       The map of licks.
         * @param key         The keys.
         * @param method      The method.
         * @param instance    The instance.
         * @param parameters  The parameters.
         * @param <R>         The result type.
         * @param <T>         The key type.
         * @return The result of the invocation.
         * @throws Throwable If an exception is thrown.
         */
        protected <R, T> R call(Map<T, ReentrantLock> locks, T key, Method method, Object instance, Object... parameters) throws Throwable {
            ReentrantLock lock = this.lockOn(locks, key);
            try {
                return ReflectionUtils.invoke(method, instance, parameters);
            } finally {
                this.lockOff(lock, locks, key);
            }
        }

        /**
         * Locks the parameter on the given object and locks the lock.
         *
         * @param locks   The list of locks.
         * @param value   The key to search for the current lock.
         * @param <T> The type of the key.
         * @return The actual lock.
         */
        protected <T> ReentrantLock lockOn(Map<T, ReentrantLock> locks, T value) {
            // Get the current lock for the component.
            ReentrantLock lock;
            synchronized (locks) {
                if (locks.containsKey(value))
                    lock = locks.get(value);
                else
                    locks.put(value, lock = new ReentrantLock(true));
            }

            lock.lock();

            return lock;
        }

        /**
         * Locks the parameter on the given object and locks the lock.
         *
         * @param lock    The lock to unlock.
         * @param locks   The list of locks.
         * @param value   The key to search for the current lock.
         * @param <T> The type of the key.
         */
        protected <T> void lockOff(ReentrantLock lock, Map<T, ReentrantLock> locks, T value) {
            lock.unlock();

            // Remove the lock if it is not used anymore.
            synchronized (locks) {
                if (lock.isHeldByCurrentThread() && lock.getHoldCount() <= 0) {
                    locks.remove(value);
                }
            }
        }

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
