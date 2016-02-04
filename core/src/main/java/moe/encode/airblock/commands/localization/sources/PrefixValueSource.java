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

package moe.encode.airblock.commands.localization.sources;

import lombok.NonNull;
import moe.encode.airblock.utils.Optional;
import moe.encode.airblock.commands.localization.ValueSource;

import javax.annotation.Nullable;

/**
 * A value source that uses prefixes.
 */
public class PrefixValueSource implements ValueSource {

    /**
     * The prefix that should be used.
     */
    private final String prefix;

    /**
     * The value source that should be used.
     */
    private final ValueSource source;

    /**
     * Creates a new value source that uses prefixes.
     * @param prefix The prefix.
     * @param source The source.
     */
    public PrefixValueSource(@NonNull String prefix, @NonNull ValueSource source) {
        this.prefix = prefix;
        this.source = source;
    }


    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!value.startsWith(this.prefix))
            return Optional.empty();
        return this.source.get(value.substring(this.prefix.length()));
    }
}
