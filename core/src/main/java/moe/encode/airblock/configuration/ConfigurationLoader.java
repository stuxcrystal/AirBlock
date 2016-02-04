package moe.encode.airblock.configuration;

import lombok.NonNull;
import moe.encode.airblock.configuration.parser.ConfigurationParser;
import moe.encode.airblock.configuration.parser.types.ObjectType;
import moe.encode.airblock.configuration.storage.ConfigurationModule;
import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;

/**
 * Implementation of the given configuration loader.
 */
public class ConfigurationLoader extends ConfigurationModule {

    /**
     * Contains the parser for the configuration.
     */
    private final ConfigurationParser parser = new ConfigurationParser();

    /**
     * The parser for the configuration.
     */
    public ConfigurationLoader(@NonNull ConfigurationStorage location) {
        super(null);
        this.setLocation(location);
    }

    /**
     * Set the configuration location.
     * @param location The configuration location
     */
    public void setLocation(@NonNull ConfigurationStorage location) {
        super.setLocation(location);
    }

    @Override
    public ConfigurationParser getParser() {
        return this.parser;
    }

    /**
     * Adds the given type.
     * @param objectType The type that should be added.
     */
    public void addType(ObjectType objectType) {
        getParser().addType(objectType);
    }

    /**
     * Removes the type.
     * @param objectType The type that should be removed.
     */
    public void removeType(ObjectType objectType) {
        getParser().removeType(objectType);
    }

}
