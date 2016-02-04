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

import moe.encode.airblock.utils.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.List;

/**
 * Implements an abstract argument itrable.
 * @param <T> The type of the values.
 */
public abstract class ArgumentIterable<T> extends AbstractList<T> {

    /**
     * The parent of the iterable.
     */
    protected final ArgumentContainer parent;

    /**
     * The type of the iterable.
     */
    protected final Type type;

    /**
     * Creates a new abstract argument iterable.
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public ArgumentIterable(ArgumentContainer parent, Type type) {
        this.parent = parent;
        this.type = type;
    }

    /**
     * Returns the argument at the given index.
     * @param index The argument at the given index.
     * @return The argument at the given index.
     */
    public abstract T get(int index);

    /**
     * Returns all arguments as strings.
     * @return All arguments as raw strings.
     */
    public List<T> copy() {
        return this.getParent().getArguments(ReflectionUtils.toClass(type));
    }

    /**
     * Returns the parent iterable.
     * @return The parent iterable.
     */
    public ArgumentContainer getParent() {
        return this.parent;
    }

    /**
     * Returns the argument list behind the iterable.
     * @return The argument list behind the iterable.
     */
    public ArgumentList getArgumentList() {
        return this.getParent().getArgumentList();
    }

    /**
     * Copied from JDK 8.
     * @param index the index that has been passed.
     * @return The message.
     */
    protected String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }
}
