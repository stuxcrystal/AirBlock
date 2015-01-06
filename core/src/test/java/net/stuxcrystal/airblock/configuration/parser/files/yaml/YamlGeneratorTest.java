package net.stuxcrystal.airblock.configuration.parser.files.yaml;

import net.stuxcrystal.airblock.configuration.Configuration;
import net.stuxcrystal.airblock.configuration.Value;
import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class YamlGeneratorTest {

    @Configuration
    public static class TestClass {

        @Value(name = "test")
        public List<String> abc = Arrays.asList("a", "b", "c", "d");

        @Value(name = "test2")
        public boolean test  = false;

    }

    @Test
    public void testRecreation() throws IOException {
        ConfigurationParser cp = new ConfigurationParser();
        TestClass tc = new TestClass();
        tc.abc = Arrays.asList("b", "c", "f", "abc");
        tc.test = true;

        YamlGenerator yg = new YamlGenerator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yg.writeRaw(baos, cp.dump(tc));

        System.out.println(baos.toString("UTF-8"));

        TestClass tc2 = cp.parse(yg.load(new ByteArrayInputStream(baos.toByteArray())), TestClass.class);
        assertEquals(tc.abc, tc2.abc);
        assertEquals(tc.test, tc2.test);

    }
}