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

package moe.encode.airblock.commands.contrib;

/**
 * <p>Interface for permissions.</p>
 * <p>
 *     Primarily designed for Handle-Implementations so they can provide their
 *     implementations of a permissions interface.
 * </p>
 */
public interface Permissions {

    /**
     * Checks if the given user has the permission needed to perform the method.
     * @param node  The node to check.
     * @return {@code true} if the executor has the permission needed.
     */
    public boolean hasPermission(String node);

}
