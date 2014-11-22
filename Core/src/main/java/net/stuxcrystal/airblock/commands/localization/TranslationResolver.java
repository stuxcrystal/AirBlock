package net.stuxcrystal.airblock.commands.localization;

import java.util.Locale;

/**
 * The resolver for translations.
 */
public interface TranslationResolver {

    /**
     * Returns the translation string for the given locale.
     * @param locale  The translation string for the given locale.
     * @param text    The text.
     * @return The raw translation string.
     */
    public String getTranslation(Locale locale, String text);

}
