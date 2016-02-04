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

package moe.encode.airblock.commands.arguments;

import moe.encode.airblock.commands.Executor;
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