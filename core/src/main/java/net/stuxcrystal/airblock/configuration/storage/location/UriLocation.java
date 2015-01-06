package net.stuxcrystal.airblock.configuration.storage.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * An URI that points to the given location.
 */
@AllArgsConstructor
public class UriLocation extends ConfigurationLocation {

    /**
     * The URI.
     */
    @Getter
    private final URI uri;

    /**
     * Can you write to this location.
     *
     * @return If we can write to this location.
     */
    @Override
    public boolean canWrite() {
        return false;
    }

    /**
     * Opens an input stream to this location.
     *
     * @return The input-stream to this location.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return this.uri.toURL().openStream();
    }

    /**
     * The output-stream.
     *
     * @return The output-stream for this location.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Unsupported operation.");
    }

}
