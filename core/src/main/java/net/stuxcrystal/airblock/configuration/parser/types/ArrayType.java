package net.stuxcrystal.airblock.configuration.parser.types;

import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import net.stuxcrystal.airblock.configuration.parser.node.ListNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import net.stuxcrystal.airblock.utils.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an array node.
 */
public class ArrayType implements ObjectType {

    /**
     * Is the type supported.
     *
     * @param type Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    @Override
    public boolean supportsType(Type type) {
        return ReflectionUtils.toClass(type).isArray();
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
        if (!(node instanceof ListNode))
            throw new IllegalArgumentException("Node not supported.");
        Type componentType = ReflectionUtils.getGenericComponentType(type);
        Object result = Array.newInstance(ReflectionUtils.toClass(componentType), ((ListNode) node).getNodes().size());
        for (int i = 0; i<((ListNode) node).getNodes().size(); i++)
            Array.set(result, i, parser.parseNode(componentType, ((ListNode) node).getNodes().get(i)));
        return result;
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
        Type componentType = ReflectionUtils.getGenericComponentType(type);
        List<Node> result = new ArrayList<Node>(Array.getLength(object));
        for (int i = 0; i<Array.getLength(object); i++)
            result.add(parser.dumpNode(componentType, Array.get(object, i)));
        return new ListNode(result);
    }
}
