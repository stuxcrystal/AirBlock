package moe.encode.airblock.configuration.parser.types;

import moe.encode.airblock.utils.ReflectionUtils;
import moe.encode.airblock.configuration.parser.ConfigurationParser;
import moe.encode.airblock.configuration.parser.node.MapNode;
import moe.encode.airblock.configuration.parser.node.Node;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Supported map types.
 */
public class MapType implements ObjectType {
    /**
     * Is the type supported.
     *
     * @param type Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    @Override
    public boolean supportsType(Type type) {
        return ReflectionUtils.toClass(type).equals(Map.class);
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
        if (!(node instanceof MapNode))
            throw new IllegalArgumentException("Invalid node type.");
        Map<Node, Node> nodes = ((MapNode) node).getNodes();
        Map result = new LinkedHashMap(nodes.size());

        Type keyType = ReflectionUtils.getGenericArguments(type)[0];
        Type valueType = ReflectionUtils.getGenericArguments(type)[1];

        for (Map.Entry<Node, Node> values : nodes.entrySet())
            result.put(parser.parseNode(keyType, values.getKey()), parser.parseNode(valueType, values.getValue()));
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
        Map o = (Map)object;
        Map<Node, Node> result = new LinkedHashMap<Node, Node>();

        Type keyType = ReflectionUtils.getGenericArguments(type)[0];
        Type valueType = ReflectionUtils.getGenericArguments(type)[1];
        for (Object key : o.keySet()) {
            result.put(parser.dumpNode(keyType, key), parser.dumpNode(valueType, o.get(key)));
        }
        return new MapNode(result);
    }
}
