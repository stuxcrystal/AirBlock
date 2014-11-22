package net.stuxcrystal.airblock.commands.core.settings;

import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * Internal class for classes that contain the state of the locale.
 */
public interface CommandSettings {

    /**
     * Sets the {@link net.stuxcrystal.airblock.commands.arguments.ArgumentConverter} for the framework.
     * @param converter The new converter for the arguments.
     */
    public void setArgumentConverter(@Nonnull ArgumentConverter converter);

    /**
     * Returns the {@link net.stuxcrystal.airblock.commands.arguments.ArgumentConverter}.
     * @return The argument converter.
     */
    @Nonnull
    public ArgumentConverter getArgumentConverter();

    /**
     * Returns the argument splitter.
     * @param splitter The argument splitter.
     */
    public void setArgumentSplitter(@Nonnull ArgumentSplitter splitter);

    /**
     * Returns the {@link net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter}.
     * @return The argument splitter.
     */
    @Nonnull
    public ArgumentSplitter getArgumentSplitter();

    /**
     * Returns the root setting container.
     * @return The root setting container.
     */
    @Nonnull
    public Environment getEnvironment();

    /**
     * Returns the logger of the environment.
     * @return The logger of the environment.
     */
    @Nonnull
    public Logger getLogger();

}
