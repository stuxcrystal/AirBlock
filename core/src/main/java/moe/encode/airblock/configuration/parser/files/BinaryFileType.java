package moe.encode.airblock.configuration.parser.files;

import moe.encode.airblock.configuration.parser.node.Node;

import javax.annotation.WillClose;
import java.io.*;

/**
 * Raw binary file writer.
 */
public class BinaryFileType extends WrapClosingParser {

    @Override
    protected void writeRaw(@WillClose OutputStream os, Node node) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        try {
            oos.writeObject(node);
        } finally {
            oos.close();
        }
    }

    @Override
    public Node load(@WillClose InputStream stream) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(stream);
        try {
            return (Node) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Class behind object not found", e);
        } finally {
            ois.close();
        }
    }
}
