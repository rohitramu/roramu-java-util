package roramu.util.graph;

/**
 * The contract for a directed, weighted graph.
 *
 * @param <V> The vertex value type.
 * @param <W> The edge weight type.
 */
public interface IDirectedWeightedGraph<V, W> extends IDirectedGraph<V>, IWeightedGraph<V, W> {
}
