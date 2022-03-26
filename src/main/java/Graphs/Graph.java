package Graphs;


import java.util.*;

public class Graph<T> {

    public static void main(String[] args) {
        Graph<String> graph = new Graph<>();
        graph.addNode("Node 0");
        graph.connectNode("Node 0", "Node 1");
        System.out.print(graph.nodes);
    }

    Set<Node> nodes = new HashSet<>();

    public boolean addNode(T data) {
        return nodes.add(new Node(data));
    }

    public boolean connectNode(T originalData, T neighbourData) {
       if(originalData == null || neighbourData == null){
           throw new IllegalArgumentException("Arguments cannot be null");
       }
       Node neighbour = new Node(neighbourData);
       Optional<Node> optNeighbour = nodes.stream().filter(neighbour::equals).findFirst();
       if(optNeighbour.isPresent()){
           optNeighbour.get().addNeighbour(originalData);
           return true;
       }
        throw new RuntimeException();
    }

    private class Node {
        T data;
        List<T> neighbours = new ArrayList<>();

        Node(T data) {
            this.data = data;
        }

        public boolean addNeighbour(T neighbour) {
            return neighbours.add(neighbour);
        }

        public boolean addNeighbours(List<T> neighbours) {
            return this.neighbours.addAll(neighbours);
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Graph.Node node)) {
                return false;
            } else
                return Objects.equals(data, node.data);
        }

        @Override public int hashCode() {
            return Objects.hash(data);
        }
    }
}
