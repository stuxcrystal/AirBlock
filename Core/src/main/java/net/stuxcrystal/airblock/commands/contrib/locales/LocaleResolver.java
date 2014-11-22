package net.stuxcrystal.airblock.commands.contrib.locales;

import java.util.Locale;

/**
 * Implementation for a locale resolver.
 */
public interface LocaleResolver {

    /**
     * Returns the locale of the user.
     * @return The locale of the user.
     */
    public Locale getLocale();

}
