package net.stuxcrystal.airblock.commands.core;

import net.stuxcrystal.airblock.commands.Executor;

/**
 * <p>Contains the implementation of a command.</p>
 * <p>
 *     This is a backend-class to pass command-implementations.
 * </p>
 */
public interface CommandImplementation {

    /**
     * Executes the given command.
     * @param executor  The executor of the command.
     * @param label     The name of the command.
     * @param arguments The arguments that have been passed to the command.
     */
    public void execute(Executor executor, String label, String arguments);

    /**
     * Returns the description of the command.
     * @param executor  The executor.
     * @param label     The name of the command.
     */
    public String getDescription(Executor executor, String label);

}
