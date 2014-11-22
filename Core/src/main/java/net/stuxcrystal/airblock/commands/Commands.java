package net.stuxcrystal.airblock.commands;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.SubCommand;
import net.stuxcrystal.airblock.commands.core.list.Command;
import net.stuxcrystal.airblock.commands.core.list.CommandList;
import net.stuxcrystal.airblock.commands.core.list.CommandRegistrar;
import net.stuxcrystal.airblock.commands.core.settings.CommandLocale;
import net.stuxcrystal.airblock.commands.core.settings.CommandSettings;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the commands.
 */
@AllArgsConstructor
public class Commands {

    /**
     * Contains all commands.
     */
    private CommandList commands;

    /**
     * Contains all children of this commands.
     */
    private List<Commands> children;

    /**
     * The locale of the command.
     */
    private final CommandSettings locale;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The new commands class.
     * @param parent The parent locale
     */
    public Commands(@NonNull CommandSettings parent) {
        this.commands = new CommandList();
        this.children = new ArrayList<Commands>();
        this.locale = new CommandLocale(parent);
    }

    /**
     * Copy constructor.
     * @param parent The copy constructor.
     */
    public Commands(@NonNull Commands parent) {
        this(parent.locale);
    }

    /**
     * Standard constructor for Commands.
     */
    public Commands() {
        this.commands = new CommandList();
        this.children = new ArrayList<Commands>();
        this.locale = new CommandLocale(Environment.getInstance());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contains all registered commands.
     * @return All registered commands.
     */
    @NonNull
    public List<Command> getCommands() {
        List<Command> result = this.commands.getCommands();
        for (Commands child : this.children)
            result.addAll(child.getCommands());
        return result;
    }

    /**
     * Returns the commands for the list.
     * @param name The value of the command.
     * @return All commands with the given value.
     */
    @NonNull
    public List<Command> getCommand(@NonNull String name) {
        List<Command> result = commands.getCommand(name);
        for (Commands child : this.children)
            result.addAll(child.getCommand(name));
        return result;
    }

    /**
     * Registers a new command.
     * @param command The new command to add.
     */
    public Commands register(@NonNull Object command) {
        this.commands.register(command);
        return this;
    }

    /**
     * Adds a registrar to the command list.
     * @param registrar The new command list.
     */
    public Commands addRegistrar(@NonNull CommandRegistrar registrar) {
        this.commands.addRegistrar(registrar);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tries to find and execute the first command that can be executed.
     *
     * @param command   The value of the command that should be executed.
     * @param executor  The executor of the command.
     * @param args      The arguments that have been passed to the command.
     * @return {@code true} if the command has been found and executed.
     */
    public boolean execute(@NonNull String command, @NonNull Executor executor, @NonNull String args) {
        // Make sure we push the context of the command-system to the executor.
        executor.pushContext(this.locale);

        // Try to find a suitable command that we had registered.
        if (this.commands.execute(command, executor, args))
            return true;

        // Make sure we pop it.
        executor.popContext();

        // Query child command handlers.
        for (Commands child : this.children)
            if (child.execute(command, executor, args))
                return true;

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new child command handler.
     * @return The new child command handler.
     */
    @NonNull
    public Commands newChild() {
        return new Commands(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a new command-object that is used for calling sub-commands.
     * @param name           The value of the command.
     * @param description    The desired description.
     * @return The sub-command-object.
     */
    public Command asSubCommand(@NonNull String name, @Nullable String description) {
        return new SubCommand(name, description, this);
    }

    /**
     * Returns a new command-object that is used for calling sub-commands.
     * @param name           The value of the command.
     * @return The sub-command-object.
     */
    public Command asSubCommand(@NonNull String name) {
        return this.asSubCommand(name, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
