package net.stuxcrystal.airblock.commands.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubCommandTest {

    static final String[] toParse = new String[] {
            null,
            "",
            "ab",
            "ab abc",
            "ab abc def"
    };

    static final String[][] result = new String[][] {
            new String[] {"", ""},
            new String[] {"", ""},
            new String[] {"ab", ""},
            new String[] {"ab", "abc"},
            new String[] {"ab", "abc def"}
    };

    @Test
    public void testSplitArguments() throws Exception {
        for (int i = 0; i<SubCommandTest.toParse.length; i++) {
            String[] result = SubCommand.splitArguments(SubCommandTest.toParse[i]);
            assertArrayEquals(SubCommandTest.result[i], result);
        }
    }
}