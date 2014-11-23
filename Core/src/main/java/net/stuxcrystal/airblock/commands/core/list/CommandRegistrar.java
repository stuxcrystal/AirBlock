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

package net.stuxcrystal.airblock.commands.core.list;

import lombok.NonNull;

import javax.annotation.concurrent.Immutable;

/**
 * Subclasses will implement ways to register new commands.
 */
@Immutable
public interface CommandRegistrar {

    /**
     * Registers all commands for the command registrar.
     * @param object The that should register new objects.
     */
    public void register(@NonNull CommandList list, @NonNull Object object);

}
