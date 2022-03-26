package Graphs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    Graph<String> graph = new Graph<>();

    @Test void connectNodeDoesNotTakeNulls() {
        assertThrows(IllegalArgumentException.class, () -> graph.connectNode(null, "Node 0"));
        assertThrows(IllegalArgumentException.class, () -> graph.connectNode("Node 0", null));
        assertThrows(IllegalArgumentException.class, () -> graph.connectNode(null, null));
    }
    @Test
    void test(){
        assertTrue(true);
        assertFalse(false);
        assertTrue(false);
    }
}
