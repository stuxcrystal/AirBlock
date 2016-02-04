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

package moe.encode.airblock.commands.core.hooks;

import moe.encode.airblock.commands.core.settings.Environment;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class HookManagerTest {

    public static class HookImpl implements Hook {

        public Stack<String> lc = new Stack<String>();

    }

    public static class HookHandlerImpl {

        @HookHandler(priority = HookHandler.Priority.HIGH)
        public void highTest(HookImpl impl) {
            impl.lc.push("high");
        }

        @HookHandler(priority = HookHandler.Priority.MEDIUM)
        public void mediumTest(HookImpl impl) {
            impl.lc.push("medium");
        }

        @HookHandler(priority = HookHandler.Priority.LOW)
        public void lowTest(HookImpl impl) {
            impl.lc.push("low");
        }

    }


    @Test
    public void testCall() throws Exception {

        HookManager hm = new HookManager(mock(Environment.class));
        hm.registerHooks(new HookHandlerImpl());

        HookImpl hi = new HookImpl();
        hm.call(hi);

        System.out.println(hi.lc);
        assertEquals("low", hi.lc.pop());
        assertEquals("medium", hi.lc.pop());
        assertEquals("high", hi.lc.pop());

    }
}