package net.stuxcrystal.airblock.configuration.parser.files.yaml;

import net.stuxcrystal.airblock.configuration.parser.node.DataNode;
import net.stuxcrystal.airblock.configuration.parser.node.ListNode;
import net.stuxcrystal.airblock.configuration.parser.node.MapNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Implements a comment-aware dumper.
 */
public class YamlDumper {


    /**
     * True if something was written.
     */
    private boolean wroteSomething = false;


    /**
     * Dumps the node to the file.
     * @param writer The writer of the file
     * @param parent The parent node to dump.
     * @throws IOException I an I/O-Operation fails.
     */
    public void dump(Writer writer, Node parent) throws IOException {
        BufferedWriter buffer = new BufferedWriter(writer);
        writeComment(buffer, parent.getComment(), 0);
        writeNode(buffer, parent, 0);
        buffer.flush();
    }

    /**
     * Writes a writer.
     *
     * @param writer The writer to write in.
     * @param lines  The lines to write.
     * @param indent The indent to add.
     */
    void writeComment(BufferedWriter writer, String[] lines, int indent) {
        if (lines == null) return;

        for (String line : lines) {
            writeIndent(writer, indent);
            if (line == null || line.isEmpty()) {
                _newline(writer);
                continue;
            }

            _write(writer, "# ");
            _write(writer, line);
            _newline(writer);
        }
    }

    /**
     * Writes a node.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     * @param indent The indent to add.
     */
    void writeNode(BufferedWriter writer, Node node, int indent) {
        if (node instanceof DataNode)
            writeScalar(writer, (DataNode) node, indent);
        else if (node instanceof ListNode)
            writeSequence(writer, (ListNode) node, indent);
        else if (node instanceof MapNode)
            writeMapping(writer, (MapNode) node, indent);
    }

    /**
     * Writes the mapping.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     * @param indent The indent to add.
     */
    @SuppressWarnings("unchecked")
    void writeMapping(BufferedWriter writer, MapNode node, int indent) {
        for (Map.Entry<Node, Node> entry: node.getNodes().entrySet()) {
            if (!(entry.getKey() instanceof DataNode))
                throw new RuntimeException("Unsupported key type: Data");

            _newline(writer);
            writeComment(writer, entry.getValue().getComment(), indent);
            writeIndent(writer, indent);
            writeNodeKey(writer, (DataNode) entry.getKey());
            writeNode(writer, entry.getValue(), indent + 2);

        }
    }

    /**
     * Writes a sequence.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     */
    @SuppressWarnings("unchecked")
    void writeSequence(BufferedWriter writer, ListNode node, int indent) {
        for (Node current : node.getNodes()) {
            _newline(writer);
            writeComment(writer, current.getComment(), indent);
            writeIndent(writer, indent - 2);
            _write(writer, "- ");
            writeNode(writer, current, indent + 2);

        }
    }

    /**
     * Writes a scalar node.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     */
    @SuppressWarnings("unchecked")
    void writeScalar(BufferedWriter writer, DataNode node, int indent) {
        if (node.getData().isEmpty()) {
            _write(writer, "\"\"");
            return;
        }

        String text = node.getData();
        if (!text.contains("\n")) {
            _write(writer, StringEscapeUtils.escapeXml11(node.getData()));
        } else {
            // WARNING: PARSING MAY BE INACCURATE
            _write(writer, ">");
            _newline(writer);
            for (String line : text.split("\n")) {
                writeIndent(writer, indent);
                _write(writer, line);
                _newline(writer);
            }


        }
    }

    /**
     * Writes the Node-Key.
     *
     * @param writer The writer to write a key to.
     * @param node   The node
     */
    void writeNodeKey(BufferedWriter writer, DataNode node) {
        _write(writer, node.getData());
        _write(writer, ": ");
    }

    /**
     * Writes the indentation.
     *
     * @param writer The writer to write a key to.
     * @param indent The indent to write.
     */
    private void writeIndent(BufferedWriter writer, int indent) {
        char[] chars = new char[indent];
        for (int i = 0; i < indent; i++)
            chars[i] = ' ';

        _write(writer, new String(chars));
    }

    /**
     * Writes a string to the writer.
     *
     * @param writer The writer to write a text to.
     * @param text   The text to write.
     */
    private void _write(BufferedWriter writer, String text) {
        this.wroteSomething = true;

        try {
            writer.write(text);
        } catch (IOException e) {
            throw new RuntimeException("Failed to dump data", e);
        }
    }

    /**
     * Writes a newline to the writer.
     *
     * @param writer The writer to write to.
     */
    private void _newline(BufferedWriter writer) {
        if (!this.wroteSomething) return;

        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
