package net.stuxcrystal.airblock.configuration.parser.files;

import net.stuxcrystal.airblock.configuration.parser.node.Node;

import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Used to ensure that the parser does not close the passed output stream
 * by writing the output into a buffer that can be closed by the implementation.
 */
public abstract class WrapClosingParser implements FileType {

    /**
     * Override this method. The O
     * @param os    The output-stream. Closable.
     * @param node  The node to write.
     * @throws IOException If an I/O-Operation fails.
     */
    protected abstract void writeRaw(@WillClose OutputStream os, Node node) throws IOException;

    /**
     * Ensures that the stream is not closed when internal dumper is dumping the
     * data into the file.
     * @param stream  The node that should be written.
     * @param node    The node to write.
     * @throws IOException If an I/O-Exception fails.
     */
    @Override
    public final void write(@WillNotClose OutputStream stream, Node node) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeRaw(baos, node);
        stream.write(baos.toByteArray());
    }

}
