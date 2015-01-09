package net.stuxcrystal.airblock.configuration.storage.storage.multi;

import lombok.AllArgsConstructor;
import net.stuxcrystal.airblock.configuration.storage.location.ConfigurationLocation;
import net.stuxcrystal.airblock.configuration.storage.storage.ConfigurationStorage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Location that uses multiple storages.</p>
 * <p>However if you open a file to write, it will shadow t</p>
 */
@AllArgsConstructor
public class MultiStorageLocation extends ConfigurationLocation {

    /**
     * The multi-storage-object.
     */
    private MultiStorage storage;

    /**
     * The module.
     */
    private String[] module;

    /**
     * The configuration.
     */
    private String configuration;

    /**
     * Can you write to this location.
     *
     * @return If we can write to this location.
     */
    @Override
    public boolean canWrite() {
        return storage.getWriteStorage().canWrite(module, configuration);
    }

    /**
     * Opens an input stream to this location.
     *
     * @return The input-stream to this location.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        // Sine we are going to suppress all unhandles IOExceptions and only throw them
        // if we were unable to get a stream, we will access the location.
        List<IOException> exceptionList = new ArrayList<IOException>();

        // Try all locations but try the write-storage first.
        for (ConfigurationStorage cs : ArrayUtils.add(storage.getChildStorage(), 0, storage.getWriteStorage())) {
            // Try to access the storage location.
            InputStream is = null;
            try {
                is = cs.getInputStream(module, configuration);
            } catch (IOException e) {
                // Just suppress IOExceptions.
                exceptionList.add(e);
            }

            // If we were able to access the location, return the stream.
            if (is != null)
                return is;
        }

        if (exceptionList.size() == 0)
            return null;
        else if (exceptionList.size() == 1) {
            // Just rethrow the only one that occured.
            throw exceptionList.get(0);
        } else {
            // Throw all suppressed exceptions.
            IOException exception = new IOException("Failed to obtain the stream. The following exceptions have been suppressed.");
            for (IOException e : exceptionList)
                exception.addSuppressed(e);
            throw exception;
        }
    }

    /**
     * The output-stream.
     *
     * @return The output-stream for this location.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.storage.getWriteStorage().getOutputStream(module, configuration);
    }
}
