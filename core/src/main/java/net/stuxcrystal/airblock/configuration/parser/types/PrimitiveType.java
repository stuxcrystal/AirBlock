package net.stuxcrystal.airblock.configuration.parser.types;

import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import net.stuxcrystal.airblock.configuration.parser.node.DataNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import net.stuxcrystal.airblock.utils.ReflectionUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Represents a primitive type.
 */
public class PrimitiveType implements ObjectType {

    /**
     * Contains all values that can be considered as representative of a true value.
     */
    public static final String[] TRUE_VALUES = "1,yes,y,true,t".split(",");

    /**
     * The default true value.
     */
    public static final String TRUE_VALUE = "true";

    /**
     * Contains all values that can be considered as representative of a false value.
     */
    public static final String[] FALSE_VALUES = "0,no,n,false,f".split(",");

    /**
     * The default false value.
     */
    public static final String FALSE_VALUE = "false";

    /**
     * Checks if the value is true or false.
     * @param value     The value.
     * @return {@code true} if this is a supported true value. {@code false} if not
     * @throws NumberFormatException If the value is unknown.
     */
    private boolean isTrue(String value) {
        // Retrieve true and false values from the translation subsystem.
        List<String> trueValues = Arrays.asList(PrimitiveType.TRUE_VALUES);
        List<String> falseValues = Arrays.asList(PrimitiveType.FALSE_VALUES);

        // Check the values.
        if (trueValues.contains(value.toLowerCase()))
            return true;
        if (falseValues.contains(value.toLowerCase()))
            return false;

        throw new NumberFormatException("Unsupported flag expression");
    }

    /**
     * Is the type supported.
     *
     * @param type Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    @Override
    public boolean supportsType(Type type) {
        return ClassUtils.isPrimitiveOrWrapper(ReflectionUtils.toClass(type)) || String.class.isAssignableFrom(ReflectionUtils.toClass(type));
    }

    /**
     * The node that should be parsed.
     *
     * @param parser The parser that can be used to parse subnodes.
     * @param type   The type of the object.
     * @param node   The node that should be parsed..
     * @return The parsed object.
     */
    @Override
    public Object parse(ConfigurationParser parser, Type type, Node node) throws ReflectiveOperationException {
        if (!(node instanceof DataNode))
            throw new IllegalArgumentException("Invalid node type for primitive types.");

        String value = ((DataNode) node).getData();

        Class<?> cls = ReflectionUtils.toClass(type);
        if (ClassUtils.isPrimitiveWrapper(cls))
            cls = ClassUtils.wrapperToPrimitive(cls);
        else if (String.class.isAssignableFrom(cls))
            return new DataNode(value);

        if (cls.equals(boolean.class))
            return this.isTrue(value);
        else if (cls.equals(char.class)) {
            if (value.length() > 0)
                throw new NumberFormatException("Character arguments cannot be longer than one characters");
            return value.charAt(0);
        }

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ROOT);
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

    /**
     * A object should be dumped into a node.
     *
     * @param parser The parsed that will be used to dump object that are contained by this object.
     * @param type   The type of the object.
     * @param object The object that should be parsed.
     * @return The dumped object.
     */
    @Override
    public Node dump(ConfigurationParser parser, Type type, Object object) throws ReflectiveOperationException {
        DataNode node = new DataNode();
        node.setData(object.toString());
        return node;
    }
}
