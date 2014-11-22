package net.stuxcrystal.airblock.commands.arguments.list;

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
    public TypedArgumentIterable(ArgumentContainer parent, Class<T> type) {
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
