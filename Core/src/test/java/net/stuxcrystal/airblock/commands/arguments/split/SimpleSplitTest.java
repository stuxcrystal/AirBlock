package net.stuxcrystal.airblock.commands.arguments.split;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SimpleSplitTest {

    SimpleSplit qs = new SimpleSplit();

    static String[] INPUT = new String[] {
            "test a \"a \"b 'c 'd",
            "-a test test",
            "-a -c test"
    };

    static String[][] OUTPUT = new String[][] {
            new String[] {"", "test", "a", "\"a", "\"b", "'c", "'d"},
            new String[] {"a", "test", "test"},
            new String[] {"a", "-c", "test"},
    };

    @Test
    public void testSplit() throws Exception {
        for (int i = 0; i< SimpleSplitTest.INPUT.length; i++) {
            String[] values = this.qs.split(SimpleSplitTest.INPUT[i], true);

            try {
                assertArrayEquals(SimpleSplitTest.OUTPUT[i], values);
            } catch (AssertionError e) {
                throw new AssertionError("[" + i + "] " + ArrayUtils.toString(SimpleSplitTest.OUTPUT[i]) + " != " + ArrayUtils.toString(values), e);
            }
        }
    }
}