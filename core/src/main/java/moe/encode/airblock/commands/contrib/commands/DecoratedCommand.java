package moe.encode.airblock.commands.contrib.commands;

import lombok.Getter;
import lombok.NonNull;
import moe.encode.airblock.commands.Executor;
import moe.encode.airblock.commands.core.list.Command;

import javax.annotation.Nullable;

/**
 * Decorate the can-execute method.
 */
public abstract class DecoratedCommand implements Command {

    /**
     * The command that should be executed.
     */
    @Getter
    private final Command command;

    /**
     * Creates a new decorator.
     * @param command The command.
     */
    protected DecoratedCommand(@NonNull Command command) {
        this.command = command;
    }

    /**
     * Returns the value of the command.
     *
     * @return The value of the command.
     */
    @Override
    public String getName() {
        return this.command.getName();
    }

    /**
     * Returns the description for the command.
     *
     * @return The description for the command.
     */
    @Nullable
    @Override
    public String getDescription() {
        return this.command.getDescription();
    }

    /**
     * Can the executor execute the command.
     *
     * @param executor     The entity that executes the command.
     * @param rawArguments The raw arguments as a single string.
     * @return If the command can be executed by the executor.
     */
    @Override
    public boolean canExecute(@NonNull Executor executor, @Nullable String rawArguments) {
        return this.checkExecuteable(executor, rawArguments) && this.command.canExecute(executor, rawArguments);
    }

    /**
     * Executes the command.
     *
     * @param executor     The executor that executes the command.
     * @param rawArguments The arguments as a single string.
     */
    @Override
    public void execute(@NonNull Executor executor, @NonNull String rawArguments) {
        this.command.execute(executor, rawArguments);
    }

    /**
     * Check if the executor can execute the command.
     * @param executor        The executor.
     * @param rawArguments    The raw arguments.
     * @return {@code true} if the command can be executed.
     */
    public boolean checkExecuteable(@NonNull Executor executor, @Nullable String rawArguments) {
        return true;
    }
}
