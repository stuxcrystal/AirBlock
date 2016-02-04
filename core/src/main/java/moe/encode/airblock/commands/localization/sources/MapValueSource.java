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
import java.util.HashMap;
import java.util.Map;

/**
 * Uses maps for determining the value source.
 */
public class MapValueSource implements ValueSource {

    /**
     * The map that holds the values.
     */
    private final Map<String, Object> values = new HashMap<String, Object>();

    /**
     * Creates the value source.
     * @param values The values.
     */
    public MapValueSource(@NonNull Map<?, ?> values) {
        for (Map.Entry<?, ?> entry : values.entrySet()) {
            this.values.put(entry.getKey().toString(), entry.getValue());
        }
    }

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!values.containsKey(value))
            return Optional.empty();
        return Optional.from(this.values.get(value));
    }
}
