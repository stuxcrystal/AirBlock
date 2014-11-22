package net.stuxcrystal.airblock.commands.arguments;

import lombok.AllArgsConstructor;
import net.stuxcrystal.airblock.commands.Executor;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    public void registerParser(ArgumentParser<?> parser) {
        this.parsers.add(parser);
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

}
