package net.stuxcrystal.airblock.commands.core;

import net.stuxcrystal.airblock.commands.Executor;

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
