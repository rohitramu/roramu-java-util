package roramu.util.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 *
 */
public final class AsyncUtils {
    /**
     * Wraps a {@link Future} in a {@link CompletableFuture}.
     *
     * @param future The future.
     * @param <T> The return type of the {@link Future}.
     *
     * @return The created {@link CompletableFuture}.
     */
    public static <T> CompletableFuture<T> ToCompletableFuture(Future<T> future) {
        if (future == null) {
            return null;
        }

        return startTask(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Starts a task asynchronously and returns a reference to it.
     *
     * @param task The task to run. If this is null, the created CompletedFuture
     * will return null.
     *
     * @return The reference to the task.
     */
    public static CompletableFuture<Void> startTask(Runnable task) {
        return startTask(() -> {
            task.run();
            return null;
        });
    }

    /**
     * Starts a task asynchronously and returns a reference to it.
     *
     * @param task The task to run. If this is null, the created CompletedFuture
     * will return null.
     * @param <T> The result type. Use {@link Void} if the task does not return
     * a result.
     *
     * @return The reference to the task.
     */
    public static <T> CompletableFuture<T> startTask(Supplier<T> task) {
        if (task == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(task);
    }

    /**
     * Sleeps for the given amount of time.
     *
     * @param timeout The length of time to sleep.
     * @param timeoutUnits The timeout units.
     */
    public static void sleep(long timeout, TimeUnit timeoutUnits) {
        try {
            timeoutUnits.sleep(timeout);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Thread was interrupted while sleeping", ex);
        }
    }
}
