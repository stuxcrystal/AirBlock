package net.stuxcrystal.airblock.commands.localization.sources;

import net.stuxcrystal.airblock.utils.Optional;
import net.stuxcrystal.airblock.commands.localization.ValueSource;
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