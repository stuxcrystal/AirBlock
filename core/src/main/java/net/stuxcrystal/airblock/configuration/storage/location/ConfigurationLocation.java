package net.stuxcrystal.airblock.configuration.storage.location;

import net.stuxcrystal.airblock.configuration.parser.files.FileType;
import net.stuxcrystal.airblock.configuration.parser.node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>The location for the configuration.</p>
 * <p>
 *     The location of the input-file may not be the same as the location of the output file.
 *     Thus, make sure you write *ALL* your data into the stream.
 * </p>
 */
public abstract class ConfigurationLocation {

    /**
     * Can you write to this location.
     * @return If we can write to this location.
     */
    public abstract boolean canWrite();

    /**
     * <p>Opens an input stream to this location.</p>
     * <p>
     *     Return {@code null} if the file does not exist.
     * </p>
     * @return The input-stream to this location.
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Loads the given configuration file.
     * @param fileType    The type of the file.
     * @return The given node.
     * @throws IOException If an I/O-Operation fails.
     */
    public Node read(FileType fileType) throws IOException {
        InputStream is = null;
        // I h8 TCFTC.
        try {
            is = this.getInputStream();
            if (is == null)
                throw new IOException("No stream obtained.");
            return fileType.load(is);
        } finally {
            if (is != null) try { is.close(); } catch (IOException ignored) {};
        }
    }

    /**
     * The output-stream.
     * @return The output-stream for this location.
     */
    public abstract OutputStream getOutputStream() throws IOException;

    /**
     * Saves the given node to
     * @param node       The node of the file.
     * @param fileType   The file type.
     * @throws IOException If an I/O-Operation fails.
     */
    public void write(Node node, FileType fileType) throws IOException {
        if (!this.canWrite())
            throw new IOException("You cannot write to this location.");
        OutputStream os = null;
        try {
            os = this.getOutputStream();
            if (os == null)
                throw new IOException("No stream obtained.");
            fileType.write(os, node);
        } finally {
            if (os != null) try { os.close(); } catch (IOException ignored) {};
        }
    }
}
