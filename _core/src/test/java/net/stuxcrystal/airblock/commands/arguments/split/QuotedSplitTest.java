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