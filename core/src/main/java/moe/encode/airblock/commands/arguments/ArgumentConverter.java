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

package moe.encode.airblock.commands.arguments;

import lombok.AllArgsConstructor;
import moe.encode.airblock.commands.Executor;
import moe.encode.airblock.commands.arguments.types.ArgumentParsers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converts arguments
 */
@AllArgsConstructor
public class ArgumentConverter {

    /**
     * Contains the argument parser.
     */
    private List<ArgumentParser> parsers;

    /**
     * Creates a new argument converter.
     */
    public ArgumentConverter() {
        this.parsers = new ArrayList<ArgumentParser>();
    }

    /**
     * Adds a parser to the converter.
     * @param parser The parser for a value.
     */
    public void registerParser(ArgumentParser<?>... parser) {
        this.parsers.addAll(Arrays.asList(parser));
    }

    /**
     * Returns the parser for the converter.
     * @param type The type of the argument.
     * @return The parser for the argument.
     */
    public ArgumentParser getParserFor(Type type) {
        for (ArgumentParser<?> parser : this.parsers)
            if (parser.canConvert(this, type))
                return parser;
        return null;
    }

    /**
     * Parses a value.
     * @param executor  The executor that parses the command.
     * @param type      The type of the command.
     * @param arg       The argument.
     * @param <T>       The type that should be returned.
     * @return The converted value.
     * @throws NumberFormatException If we cannot parse the value.
     */
    @SuppressWarnings("unchecked")
    public <T> T parse(Executor executor, Type type, String arg) throws NumberFormatException {
        ArgumentParser<T> parser = (ArgumentParser<T>)this.getParserFor(type);
        if (parser == null)
            throw new NumberFormatException("Cannot parse this value.");
        return parser.convert(executor, this, type, arg);
    }

    /**
     * Returns the ArgumentConverter with the default parsers.
     * @return The ArgumentConverter with the default parsers.
     */
    public static ArgumentConverter getSimpleArgumentConverter() {
        ArgumentConverter ac = new ArgumentConverter();
        ac.registerParser(ArgumentParsers.getDefaultParsers());
        return ac;
    }
}
