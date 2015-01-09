package net.stuxcrystal.airblock.configuration.storage.storage.simple;

import lombok.NonNull;
import net.stuxcrystal.airblock.configuration.parser.files.FileType;
import net.stuxcrystal.airblock.configuration.storage.location.ConfigurationLocation;
import net.stuxcrystal.airblock.configuration.storage.location.NullLocation;
import net.stuxcrystal.airblock.configuration.storage.location.UriLocation;
import net.stuxcrystal.airblock.configuration.storage.storage.SingleStringSeparatedLocationStorage;

import java.net.URL;

/**
 * The resource-stream-storage.
 */
public class ResourceStreamStorage extends SingleStringSeparatedLocationStorage {

    /**
     * The default file-type.
     */
    private final FileType ft;

    /**
     * The base uri.
     */
    private ClassLoader cl;

    /**
     * The base path.
     */
    private String base;

    /**
     * The separator character.
     *
     * @param suffix    The default suffix.
     * @param ft        The default file-type.
     */
    public ResourceStreamStorage(@NonNull ClassLoader cl, @NonNull String base, @NonNull String suffix, FileType ft) {
        super("/", suffix);
        this.cl = cl;
        this.ft = ft;
        this.base = base;
    }

    /**
     * The given configuration location.
     *
     * @param location The given location.
     * @return The configuration.
     */
    @Override
    public ConfigurationLocation getConfiguration(String location) {
        // Get the correct path.
        String result = this.base;
        if (!result.endsWith("/")) result += "/";
        result += location;

        // Access the resource.
        URL url = this.cl.getResource(result);
        if (url == null)
            return new NullLocation();

        // Create an URI-Location.
        UriLocation ul = UriLocation.fromURL(url);
        return ul!=null?ul:new NullLocation();
    }

    /**
     * Returns the file-type at the given location.
     *
     * @param location The location.
     * @return the file type.
     */
    @Override
    public FileType getType(String location) {
        return ft;
    }
}
