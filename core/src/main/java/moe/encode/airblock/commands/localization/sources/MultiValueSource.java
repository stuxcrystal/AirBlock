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
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a value source that retrieves its values from other sources.
 */
public class MultiValueSource implements ValueSource {

    /**
     * The sources that are accessed by the multi-value source.
     */
    private final List<ValueSource> sources = new ArrayList<ValueSource>();

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        for (ValueSource source : this.sources) {
            Optional<?> res = source.get(value);
            if (!Optional.isEmpty(res))
                return res;
        }
        return Optional.empty();
    }


    /**
     * Adds a source to the translation manager.
     * @param source The source that has been added.
     */
    public void addSource(@NonNull ValueSource source) {
        this.sources.add(source);
    }

    /**
     * The source to remove.
     * @param source The source to remove.
     */
    public void removeSource(@NonNull ValueSource source) {
        this.sources.remove(source);
    }

}
