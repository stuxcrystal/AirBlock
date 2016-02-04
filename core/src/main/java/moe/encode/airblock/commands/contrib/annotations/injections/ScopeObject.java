package moe.encode.airblock.commands.contrib.annotations.injections;

import moe.encode.airblock.commands.Executor;

import java.lang.annotation.*;

/**
 * Will automatically match the invoker of the command.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ScopeObject {

    /**
     * What should be retrieved.
     */
    public static enum Type {

        /**
         * Returns the executor that invokes the command.
         */
        EXECUTOR {
            /**
             * Returns the executor of the invoker.
             *
             * @param scope The actual scope.
             * @return The executor.
             */
            @Override
            public Executor getObject(Executor scope) {
                return scope;
            }
        },

        /**
         * Returns the console.
         */
        CONSOLE {
            /**
             * Returns the executor of the invoker.
             *
             * @param scope The actual scope.
             * @return The executor.
             */
            @Override
            public Executor getObject(Executor scope) {
                return scope.getEnvironment().getBackend().getConsole();
            }
        },

        /**
         * Returns the environment.
         */
        ENVIRONMENT {
            /**
             * Returns the executor of the invoker.
             *
             * @param scope The scope.
             * @return The executor.
             */
            @Override
            public Object getObject(Executor scope) {
                return scope.getEnvironment();
            }
        },


        /**
         * The context of the command call.
         */
        CONTEXT {
            /**
             * Returns the executor of the invoker.
             *
             * @param scope The scope.
             * @return The executor.
             */
            @Override
            public Object getObject(Executor scope) {
                return scope.getContext();
            }
        }
        ;

        /**
         * Returns the executor of the invoker.
         * @return The executor.
         */
        public abstract Object getObject(Executor scope);

    }

    /**
     * The object that should be retrieved.
     * @return The value that should be retrieved.
     */
    public Type value();

}
