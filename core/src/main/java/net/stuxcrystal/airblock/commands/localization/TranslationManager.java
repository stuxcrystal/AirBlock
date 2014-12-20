/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.stuxcrystal.airblock.commands.localization;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.contrib.locales.LocaleResolver;
import net.stuxcrystal.airblock.commands.contrib.locales.SystemDefaultLocaleResolver;
import net.stuxcrystal.airblock.commands.localization.sources.ArrayValueSource;
import net.stuxcrystal.airblock.commands.localization.sources.MapValueSource;
import net.stuxcrystal.airblock.commands.localization.sources.MultiValueSource;
import org.apache.commons.lang3.text.StrSubstitutor;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;

/**
 * <p>The actual translation manager.</p>
 * <p>
 *     Please note that the translation-manager will always use the locale of the executor.
 * </p>
 */
public class TranslationManager extends MultiValueSource {

    /**
     * Contains the default values of the translation system.
     */
    private static final HashMap<String, String> DEFAULT_VALUES = new HashMap<String, String>();

    /**
     * The key that should be used when the no permission message has been given.
     */
    public static final String COMMAND_NOT_FOUND = "The command has not been found.";

    /**
     * The key that should be used when an error has been caught while executing the command.
     */
    public static final String COMMAND_FAILURE = "An error was detected while executing the command.";

    /**
     * The key that should be used when the player executes a command without permission.
     */
    public static final String COMMAND_NO_PERMISSION = "You don't have permission to execute this command.";

    /**
     * The key that should be used when the command has no description available.
     */
    public static final String NO_DESCRIPTION_AVAILABLE = "The command does not have a description.";

    /**
     * Contains the translation resolver.
     */
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @Nullable
    private TranslationResolver resolver = null;

    /**
     * Contains the formatter for the translations.
     */
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NonNull
    private ValueFormatter formatter = new MessageValueFormatter();

    /**
     * Initiate the default translation values.
     */
    static {
        for (String value : new String[] {
                TranslationManager.COMMAND_FAILURE,
                TranslationManager.COMMAND_NO_PERMISSION,
                TranslationManager.COMMAND_NOT_FOUND,
                TranslationManager.NO_DESCRIPTION_AVAILABLE
        }) {
            TranslationManager.DEFAULT_VALUES.put(value, value);
        }
    }

    /**
     * Implementation of the translation manager.
     */
    public TranslationManager() {}


    /**
     * Returns the raw translation.
     * @param locale   The locale that will be used to translate values.
     * @param name     The value of the translation key.
     * @return The translated value or the value itself.
     */
    @NonNull
    String getRawTranslation(@NonNull Locale locale, @NonNull String name) {
        String result = null;
        if (this.resolver != null)
            result = this.resolver.getTranslation(locale, name);
        if (result == null)
            result = TranslationManager.DEFAULT_VALUES.get(name);
        if (result == null)
            result = name;
        return result;
    }

    /**
     * Translates the message with the given values.
     * @param executor  The executor which determines which language is being used.
     * @param msg       The message-key that will be used for translation.
     * @param values    The values that should be used for translation.
     * @return The translated string.
     */
    @NonNull
    public String translate(@NonNull Executor executor, @NonNull String msg, @Nullable Object... values) {
        return this.translate(TranslationManager.getResolver(executor).getLocale(), msg, values);
    }

    /**
     * Translates the message with the given values.
     * @param locale    The language that should be used.
     * @param msg       The message-key that will be used for translation.
     * @param values    The values that should be used for translation.
     * @return The translated message.
     */
    public String translate(@NonNull Locale locale, @NonNull String msg, @Nullable Object... values) {
        // Create the value source that should be used for resolving the values.
        // Also create the string-substitutor that should be used for translating strings.
        MultiValueSource mvs = new MultiValueSource();
        mvs.addSource(new ArrayValueSource(values));                            // The values given by the executor.
        mvs.addSource(this);                                                    // The values added to this manager.
        mvs.addSource(new MapValueSource(System.getProperties()));              // The system properties.
        StrSubstitutor ss = new StrSubstitutor(new ValueSourceLookup(this, mvs, locale), "{", "}", '\\');

        // Replace the values.
        return ss.replace(this.getRawTranslation(locale, msg));
    }

    /**
     * Returns the locale-resolver for the executor.
     * @param executor The executor.
     * @return The locale resolver.
     */
    @NonNull
    public static LocaleResolver getResolver(@NonNull Executor executor) {
        if (!executor.hasComponent(LocaleResolver.class))
            executor.addComponent(new SystemDefaultLocaleResolver());
        return executor.getComponent(LocaleResolver.class);
    }

}
