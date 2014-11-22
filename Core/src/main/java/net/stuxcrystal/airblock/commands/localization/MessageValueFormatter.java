package net.stuxcrystal.airblock.commands.localization;

import lombok.NonNull;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * The default value formatter.
 */
public class MessageValueFormatter implements ValueFormatter {

    /**
     * <p>Just use a message-format object to format the values.</p>
     * <p>
     *     Please note that we will always use the determined locale of the executor.
     * </p>
     * @param value   The value to format.
     * @param format  The formatted value.
     * @return The formatted value.
     */
    @Override
    public String format(@NonNull Locale locale, @NonNull Object value, @Nullable String format) {
        String actualFormat = "{0" + (format==null?"":","+format) + "}";
        return new MessageFormat(actualFormat, locale).format(value);
    }
}
