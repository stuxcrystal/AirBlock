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

package net.stuxcrystal.airblock.commands.contrib.annotations;

import java.lang.annotation.*;

/**
 * Contains the command.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SimpleCommand {

    /**
     * Contains the supported executors for this command.
     */
    public static enum Executor
    {
        /**
         * If this executor is in the supported executor list,
         * the console is allowed to use this command.
         */
        CONSOLE {
            @Override
            public boolean isSupported(net.stuxcrystal.airblock.commands.Executor executor) {
                return executor.isConsole();
            }
        },

        /**
         * If this executor is in the supported executor list,
         * a player is supported as executor of the command.
         */
        PLAYER {
            @Override
            public boolean isSupported(net.stuxcrystal.airblock.commands.Executor executor) {
                return !executor.isConsole();
            }
        }

        ;

        /**
         * Checks if the executor is actually supported.
         * @param executor The executor to check.
         * @return If the user
         */
        public abstract boolean isSupported(net.stuxcrystal.airblock.commands.Executor executor);
    }

    /**
     * Returns the value of the command.
     * @return The value of the command.
     */
    public String value() default " ";

    /**
     * Returns the description of the command.
     * @return The description of the command.
     */
    public String description() default "";

    /**
     * Returns the required permission node for the command.
     * @return The required permission node for the command.
     */
    public String permission() default "";

    /**
     * Contains the supported executors.
     * @return The supported executors.
     */
    public Executor[] executors() default {};

    /**
     * If there is no permission support, do we required administrator
     * permissions?
     * @return {@code true} if administrator account is required.
     */
    public boolean adminRequired() default false;

    /**
     * Should this command be executed asynchronously?
     * @return {@code true} if the command is executed asynchronously.
     */
    public boolean async() default false;

    /**
     * Should flags be parsed.
     * @return {@code true} if flags should be supported.
     */
    public boolean useFlags() default true;
}
