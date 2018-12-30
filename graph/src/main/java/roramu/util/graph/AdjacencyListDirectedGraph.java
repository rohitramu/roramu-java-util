package roramu.util.graph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe implementation of a directed graph using an adjacency list.
 *
 * @param <V> The vertex value type.
 */
public class AdjacencyListDirectedGraph<V> implements IDirectedGraph<V> {
    private final Map<String, DirectedNode<V>> graph = new ConcurrentHashMap<>();

    @Override
    public void addEdge(String fromKey, String toKey) {
        if (fromKey == null) {
            throw new NullPointerException("'fromKey' cannot be null");
        }
        if (toKey == null) {
            throw new NullPointerException("'toKey' cannot be null");
        }

        // Check that "from" node exists
        DirectedNode<V> fromVertex = this.graph.get(fromKey);
        if (fromVertex == null) {
            throw new IllegalArgumentException("Node '" + fromKey + "' does not exist");
        }

        // Check that "to" node exists
        DirectedNode<V> toVertex = this.graph.get(toKey);
        if (toVertex == null) {
            throw new IllegalArgumentException("Node '" + toKey + "' does not exist");
        }

        // Make both the "from" and "to" nodes aware of the edge
        fromVertex.addOrSetEdgeTo(toKey);
        toVertex.addOrSetEdgeFrom(fromKey);
    }

    @Override
    public void addVertex(String key, V value) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Put the vertex in the graph
        this.graph.put(key, new DirectedNode<>(value));
    }

    @Override
    public boolean containsVertex(String key) {
        return this.graph.containsKey(key);
    }

    @Override
    public Set<String> getNeighbors(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> vertex = this.graph.get(key);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex '" + key + "' does not exist");
        }

        return Collections.unmodifiableSet(vertex.getNeighbors());
    }

    @Override
    public Set<String> getReverseNeighbours(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> vertex = this.graph.get(key);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex '" + key + "' does not exist");
        }

        return Collections.unmodifiableSet(vertex.getReverseNeighbors());
    }

    @Override
    public Set<String> getVertexKeys() {
        return Collections.unmodifiableSet(this.graph.keySet());
    }

    @Override
    public V getVertexValue(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> vertex = this.graph.get(key);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex '" + key + "' does not exist");
        }

        return vertex.getValue();
    }

    @Override
    public void setVertexValue(String key, V value) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> vertex = this.graph.get(key);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex '" + key + "' does not exist");
        }

        vertex.setValue(value);
    }

    @Override
    public boolean isConnected(String fromKey, String toKey) {
        if (fromKey == null) {
            throw new NullPointerException("'fromKey' cannot be null");
        }
        if (toKey == null) {
            throw new NullPointerException("'toKey' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> fromVertex = this.graph.get(fromKey);
        if (fromVertex == null) {
            throw new IllegalArgumentException("Vertex '" + fromKey + "' does not exist");
        }

        return fromVertex.isConnectedTo(toKey);
    }

    @Override
    public void removeEdge(String fromKey, String toKey) {
        if (fromKey == null) {
            throw new NullPointerException("'fromKey' cannot be null");
        }
        if (toKey == null) {
            throw new NullPointerException("'toKey' cannot be null");
        }

        // Make sure vertices exist
        DirectedNode<V> fromVertex = this.graph.get(fromKey);
        if (fromVertex == null) {
            throw new IllegalArgumentException("Vertex '" + fromKey + "' does not exist");
        }
        DirectedNode<V> toVertex = this.graph.get(toKey);
        if (toVertex == null) {
            throw new IllegalArgumentException("Vertex '" + toKey + "' does not exist");
        }

        // Make both the "from" and "to" nodes aware of the removed edge
        fromVertex.removeEdgeTo(toKey);
        toVertex.removeEdgeFrom(fromKey);
    }

    @Override
    public void removeVertex(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }

        // Make sure vertex exists
        DirectedNode<V> vertex = this.graph.get(key);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex '" + key + "' does not exist");
        }

        // Remove edges to/from neighbors
        for (String toKey : vertex.getNeighbors()) {
            this.removeEdge(key, toKey);
        }
        for (String fromKey : vertex.getReverseNeighbors()) {
            this.removeEdge(fromKey, key);
        }

        // Remove the vertex
        this.graph.remove(key);
    }
}
