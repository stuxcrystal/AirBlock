package net.stuxcrystal.airblock.configuration.storage.storage;

import lombok.NonNull;
import net.stuxcrystal.airblock.configuration.parser.files.FileType;
import net.stuxcrystal.airblock.configuration.storage.location.ConfigurationLocation;
import org.apache.commons.lang3.StringUtils;

/**
 * A location-storage that processes the configuration location first.
 */
public abstract class StringSeparatedLocationStorage extends ConfigurationStorage {

    /**
     * The separator character.
     */
    protected final String separator;

    public StringSeparatedLocationStorage(String separator) {
        this.separator = separator;
    }

    /**
     * Returns the separator.
     * @return The separator.
     */
    public String getSeparator() { return this.separator; }

    /**
     * Stores the location of the configuration
     *
     * @param modulePath     The path to the module.
     * @param configuration  The configuration of the module.
     * @return The given configuration.
     */
    public abstract ConfigurationLocation getConfiguration(@NonNull String modulePath, @NonNull String configuration);

    /**
     * Returns the filetype of the given path.
     * @param modulePath      The module path.
     * @param configuration   The configuration path.
     * @return The file-type.
     */
    public abstract FileType getType(@NonNull String modulePath, @NonNull String configuration);

    @Override
    public ConfigurationLocation getConfiguration(@NonNull String[] modulePath, @NonNull String configuration) {
        return this.getConfiguration(this.join(modulePath), configuration);
    }

    @Override
    public FileType getType(@NonNull String[] modulePath, @NonNull String configuration) {
        return this.getType(this.join(modulePath), configuration);
    }

    /**
     * Joins the path parts.
     * @param modulePath The module path.
     * @return The joined path.
     */
    private String join(String[] modulePath) {
        return StringUtils.join(this.getSeparator(), modulePath);
    }
}
