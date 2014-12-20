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

package net.stuxcrystal.airblock.commands.contrib.annotations;

import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.ArgumentParser;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.commands.arguments.types.ArgumentParsers;
import net.stuxcrystal.airblock.commands.backend.BackendHandle;
import net.stuxcrystal.airblock.commands.contrib.annotations.injections.Argument;
import net.stuxcrystal.airblock.commands.contrib.annotations.injections.ScopeObject;
import net.stuxcrystal.airblock.commands.core.list.CommandList;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SimpleAnnotationRegistrarTest {

    public static class TestRegistration {

        String raw;

        @Command("test")
        public void execute(Executor executor, ArgumentList list) {

        }

        @Command
        public void ttt(Executor executor, ArgumentList list) {

        }

        @Command(description = "test test test")
        public void description(Executor executor, ArgumentList list) {

        }

        @Command(strategy = Command.Strategy.RAW, executors = {Command.Executor.CONSOLE, Command.Executor.PLAYER})
        public void rawCall(Executor executor, String raw) {
            this.raw = raw;
        }

        @Command(strategy = Command.Strategy.INJECTION, executors = {Command.Executor.CONSOLE, Command.Executor.PLAYER})
        public void inject(@ScopeObject(ScopeObject.Type.EXECUTOR) Executor executor, @Argument(value=5, to=9, step=-1) List<Integer> integers) {
            int[] result = new int[integers.size()];
            for (int i = 0; i<result.length; i++)
                result[i] = integers.get(i);
            assertArrayEquals(new int[]{10, 9, 8, 7, 6}, result);
        }

    }

    @Test
    public void testRegistration() {
        CommandList cl = new CommandList();
        cl.addRegistrar(new SimpleAnnotationRegistrar());

        TestRegistration tr = new TestRegistration();
        cl.register(tr);
        List<net.stuxcrystal.airblock.commands.core.list.Command> commands = cl.getCommands();
        assertEquals(5, commands.size());
        for (net.stuxcrystal.airblock.commands.core.list.Command command : commands) {
            assertTrue("Command name listed", Arrays.asList("test", "ttt", "description", "rawCall", "inject").contains(
                    command.getName())
            );
        }
        assertEquals("test test test", cl.getCommand("description").get(0).getDescription());
        assertEquals(null, cl.getCommand("ttt").get(0).getDescription());

        assertTrue("/rawCall not executed", cl.execute("rawCall", SimpleAnnotationRegistrarTest.getExecutor(), "test test test test"));
        assertEquals(tr.raw, "test test test test");



        assertTrue("/inject not executed", cl.execute("inject", SimpleAnnotationRegistrarTest.getExecutor(), "1 2 3 4 5 6 7 8 9 10"));
    }

    private static Executor getExecutor() {
        Environment environment = new Environment(mock(BackendHandle.class));
        for (ArgumentParser parser : ArgumentParsers.getDefaultParsers())
            environment.getArgumentConverter().registerParser(parser);

        Executor executor = mock(Executor.class);
        when(executor.getContext()).thenReturn(environment);
        when(executor.getEnvironment()).thenReturn(environment);
        when(executor.isConsole()).thenReturn(false);
        return executor;
    }


}