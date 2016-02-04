/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.encode.airblock.commands;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import moe.encode.airblock.commands.core.CommandImplementation;
import moe.encode.airblock.commands.core.ExecutionFilter;
import moe.encode.airblock.commands.core.SubCommand;
import moe.encode.airblock.commands.core.exceptions.PermissionDenied;
import moe.encode.airblock.commands.core.list.Command;
import moe.encode.airblock.commands.core.list.CommandList;
import moe.encode.airblock.commands.core.list.CommandRegistrar;
import moe.encode.airblock.commands.core.settings.CommandLocale;
import moe.encode.airblock.commands.core.settings.CommandSettings;
import moe.encode.airblock.commands.core.settings.Environment;
import moe.encode.airblock.commands.localization.TranslationManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Handles the commands.
 */
@AllArgsConstructor
public class Commands implements CommandImplementation {

    /**
     * The default filter that allows everyone to use the commands.
     */
    public static ExecutionFilter DEFAULT_FILTER = new ExecutionFilter() {
        @Override
        public boolean canExecute(Executor executor, String label, String arguments) {
            return true;
        }
    };

    /**
     * Storage for all child handlers.
     */
    @AllArgsConstructor
    public class ChildHandler {

        /**
         * The command.
         */
        final Commands command;

        /**
         * The filter.
         */
        final ExecutionFilter filter;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contains all commands.
     */
    private CommandList commands;

    /**
     * Contains all children of this commands.
     */
    private List<ChildHandler> children;

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
        this.children = new ArrayList<ChildHandler>();
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
        this.children = new ArrayList<ChildHandler>();
        this.locale = new CommandLocale(Environment.getInstance());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contains the settings of the current command object.
     * @return The settings.
     */
    public CommandSettings getSettings() {
        return this.locale;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contains all registered commands.
     * @return All registered commands.
     */
    @NonNull
    public List<Command> getCommands() {
        List<Command> result = this.commands.getCommands();
        for (ChildHandler child : this.children)
            result.addAll(child.command.getCommands());
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
        for (ChildHandler child : this.children)
            result.addAll(child.command.getCommand(name));
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
     *         {@code null} if the command has been found but the user did not have permission.
     */
    public Boolean runCommand(@NonNull String command, @NonNull Executor executor, @NonNull String args) {
        // Make sure we push the context of the command-system to the executor.
        // We will not pop the context - EVER!
        executor.pushContext(this.locale);

        Boolean result = false;

        // Try to find a suitable command that we had registered.
        if (this.commands.execute(command, executor, args)) {
            result = true;
        } else if (this.commands.getCommand(command).size() != 0) {
            result = null;
        }

        if (result == null || !result) {
            // Query child command handlers.
            for (ChildHandler child : this.children) {
                // Filter out unwanted handlers.
                if (!child.filter.canExecute(executor, command, args))
                    continue;

                // The command that should be executed.
                result = child.command.runCommand(command, executor, args);
                if (result != null && result)
                    return true;
            }
        }

        return result;
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

    /**
     * Adds the given child to the command handler.
     * @param commands  The commands.
     * @param filter    The filter.
     * @return Itself.
     */
    @NonNull
    public Commands addChild(@NonNull Commands commands, @Nullable ExecutionFilter filter) {
        this.children.add(new ChildHandler(commands, filter!=null?filter:Commands.DEFAULT_FILTER));
        return this;
    }

    /**
     * Adds the given child to the command handler.
     * @param commands  The commands.
     * @return Itself.
     */
    @Nullable
    public Commands addChild(@NonNull Commands commands) {
        return this.addChild(commands, Commands.DEFAULT_FILTER);
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

    @Override
    public void execute(Executor executor, String label, String arguments) {
        Boolean result;
        try {
            result = this.runCommand(label, executor, arguments);

        } catch(PermissionDenied e) {
            // Runs the result.
            result = null;
        } catch (Throwable e) {
            // The exception handler.
            this.locale.getExceptionHandlers().onException(e, this, executor, label, arguments);
            return;
        }

        if (result == null) {
            // If the result is null, the user did not have permission to execute the command.
            executor.sendMessage(
                    this.locale.getEnvironment().getTranslationManager().translate(
                            executor, TranslationManager.COMMAND_NO_PERMISSION, label
                    )
            );
        } else if (!result) {
            // Otherwise there was really no command found.
            executor.sendMessage(
                    this.locale.getEnvironment().getTranslationManager().translate(
                            executor, TranslationManager.COMMAND_NOT_FOUND, label
                    )
            );
        }
    }

    @Override
    public String getDescription(Executor executor, String label) {
        List<Command> commands = this.getCommand(label);
        for (Command command : commands) {
            if (command.canExecute(executor, null))
                return command.getDescription();
        }

        if (commands.size() > 0)
            return commands.get(0).getDescription();
        return this.locale.getEnvironment().getTranslationManager().translate(executor, "No description available");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Registers the base commands.
     * @return this
     */
    public Commands registerBaseCommands() {
        HashSet<String> registered = new HashSet<String>();
        for (Command command : this.getCommands()) {
            if (registered.contains(command.getName()))
                continue;
            registered.add(command.getName());
            this.getSettings().getEnvironment().getBackend().getHandle().registerCommand(
                    command.getName(),
                    this
            );
        }
        return this;
    }

    /**
     * Register the given Commands-Instance as a single command.
     * @param name The given description.
     * @return this
     */
    public Commands registerCommand(String name, final String description) {
        this.getSettings().getEnvironment().getBackend().getHandle().registerCommand(
                name,
                new CommandImplementation() {
                    @Override
                    public void execute(Executor executor, String label, String arguments) {
                        String[] parsed = SubCommand.splitArguments(arguments);
                        Commands.this.runCommand(parsed[0], executor, arguments);
                    }

                    @Override
                    public String getDescription(Executor executor, String label) {
                        return description;
                    }
                }
        );
        return this;
    }
}
