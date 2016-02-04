package moe.encode.airblock.commands.contrib.annotations;

import moe.encode.airblock.commands.Executor;

/**
 * Strategy for command calling.
 */
public interface CommandCallingStrategy {

    /**
     * Executes the command.
     * @param command   The command that has been executed.
     * @param executor  The executor that is executed.
     * @param raw       The raw command.
     */
    public void call(AnnotationCommand command, Executor executor, String raw);

    /**
     * The raw name of the command.
     * @return The raw name.
     */
    public String getName();

}
