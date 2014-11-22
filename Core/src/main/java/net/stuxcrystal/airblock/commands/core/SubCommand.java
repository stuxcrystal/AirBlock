package net.stuxcrystal.airblock.commands.core;

import lombok.Getter;
import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Commands;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.list.Command;

import javax.annotation.Nullable;

/**
 * Created by Stux on 16.11.2014.
 */
public class SubCommand implements Command {

    /**
     * The value of the sub-command.
     */
    @Getter
    private final String name;

    /**
     * The description of the subcommand.
     */
    @Getter
    private final String description;

    /**
     * Contains the commands.
     */
    private final Commands commands;

    /**
     * Contains the container for sub-commands.
     * @param name        The value of the sub-command.
     * @param description The description for the sub-command.
     * @param container   The container for the sub-commands.
     */
    public SubCommand(@NonNull String name, @Nullable String description, @NonNull Commands container) {
        this.name = name;
        this.description = description;
        this.commands = container;
    }

    @Override
    public boolean canExecute(@NonNull Executor executor, @NonNull String rawArguments) {
        return true;
    }

    @Override
    public void execute(@NonNull Executor executor, @NonNull String rawArguments) {
        String[] splitter = rawArguments.split(" ", 2);
        this.commands.execute(splitter[0], executor, splitter.length==2?splitter[1]:"");
    }
}
