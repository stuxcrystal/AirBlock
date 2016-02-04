package moe.encode.airblock.configuration.parser.types;

import moe.encode.airblock.configuration.parser.ConfigurationParser;
import moe.encode.airblock.configuration.parser.node.Node;

import java.lang.reflect.Type;

/**
 * Parses the object.
 */
public interface ObjectType {

    /**
     * Is the type supported.
     * @param type  Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    public boolean supportsType(Type type);

    /**
     * The node that should be parsed.
     * @param parser  The parser that can be used to parse subnodes.
     * @param type    The type of the object.
     * @param node    The node that should be parsed..
     * @return The parsed object.
     */
    public Object parse(ConfigurationParser parser, Type type, Node node) throws ReflectiveOperationException;

    /**
     * A object should be dumped into a node.
     * @param parser   The parsed that will be used to dump object that are contained by this object.
     * @param type     The type of the object.
     * @param object   The object that should be parsed.
     * @return The dumped object.
     */
    public Node dump(ConfigurationParser parser, Type type, Object object) throws ReflectiveOperationException;
}
