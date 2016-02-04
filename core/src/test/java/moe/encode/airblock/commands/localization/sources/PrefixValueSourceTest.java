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

package moe.encode.airblock.commands.localization.sources;

import moe.encode.airblock.utils.Optional;
import moe.encode.airblock.commands.localization.ValueSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PrefixValueSourceTest {

    private Map<String, Optional<String>> result = new HashMap<String, Optional<String>>();

    private String[] values = new String[]{ "abc", "test" };

    private String[] prefixes = new String[] {"ttt:", "0!", "s@", "t*"};

    public PrefixValueSourceTest() {
        this.result.put("t", Optional.<String>empty());
        this.result.put("-1", Optional.<String>empty());
        this.result.put("1a", Optional.<String>empty());
        this.result.put("1.5", Optional.<String>empty());
        this.result.put("1", Optional.from("test"));
        this.result.put("0", Optional.from("abc"));
        this.result.put("1000", Optional.<String>empty());
    }

    @Test
    public void testGet() throws Exception {
        for (String actualPrefix : prefixes) {
            ValueSource avs = new PrefixValueSource(actualPrefix, new ArrayValueSource(this.values));
            for (String checkPrefix : prefixes) {
                for (Map.Entry<String, Optional<String>> testvals : this.result.entrySet()) {
                    Optional<?> result = avs.get(checkPrefix + testvals.getKey());
                    if (actualPrefix.equals(checkPrefix))
                        assertEquals(testvals.getValue(), result);
                    else
                        assertEquals(Optional.<String>empty(), result);
                }
            }
        }
    }
}