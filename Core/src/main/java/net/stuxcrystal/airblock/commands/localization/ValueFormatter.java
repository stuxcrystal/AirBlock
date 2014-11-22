package net.stuxcrystal.airblock.commands.localization;

import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Basic implementation of a value formatter.
 */
public interface ValueFormatter {

    /**
     * <p>Formats a value.</p>
     * <p>
     *     Please not that if {@code format} is {@code null}, use the default
     *     format value.
     * </p>
     * @param value   The value to format.
     * @param format  The formatted value.
     * @return The formatted string.
     */
    public String format(@NonNull Locale locale, @NonNull Object value, @Nullable String format);

}
