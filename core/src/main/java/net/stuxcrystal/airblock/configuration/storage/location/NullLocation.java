package net.stuxcrystal.airblock.configuration.storage.location;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Points to nothing.
 */
public class NullLocation extends ConfigurationLocation {
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
     * <p>Opens an input stream to this location.</p>
     * <p>
     * Return {@code null} if the file does not exist.
     * </p>
     *
     * @return The input-stream to this location.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * The output-stream.
     *
     * @return The output-stream for this location.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
