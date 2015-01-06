package net.stuxcrystal.airblock.configuration.parser.types;

import net.stuxcrystal.airblock.configuration.Configuration;
import net.stuxcrystal.airblock.configuration.Value;
import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import net.stuxcrystal.airblock.configuration.parser.node.DataNode;
import net.stuxcrystal.airblock.configuration.parser.node.MapNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import net.stuxcrystal.airblock.utils.ReflectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a configuration node.
 */
public class ConfigurationType implements ObjectType {

    /**
     * All serializable objects will be serializable by this configuration node.
     *
     * @param type Checks if the type is supported.
     * @return {@code true} if the type is supported.
     */
    @Override
    public boolean supportsType(Type type) {
        if (!ReflectionUtils.toClass(type).isAnnotationPresent(Configuration.class))
            return false;

        try {
            // Try to get a constructor.
            ReflectionUtils.toClass(type).getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
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

        // Convert all nodes.
        Map<String, Node> nodes = new LinkedHashMap<String, Node>(((MapNode) node).getNodes().size());
        for (Map.Entry<Node, Node> entry : ((MapNode) node).getNodes().entrySet()) {
            if (!(entry.getKey() instanceof DataNode))
                throw new IllegalArgumentException("Invalid key node type.");
            nodes.put(((DataNode) entry.getKey()).getData(), entry.getValue());
        }

        // Create a new instance
        Class<?> cls = ReflectionUtils.toClass(type);
        Object o = ReflectionUtils.newInstance(cls);

        this.setFields(parser, cls, nodes, o);
        return o;
    }

    /**
     * Sets the field of the class.
     * @param parser   The parser of the fields.
     * @param cls      The class of the field.
     * @param fields   The fields itself.
     * @param object   The object of the fields.
     * @throws ReflectiveOperationException  If an I/O-Operation fails.
     */
    public void setFields(ConfigurationParser parser, Class<?> cls, Map<String, Node> fields, Object object) throws ReflectiveOperationException {
        for (Field field : cls.getDeclaredFields()) {
            // Make sure we don't access static fields.
            if (Modifier.isStatic(field.getModifiers()))
                continue;

            // Make sure no transient fields are accessed.
            if (Modifier.isTransient(field.getModifiers()))
                continue;

            // Make sure we can write to this field.
            if (Modifier.isFinal(field.getModifiers()))
                FieldUtils.removeFinalModifier(field, true);

            // Write to the field.
            String name = ConfigurationType.getActualFieldName(field);
            if (fields.containsKey(name))
                FieldUtils.writeField(field, object, parser.parseNode(field.getType(), fields.get(name)), true);
        }

        if (this.supportsType(cls.getSuperclass()))
            this.setFields(parser, cls.getSuperclass(), fields, object);
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
        Class<?> cls = ReflectionUtils.toClass(type);
        Map<String, Node> result = new LinkedHashMap<String, Node>();
        this.dumpClass(parser, result, cls, object);

        Map<Node, Node> data = new LinkedHashMap<Node, Node>();
        for (Map.Entry<String, Node> node : result.entrySet()) {
            data.put(new DataNode(node.getKey()), node.getValue());
        }

        return new MapNode(data);
    }

    /**
     * Dumps a class.
     * @param parser   The class to dump.
     * @param data     The data that should be dumped.
     * @param cls      The class that should be dumped.
     * @param o        The object that should be dumped.
     * @throws ReflectiveOperationException
     */
    private void dumpClass(ConfigurationParser parser, Map<String, Node> data, Class<?> cls, Object o) throws ReflectiveOperationException {
        ObjectInputStream ois;

        for (Field field : cls.getDeclaredFields()) {
            // Make sure we don't access static fields.
            if (Modifier.isStatic(field.getModifiers()))
                continue;

            // Make sure no transient fields are accessed.
            if (Modifier.isTransient(field.getModifiers()))
                continue;

            String name = ConfigurationType.getActualFieldName(field);

            // Make sure we don't override already existing fields.
            if (data.containsKey(name))
                continue;

            // Parse the data.
            data.put(name, parser.dumpNode(field.getGenericType(), FieldUtils.readField(field, o, true)));
        }

        // Parse the parent type.
        if (this.supportsType(cls.getSuperclass()))
            this.dumpClass(parser, data, cls, o);
    }

    /**
     * Returns the actual field name.
     * @param field The name of the field.
     * @return The name of the field.
     */
    private static String getActualFieldName(Field field) {
        if (field.isAnnotationPresent(Value.class)) {
            Value value = field.getAnnotation(Value.class);
            if (!value.name().isEmpty())
                return value.name();
        }
        return field.getName();
    }

}
