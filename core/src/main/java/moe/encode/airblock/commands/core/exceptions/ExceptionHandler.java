package moe.encode.airblock.commands.core.exceptions;

import moe.encode.airblock.commands.Commands;
import moe.encode.airblock.commands.Executor;

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
