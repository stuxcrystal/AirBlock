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

package net.stuxcrystal.airblock.annotations;


import java.lang.annotation.*;

/**
 * The basic interface for the AirBlock-Loader interface.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AirBlockLoader {

    /**
     * Contains the entry-point class.
     * @return The entry-point class.
     */
    public Class<?> value();

    /**
     * Contains the components.
     * @return The components.
     */
    public ComponentList[] components() default {};

    /**
     * Contains all argument types that should be supported additionally to the default types.
     * @return All argument types that should be supported additionally to the default types.
     */
    public Class<?>[] argumentTypes() default {};

}
