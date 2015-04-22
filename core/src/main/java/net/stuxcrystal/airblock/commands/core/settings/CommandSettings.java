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

package net.stuxcrystal.airblock.commands.core.settings;

import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter;
import net.stuxcrystal.airblock.commands.core.exceptions.ExceptionHandlerBag;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * Internal class for classes that contain the state of the locale.
 */
public interface CommandSettings {

    /**
     * Sets the {@link net.stuxcrystal.airblock.commands.arguments.ArgumentConverter} for the framework.
     * @param converter The new converter for the arguments.
     */
    public void setArgumentConverter(@Nonnull ArgumentConverter converter);

    /**
     * Returns the {@link net.stuxcrystal.airblock.commands.arguments.ArgumentConverter}.
     * @return The argument converter.
     */
    @Nonnull
    public ArgumentConverter getArgumentConverter();

    /**
     * Returns the argument splitter.
     * @param splitter The argument splitter.
     */
    public void setArgumentSplitter(@Nonnull ArgumentSplitter splitter);

    /**
     * Returns the {@link net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter}.
     * @return The argument splitter.
     */
    @Nonnull
    public ArgumentSplitter getArgumentSplitter();

    /**
     * Returns the root setting container.
     * @return The root setting container.
     */
    @Nonnull
    public Environment getEnvironment();

    /**
     * Returns the logger of the environment.
     * @return The logger of the environment.
     */
    @Nonnull
    public Logger getLogger();

    /**
     * Returns exception handlers of the environment.
     * @return excpetion handlers of the environment.
     */
    @Nonnull
    public ExceptionHandlerBag getExceptionHandlers();

}
