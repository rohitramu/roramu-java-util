package roramu.util.json;

import roramu.util.reflection.TypeInfo;

public class SimpleJsonConverter<T> implements JsonConverter<T> {
    private final TypeInfo<T> typeInfo;

    public SimpleJsonConverter(TypeInfo<T> typeInfo) {
        this.typeInfo = typeInfo;
    }

    @Override
    public String serialize(T obj) {
        return JsonUtils.write(obj);
    }

    @Override
    public T deserialize(String json) {
        return JsonUtils.read(json, this.getTypeInfo());
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
