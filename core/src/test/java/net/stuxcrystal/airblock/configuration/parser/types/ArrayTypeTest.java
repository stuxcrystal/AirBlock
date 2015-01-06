package net.stuxcrystal.airblock.configuration.parser.types;

import junit.framework.TestCase;
import net.stuxcrystal.airblock.configuration.parser.ConfigurationParser;
import net.stuxcrystal.airblock.configuration.parser.node.DataNode;
import net.stuxcrystal.airblock.configuration.parser.node.ListNode;
import net.stuxcrystal.airblock.configuration.parser.node.Node;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ArrayTypeTest extends TestCase {

    public void testSupportsType() throws Exception {
        ArrayType type = new ArrayType();
        assertTrue("Failed to support string array type.", type.supportsType(String[].class));
    }

    public void testParse() throws Exception {
        ArrayType type = new ArrayType();
        ConfigurationParser parser = mock(ConfigurationParser.class);
        // when(parser.parseNode(any(Class.class), any(Node.class))).thenThrow(new IllegalArgumentException());
        when(parser.parseNode(eq(String.class), any(DataNode.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return ((DataNode) invocationOnMock.getArguments()[1]).getData();
            }
        });

        assertTrue(Arrays.equals(
                new String[] {"test", "a", "b"},
                (Object[]) type.parse(parser, String[].class, new ListNode(Arrays.<Node>asList(
                        new DataNode("test"), new DataNode("a"), new DataNode("b")
                )))
        ));
    }

    public void testDump() throws Exception {
        ArrayType type = new ArrayType();
        ConfigurationParser parser = mock(ConfigurationParser.class);
        when(parser.dumpNode(eq(String.class), any(DataNode.class))).thenAnswer(new Answer<DataNode>() {
            @Override
            public DataNode answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new DataNode((String) invocationOnMock.getArguments()[1]);
            }
        });

        String[] values = new String[] {"test", "a", "b"};
        Node node = type.dump(parser, String[].class, values);
        assertTrue("Invalid instance types", node instanceof ListNode);
        for (int i = 0; i<((ListNode)node).getNodes().size(); i++) {
            Node child = ((ListNode)node).getNodes().get(i);
            assertTrue("Correct instance type", child instanceof DataNode);
            assertEquals(values[i], ((DataNode)child).getData());
        }
    }
}