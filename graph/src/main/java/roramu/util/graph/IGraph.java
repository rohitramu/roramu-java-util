package roramu.util.graph;

import java.util.Collections;
import java.util.Set;

/**
 * The contract for a graph which is not necessarily weighted or directed.
 *
 * @param <V> The vertex value type.
 */
public interface IGraph<V> {
    /**
     * Checks whether the graph contains a specified vertex.
     *
     * @param key The key which identifies the vertex.
     *
     * @return True if the vertex is in the graph, otherwise false.
     */
    boolean containsVertex(String key);

    /**
     * Adds a vertex to the graph.
     *
     * @param key The key to be used to identify the vertex. This should be
     * unique in the graph.
     * @param value The value associated with this vertex.
     */
    void addVertex(String key, V value);

    /**
     * Removes a vertex and all of it's edges from the graph.
     *
     * @param key The key which identifies the vertex to remove.
     */
    void removeVertex(String key);

    /**
     * Adds an edge between two vertices.
     *
     * @param fromKey The key which identifies the vertex that the edge should
     * start from.
     * @param toKey The key which identifies the vertex that the edge should end
     * at.
     */
    void addEdge(String fromKey, String toKey);

    /**
     * Removes an edge from the graph.
     *
     * @param fromKey The key which identifies the vertex that the edge starts
     * from.
     * @param toKey The key which identifies the vertex that the edge ends at.
     */
    void removeEdge(String fromKey, String toKey);

    /**
     * Gets all the keys that identify all the vertices in the graph. The
     * returned set should be immutable (e.g. by wrapping the result in {@link
     * Collections#unmodifiableSet(java.util.Set)})
     *
     * @return The set of keys that identify all the vertices in the graph.
     */
    Set<String> getVertexKeys();

    /**
     * Gets the value of a vertex.
     *
     * @param key The key that identifies the vertex to get the value for.
     *
     * @return The value of the given vertex.
     */
    V getVertexValue(String key);

    /**
     * Sets the value of a vertex.
     *
     * @param key The key that identifies the vertex to set the value for.
     * @param value The value to set.
     */
    void setVertexValue(String key, V value);

    /**
     * Determines whether two vertices in the graph are connected by an edge.
     * The order of the provided keys may matter depending on the implementation
     * of the graph. In other words, if this is a directed graph, the order
     * matters, otherwise it does not.
     *
     * @param fromKey The key which identifies the vertex that the edge might
     * start from.
     * @param toKey The key which identifies the vertex that the edge might end
     * at.
     *
     * @return True if the two vertices are connected by an edge.
     */
    boolean isConnected(String fromKey, String toKey);

    /**
     * Gets the vertices that are connected from the given vertex by an edge.
     * The returned set should be immutable (e.g. by wrapping the result in
     * {@link Collections#unmodifiableSet(java.util.Set)})
     *
     * @param key The key which identifies the vertex to get neighbors for.
     *
     * @return The keys that identify the given vertex's neighboring vertices.
     */
    Set<String> getNeighbors(String key);
}
