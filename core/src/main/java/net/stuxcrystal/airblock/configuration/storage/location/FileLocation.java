package net.stuxcrystal.airblock.configuration.storage.location;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.*;

/**
 * Pointer to a file location.
 */
@AllArgsConstructor
public class FileLocation extends ConfigurationLocation {

    /**
     * Contains the file.
     */
    @NonNull
    private final File file;

    /**
     * Can you write to this location.
     *
     * @return If we can write to this location.
     */
    @Override
    public boolean canWrite() {
        return this.file.canWrite();
    }

    /**
     * Opens an input stream to this location.
     *
     * @return The input-stream to this location.
     */
    @Override
    public InputStream getInputStream() throws IOException{
        try {
            return new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * The output-stream.
     *
     * @return The output-stream for this location.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        FileLocation.ensureExists(this.file);
        return new FileOutputStream(this.file);
    }

    /**
     * Ensures that the file at the given location exists.
     * @param file
     * @throws IOException
     */
    private static void ensureExists(File file) throws IOException{
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs())
                    throw new IOException("Parent directory couldn't be created.");
            }
            if (!file.createNewFile())
                throw new IOException("Failed to create directory.");
        }
    }
}
