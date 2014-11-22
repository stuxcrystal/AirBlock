package net.stuxcrystal.airblock.commands.arguments;

import net.stuxcrystal.airblock.commands.Executor;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

/**
 * Parses arguments.
 */
public interface ArgumentParser<T> {

    /**
     * <p>Can this argument parses convert this type.</p>
     * @param type The type of the object to convert.
     * @return {@code true} if the argument parser can parse the argument.
     */
    public boolean canConvert(@Nonnull ArgumentConverter parser, @Nonnull Type type);

    /**
     * <p>Converts the object to the given type.</p>
     * @param value The value of the argument.
     * @return The converted value.
     */
    @Nonnull
    public T convert(@Nonnull Executor executor, @Nonnull ArgumentConverter converter, @Nonnull Type type, @Nonnull String value);
}
