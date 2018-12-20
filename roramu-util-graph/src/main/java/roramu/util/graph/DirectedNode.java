package roramu.util.graph;

/**
 * Represents a node in a directed graph.
 *
 * @param <V> The type of value this node can hold.
 */
public class DirectedNode<V> extends DirectedWeightedNode<V, Object> {
    public DirectedNode(V value) {
        super(value);
    }

    public void addOrSetEdgeTo(String key) {
        super.addOrSetEdgeTo(key, null);
    }

    public void addOrSetEdgeFrom(String key) {
        super.addOrSetEdgeFrom(key, null);
    }
}
