package nl.tyla.pipeline.example3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * All forkjoin pool threads are used for processing. A downstream join does not tie
 * up a forkjoin pool thread.
 */
public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting");
        CompletableFuture[] futures = new CompletableFuture[100];
        for (int i = 0; i < 100; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> {
                LOGGER.info("supply");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ignored) {
                }
                return null;
            });
        }

        CompletableFuture.allOf(futures).thenRun(() -> LOGGER.info("Done")).join();
    }
}
