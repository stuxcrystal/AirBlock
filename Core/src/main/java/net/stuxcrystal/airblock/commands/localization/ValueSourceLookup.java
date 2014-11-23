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
