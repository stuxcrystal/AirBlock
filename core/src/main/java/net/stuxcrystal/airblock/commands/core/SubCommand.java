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

package net.stuxcrystal.airblock.commands.core;

import lombok.Getter;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Commands;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.list.Command;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Represents a subcommand.
 */
public class SubCommand implements Command {

    /**
     * The value of the sub-command.
     */
    @Getter
    private final String name;

    /**
     * The description of the subcommand.
     */
    @Getter
    private final String description;

    /**
     * Contains the commands.
     */
    private final Commands commands;

    /**
     * Contains the container for sub-commands.
     * @param name        The value of the sub-command.
     * @param description The description for the sub-command.
     * @param container   The container for the sub-commands.
     */
    public SubCommand(@NonNull String name, @Nullable String description, @NonNull Commands container) {
        this.name = name;
        this.description = description;
        this.commands = container;
    }

    @Override
    public boolean canExecute(@NonNull Executor executor, @NonNull String rawArguments) {
        return this.commands.getCommand(SubCommand.splitArguments(rawArguments)[0]).size() > 0;
    }

    @Override
    public void execute(@NonNull Executor executor, @NonNull String rawArguments) {
        String[] splitter = SubCommand.splitArguments(rawArguments);
        this.commands.runCommand(splitter[0], executor, splitter[1]);
    }

    public static String[] splitArguments(String rawArguments) {
        if (StringUtils.isBlank(rawArguments))
            return new String[] {"", ""};

        String[] splitter = rawArguments.split(" ", 2);
        if (splitter.length == 1) {
            return new String[] {splitter[0], ""};
        }
        return splitter;
    }
}
