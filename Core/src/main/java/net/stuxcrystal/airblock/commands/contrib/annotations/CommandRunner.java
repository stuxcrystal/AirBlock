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

import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.settings.CommandSettings;

/**
 * Used for executing commands asynchronously.
 */
public class CommandRunner implements Runnable {

    private final AnnotationCommand command;

    private final Executor executor;

    private final String rawArguments;

    private final CommandSettings locale;

    public CommandRunner(AnnotationCommand command, Executor executor, String rawArguments) {
        this.command = command;
        this.executor = executor;
        this.rawArguments = rawArguments;
        this.locale = executor.getContext();
    }

    @Override
    public void run() {
        this.executor.pushContext(this.locale);
        this.command.executeNow(this.executor, this.rawArguments);
        this.executor.popContext();
    }
}
