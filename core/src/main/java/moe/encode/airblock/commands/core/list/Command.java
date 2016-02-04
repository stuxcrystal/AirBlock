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

package moe.encode.airblock.commands.core.list;

import lombok.NonNull;
import moe.encode.airblock.commands.Executor;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Defines a command
 */
@Immutable
public interface Command {

    /**
     * Returns the value of the command.
     * @return The value of the command.
     */
    @NonNull
    public String getName();

    /**
     * Returns the description for the command.
     * @return The description for the command.
     */
    @Nullable
    public String getDescription();

    /**
     * Can the executor execute the command.
     * @param executor      The entity that executes the command.
     * @param rawArguments  The raw arguments as a single string.
     * @return If the command can be executed by the executor.
     */
    public boolean canExecute(@NonNull Executor executor, @Nullable String rawArguments);

    /**
     * Executes the command.
     * @param executor       The executor that executes the command.
     * @param rawArguments   The arguments as a single string.
     */
    public void execute(@NonNull Executor executor, @NonNull String rawArguments);

}
