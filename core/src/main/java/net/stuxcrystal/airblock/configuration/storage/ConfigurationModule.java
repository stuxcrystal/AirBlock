package net.stuxcrystal.airblock.configuration.storage;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Stack;

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
    private ConfigurationLocation location = null;

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
     * @param type  The type of the configuration.
     * @param <T>   The type of the configuration.
     * @return The parsed configuration.
     * @throws IOException If an I/O-Operation fails.
     */
    public <T> T getConfiguration(@NonNull String name, @NonNull Class<T> type) throws IOException {
        return this.getParser().parse(
                this.getLocation().read(this.getEffectiveModulePath(), name),
                type
        );
    }

    /**
     * Returns the location of the configurations.
     * @return The location of the configurations.
     */
    public @NonNull ConfigurationLocation getLocation() {
        if (this.location != null)
            return this.location;
        return this.getParent().getLocation();
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
        Stack<String> result = new Stack<String>();
        this.determineModulePath(result, stopAtLocation);
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
    private void determineModulePath(@NonNull Stack<String> stack, boolean stopAtLocation) {
        if (this.name != null)
            stack.push(this.name);

        if (this.parent != null) {
            if (this.location != null && stopAtLocation)
                this.parent.determineModulePath(stack, stopAtLocation);
        }
    }
}
