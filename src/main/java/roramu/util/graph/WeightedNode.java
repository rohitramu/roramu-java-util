package roramu.util.graph;

/**
 * Represents a node in a weighted graph.
 *
 * @param <V> The type of value this node can hold.
 * @param <E> The type of value that represents edge weight.
 */
public class WeightedNode<V, E> extends DirectedWeightedNode<V, E> {
    public WeightedNode(V value) {
        super(value);
    }

    @Override
    public void addOrSetEdgeTo(String key, E edgeWeight) {
        super.addOrSetEdgeTo(key, edgeWeight);
        super.addOrSetEdgeFrom(key, edgeWeight);
    }

    @Override
    public void addOrSetEdgeFrom(String key, E edgeWeight) {
        this.addOrSetEdgeTo(key, edgeWeight);
    }

    @Override
    public void removeEdgeTo(String key) {
        super.removeEdgeTo(key);
        super.removeEdgeFrom(key);
    }

    @Override
    public void removeEdgeFrom(String key) {
        this.removeEdgeTo(key);
    }
}
