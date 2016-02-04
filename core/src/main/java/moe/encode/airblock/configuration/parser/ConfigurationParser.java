package moe.encode.airblock.configuration.parser;

import moe.encode.airblock.configuration.parser.node.Node;
import moe.encode.airblock.configuration.parser.types.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * The facade of this package.
 */
public class ConfigurationParser {

    /**
     * Returns all supported types.
     * @return All supported types.
     */
    public LinkedList<ObjectType> getSupportedTypes() {
        LinkedList<ObjectType> result = new LinkedList<ObjectType>();
        result.addAll(this.types);
        if (this.parent != null)
            result.addAll(this.parent.getSupportedTypes());
        return result;
    }

    /**
     * The parent parser.
     */
    private ConfigurationParser parent = null;

    /**
     * All supported types.
     */
    public LinkedList<ObjectType> types = new LinkedList<ObjectType>();

    /**
     * Basic Constructor for a configuration parser.
     */
    public ConfigurationParser() {
        this.types = new LinkedList<ObjectType>(Arrays.asList(
                new PrimitiveType(),
                new ListType(),
                new ArrayType(),
                new MapType(),
                new ConfigurationType()
        ));
    }

    /**
     * Configuration parser that allows to directly pass the configuration
     * types.
     * @param types The types to support.
     */
    public ConfigurationParser(ObjectType... types) {
        this.types = new LinkedList<ObjectType>(Arrays.asList(types));
    }

    /**
     * Configuration parser with parent parser.
     * @param parent The parent parser.
     */
    public ConfigurationParser(ConfigurationParser parent) {
        this.parent = parent;
    }

    /**
     * Adds the given type.
     * @param objectType The type that should be added.
     */
    public void addType(ObjectType objectType) {
        this.types.addFirst(objectType);
    }

    /**
     * Removes the type.
     * @param objectType The type that should be removed.
     */
    public void removeType(ObjectType objectType) {
        this.types.remove(objectType);
    }

    /**
     * Parses the object and dumps it into a node.
     * @param type    The type of the object.
     * @param object  The instance of the type.
     * @return The parsed node
     * @throws java.lang.ReflectiveOperationException If we failed to parse the object.
     */
    public Node dumpNode(Type type, Object object) throws ReflectiveOperationException {

        for (ObjectType ot : this.getSupportedTypes()) {
            if (ot.supportsType(type))
                return ot.dump(this, type, object);
        }

        throw new ReflectiveOperationException(new UnsupportedOperationException("Unsupported type."));

    }

    /**
     * Parses the node and deserialize it.
     * @param type  The type to serialize.
     * @param node  The node to pass.
     * @return The parsed object.
     * @throws ReflectiveOperationException If we failed to dump the object.
     */
    public Object parseNode(Type type, Node node) throws ReflectiveOperationException {
        for (ObjectType ot : this.getSupportedTypes())
            if (ot.supportsType(type))
                return ot.parse(this, type, node);

        throw new ReflectiveOperationException(new UnsupportedOperationException("Unsupported type."));
    }

    /**
     * Dumps an object to a node.
     * @param o The object that should be dumped.
     * @return The dumped node.
     */
    public Node dump(Object o) {
        try {
            return this.dumpNode(o.getClass(), o);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the node.
     * @param node  The node.
     * @param cls   the type.
     * @param <T>   The return type.
     * @return The parsed node.
     */
    @SuppressWarnings("unchecked")
    public <T> T parse(Node node, Class<T> cls) {
        try {
            return (T) this.parseNode(cls, node);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
