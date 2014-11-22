package net.stuxcrystal.airblock.commands.localization.sources;

import net.stuxcrystal.airblock.commands.core.utils.Optional;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ArrayValueSourceTest {

    private Map<String, Optional<String>> result = new HashMap<String, Optional<String>>();

    private String[] values = new String[]{ "abc", "test" };

    public ArrayValueSourceTest() {
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
        ArrayValueSource avs = new ArrayValueSource(this.values);
        for (Map.Entry<String, Optional<String>> testvals : this.result.entrySet()) {
            assertEquals(testvals.getValue(), avs.get(testvals.getKey()));
        }
    }
}