package net.stuxcrystal.airblock.commands.core.exceptions;

import net.stuxcrystal.airblock.commands.Commands;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;

/**
 * Handler for exceptions.
 */
public interface ExceptionHandler<T extends Throwable> {

    /**
     * Handles the given exception.
     * @param exception   The exception that occured.
     * @param commands    The commands-container that was executed.
     * @param executor    The executor who executed the command.
     * @param label       The label of the command that has been executed.
     * @param raw         The raw arguments that has been passed.
     */
    public void handle(T exception, Commands commands, Executor executor, String label, String raw);

}
