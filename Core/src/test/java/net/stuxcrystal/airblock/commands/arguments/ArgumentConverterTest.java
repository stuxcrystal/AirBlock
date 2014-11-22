package net.stuxcrystal.airblock.commands.arguments;

import net.stuxcrystal.airblock.commands.Executor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArgumentConverterTest {

    @Test
    public void testParameterTypeCheck() {
        ArgumentConverter ac = new ArgumentConverter();
        ArgumentParser<?> intparse, stringparse, execparse;

        ac.registerParser(intparse = this.createParser(Integer.class));
        ac.registerParser(stringparse = this.createParser(String.class));
        ac.registerParser(execparse = this.createParser(Executor.class));

        assertEquals(intparse, ac.getParserFor(Integer.class));
        assertEquals(stringparse, ac.getParserFor(String.class));
        assertEquals(execparse, ac.getParserFor(Executor.class));
    }

    private ArgumentParser<?> createParser(Class<?> cls) {
        ArgumentParser parser = mock(ArgumentParser.class);
        when(parser.canConvert(any(ArgumentConverter.class), any(Class.class))).thenReturn(false);
        when(parser.canConvert(any(ArgumentConverter.class), eq(cls))).thenReturn(true);
        return parser;
    }

}