package moe.encode.airblock.commands.core;

import moe.encode.airblock.commands.Executor;

/**
 * Filters the
 */
public interface ExecutionFilter {

    /**
     * Checks if the child-handler can execute the commands.
     * @param executor     The executor.
     * @param label        The label.
     * @param arguments    The arguments.
     * @return {@code true} if the child-handler can be used.
     */
    public boolean canExecute(Executor executor, String label, String arguments);

}
