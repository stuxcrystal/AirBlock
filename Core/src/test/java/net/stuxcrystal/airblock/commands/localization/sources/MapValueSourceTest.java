package net.stuxcrystal.airblock.commands.localization.sources;

import net.stuxcrystal.airblock.utils.Optional;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
            assertEquals(cResult.getValue(), mvs.get(cResult.getKey()));
        }

    }
}