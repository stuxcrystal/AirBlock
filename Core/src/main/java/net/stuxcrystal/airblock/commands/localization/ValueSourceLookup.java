package net.stuxcrystal.airblock.commands.localization;

import net.stuxcrystal.airblock.utils.Optional;
import org.apache.commons.lang3.text.StrLookup;

import java.util.Locale;

/**
 * The StrLookup that gets its values from the sources.
 */
public class ValueSourceLookup extends StrLookup<String> {

    /**
     * The manager that defines the translation-manager.
     */
    public final TranslationManager manager;

    /**
     * The value source.
     */
    public final ValueSource source;

    /**
     * The locale of the user.
     */
    public final Locale locale;

    /**
     * Creates a new value-source lookup.
     * @param manager  The manager that will be used for formatting the values.
     * @param source   The source of the values.
     * @param locale   The locale of the user.
     */
    public ValueSourceLookup(TranslationManager manager, ValueSource source, Locale locale) {
        this.manager = manager;
        this.source = source;
        this.locale = locale;
    }

    @Override
    public String lookup(String key) {
        String[] raw = key.split(",", 2);
        String name = raw[0];
        String format = raw.length==2?raw[1]:null;

        Optional<?> value = this.source.get(name);
        if (Optional.empty().equals(value)) {
            return "";
        }

        return this.manager.getFormatter().format(locale, Optional.get(value), format);
    }
}
