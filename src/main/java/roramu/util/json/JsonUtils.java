package roramu.util.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import roramu.util.reflection.TypeInfo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

/**
 * Utilities for JSON operations.
 */
public final class JsonUtils {
    private static final ObjectMapper MAPPER = JsonUtils.getDefaultMapper();

    private JsonUtils() {}

    /**
     * Creates a new ObjectMapper with the default options enabled.
     *
     * @return The new ObjectMapper instance.
     */
    private static ObjectMapper getDefaultMapper() {
        return new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)

            .enable(DeserializationFeature.WRAP_EXCEPTIONS)

            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))

            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    }

    /**
     * Creates a new ObjectMapper instance which will pretty-print when
     * serializing JSON.
     *
     * @return The new ObjectMapper instance.
     */
    private static ObjectMapper toPrettyPrintMapper(ObjectMapper objectMapper) {
        return objectMapper.copy()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .enable(SerializationFeature.INDENT_OUTPUT);
    }

    private static ObjectMapper getMapper(boolean prettyPrint) {
        ObjectMapper mapper = MAPPER;
        if (prettyPrint) {
            mapper = toPrettyPrintMapper(mapper);
        }

        return mapper;
    }

    /**
     * Converts a Java object into a JSON string by looking at public getters
     * and fields.
     *
     * @param obj The object.
     *
     * @return The serialized JSON string.
     */
    public static final String write(Object obj) {
        return write(obj, false);
    }

    /**
     * Converts a Java object into a JSON string.
     *
     * @param obj The object.
     * @param prettyPrint Whether to pretty-print the JSON string.
     *
     * @return The serialized JSON string.
     */
    public static final String write(Object obj, boolean prettyPrint) {
        ObjectMapper mapper = getMapper(prettyPrint);
        String result = writeInternal(mapper, obj);
        return result;
    }

    /**
     * Internal implementation of writing a JSON string. This should be
     * abstracted by the API to avoid exposing implementation details (to
     * prevent callers from requiring a dependency on the Jackson JSON
     * library).
     *
     * @param mapper The mapper object which will be used to write the JSON
     * string.
     * @param obj The object.
     *
     * @return The serialized JSON string.
     *
     * @throws UncheckedIOException If there was any failure while writing the
     * JSON string.
     */
    private static String writeInternal(ObjectMapper mapper, Object obj) throws IllegalArgumentException, UncheckedIOException {
        if (obj == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new UncheckedIOException("Failed to write object of type '" + obj.getClass().toString() + "' as JSON: " + obj.toString(), ex);
        }
    }

    /**
     * Converts a JSON string into a Java object.
     *
     * @param <T> The type to convert the JSON into.
     * @param json The JSON string.
     * @param typeInfo The reference to the type to convert the JSON into.
     *
     * @return The deserialized Java object.
     *
     * @throws IllegalArgumentException If the provided string is not a valid
     * JSON string.
     * @throws UncheckedIOException If there was any other failure while reading
     * the JSON string.
     */
    public static final <T> T read(String json, TypeInfo<T> typeInfo) throws IllegalArgumentException, UncheckedIOException {
        ObjectMapper mapper = getMapper(false);
        Type type = typeInfo.getType();
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        T result = read(mapper, json, javaType);

        return result;
    }

    /**
     * Converts a JSON string into a Java object.
     *
     * @param <T> The type to convert the JSON into.
     * @param json The JSON string.
     * @param type The type to convert the JSON into.
     *
     * @return The deserialized Java object.
     *
     * @throws IllegalArgumentException If the provided string is not a valid
     * JSON string.
     * @throws UncheckedIOException If there was any other failure while reading
     * the JSON string.
     */
    public static final <T> T read(String json, Class<T> type) throws IllegalArgumentException, UncheckedIOException {
        ObjectMapper mapper = getMapper(false);
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        T result = read(mapper, json, javaType);

        return result;
    }

    /**
     * Internal implementation of reading a JSON string. This should be
     * abstracted by the API to avoid exposing implementation details (i.e. to
     * prevent callers from requiring a dependency on the Jackson JSON library).
     *
     * @param mapper The mapper object which will be used to read the JSON
     * string.
     * @param json The JSON string.
     * @param javaType The type to deserialize the JSON string into.
     *
     * @return The deserialized Java object.
     *
     * @throws IllegalArgumentException If the provided string is not a valid
     * JSON string.
     * @throws UncheckedIOException If there was any other failure while reading
     * the JSON string.
     */
    private static <T> T read(ObjectMapper mapper, String json, JavaType javaType) throws IllegalArgumentException, UncheckedIOException {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(json, javaType);
        } catch (JsonParseException ex) { // Unable to parse
            throw new IllegalArgumentException("Not a valid JSON string: " + json, ex);
        } catch (JsonMappingException ex) { // Bad format
            throw new IllegalArgumentException("Unexpected format: " + json, ex);
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to read message as JSON: " + json, ex);
        }
    }
}
