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
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapValueSourceTest {

    private Map<String, String> values = new HashMap<String, String>();

    private Map<String, Optional<String>> result = new HashMap<String, Optional<String>>();

    public MapValueSourceTest() {
        this.values.put("t", null);
        this.values.put("-1", "47");
        this.values.put("1a", "sz");


        this.result.put("t", Optional.<String>from(null));
        this.result.put("-1", Optional.from("47"));
        this.result.put("1a", Optional.from("sz"));
        this.result.put("az", Optional.<String>empty());
    }

    @Test
    public void testGet() throws Exception {

        MapValueSource mvs = new MapValueSource(this.values);
        for (Map.Entry<String, Optional<String>> cResult : this.result.entrySet()) {
            Assert.assertEquals(cResult.getValue(), mvs.get(cResult.getKey()));
        }

    }
}