package net.stuxcrystal.airblock.sponge;

import net.stuxcrystal.airblock.commands.core.CommandImplementation;
import net.stuxcrystal.airblock.commands.core.settings.Environment;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Sponge command.
 */
public class SpongeCommand implements CommandCallable {

    private CommandImplementation impl;

    private String label;

    private SpongeServerBackend backend;

    public SpongeCommand(SpongeServerBackend backend, CommandImplementation impl, String label) {
        this.impl = impl;
        this.backend = backend;
        this.label = label;
    }

    public CommandResult process(CommandSource commandSource, String s) throws CommandException {
        this.impl.execute(
                this.backend.wrap(commandSource).wrap(Environment.getInstance()),
                this.label,
                s

        );
        return CommandResult.success();
    }

    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        return Collections.emptyList();
    }

    public boolean testPermission(CommandSource commandSource) {
        return true;
    }

    public Optional<? extends Text> getShortDescription(CommandSource commandSource) {
        return Optional.of(Text.of(
                this.impl.getDescription(this.backend.wrap(commandSource).wrap(Environment.getInstance()), this.label)
        ));
    }

    public Optional<? extends Text> getHelp(CommandSource commandSource) {
        return Optional.empty();
    }

    public Text getUsage(CommandSource commandSource) {
        return Text.of();
    }
}
