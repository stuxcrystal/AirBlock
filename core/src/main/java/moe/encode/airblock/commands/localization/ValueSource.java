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

package moe.encode.airblock.commands.localization;

import lombok.NonNull;
import moe.encode.airblock.utils.Optional;

import javax.annotation.Nullable;

/**
 * Represents the source of a value.
 */
public interface ValueSource {

    /**
     * Returns value with the given value.
     * @param value The value of the value.
     * @return The value with the given value.
     */
    @Nullable
    public Optional<?> get(@NonNull String value);
}
