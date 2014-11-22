package net.stuxcrystal.airblock.commands.contrib.annotations;

import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.commands.core.list.Command;
import net.stuxcrystal.airblock.commands.core.list.CommandList;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleAnnotationRegistrarTest {

    public static class TestRegistration {

        @SimpleCommand("test")
        public void execute(Executor executor, ArgumentList list) {

        }

        @SimpleCommand
        public void ttt(Executor executor, ArgumentList list) {

        }

        @SimpleCommand(description = "test test test")
        public void description(Executor executor, ArgumentList list) {

        }

    }

    @Test
    public void testRegistration() {
        CommandList cl = new CommandList();
        cl.addRegistrar(new SimpleAnnotationRegistrar());

        cl.register(new TestRegistration());
        List<Command> commands = cl.getCommands();
        assertEquals(3, commands.size());
        for (Command command : commands) {
            assertTrue("Command name listed", Arrays.asList("test", "ttt", "description").contains(command.getName()));
        }
        assertEquals("test test test", cl.getCommand("description").get(0).getDescription());
        assertEquals(null, cl.getCommand("ttt").get(0).getDescription());
    }


}