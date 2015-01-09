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

package net.stuxcrystal.airblock.commands.arguments.list;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements an iterable for the argument parser.
 */
public abstract class ArgumentContainer extends ArgumentIterable<String> {

    /**
     * The argument iterable we will be using.
     * @param iterable The iterable to use.
     */
    protected ArgumentContainer(ArgumentContainer iterable) {
        super(iterable, String.class);
    }

    /**
     * Returns the size of arguments that this iterable has.
     * @return The iterable.
     */
    public abstract int size();

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param cls   The type of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public <E> E get(int index, Type cls) throws NumberFormatException {
        return this.getRaw(index, cls, null);
    };

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param cls   The class of the argument.
     * @param def   The default value. {@code null} if no default is given.
     * @param <T>   The type of the argument
     * @return The converted argument.
     */
    protected <T> T get(int index, Class<T> cls, T def) {
        try {
            return this.get(index, cls);
        } catch (NumberFormatException nfe) {
            return def;
        } catch (IndexOutOfBoundsException nfe) {
            return def;
        }
    }

    /**
     * Returns the object using the raw value.
     * @param index   The index.
     * @param cls     The class.
     * @param def     The default string value.
     * @param <E>     The element type.
     * @return The raw value.
     */
    public abstract <E> E getRaw(int index, Type cls, String def);

    @Override
    public String get(int index) {
        return this.get(index, this.type);
    }

    /**
     * Returns all arguments in this iterator.
     * @param type The type of the values in the list.
     * @param <T>  The type of the argument.
     * @return The argument.
     */
    public <T> List<T> getArguments(Type type) {
        List<T> result = new ArrayList<T>();
        for (T arg : this.<T>as(type))
            result.add(arg);
        return result;
    }

    /**
     * Returns an iterable that returns the arguments in the given type.
     * @param cls The type of the values.
     * @param <T> The type.
     * @return An iterable that returns the iterable that returns the arguments at the given value.
     */
    @SuppressWarnings("unchecked")
    public <T> ArgumentIterable<T> as(Type cls) {
        return new TypedArgumentIterable<T>(this, cls);
    }

    /**
     * Slices the arguments of the argument parser.
     * @param start The first argument that is included in the slice.
     * @param stop  The first argument that should <b>not</b> be included in the slice.
     * @param step  The step or stride that should be between each object.
     *              (If this value is negative, the list will be traversed in reverse order)
     * @return A slice of the arguments.
     */
    public ArgumentContainer slice(Integer start, Integer stop, Integer step) {
        return new SliceIterable(this, start, stop, step);
    }

    /**
     * Slices the arguments of the argument parser.
     * @param start The first argument that is included in the slice.
     * @param stop  The first argument that should <b>not</b> be included in the slice.
     * @return A slice of the arguments.
     */
    public ArgumentContainer slice(Integer start, Integer stop) {
        return this.slice(start, stop, null);
    }

    /**
     * Since this list is unmodifiable, we just use our own slice-implementation that provides
     * these features for us.
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return The sublist for the abstract list.
     */
    @Override
    public ArgumentContainer subList(int fromIndex, int toIndex) {
        return this.slice(fromIndex, toIndex);
    }

    /**
     * Returns an ArgumentIterable that contains all arguments from the given argument (inclusive).
     * @param start The first argument that is included in the iterable.
     * @return An iterable with arguments.
     */
    public ArgumentContainer from(int start) {
        return this.slice(start, null);
    }

    /**
     * Returns an ArgumentIterable that contains all arguments to the given argument (exclusive).
     * @param stop The first argument that is to be excluded in the iterable.
     * @return An iterable with arguments.
     */
    public ArgumentContainer to(int stop) {
        return this.slice(null, stop);
    }

    /**
     * Returns an ArgumentIterable that conains every n-th(step) argument.
     * @param step The n in n-th.
     * @return A new iterable.
     */
    public ArgumentContainer step(int step) {
        return this.slice(null, null, step);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public int getInt(int index) {
        return this.get(index, int.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public int getInt(int index, int def) {
        return this.get(index, int.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public float getFloat(int index) {
        return this.get(index, float.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public float getFloat(int index, float def) {
        return this.get(index, float.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public double getDouble(int index) {
        return this.get(index, double.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public double getDouble(int index, double def) {
        return this.get(index, double.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public boolean getBoolean(int index) {
        return this.get(index, boolean.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public boolean getBoolean(int index, boolean def) {
        return this.get(index, boolean.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public char getChar(int index) {
        return this.get(index, char.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public char getChar(int index, char def) {
        return this.get(index, char.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public long getLong(int index) {
        return this.get(index, long.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public long getLong(int index, long def) {
        return this.get(index, long.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public short getShort(int index) {
        return this.get(index, short.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public short getShort(int index, short def) {
        return this.get(index, short.class, def);
    }


    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public byte getByte(int index) {
        return this.get(index, byte.class);
    }

    /**
     * <p>Returns the converted argument at the given index.</p>
     *
     * <p>The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.</p>
     *
     * <p>If the given argument was not found or couldn't be converted, null will be returned.</p>
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public byte getByte(int index, byte def) {
        return this.get(index, byte.class, def);
    }

    /**
     * Returns the real index as specified in get.
     * @param index       The index passed in get.
     * @param exclusive   Is this argument exclusive or inclusive.
     * @return The real index or -1 if the index is invalid.
     */
    protected int getRealIndex(int index, boolean exclusive) {
        int cnt = this.size();
        if (index > cnt) {
            // Index greater than the size of arguments.
            return -1;
        } else if (!exclusive && index == cnt) {
            // Disallow passing the actual length of the argument.
            return -1;
        } else if (index < 0) {
            // Support python-like indices.
            index += cnt;

            // If the index is still invalid, throw an exception.
            if (index < 0) {
                return -1;
            }
        }

        return index;
    }

    /**
     * Returns the real index as specified in get.
     * @param index The index passed in get.
     * @return The real index or -1 if the index is invalid.
     */
    protected int getRealIndex(int index) {
        return this.getRealIndex(index, false);
    }
}
