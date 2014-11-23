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

import org.apache.commons.lang3.ArrayUtils;

/**
 * A simple splitter that just splits after every space.
 */
public class SimpleSplit implements ArgumentSplitter {

    @Override
    public String[] split(String raw, boolean parseFlags) {
        String[] rawSplit = raw.split(" ");
        if (rawSplit.length == 0) {
            return new String[] {""};
        }

        if (parseFlags) {
            if (rawSplit[0].startsWith("-")) {
                rawSplit[0] = rawSplit[0].substring(1);
            } else {
                return ArrayUtils.add(rawSplit, 0, "");
            }
        }

        return rawSplit;
    }
}
