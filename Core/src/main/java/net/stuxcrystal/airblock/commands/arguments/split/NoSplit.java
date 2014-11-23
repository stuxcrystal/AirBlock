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

package net.stuxcrystal.airblock.commands.arguments.split;

/**
 * This splitter does not split.
 */
public class NoSplit implements ArgumentSplitter {


    /**
     * So simple it doesn't need a test.
     * @param raw The raw string that should be split.
     * @param parseFlags Ignored.
     * @return The raw value.
     */
    @Override
    public String[] split(String raw, boolean parseFlags) {
        return new String[] {"", raw};
    }
}
