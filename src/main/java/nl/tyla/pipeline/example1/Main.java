package nl.tyla.pipeline.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

/**
 * linear sequence of background tasks, with a parallel worker
 */
public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage: Main <string>");
            System.exit(-1);
        }
        LOGGER.info("Starting");

        String input = args[0];
        String s =
                supplyAsync(() -> {
                    LOGGER.info("input: {}", input);
                    return input;
                })
                .thenApplyAsync(Main::split)
                .thenComposeAsync(inputs -> forkJoin(inputs, Main::worker))
                .thenApplyAsync(Main::merge)
                .get();


        LOGGER.info("output: " + s);
    }

    private static List<String> split(String s1) {
        return Arrays.asList(s1.split("(?!^)"));
    }

    private static <I, O> CompletableFuture<List<O>> forkJoin(List<I> inputs, Function<I, O> aFunction) {
        CompletableFuture<O>[] futures = inputs.stream()
                .map(i -> supplyAsync(() -> aFunction.apply(i)))
                .toArray((IntFunction<CompletableFuture<O>[]>) CompletableFuture[]::new);
        return allOf(futures).thenApply(v -> Arrays.stream(futures)
                .map(CompletableFuture::join)
                .collect(toList()));
    }

    private static String merge(List<String> outputs) {
        return outputs.stream().collect(Collectors.joining());
    }

    private static String worker(String input) {
        LOGGER.info("worker");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return input.toUpperCase();
    }
}
