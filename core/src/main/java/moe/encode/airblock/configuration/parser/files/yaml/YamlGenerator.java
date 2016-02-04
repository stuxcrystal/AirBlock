package moe.encode.airblock.configuration.parser.files.yaml;

import moe.encode.airblock.configuration.parser.node.Node;
import moe.encode.airblock.configuration.parser.files.WrapClosingParser;

import javax.annotation.WillClose;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Generator for Yaml.
 */
public class YamlGenerator extends WrapClosingParser {

    @Override
    protected void writeRaw(@WillClose OutputStream os, Node node) throws IOException {
        new YamlDumper().dump(new OutputStreamWriter(os, "UTF-8"), node);
    }

    @Override
    public Node load(@WillClose InputStream stream) throws IOException {
        return YamlParser.parse(stream);
    }
}
