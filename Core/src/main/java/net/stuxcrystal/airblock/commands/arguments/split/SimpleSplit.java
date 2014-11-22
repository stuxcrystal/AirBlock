package net.stuxcrystal.airblock.commands.arguments.split;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A simple splitter that just splits after every space.
 */
public class SimpleSplit implements ArgumentSplitter {

    @Override
    public String[] split(String raw, boolean parseFlags) {
        String[] rawSplit = raw.split(" ");
        if (rawSplit.length == 0) {
            return new String[] {""};
        }

        if (parseFlags) {
            if (rawSplit[0].startsWith("-")) {
                rawSplit[0] = rawSplit[0].substring(1);
            } else {
                return ArrayUtils.add(rawSplit, 0, "");
            }
        }

        return rawSplit;
    }
}
