package roramu.util.json;

/**
 * Contains the methods required to serialize and deserialize JSON to/from a
 * given type.
 *
 * @param <T> The type that the JsonConverter can serialize and deserialize
 * to/from.
 */
public interface JsonConverter<T> extends JsonSerializer<T>, JsonDeserializer<T> {}
