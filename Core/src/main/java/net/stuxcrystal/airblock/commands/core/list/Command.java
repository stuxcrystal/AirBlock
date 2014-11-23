package net.stuxcrystal.airblock.commands.core.list;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Defines a command
 */
@Immutable
public interface Command {

    /**
     * Returns the value of the command.
     * @return The value of the command.
     */
    @NonNull
    public String getName();

    /**
     * Returns the description for the command.
     * @return The description for the command.
     */
    @Nullable
    public String getDescription();

    /**
     * Can the executor execute the command.
     * @param executor      The entity that executes the command.
     * @param rawArguments  The raw arguments as a single string.
     * @return If the command can be executed by the executor.
     */
    public boolean canExecute(@NonNull Executor executor, @Nullable String rawArguments);

    /**
     * Executes the command.
     * @param executor       The executor that executes the command.
     * @param rawArguments   The arguments as a single string.
     */
    public void execute(@NonNull Executor executor, @NonNull String rawArguments);

}
