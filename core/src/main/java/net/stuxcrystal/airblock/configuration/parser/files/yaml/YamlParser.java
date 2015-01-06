package net.stuxcrystal.airblock.configuration.parser.files.yaml;

import net.stuxcrystal.airblock.configuration.parser.node.DataNode;
import net.stuxcrystal.airblock.configuration.parser.node.ListNode;
import net.stuxcrystal.airblock.configuration.parser.node.MapNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A dumper for yaml documents.
 */
public class YamlParser {

    /**
     * A yaml parser that only supports strings.
     */
    private static class StringOnlyResolver extends Resolver {

        /**
         * Don't implement any resolvers.
         */
        protected void addImplicitResolvers() {

        }

    }

    /**
     * The yaml parser to use.
     */
    private static Yaml parser;

    /**
     * Prepare the parser.
     */
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        parser = new Yaml(new Constructor(), new Representer(), options, new StringOnlyResolver());
    }

    /**
     * Parses the node.
     * @param stream The node to parse.
     * @return If an I/O-Operation or parsing fails.
     */
    public static Node parse(InputStream stream) throws IOException {
        try {
            return parseNode(parser.load(stream));
        } catch (YAMLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Parses the node.
     * @param value The value.
     * @return The node.
     */
    @SuppressWarnings("unchecked")
    private static Node parseNode(Object value) {
        if (value instanceof Map) {
            return parseMap((Map<Object, Object>) value);
        } else if (value instanceof List) {
            return parseList((List<Object>) value);
        } else {
            return new DataNode((String) value);
        }
    }

    /**
     * Parses the map.
     * @param data The data to parse.
     * @return The map-node.
     */
    private static Node parseMap(Map<Object, Object> data) {
        Map<Node, Node> entries = new LinkedHashMap<Node, Node>();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            entries.put(parseNode(entry.getKey()), parseNode(entry.getValue()));
        }
        return new MapNode(entries);
    }

    /**
     * Parses a list.
     * @param data The list to parse.
     * @return The node.
     */
    private static Node parseList(List<Object> data) {
        ArrayList<Node> nodes = new ArrayList<Node>(data.size());
        for (Object o : data) {
            nodes.add(parseNode(o));
        }
        return new ListNode(nodes);
    }

}
