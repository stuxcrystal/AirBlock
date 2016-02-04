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

package moe.encode.airblock.commands.core;

import moe.encode.airblock.commands.Executor;

/**
 * <p>Contains the implementation of a command.</p>
 * <p>
 *     This is a backend-class to pass command-implementations.
 * </p>
 */
public interface CommandImplementation {

    /**
     * Executes the given command.
     * @param executor  The executor of the command.
     * @param label     The name of the command.
     * @param arguments The arguments that have been passed to the command.
     */
    public void execute(Executor executor, String label, String arguments);

    /**
     * Returns the description of the command.
     * @param executor  The executor.
     * @param label     The name of the command.
     */
    public String getDescription(Executor executor, String label);

}
