package net.stuxcrystal.airblock.commands;

import net.stuxcrystal.airblock.commands.core.list.CommandList;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * A test for the command manager.
 */
public class CommandsTest {

    @Test
    public void testCommandDelegation() {
        CommandList cl = mock(CommandList.class);
        when(cl.execute(any(String.class), any(Executor.class), any(String.class))).thenReturn(false);

        Commands cmds = new Commands(cl, new ArrayList<Commands>(), mock(Environment.class));
        Assert.assertFalse("Command-Delegation reported that a command has been executed.", cmds.execute("cmd", mock(Executor.class), ""));

        // Verify that we tried to execute the commands test.
        verify(cl).execute(eq("cmd"), any(Executor.class), any(String.class));
    }

    @Test
    public void testChildCommandDelegation() {

        List<Commands> t_cmds = new ArrayList<Commands>();
        for (int i = 0; i<10; i++) {
            Commands mcl = mock(Commands.class);
            when(mcl.execute(any(String.class), any(Executor.class), any(String.class))).thenReturn(false);
            t_cmds.add(mcl);
        }

        Commands t_cmd = mock(Commands.class);
        when(t_cmd.execute(any(String.class), any(Executor.class), any(String.class))).thenReturn(true);

        List<Commands> n_cmds = new ArrayList<Commands>();
        for (int i = 0; i<10; i++) {
            Commands mcl = mock(Commands.class);
            when(mcl.execute(any(String.class), any(Executor.class), any(String.class))).thenReturn(false);
            n_cmds.add(mcl);
        }

        List<Commands> a_cmds = new ArrayList<Commands>(21);
        a_cmds.addAll(t_cmds);
        a_cmds.add(t_cmd);
        a_cmds.addAll(n_cmds);

        CommandList fcl = mock(CommandList.class);
        when(fcl.execute(any(String.class), any(Executor.class), any(String.class))).thenReturn(false);

        Commands tCmd = new Commands(fcl, a_cmds, mock(Environment.class));
        Assert.assertTrue("Command delegation reported that there was a command.", tCmd.execute("cmd", mock(Executor.class), ""));

        for (Commands commands : t_cmds) {
            verify(commands, times(1)).execute(eq("cmd"), any(Executor.class), any(String.class));
        }

        verify(t_cmd, times(1)).execute(eq("cmd"), any(Executor.class), any(String.class));

        for (Commands commands : n_cmds) {
            verify(commands, never()).execute(any(String.class), any(Executor.class), any(String.class));
        }

    }

}