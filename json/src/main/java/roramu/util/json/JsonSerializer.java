package roramu.util.json;

/**
 * Method for serializing objects into JSON strings.
 *
 * @param <T> The type of object that this can serialize to JSON.
 */
@FunctionalInterface
public interface JsonSerializer<T> {
    /**
     * Serializes the given object into a JSON string.
     *
     * @param obj The object to serialize.
     * @return The serialized JSON string.
     */
    RawJsonString serialize(T obj);
}
