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

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Just executes the command.
 */
public class LeafAnnotationCommand extends AnnotationCommand {

    /**
     * Creates a new annotation command.
     *
     * @param command The new command.
     * @param method  The method.
     */
    public LeafAnnotationCommand(SimpleCommand command, Method method, Object instance) {
        super(command, method, instance);
    }

    @Override
    public void executeNow(@NonNull Executor executor, @NonNull String rawArguments) {
        // Parse the arguments.
        ArgumentList al = new ArgumentList(rawArguments, this.getCommand().useFlags(), executor);

        try {
            ReflectionUtils.invoke(this.getMethod(), this.getInstance(), executor, al);
        } catch (Throwable throwable) {
            executor.getEnvironment().getBackend().getLogger().log(
                    Level.SEVERE,
                    "Error occured while '" +  executor.getName() +"' executed '/" + this.getName() + " " + rawArguments,
                    throwable);
        }
    }

    @Override
    public void execute(@NonNull Executor executor, @NonNull String rawArguments) {
        if (this.getCommand().async()) {
            executor.getEnvironment().getBackend().runAsynchronously(new CommandRunner(this, executor, rawArguments));
        } else {
            this.executeNow(executor, rawArguments);
        }
    }
}
