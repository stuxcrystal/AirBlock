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

package net.stuxcrystal.airblock.commands.localization.sources;

import lombok.NonNull;
import net.stuxcrystal.airblock.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements a value source that uses an array (or list) as
 * its source.
 */
public class ArrayValueSource implements ValueSource {

    /**
     * The holder of the values.
     */
    private final List<Object> values;

    /**
     * Creates a new value source that uses a list as its backend.
     * @param values The values.
     */
    public ArrayValueSource(@NonNull List<Object> values) {
        this.values = new ArrayList<Object>(values);
    }

    /**
     * Creates a new value source that uses an array as its backend.
     * @param values The values.
     */
    public ArrayValueSource(Object... values) {
        this(Arrays.asList(values));
    }

    @Nullable
    @Override
    public Optional<?> get(@NonNull String value) {
        if (!StringUtils.isNumeric(value))
            return Optional.empty();

        int index = Integer.valueOf(value);
        if (index < 0 || index >= this.values.size())
            return Optional.empty();
        return Optional.from(this.values.get(index));
    }
}
