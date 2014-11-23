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
