package roramu.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * This class is used to reference a type and it's generic type arguments at
 * runtime, since they are not normally available due to type-erasure.
 *
 * @param <T> The type to reference.
 */
public abstract class TypeInfo<T> implements Supplier<TypeInfo<T>> {
    public static final TypeInfo<Void> VOID = new TypeInfo<Void>() {};
    public static final TypeInfo<Object> OBJECT = new TypeInfo<Object>() {};
    public static final TypeInfo<Boolean> BOOLEAN = new TypeInfo<Boolean>() {};
    public static final TypeInfo<String> STRING = new TypeInfo<String>() {};
    public static final TypeInfo<Character> CHARACTER = new TypeInfo<Character>() {};
    public static final TypeInfo<Byte> BYTE = new TypeInfo<Byte>() {};
    public static final TypeInfo<Short> SHORT = new TypeInfo<Short>() {};
    public static final TypeInfo<Integer> INTEGER = new TypeInfo<Integer>() {};
    public static final TypeInfo<Long> LONG = new TypeInfo<Long>() {};
    public static final TypeInfo<Double> DOUBLE = new TypeInfo<Double>() {};
    public static final TypeInfo<Float> FLOAT = new TypeInfo<Float>() {};

    protected final Type type;

    protected TypeInfo() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            // This should never happen
            throw new IllegalArgumentException("TypeReference cannot be constructed without type information");
        }

        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public TypeInfo<T> get() {
        // We just need an implementation, not a good one...
        return this;
    }
}
