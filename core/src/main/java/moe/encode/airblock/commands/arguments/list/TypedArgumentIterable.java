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

package moe.encode.airblock.commands.arguments.list;

import java.lang.reflect.Type;

/**
 * Implements a typed argument iterable.
 */
class TypedArgumentIterable<T> extends ArgumentIterable<T> {

    /**
     * Creates a new abstract argument iterable.
     *
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public TypedArgumentIterable(ArgumentContainer parent, Type type) {
        super(parent, type);
    }

    @Override
    public T get(int index) {
        return this.parent.get(index, this.type);
    }

    @Override
    public int size() {
        return this.getParent().size();
    }
}
