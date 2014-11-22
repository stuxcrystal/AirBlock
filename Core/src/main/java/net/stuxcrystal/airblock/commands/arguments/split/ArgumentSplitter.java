package net.stuxcrystal.airblock.commands.arguments.split;

import javax.annotation.concurrent.Immutable;

/**
 * <p>Interface that splits arguments.</p>
 * <p>
 *
 * </p>
 */
@Immutable
public interface ArgumentSplitter {

    /**
     * <p>Splits a string.</p>
     * <p>
     *     The result array must always contain at least one element that will contain the flags
     *     passed to the argument list.
     *     All array other items will contain the actual array elements.
     * </p>
     * @param raw The raw string that should be split.
     * @return The splitter.
     */
    public String[] split(String raw, boolean parseFlags);

}
