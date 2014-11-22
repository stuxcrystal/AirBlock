package net.stuxcrystal.airblock.commands.arguments.split;

/**
 * This splitter does not split.
 */
public class NoSplit implements ArgumentSplitter {


    /**
     * So simple it doesn't need a test.
     * @param raw The raw string that should be split.
     * @param parseFlags Ignored.
     * @return The raw value.
     */
    @Override
    public String[] split(String raw, boolean parseFlags) {
        return new String[] {"", raw};
    }
}
