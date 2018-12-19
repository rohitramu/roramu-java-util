package roramu.util.time;

import java.time.Instant;

/**
 *
 */
public final class TimeUtils {
    private TimeUtils() {}

    public static final long getCurrentMillis() {
        return Instant.now().toEpochMilli();
    }
}
