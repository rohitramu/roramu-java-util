package roramu.util.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 */
public final class ExceptionUtils {
    private ExceptionUtils() {}

    public static String getStackTraceAsString(Throwable throwable) {
        try (
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteStream)
        ) {
            throwable.printStackTrace(printStream);
            String result = byteStream.toString();
            return result;
        } catch (IOException ex) {
            // This should never happen
            throw new RuntimeException("Failed to get stack trace", ex);
        }
    }

    public static void swallowRuntimeExceptions(ThrowingRunnable task) {
        try {
            task.run();
        } catch (RuntimeException ex) {
            // TODO: log
            System.err.println("Swallowed RuntimeException: " + ex.toString());
        }
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws RuntimeException;
    }
}
