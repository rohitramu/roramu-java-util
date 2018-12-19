package roramu.util.json;

/**
 * Method for deserializing objects into JSON strings.
 *
 * @param <T> The type of object that this can deserialize from JSON.
 */
@FunctionalInterface
public interface JsonDeserializer<T> {
    /**
     * Deserializes a JSON string into an object.
     *
     * @param json The JSON string.
     *
     * @return The deserialized object.
     */
    T deserialize(String json);
}
