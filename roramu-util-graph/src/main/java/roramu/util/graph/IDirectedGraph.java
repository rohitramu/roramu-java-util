package roramu.util.graph;

import java.util.Set;

/**
 * The contract for a directed graph which is not necessarily weighted.
 *
 * @param <V> The vertex value type.
 */
public interface IDirectedGraph<V> extends IGraph<V> {
    /**
     * Gets the vertices that are connected to the given vertex by an edge. <br>
     * NOTE: The word "to" is used here instead of "from" as in the description
     * of {@link IGraph#getNeighbors(java.lang.String) getNeighbors()}.
     *
     * @param key The key which identifies the vertex to get the reverse
     * neighbors for.
     *
     * @return The keys that identify the given vertex's reverse neighbors.
     */
    Set<String> getReverseNeighbours(String key);
}
