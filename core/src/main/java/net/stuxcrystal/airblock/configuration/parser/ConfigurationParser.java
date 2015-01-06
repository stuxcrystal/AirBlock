package net.stuxcrystal.airblock.configuration.parser;

import net.stuxcrystal.airblock.configuration.parser.node.Node;
import net.stuxcrystal.airblock.configuration.parser.types.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * The facade of this package.
 */
public class ConfigurationParser {

    /**
     * All supported types.
     */
    public LinkedList<ObjectType> types = new LinkedList<ObjectType>(Arrays.asList(
            new PrimitiveType(),
            new ListType(),
            new ArrayType(),
            new MapType(),
            new ConfigurationType()
    ));

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

        for (ObjectType ot : this.types) {
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
        for (ObjectType ot : this.types)
            if (ot.supportsType(type))
                return ot.parse(this, type, node);

        throw new ReflectiveOperationException(new UnsupportedOperationException("Unsupported type."));
    }

}
