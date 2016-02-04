package moe.encode.airblock.configuration.parser.types;

import junit.framework.TestCase;
import moe.encode.airblock.configuration.parser.ConfigurationParser;
import moe.encode.airblock.configuration.parser.node.DataNode;
import moe.encode.airblock.configuration.parser.node.ListNode;
import moe.encode.airblock.configuration.parser.node.Node;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTypeTest extends TestCase {

    static class Fields {
        public List<String> field;
    }

    public void testSupportsType() throws Exception {
        ListType type = new ListType();
        assertTrue("Failed to support string array type.", type.supportsType(List.class));
    }

    public Type getTypeOfField() {
        return FieldUtils.getDeclaredField(Fields.class, "field").getGenericType();
    }

    public void testParse() throws Exception {
        ListType type = new ListType();
        ConfigurationParser parser = mock(ConfigurationParser.class);
        // when(parser.parseNode(any(Class.class), any(Node.class))).thenThrow(new IllegalArgumentException());
        when(parser.parseNode(eq(String.class), any(DataNode.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return ((DataNode) invocationOnMock.getArguments()[1]).getData();
            }
        });

        assertEquals(
                Arrays.asList("test", "a", "b"),
                type.parse(parser, getTypeOfField(), new ListNode(Arrays.<Node>asList(
                        new DataNode("test"), new DataNode("a"), new DataNode("b")
                )))
        );
    }

    public void testDump() throws Exception {
        ListType type = new ListType();
        ConfigurationParser parser = mock(ConfigurationParser.class);
        when(parser.dumpNode(eq(String.class), any(DataNode.class))).thenAnswer(new Answer<DataNode>() {
            @Override
            public DataNode answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new DataNode((String) invocationOnMock.getArguments()[1]);
            }
        });

        List<String> values = Arrays.asList("test", "a", "b");
        Node node = type.dump(parser, getTypeOfField(), values);
        assertTrue("Invalid instance types", node instanceof ListNode);
        for (int i = 0; i<((ListNode)node).getNodes().size(); i++) {
            Node child = ((ListNode)node).getNodes().get(i);
            assertTrue("Correct instance type", child instanceof DataNode);
            assertEquals(values.get(i), ((DataNode)child).getData());
        }
    }

}