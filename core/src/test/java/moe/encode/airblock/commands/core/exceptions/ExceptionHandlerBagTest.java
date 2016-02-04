package moe.encode.airblock.commands.core.exceptions;

import junit.framework.TestCase;
import moe.encode.airblock.commands.Commands;
import moe.encode.airblock.commands.Executor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for exceptions.
 */
public class ExceptionHandlerBagTest extends TestCase {

    public static class MockExceptionHandlerImplementation<T extends Throwable> implements ExceptionHandler<T> {

        List<Throwable> exceptions = new ArrayList<Throwable>();


        @Override
        public void handle(Throwable exception, Commands commands, Executor executor, String label, String raw) {
            this.exceptions.add(exception);
        }
    }

    public void checkException(MockExceptionHandlerImplementation mehi, Throwable e) {
        if (e == null) {
            assertEquals(0, mehi.exceptions.size());
            return;
        }
        assertEquals(1, mehi.exceptions.size());
        assertEquals(e, mehi.exceptions.get(0));
        mehi.exceptions.clear();
    }

    public void testSingleHandler() throws Exception {
        MockExceptionHandlerImplementation mehi = new MockExceptionHandlerImplementation();
        MockExceptionHandlerImplementation mehi2 = new MockExceptionHandlerImplementation();

        Exception exc = new RuntimeException();
        Exception exc2 = new IOException();
        Exception exc3 = new FileNotFoundException();

        ExceptionHandlerBag bag = new ExceptionHandlerBag(null);

        // Register Simple Handler.
        bag.registerHandler(Throwable.class, mehi);

        bag.onException(exc, null, null, null, null);
        checkException(mehi, exc);

        bag.registerHandler(IOException.class, mehi2);
        bag.onException(exc, null, null, null, null);
        checkException(mehi, exc);
        checkException(mehi2, null);

        bag.onException(exc2, null, null, null, null);
        checkException(mehi, null);
        checkException(mehi2, exc2);

        bag.onException(exc3, null, null, null, null);
        checkException(mehi, null);
        checkException(mehi2, exc3);
    }

    public void testParentedHandler() throws Exception {
        MockExceptionHandlerImplementation mehi1 = new MockExceptionHandlerImplementation();
        MockExceptionHandlerImplementation mehi2 = new MockExceptionHandlerImplementation();
        MockExceptionHandlerImplementation mehi3 = new MockExceptionHandlerImplementation();
        MockExceptionHandlerImplementation mehi4 = new MockExceptionHandlerImplementation();

        ExceptionHandlerBag bag1 = new ExceptionHandlerBag(null);
        ExceptionHandlerBag bag2 = new ExceptionHandlerBag(bag1);

        Exception exc1 = new RuntimeException();
        Exception exc2 = new IOException();
        Exception exc3 = new FileNotFoundException();
        Exception exc4 = new Exception();

        // Register Simple Handler.
        bag1.registerHandler(Throwable.class, mehi1);
        bag1.registerHandler(IOException.class, mehi2);
        bag2.registerHandler(RuntimeException.class, mehi3);

        bag2.onException(exc1, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, null);
        checkException(mehi3, exc1);

        bag2.onException(exc2, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, exc2);
        checkException(mehi3, null);

        bag2.onException(exc3, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, exc3);
        checkException(mehi3, null);

        bag2.onException(exc4, null, null, null, null);
        checkException(mehi1, exc4);
        checkException(mehi2, null);
        checkException(mehi3, null);

        bag2.registerHandler(IOException.class, mehi4);

        bag2.onException(exc1, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, null);
        checkException(mehi3, exc1);
        checkException(mehi4, null);

        bag2.onException(exc2, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, null);
        checkException(mehi3, null);
        checkException(mehi4, exc2);

        bag2.onException(exc3, null, null, null, null);
        checkException(mehi1, null);
        checkException(mehi2, null);
        checkException(mehi3, null);
        checkException(mehi4, exc3);

        bag2.onException(exc4, null, null, null, null);
        checkException(mehi1, exc4);
        checkException(mehi2, null);
        checkException(mehi3, null);
        checkException(mehi4, null);
    }
}