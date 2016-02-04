package moe.encode.airblock.configuration.storage;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;
import moe.encode.airblock.configuration.storage.storage.SubModuleStorage;
import moe.encode.airblock.configuration.parser.ConfigurationParser;
import moe.encode.airblock.configuration.parser.files.FileType;
import moe.encode.airblock.configuration.parser.node.Node;
import moe.encode.airblock.configuration.storage.location.ConfigurationLocation;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Stores the configuration.
 */
public class ConfigurationModule {

    /**
     * The name of the module.
     */
    @Getter
    private final String name;

    /**
     * The parent module.
     */
    @Getter
    private final ConfigurationModule parent;

    /**
     * Contains the current configuration location.
     */
    @Setter
    @Nullable
    private ConfigurationStorage location = null;

    /**
     * Constructor for subclasses of the configuration module.
     * @param parent The parent module.
     */
    protected ConfigurationModule(ConfigurationModule parent) {
        this.name = null;
        this.parent = parent;
    }

    /**
     * Creates a new module for configurations.
     * @param name   The name of the module.
     * @param parent The parent configuration of the module.
     */
    public ConfigurationModule(String name, @NonNull ConfigurationModule parent) {
        this.name = name;
        this.parent = parent;
    }

    /**
     * Returns the given configuration file.
     * @param name  The name of the configuration.
     * @return The parsed configuration.
     * @throws IOException If an I/O-Operation fails.
     */
    public ConfigurationLocation getConfiguration(@NonNull String name) throws IOException {
        return this.getStorage().getConfiguration(this.getEffectiveModulePath(), name);
    }

    /**
     * The parser.
     * @param cls    The class.
     * @param node   The node.
     * @param <T>    The return type.
     * @return The parsed file.
     */
    @SuppressWarnings("unchecked")
    private <T> T parse(@NonNull Class<?> cls, @NonNull Node node) {
        return (T) this.getParser().parse(node, cls);
    }

    /**
     * Returns the Input-Stream to this file.
     * @param name  The name of the configuration file.
     * @return The input stream.
     * @throws IOException If an I/O-Operation fails.
     */
    public InputStream getInputStream(@NonNull String name) throws IOException {
        return this.getConfiguration(name).getInputStream();
    }

    /**
     * Loads the give file and parses it.
     * @param cls    The class.
     * @param name   The name of the configuration file.
     * @param ft     The file types.
     * @param <T>    The file type.
     * @return The parsed class.
     * @throws IOException If an I/O-Operation fails.
     */
    public <T> T load(@NonNull Class<?> cls, @NonNull String name, @NonNull FileType ft) throws IOException {
        return this.parse(cls, this.getStorage().read(this.getEffectiveModulePath(), name, ft));
    }

    /**
     * Loads the give file and parses it.
     * @param cls    The class.
     * @param name   The name of the configuration file.
     * @param <T>    The file type.
     * @return The parsed class.
     * @throws IOException If an I/O-Operation fails.
     */
    public <T> T load(@NonNull Class<?> cls, @NonNull String name) throws IOException {
        return this.parse(cls, this.getStorage().read(this.getEffectiveModulePath(), name));
    }

    /**
     * Dumps the given object into a node tree.
     * @param o The object.
     * @return The dumped node.
     */
    private Node dump(Object o) {
        return this.getParser().dump(o);
    }

    /**
     * Returns the Input-Stream to this file.
     * @param name  The name of the configuration file.
     * @return The input stream.
     * @throws IOException If an I/O-Operation fails.
     */
    public OutputStream getOutputStream(@NonNull String name) throws IOException {
        return this.getConfiguration(name).getOutputStream();
    }

    /**
     * Loads the give file and parses it.
     * @param cls    The class.
     * @param name   The name of the configuration file.
     * @param o      The instance that holds the data.
     * @param ft     The file types.
     * @throws IOException If an I/O-Operation fails.
     */
    public void save(@NonNull Class<?> cls, @NonNull String name, @NonNull Object o, @NonNull FileType ft) throws IOException {
        this.getStorage().write(this.getEffectiveModulePath(), name, this.dump(o), ft);
    }

    /**
     * Loads the give file and parses it.
     * @param cls    The class.
     * @param name   The name of the configuration file.
     * @param o      The instance that holds the data.
     * @throws IOException If an I/O-Operation fails.
     */
    public void save(@NonNull Class<?> cls, @NonNull String name, @NonNull Object o) throws IOException {
        this.getStorage().write(this.getEffectiveModulePath(), name, this.dump(o));
    }

    /**
     * Returns the location of the configurations.
     * @return The location of the configurations.
     */
    public @NonNull ConfigurationStorage getStorage() {
        if (this.location != null)
            return this.location;
        return this.getParent().getStorage();
    }

    /**
     * Returns the parser for the configuration.
     * @return The parser for the configuration.
     */
    public @NonNull ConfigurationParser getParser() {
        return this.getParent().getParser();
    }

    /**
     * Returns the module path for the configuration location
     * the configuration files are stored to.
     * @param stopAtLocation   Should we stop resolving when we encounter a location.
     * @return The effective module path.
     */
    private @NonNull String[] getModulePath(boolean stopAtLocation) {
        LinkedList<String> result = new LinkedList<String>();
        this.determineModulePath(result, stopAtLocation);
        Collections.reverse(result);
        return result.toArray(new String[result.size()]);
    }

    /**
     * Returns the complete path of the module.
     * @return The complete path of the module.
     */
    public @NonNull String[] getModulePath() {
        return this.getModulePath(false);
    }

    /**
     * Returns the effective module path.
     * @return The effective module path.
     */
    public @NonNull String[] getEffectiveModulePath() {
        return this.getModulePath(true);
    }

    /**
     * Determines the path of the module.
     * @param stack The path of the module.
     */
    private void determineModulePath(@NonNull Queue<String> stack, boolean stopAtLocation) {
        if (this.name != null)
            stack.add(this.name);

        if (this.parent != null) {
            if (this.location == null || !stopAtLocation)
                this.parent.determineModulePath(stack, stopAtLocation);
        }
    }

    /**
     * Converts this configuration-module to a storage-location.
     * @return The Configuration-Storage that can be passed as other location.
     */
    public ConfigurationStorage toStorage() {
        return new SubModuleStorage(this);
    }

    /**
     * Adds the read-only locations to the module.
     * @param storages The storage.
     */
    public void addLocations(ConfigurationStorage... storages) {
        ConfigurationStorage.addToModule(this, storages);
    }
}
