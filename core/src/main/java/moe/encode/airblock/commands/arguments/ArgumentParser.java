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

package moe.encode.airblock.commands.arguments;

import moe.encode.airblock.commands.Executor;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

/**
 * Parses arguments.
 */
public interface ArgumentParser<T> {

    /**
     * <p>Can this argument parses convert this type.</p>
     * @param type The type of the object to convert.
     * @return {@code true} if the argument parser can parse the argument.
     */
    public boolean canConvert(@Nonnull ArgumentConverter parser, @Nonnull Type type);

    /**
     * <p>Converts the object to the given type.</p>
     * @param value The value of the argument.
     * @return The converted value.
     */
    @Nonnull
    public T convert(@Nonnull Executor executor, @Nonnull ArgumentConverter converter, @Nonnull Type type, @Nonnull String value);
}
