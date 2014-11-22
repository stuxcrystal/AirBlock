package net.stuxcrystal.airblock.commands.arguments.list;

import net.stuxcrystal.airblock.commands.Executor;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Implementation of the argument list.
 */
public class ArgumentList extends ArgumentContainer {

    /**
     * Contains all values.
     */
    protected final String[] values;

    /**
     * Defines the flags.
     */
    protected final String flags;

    /**
     * Contains the executor.
     */
    private final Executor executor;

    /**
     * Parses the arguments and the flags.
     * @param arguments   The arguments to flags.
     * @param executor    The flags.
     */
    public ArgumentList(String arguments, Executor executor) {
        this(arguments, true, executor);
    }

    /**
     * Creates a new arguments list and splits the raw arguments.
     * @param arguments   The raw string of arguments.
     * @param parseFlags  Parse the flags.
     * @param executor    The executor.
     */
    public ArgumentList(String arguments, boolean parseFlags, Executor executor) {
        this(executor.getContext().getArgumentSplitter().split(arguments, parseFlags), parseFlags, executor);
    }

    /**
     * Creates a new argument list.
     * @param arguments    The arguments that have been passed to the command.
     * @param executor     The executor of the command.
     */
    public ArgumentList(String[] arguments, Executor executor) {
        this(arguments, false, executor);
    }

    /**
     * Creates a new argument list.
     * @param arguments    The arguments that have been passed to the command.
     * @param parseFlags   Check if the flags should be parsed.
     * @param executor     The executor of the command.
     */
    public ArgumentList(String[] arguments, boolean parseFlags, Executor executor) {
        super(null);
        if (parseFlags) {
            this.values = ArrayUtils.remove(arguments, 0);
            this.flags = arguments[0];
        } else {
            this.values = arguments;
            this.flags = "";
        }
        this.executor = executor;
    }

    /**
     * Returns all flags that have been passed.
     * @return All flags that have been passed.
     */
    public String getFlags() {
        return this.flags;
    }

    /**
     * Checks if this flag has been passed to the command.
     * @param flag Checks if the flag has been passed to the command.
     * @return The flag that has been passed.
     */
    public boolean hasFlag(char flag) {
        return this.flags.contains("" + flag);
    }

    @Override
    public int size() {
        return this.values.length;
    }

    @Override
    public <E> E get(int index, Class<? extends E> cls) throws NumberFormatException {
        // Find the real index.
        int i = this.getRealIndex(index);

        // Check if the index exists.
        if (i == -1)
            throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));

        // Parse the values.
        return this.executor.getContext().getArgumentConverter().parse(this.executor, cls, this.values[i]);
    }

    @Override
    public ArgumentList getArgumentList() {
        return this;
    }
}
