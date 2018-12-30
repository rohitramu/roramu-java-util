package roramu.util.reflection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class InMemoryClassLoader extends ClassLoader {
    private final byte[][] jars;
    private final Map<String, Class<?>> cache = new HashMap<>();

    public InMemoryClassLoader(byte[]... jars) {
        this(InMemoryClassLoader.class.getClassLoader());
    }

    public InMemoryClassLoader(ClassLoader parent, byte[]... jars) {
        super(parent);
        this.jars = jars;
    }

    // @Override
    // public Class<?> loadClass(String name) throws ClassNotFoundException {
    //    Class<?> result = this.findLoadedClass(name);
    //
    //    if (result == null) {
    //        try {
    //            result = this.findClass(name);
    //        } catch (ClassNotFoundException ex) {
    //            ClassLoader parent = this.getParent();
    //            if (parent == null) {
    //                throw ex;
    //            }
    //
    //            result = parent.loadClass(name);
    //        }
    //    }
    //
    //    return result;
    // }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // First try to get the class from the cache
        Class<?> result = cache.get(name);

        // If the cache does not contain the class, load it from the in-memory JARs
        if (result == null) {
            for (int i = 0; i < jars.length && result == null; i++) {
                // Get the next JAR
                byte[] jar = jars[i];

                try {
                    // Read the JAR to see if it contains the requested class
                    byte[] bytes = JarUtils.getClassBytesFromJar(name, jar);
                    if (bytes != null) {
                        result = this.defineClass(name, bytes, 0, bytes.length);
                    }
                } catch (IOException ex) {
                    // Swallow the exception so we can continue trying the next jars
                    // TODO: log
                    ex.printStackTrace();
                }
            }
        }

        if (result == null) {
            // We couldn't find the class, so throw an exception
            throw new ClassNotFoundException();
        } else {
            // Cache the class
            this.cache.put(name, result);
        }

        return result;
    }
}
