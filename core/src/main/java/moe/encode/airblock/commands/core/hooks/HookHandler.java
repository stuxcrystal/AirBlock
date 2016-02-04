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

package moe.encode.airblock.commands.core.hooks;

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
