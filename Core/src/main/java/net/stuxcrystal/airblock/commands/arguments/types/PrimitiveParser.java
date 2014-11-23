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

package net.stuxcrystal.airblock.commands.arguments.types;

import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.ArgumentParser;
import net.stuxcrystal.airblock.utils.ReflectionUtils;
import net.stuxcrystal.airblock.commands.localization.TranslationManager;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Parser for primitives.
 */
public class PrimitiveParser implements ArgumentParser<Object> {

    /**
     * Checks if the value is true or false.
     * @param executor  The executor.
     * @param value     The value.
     * @return {@code true} if this is a supported true value. {@code false} if not
     * @throws NumberFormatException If the value is unknown.
     */
    private boolean isTrue(Executor executor, String value) {
        // Get the actual translation values for the boolean values.
        String rawTrue = executor.getEnvironment().getTranslationManager().translate(executor, "true").toLowerCase();
        String rawFalse = executor.getEnvironment().getTranslationManager().translate(executor, "false").toLowerCase();

        // Retrieve true and false values from the translation subsystem.
        List<String> trueValues = Arrays.asList(rawTrue.split(","));
        List<String> falseValues = Arrays.asList(rawFalse.split(","));

        // Check the values.
        if (trueValues.contains(value.toLowerCase()))
            return true;
        if (falseValues.contains(value.toLowerCase()))
            return false;

        throw new NumberFormatException("Unsupported flag expression");
    }

    @Override
    public boolean canConvert(ArgumentConverter parser, Type type) {
        return ClassUtils.isPrimitiveOrWrapper(ReflectionUtils.toClass(type));
    }

    @Override
    public Object convert(Executor executor, ArgumentConverter parser, Type type, String value) {
        Class<?> cls = ReflectionUtils.toClass(type);
        if (ClassUtils.isPrimitiveWrapper(cls))
            cls = ClassUtils.wrapperToPrimitive(cls);

        if (cls.equals(boolean.class))
            return this.isTrue(executor, value);
        else if (cls.equals(char.class)) {
            if (value.length() > 0)
                throw new NumberFormatException("Character arguments cannot be longer than one characters");
            return value.charAt(0);
        }

        // Get the locale of the user and get a number-format according to it.
        Locale locale = TranslationManager.getResolver(executor).getLocale();
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setGroupingUsed(true);

        // Parse the value.
        Number result;
        try {
            result = nf.parse(value);
        } catch (ParseException e) {
            NumberFormatException nfe = new NumberFormatException("Invalid number");
            nfe.initCause(e);
            throw nfe;
        }

        // Returns the value in the correct type.
        if (cls.equals(int.class))
            return result.intValue();
        else if (cls.equals(float.class))
            return result.floatValue();
        else if (cls.equals(double.class))
            return result.doubleValue();
        else if (cls.equals(byte.class))
            return result.byteValue();
        else if (cls.equals(short.class))
            return result.shortValue();
        else if (cls.equals(long.class))
            return result.longValue();

        throw new NumberFormatException("Unknown primitive type.");
    }
}
