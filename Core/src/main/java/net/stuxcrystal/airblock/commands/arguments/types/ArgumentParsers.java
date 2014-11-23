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
    public final ArgumentParser<String> STRING = new ArgumentParser<String>() {
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
    public final ArgumentParser<Object> PRIMITIVE = new PrimitiveParser();

    /**
     * Parser that parses enum values.
     */
    public final ArgumentParser<Object> ENUM = new ArgumentParser<Object>() {
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
    public final ArgumentParser<Object> ARRAY = new ArgumentParser<Object>() {
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
    public ArgumentParser<?>[] getDefaultParsers() {
        return new ArgumentParser[] {
                STRING,
                PRIMITIVE,
                ENUM,
                ARRAY
        };
    }

}
