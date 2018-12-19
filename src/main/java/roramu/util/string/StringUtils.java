package roramu.util.string;

/**
 * Utilities for string operations.
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private StringUtils() {}

    public static String padLeft(int resultLength, String string) {
        return String.format("%1$" + resultLength + "s", string);
    }

    public static String padRight(int resultLength, String string) {
        return String.format("%1$-" + resultLength + "s", string);
    }
}
