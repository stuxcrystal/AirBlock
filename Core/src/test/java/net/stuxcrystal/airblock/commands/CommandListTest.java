package net.stuxcrystal.airblock.commands;

import net.stuxcrystal.airblock.commands.core.list.Command;
import net.stuxcrystal.airblock.commands.core.list.CommandList;
import net.stuxcrystal.airblock.commands.core.list.CommandRegistrar;
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
