package moe.encode.airblock.configuration.storage;

import junit.framework.TestCase;
import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;
import moe.encode.airblock.configuration.ConfigurationLoader;
import org.apache.commons.lang3.ArrayUtils;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

public class ConfigurationModuleTest extends TestCase {


    public void testGetModulePath() throws Exception {
        ConfigurationStorage cl = mock(ConfigurationStorage.class);

        // We will use german tv channels as test names.
        String[] result = new String[] {"sat.1", "zdf", "ard", "rtl", "pro7"};
        ConfigurationModule module = new ConfigurationLoader(cl);
        for (String part : result)
            module = new ConfigurationModule(part, module);

        assertArrayEquals(result, module.getModulePath());

    }

    public void testGetEffectiveModulePath() throws Exception {
        ConfigurationStorage cl = mock(ConfigurationStorage.class);

        // Mecha!
        String[] pre = new String[] {"Neon Genesis Evangelion", "Infinite Stratos", "Star Driver", "Aldnoah.Zero"};
        // We will use some german tv channels as test names.
        String[] result = new String[] {"sat.1", "zdf", "ard", "rtl", "pro7"};
        ConfigurationModule module = new ConfigurationLoader(cl);
        for (String part : pre)
            module = new ConfigurationModule(part, module);
        module.setLocation(cl);

        for (String part : result)
            module = new ConfigurationModule(part, module);

        // As the given module will contain its own name, we will add it to the list
        String[] expected = ArrayUtils.add(result, 0, "Aldnoah.Zero");
        assertArrayEquals(expected, module.getEffectiveModulePath());
    }
}