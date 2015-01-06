package net.stuxcrystal.airblock.configuration.parser.types;

import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import net.stuxcrystal.airblock.configuration.parser.node.ListNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import net.stuxcrystal.airblock.utils.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list type.
 */
public class ListType implements ObjectType {

    /**
     * Is the type supported.
     *
     * @param type Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    @Override
    public boolean supportsType(Type type) {
        return ReflectionUtils.toClass(type).equals(List.class);
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
    @SuppressWarnings("rawtypes")
    public Object parse(ConfigurationParser parser, Type type, Node node) throws ReflectiveOperationException {
        if (!(node instanceof ListNode))
            throw new IllegalArgumentException("Node not supported.");
        Type componentType = ReflectionUtils.getGenericArguments(type)[0];
        List result = new ArrayList(((ListNode) node).getNodes().size());
        for (Node child : ((ListNode) node).getNodes())
            result.add(parser.parseNode(componentType, child));
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
        List data = (List)object;
        Type componentType = ReflectionUtils.getGenericArguments(type)[0];
        List<Node> nodes = new ArrayList<Node>(data.size());
        for (Object child : data)
            nodes.add(parser.dumpNode(componentType, child));
        return new ListNode(nodes);
    }
}
