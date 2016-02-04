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

package moe.encode.airblock.commands;

import moe.encode.airblock.commands.core.list.Command;
import moe.encode.airblock.commands.core.list.CommandList;
import moe.encode.airblock.commands.core.list.CommandRegistrar;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CommandListTest {

    /**
     * Represents a test registrar.
     */
    public class TestRegistrar implements CommandRegistrar {

        @Override
        public void register(CommandList list, Object object) {
            if (!(object instanceof String))
                return;
            list.register(CommandListTest.this.createNewCommand((String)object));
        }
    }

    private static String[] names = new String[]{"air", "block", "foo", "bar"};

    @Test
    public void testCommandAdding() {
        CommandList cl = new CommandList();
        for (String name : CommandListTest.names)
            cl.register(this.createNewCommand(name));

        List<String> names = new ArrayList<String>(cl.getCommands().size());
        for (Command cmd : cl.getCommands())
            names.add(cmd.getName());

        assertThat(names.size(), is(CommandListTest.names.length));
        assertThat(names, containsInAnyOrder(CommandListTest.names));
    }

    @Test
    public void testCommandSearching() {
        CommandList cl = new CommandList();
        for (String name : CommandListTest.names)
            cl.register(this.createNewCommand(name));


        assertThat(cl.getCommand("block"), hasSize(1));
    }

    @Test
    public void testCommandRegistrar() {
        CommandList cl = new CommandList();
        cl.addRegistrar(new TestRegistrar());
        for (String name : CommandListTest.names)
            cl.register(name);

        List<String> names = new ArrayList<String>(cl.getCommands().size());
        for (Command cmd : cl.getCommands())
            names.add(cmd.getName());

        assertThat(names.size(), is(CommandListTest.names.length));
        assertThat(names, containsInAnyOrder(CommandListTest.names));
    }

    @Test
    public void testCommandExexution() {
        CommandList cl = new CommandList();
        cl.addRegistrar(new TestRegistrar());
        for (String name : CommandListTest.names)
            cl.register(name);

        Command cmd = createNewCommand("spam");
        when(cmd.canExecute(any(Executor.class), any(String.class))).thenReturn(true);
        cl.register(cmd);

        Command cmd2 = createNewCommand("spam2");
        cl.register(cmd2);

        assertFalse(cl.execute("spam2", mock(Executor.class), ""));
        assertTrue(cl.execute("spam", mock(Executor.class), ""));

        verify(cmd, times(1)).canExecute(any(Executor.class), any(String.class));
        verify(cmd, times(1)).execute(any(Executor.class), any(String.class));

        verify(cmd2, times(1)).canExecute(any(Executor.class), any(String.class));
        verify(cmd2, never()).execute(any(Executor.class), any(String.class));
    }

    private Command createNewCommand(String name) {
        Command cmd = mock(Command.class);
        when(cmd.getName()).thenReturn(name);
        when(cmd.canExecute(any(Executor.class), any(String.class))).thenReturn(false);
        return cmd;
    }
}
