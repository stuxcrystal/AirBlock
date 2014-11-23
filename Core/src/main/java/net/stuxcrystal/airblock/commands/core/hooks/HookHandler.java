package net.stuxcrystal.airblock.commands.core.hooks;

import java.lang.annotation.*;

/**
 * The handler for hooks.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HookHandler {

    /**
     * The actual priority.
     */
    public enum Priority implements Comparable<Priority> {

        /**
         * A very very low priority.
         */
        LOWEST,

        /**
         * A very low priority.
         */
        LOWER,

        /**
         * Low priority.
         */
        LOW,

        /**
         * The medium priority.
         */
        MEDIUM,

        /**
         * The high priority.
         */
        HIGH,

        /**
         * A higher priority.
         */
        HIGHER,

        /**
         * The highest priority.
         */
        HIGHEST,

        /**
         * A priority that is primarily designed to monitor events.
         */
        MONITOR,
    }

    /**
     * Contains the priority for the hooks.
     * @return The priority of the hook.
     */
    public Priority priority() default Priority.MEDIUM;

}
