package roramu.util.json;

import roramu.util.reflection.TypeInfo;

public class SimpleJsonConverter<T> implements JsonConverter<T> {
    private final TypeInfo<T> typeInfo;

    public SimpleJsonConverter(TypeInfo<T> typeInfo) {
        this.typeInfo = typeInfo;
    }

    @Override
    public RawJsonString serialize(T obj) {
        return new RawJsonString(JsonUtils.write(obj));
    }

    @Override
    public T deserialize(RawJsonString json) {
        if (json == null) {
            throw new NullPointerException("Raw JSON string parameter cannot be null");
        }

        return JsonUtils.read(json.getValue(), this.getTypeInfo());
    }

    /**
     * A reference to the type that this JsonConverter can serialize and
     * deserialize.
     *
     * @return The type reference.
     */
    public TypeInfo<T> getTypeInfo() {
        return typeInfo;
    }
}
