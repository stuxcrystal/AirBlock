package net.stuxcrystal.airblock.configuration.storage.storage.simple;

import lombok.NonNull;
import net.stuxcrystal.airblock.configuration.parser.files.FileType;
import net.stuxcrystal.airblock.configuration.storage.location.ConfigurationLocation;
import net.stuxcrystal.airblock.configuration.storage.location.UriLocation;
import net.stuxcrystal.airblock.configuration.storage.storage.SingleStringSeparatedLocationStorage;

import java.net.URI;

/**
 * Very simple storage-location for URIs.
 */
public class UriStorage extends SingleStringSeparatedLocationStorage {

    /**
     * The default file-type.
     */
    private final FileType ft;

    /**
     * The base uri.
     */
    private URI uri;

    /**
     * The separator character.
     *
     * @param suffix    The default suffix.
     * @param ft        The default file-type.
     */
    protected UriStorage(@NonNull URI uri, @NonNull String suffix, FileType ft) {
        super("/", suffix);
        this.uri = uri;
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
        String result = this.uri.toString();
        if (!result.endsWith("/")) result += "/";
        result += location;
        return new UriLocation(URI.create(result));
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
