package net.stuxcrystal.airblock.commands.core.hooks;

import net.stuxcrystal.airblock.commands.core.settings.Environment;
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