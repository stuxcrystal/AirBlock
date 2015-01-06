package net.stuxcrystal.airblock.configuration.parser.files;

import net.stuxcrystal.airblock.configuration.parser.node.Node;

import javax.annotation.WillClose;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a file type.
 */
public interface FileType {

    /**
     * Writes a node into the stream.
     * @param stream  The node that should be written.
     * @param node    The node to write.
     * @throws IOException If an I/O-Error occures.
     */
    public void write(@WillClose OutputStream stream, Node node) throws IOException;

    /**
     * Reads a node from the stream.
     * @param stream   The stream that should be read.
     * @return The node that has been read.
     * @throws IOException If an I/O-Error occures.
     */
    public Node load(@WillClose InputStream stream) throws IOException;

}
