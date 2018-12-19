package roramu.util.reflection;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * Utilities for JAR files.
 */
public final class JarUtils {
    private static final String[] excludedPackages = {
        "java.",
        "javax.",
//        "sun.",
//        "com.sun."
    };
    public static final Predicate<String> defaultFilter = excludePackagesFilter(excludedPackages);

    private JarUtils() {}

    public static final Predicate<String> includePackagesFilter(String... keepPackages) {
        return excludePackagesFilter(keepPackages).negate();
    }

    public static final Predicate<String> excludePackagesFilter(String... removePackages) {
        return (String s) -> {
            for (String removePackage : removePackages) {
                if (s.startsWith(removePackage)) {
                    return false;
                }
            }

            return true;
        };
    }

    public static final String getResourceNameFromClass(Class<?> theClass) {
        return theClass.getName().replace('.', '/') + ".class";
    }

    /**
     * Gets the JAR that the class was loaded from. This method uses the
     * default values for excluded prefixes.
     *
     * @param theClass The class.
     *
     * @return The JAR that the class was loaded from.
     *
     * @throws IOException If a ClassReader could not be created for the given
     * class.
     * @throws ClassNotFoundException If the class could not be found.
     */
    public static final byte[] getFatJarFromClass(Class<?> theClass) throws IOException, ClassNotFoundException {
        return getFatJarFromClass(theClass, defaultFilter, true);
    }

    /**
     * Gets the JAR that the class was loaded from.
     *
     * @param theClass The class.
     * @param filter A function which returns true if it's input is a class name
     * that represents a class which should be included in this method's output,
     * otherwise returns false.
     * @param ignoreMissingClasses Whether or not to swallow the exception if a
     * dependency could not be located.
     *
     * @return The JAR that the class was loaded from.
     *
     * @throws IOException If a ClassReader could not be created for the given
     * class.
     * @throws ClassNotFoundException If the class could not be found.
     */
    public static final byte[] getFatJarFromClass(Class<?> theClass, Predicate<String> filter, boolean ignoreMissingClasses) throws IOException, ClassNotFoundException {
        if (theClass == null) {
            throw new NullPointerException("The provided class cannot be null");
        }

        ClassLoader classLoader = theClass.getClassLoader();
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        // Get the names of the class and all of it's dependencies
        Set<String> classNames = getClassDependencies(theClass.getName(), filter, ignoreMissingClasses);

        // Get the class objects from the names
        Set<Class<?>> classes = new HashSet<>();
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className, false, classLoader);
                classes.add(aClass);
            } catch (ClassNotFoundException ex) {
                if (!ignoreMissingClasses) {
                    throw ex;
                }
            }
        }

        // Create the manifest
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        for (Class<?> aClass : classes) {
            manifest.getEntries().put(
                aClass.getName().replace('.', '/') + ".class",
                new Attributes());
        }

        // Create the virtual (in-memory) JAR file
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (JarOutputStream stream = new JarOutputStream(byteStream, manifest)) {
            for (Class<?> aClass : classes) {
                String resourceName = JarUtils.getResourceNameFromClass(aClass);
                byte[] classBytes = IOUtils.resourceToByteArray(resourceName, classLoader);
                JarEntry jarEntry = new JarEntry(resourceName);
                jarEntry.setSize(classBytes.length);
                stream.putNextEntry(jarEntry);
                try {
                    stream.write(classBytes);
                } finally {
                    stream.closeEntry();
                }
            }
        }

        // Get the virtual (in-memory) JAR file
        byte[] result = byteStream.toByteArray();

        // Write to file for testing
//        File tempFile = File.createTempFile(theClass.getName(), ".jar");
//        FileUtils.writeByteArrayToFile(tempFile, result);
//        System.out.println(tempFile.getAbsolutePath());

        return result;
    }

    public static final byte[] getClassBytesFromJar(String name, byte[] jar) throws IOException {
        byte[] result = null;
        String jarClassName = name.replace('.', '/') + ".class";
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(jar);
        try (JarInputStream jarStream = new JarInputStream(byteInputStream)) {
            // Check the manifest first so we don't have to scan the whole JAR
            Manifest manifest = jarStream.getManifest();
            Set<String> manifestFileEntries = manifest.getEntries().keySet();
            if (manifestFileEntries.contains(jarClassName)) {
                // Check each class in this JAR
                JarEntry jarEntry;
                while ((jarEntry = jarStream.getNextJarEntry()) != null && result == null) {
                    // If we've found a class with the same name, pick it as the result
                    if (jarClassName.equals(jarEntry.getName())) {
                        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                        int thisByte;
                        while ((thisByte = jarStream.read()) != -1) {
                            byteOutputStream.write(thisByte);
                        }
                        result = byteOutputStream.toByteArray();
                    }
                }
            }
        }

        return result;
    }

    public static final Set<String> getClassDependencies(String className) throws IOException {
        return getClassDependencies(className, defaultFilter, true);
    }

    public static final Set<String> getClassDependencies(String className, Predicate<String> filter, boolean ignoreMissingClasses) throws IOException {
        if (className == null) {
            throw new NullPointerException("The provided class name cannot be null");
        }

        Deque<String> toVisit = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        toVisit.push(className);
        while (!toVisit.isEmpty()) {
            String currentClassName = toVisit.pop();
            visited.add(currentClassName);

            ClassReader classReader = null;
            try {
                classReader = new ClassReader(currentClassName);
            } catch (IOException ex) {
                System.out.println("Could not find class '" + currentClassName + "'. " + ex.toString());
                if (!ignoreMissingClasses) {
                    throw ex;
                }
            }

            if (classReader != null) {
                DependencyVisitor dependencyVisitor = new DependencyVisitor();
                classReader.accept(dependencyVisitor, 0);
                Map<String, Map<String, Integer>> globals = dependencyVisitor.getGlobals();
                Set<String> classNames = globals.keySet();
                for (String name : classNames) {
                    if (!visited.contains(name)) {
                        toVisit.push(name);
                    }

                    Set<String> dependencies = globals.get(name).keySet()
                        .parallelStream()
                        .filter(filter)
                        .filter(s -> !visited.contains(s))
                        .distinct()
                        .collect(Collectors.toSet());

                    for (String dep : dependencies) {
                        toVisit.push(dep);
                    }
                }
            }
        }

        return visited;
    }
}
