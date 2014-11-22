package net.stuxcrystal.airblock.commands.core.list;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The implementation of the command net.stuxcrystal.airblock.commands.
 */
public class CommandList {

    /**
     * Contains all commands of the net.stuxcrystal.airblock.commands.
     */
    private List<Command> commands = new ArrayList<Command>();

    /**
     * Contains all commands.
     */
    private List<CommandRegistrar> registrars = new ArrayList<CommandRegistrar>();

    /**
     * Registers a new command.
     * @param command The new command to add.
     */
    @NonNull
    public void register(@NonNull Object command) {
        Objects.requireNonNull(command);

        if (command instanceof Command) {
            this.commands.add((Command) command);
            return;
        }

        for (CommandRegistrar registrar : this.registrars) {
            registrar.register(this, command);
        }
    }

    /**
     * Adds a registrar to the command list.
     * @param registrar The new command list.
     */
    public void addRegistrar(@NonNull CommandRegistrar registrar) {
        Objects.requireNonNull(registrar);
        this.registrars.add(registrar);
    }

    /**
     * Contains all registered commands.
     * @return All registered commands.
     */
    @NonNull
    public List<Command> getCommands() {
        return new ArrayList<Command>(this.commands);
    }

    /**
     * Returns the commands for the list.
     * @param name The value of the command.
     * @return All commands with the given value.
     */
    @NonNull
    public List<Command> getCommand(@NonNull String name) {
        Objects.requireNonNull(name);
        List<Command> commands = new ArrayList<Command>(this.commands.size());
        for (Command command : this.commands)
            if (command.getName().equals(name))
                commands.add(command);
        return commands;
    }

    /**
     * Tries to find and execute the first command that can be executed.
     *
     * @param command   The value of the command that should be executed.
     * @param executor  The executor of the command.
     * @param args      The arguments that have been passed to the command.
     * @return {@code true} if the command has been found and executed.
     */
    public boolean execute(@NonNull String command, @NonNull Executor executor, @NonNull String args) {
        for (Command cmd : this.getCommand(command)) {
            if (cmd.canExecute(executor, args)) {
                cmd.execute(executor, args);
                return true;
            }
        }
        return false;
    }

}
