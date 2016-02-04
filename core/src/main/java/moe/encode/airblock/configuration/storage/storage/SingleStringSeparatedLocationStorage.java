package moe.encode.airblock.configuration.storage.storage;

import lombok.NonNull;
import moe.encode.airblock.configuration.parser.files.FileType;
import moe.encode.airblock.configuration.storage.location.ConfigurationLocation;

/**
 * The location storage.
 */
public abstract class SingleStringSeparatedLocationStorage extends StringSeparatedLocationStorage {

    /**
     * The desired suffix.
     */
    private final String suffix;

    /**
     * The separator character.
     * @param separator The separator character.
     */
    protected SingleStringSeparatedLocationStorage(String separator, String suffix) {
        super(separator);
        this.suffix = suffix;
    }

    /**
     * The given configuration location.
     * @param location  The given location.
     * @return The configuration.
     */
    public abstract ConfigurationLocation getConfiguration(String location);

    /**
     * Returns the file-type at the given location.
     * @param location The location.
     * @return the file type.
     */
    public abstract FileType getType(String location);

    /**
     * Returns the suffix for the configuration file names.
     * @return The suffix for the configuration file names.
     */
    public String getSuffix() { return this.suffix; }

    /**
     * Joins path and filename.
     * @param modulePath     The module-path.
     * @param configuration  The configuration-path.
     * @return The module-path.
     */
    private String join(String modulePath, String configuration) {
        if (!configuration.contains("."))
            configuration+=this.getSuffix();
        return modulePath + this.getSeparator() + configuration;
    }

    /**
     * Stores the location of the configuration.
     *
     * @param modulePath    The path to the module.
     * @param configuration The configuration of the module.
     * @return The given configuration.
     */
    @Override
    public ConfigurationLocation getConfiguration(@NonNull String modulePath, @NonNull String configuration) {
        return this.getConfiguration(this.join(modulePath, configuration));
    }

    @Override
    public FileType getType(@NonNull String modulePath, @NonNull String configuration) {
        return this.getType(this.join(modulePath, configuration));
    }
}
