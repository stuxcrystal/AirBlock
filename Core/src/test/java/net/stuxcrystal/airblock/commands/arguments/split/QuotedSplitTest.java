package net.stuxcrystal.airblock.commands.arguments.split;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class QuotedSplitTest {

    /**
     * Contains the splitter.
     */
    QuotedSplit qs = new QuotedSplit();

    static String[] INPUT = new String[] {
            "test test",
            "test \"test test\"",

            "-f test test",
            "-f test \"test test\"",

            "-f -t test test",
            "-f -t test \"test test\"",

            "-f test\\\"",
            "-f test\\ test",

            "-f test\\\\test",
            "-f test \"test \\\"test\"",

            "\\-a test",
            "test -a test",

            "test a\"test test\"",
            "test \"\" test",
    };

    static String[][] OUTPUT = new String[][] {
            new String[] {"", "test", "test"},
            new String[] {"", "test", "test test"},
            new String[] {"f", "test", "test"},

            new String[] {"f", "test", "test test"},
            new String[] {"ft", "test", "test"},
            new String[] {"ft", "test", "test test"},

            new String[] {"f", "test\""},
            new String[] {"f", "test test"},

            new String[] {"f", "test\\test"},
            new String[] {"f", "test", "test \"test"},

            new String[] {"", "-a", "test"},
            new String[] {"", "test", "-a", "test"},

            new String[] {"", "test", "atest test"},
            new String[] {"", "test", "", "test"},
    };

    @Test
    public void testSplit() throws Exception {
        for (int i = 0; i< QuotedSplitTest.INPUT.length; i++) {
            String[] values = this.qs.split(QuotedSplitTest.INPUT[i], true);

            try {
                assertArrayEquals(QuotedSplitTest.OUTPUT[i], values);
            } catch (AssertionError e) {
                throw new AssertionError("[" + i + "] " + ArrayUtils.toString(QuotedSplitTest.OUTPUT[i]) + " != " + ArrayUtils.toString(values), e);
            }
        }
    }
}