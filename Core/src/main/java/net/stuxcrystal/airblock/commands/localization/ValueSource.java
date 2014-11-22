package net.stuxcrystal.airblock.commands.localization;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.utils.Optional;

import javax.annotation.Nullable;

/**
 * Represents the source of a value.
 */
public interface ValueSource {

    /**
     * Returns value with the given value.
     * @param value The value of the value.
     * @return The value with the given value.
     */
    @Nullable
    public Optional<?> get(@NonNull String value);
}
