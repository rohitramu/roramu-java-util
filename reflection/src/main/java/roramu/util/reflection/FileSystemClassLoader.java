package roramu.util.reflection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FileSystemClassLoader extends URLClassLoader {
    public FileSystemClassLoader(URL url, byte[] jar) {
        super(new URL[] {});

        Map<URL, byte[]> jars = new HashMap<>();
        jars.put(url, jar);

        init(jars);
    }

    public FileSystemClassLoader(URL url, byte[] jar, ClassLoader parent) {
        super(new URL[] {}, parent);

        Map<URL, byte[]> jars = new HashMap<>();
        jars.put(url, jar);

        init(jars);
    }

    public FileSystemClassLoader(Map<URL, byte[]> jars) {
        super(new URL[] {});

        init(jars);
    }

    public FileSystemClassLoader(Map<URL, byte[]> jars, ClassLoader parent) {
        super(new URL[] {}, parent);

        init(jars);
    }

    private void init(Map<URL, byte[]> jars) {
        if (jars == null || jars.isEmpty()) {
            throw new NullPointerException("'jars' cannot be null or empty");
        }

        // Validate the URLs and JARs before attempting to write the JARs to the file system
        for (URL url : jars.keySet()) {
            // Get the absolute path
            Path absolutePath;
            try {
                absolutePath = Paths.get(url.toURI()).normalize().toAbsolutePath();
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Found invalid URL '" + url.toString() + "'", ex);
            }

            // Check that it is pointing to a JAR file
            if (!FilenameUtils.getExtension(absolutePath.toString()).equalsIgnoreCase("jar")) {
                throw new IllegalArgumentException("Found URL '" + url.toString() + "' which does not represent a JAR file - JAR files must have the extension '.jar'");
            }

            // Make sure the file itself is not null or empty
            byte[] jar = jars.get(url);
            if (jar == null) {
                throw new IllegalArgumentException("Found JAR '" + url.toString() + "' which is null");
            }
            if (jar.length <= 0) {
                throw new IllegalArgumentException("Found JAR '" + url.toString() + "' which is an empty JAR file - JAR files cannot be empty");
            }
        }

        // Write the JARs to the file system
        for (URL url : jars.keySet()) {
            Path absolutePath;
            try {
                absolutePath = Paths.get(url.toURI()).normalize().toAbsolutePath();
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Found invalid URL '" + url.toString() + "'", ex);
            }

            // Create the file from the JAR using the URL
            try {
                System.out.println("Writing JAR '" + absolutePath.toString() + "' to disk");

                // Make sure to overwrite the file if it exists
                FileUtils.writeByteArrayToFile(absolutePath.toFile(), jars.get(url), false);
            } catch (IOException ex) {
                throw new RuntimeException("Unable to create JAR file '" + absolutePath.toString() + "'", ex);
            }

            // Add the URL to the ClassLoader
            super.addURL(url);
        }
    }

//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        Class<?> result = this.findLoadedClass(name);
//        
//        if (result == null) {
//            try {
//                result = this.findClass(name);
//            } catch (ClassNotFoundException ex) {
//                ClassLoader parent = this.getParent();
//                if (parent == null) {
//                    throw ex;
//                }
//                
//                result = parent.loadClass(name);
//            }
//        }
//        
//        return result;
//    }
}
