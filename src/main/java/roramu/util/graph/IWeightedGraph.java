package roramu.util.graph;

/**
 * The contract for a weighted graph which is not necessarily directed.
 *
 * @param <V> The vertex value type.
 * @param <E> The edge weight type.
 */
public interface IWeightedGraph<V, E> extends IGraph<V> {
    void addEdge(String fromKey, String toKey, E weight);

    E getEdgeWeight(String fromKey, String toKey);

    void setEdgeWeight(String fromKey, String toKey, E weight);
}
