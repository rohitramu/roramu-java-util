package roramu.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utilities for types.
 */
public class TypeUtils {
    public static Type getType(Class<?> rawClass, Class<?>... parameters) {
        return getType(rawClass, null, parameters);
    }

    public static Type getType(Class<?> rawClass, Class<?> ownerType, Class<?>... parameters) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return parameters;
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return ownerType;
            }
        };
    }
}
