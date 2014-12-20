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

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

/**
 * Implementation of the argument parsers.
 */
public final class ArgumentParsers {

    /**
     * Implementation of the most basic parser.
     */
    public static final ArgumentParser<String> STRING = new ArgumentParser<String>() {
        @Override
        public boolean canConvert(ArgumentConverter parser, Type type) {
            return ReflectionUtils.toClass(type).equals(String.class);
        }

        @Override
        public String convert(Executor executor, ArgumentConverter converter, Type type, String value) {
            return value;
        }
    };

    /**
     * The parser that parses primitive types.
     */
    public static final ArgumentParser<Object> PRIMITIVE = new PrimitiveParser();

    /**
     * Parser that parses enum values.
     */
    public static final ArgumentParser<Object> ENUM = new ArgumentParser<Object>() {
        @Override
        public boolean canConvert(ArgumentConverter parser, Type type) {
            return ReflectionUtils.toClass(type).isEnum();
        }

        @Override
        public Object convert(Executor executor, ArgumentConverter converter, Type type, String value) {
            for (Enum enumConstant : (Enum[]) ReflectionUtils.toClass(type).getEnumConstants()) {
                if (enumConstant.name().equals(value))
                    return enumConstant;
            }

            for (Enum enumConstant : (Enum[]) ReflectionUtils.toClass(type).getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(value))
                    return enumConstant;
            }

            throw new NumberFormatException("Unknown enum value.");
        }
    };

    /**
     * Parser that parses arrays.
     */
    public static final ArgumentParser<Object> ARRAY = new ArgumentParser<Object>() {
        @Override
        public boolean canConvert(ArgumentConverter parser, Type type) {
            if (!ReflectionUtils.toClass(type).isArray())
                return false;
            return parser.getParserFor(ReflectionUtils.getGenericComponentType(type)) != null;
        }

        @Override
        public Object convert(Executor executor, ArgumentConverter converter, Type type, String value) {
            Type componentType = ReflectionUtils.getGenericComponentType(type);
            ArgumentParser p = converter.getParserFor(componentType);

            String[] values = value.split(",");
            Object result = Array.newInstance(ReflectionUtils.toClass(type).getComponentType(), values.length);
            for (int i = 0; i<values.length;i++)
                Array.set(result, i, p.convert(executor, converter, componentType, values[i]));
            return result;
        }
    };

    /**
     * Returns the default parsers of the argument parser.
     * @return The default parsers.
     */
    @Nonnull
    public static ArgumentParser<?>[] getDefaultParsers() {
        return new ArgumentParser[] {
                STRING,
                PRIMITIVE,
                ENUM,
                ARRAY
        };
    }

}
