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

package moe.encode.airblock.commands.arguments.split;

import javax.annotation.concurrent.Immutable;

/**
 * <p>Interface that splits arguments.</p>
 * <p>
 *
 * </p>
 */
@Immutable
public interface ArgumentSplitter {

    /**
     * <p>Splits a string.</p>
     * <p>
     *     The result array must always contain at least one element that will contain the flags
     *     passed to the argument list.
     *     All array other items will contain the actual array elements.
     * </p>
     * @param raw The raw string that should be split.
     * @return The splitter.
     */
    public String[] split(String raw, boolean parseFlags);

}
