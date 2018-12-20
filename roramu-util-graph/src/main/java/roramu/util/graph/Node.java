package roramu.util.graph;

/**
 * Represents a node in a directed, weighted graph.
 *
 * @param <V> The type of value this node can hold.
 */
class Node<V> extends WeightedNode<V, Object> {
    public Node(V value) {
        super(value);
    }

    public void addOrSetEdgeTo(String key) {
        super.addOrSetEdgeTo(key, null);
    }

    public void addOrSetEdgeFrom(String key) {
        super.addOrSetEdgeFrom(key, null);
    }
}
