package moe.encode.airblock.configuration.storage.storage.simple;

import lombok.NonNull;
import moe.encode.airblock.configuration.storage.storage.SingleStringSeparatedLocationStorage;
import moe.encode.airblock.configuration.parser.files.FileType;
import moe.encode.airblock.configuration.storage.location.ConfigurationLocation;
import moe.encode.airblock.configuration.storage.location.FileLocation;

import java.io.File;

/**
 * The local storage.
 */
public class LocalStorage extends SingleStringSeparatedLocationStorage {

    /**
     * The default file-type.
     */
    private final FileType ft;

    /**
     * The base-dir.
     */
    private final File baseDir;

    /**
     * The separator character.
     *
     * @param suffix The suffix.
     */
    public LocalStorage(File baseDir, String suffix, FileType ft) {
        super(File.separator, suffix);
        this.baseDir = baseDir;
        this.ft = ft;
    }

    /**
     * The given configuration location.
     *
     * @param location The given location.
     * @return The configuration.
     */
    @Override
    public ConfigurationLocation getConfiguration(String location) {
        return new FileLocation(new File(this.baseDir, location));
    }

    /**
     * Returns the given file type
     *
     * @param location The location of the file.
     * @return The given file type.
     */
    @Override
    public FileType getType(@NonNull String location) {
        return ft;
    }
}
