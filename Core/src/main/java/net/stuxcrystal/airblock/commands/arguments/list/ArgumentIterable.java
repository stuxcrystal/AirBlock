package net.stuxcrystal.airblock.commands.arguments.list;

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
    protected final Class<T> type;

    /**
     * Creates a new abstract argument iterable.
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public ArgumentIterable(ArgumentContainer parent, Class<T> type) {
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
        return this.getParent().getArguments(type);
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
