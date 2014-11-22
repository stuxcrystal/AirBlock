package net.stuxcrystal.airblock.commands.core.settings;

import lombok.Getter;
import lombok.Setter;
import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.logging.Logger;


/**
 * Stores settings for the commands.
 */
@NotThreadSafe
public class CommandLocale implements CommandSettings {

    /**
     * The parent settings.
     */
    @Getter
    private final CommandSettings parent;

    /**
     * Contains the argument converter.
     */
    @Setter
    public ArgumentConverter argumentConverter;

    /**
     * Contains the argument splitter.
     */
    @Setter
    public ArgumentSplitter argumentSplitter;

    /**
     * Creates a new command setting module.
     * @param parent The parent locale
     */
    public CommandLocale(CommandSettings parent) {
        this.parent = parent;
    }

    @Override
    public ArgumentConverter getArgumentConverter() {
        if (this.argumentConverter != null)
            return this.argumentConverter;
        return this.parent.getArgumentConverter();
    }

    @Override
    public ArgumentSplitter getArgumentSplitter() {
        if (this.argumentSplitter != null)
            return this.argumentSplitter;
        return this.parent.getArgumentSplitter();
    }

    @Nonnull
    @Override
    public Environment getEnvironment() {
        return this.getParent().getEnvironment();
    }

    @Nonnull
    @Override
    public Logger getLogger() {
        return this.getEnvironment().getLogger();
    }
}
