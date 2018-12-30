package roramu.util.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a node in a directed, weighted graph.
 *
 * @param <V> The type of value this node can hold.
 * @param <E> The type of value that represents edge weight.
 */
class DirectedWeightedNode<V, E> {
    private final Map<String, E> connectsTo = new HashMap<>();
    private final Map<String, E> connectsFrom = new HashMap<>();
    private V value;

    public DirectedWeightedNode(V value) {
        this.value = value;
    }

    public V getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param newValue The value to set.
     *
     * @return The old value.
     */
    public V setValue(V newValue) {
        V oldValue = this.value;
        this.value = newValue;
        return oldValue;
    }

    public void addOrSetEdgeTo(String key, E edgeWeight) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        connectsTo.put(key, edgeWeight);
    }

    public void removeEdgeTo(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        connectsTo.remove(key);
    }

    public void addOrSetEdgeFrom(String key, E edgeWeight) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        connectsFrom.put(key, edgeWeight);
    }

    public void removeEdgeFrom(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        connectsFrom.remove(key);
    }

    public Set<String> getNeighbors() {
        return Collections.unmodifiableSet(connectsTo.keySet());
    }

    public Set<String> getReverseNeighbors() {
        return Collections.unmodifiableSet(connectsFrom.keySet());
    }

    public E getEdgeTo(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        return connectsTo.get(key);
    }

    public E getEdgeFrom(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        return connectsFrom.get(key);
    }

    public boolean isConnectedTo(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        return connectsTo.containsKey(key);
    }

    public boolean isConnectedFrom(String key) {
        if (key == null) {
            throw new NullPointerException("'key' cannot be null");
        }
        return connectsFrom.containsKey(key);
    }
}
