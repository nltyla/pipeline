package nl.tyla.pipeline.example2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

/**
 * Network of tasks One, Two, Three and Result. The allOf() clauses define which
 * inputs each task requires. Task Three is special because it processes each element
 * of its input list concurrently.
 */
class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Result run(Input input) {

        One one = new One();
        Two two = new Two();
        Three three = new Three();

        CompletableFuture<Input> fInput = completedFuture(input);
        // Alternative to use in case producing input is expensive
        //CompletableFuture<Input> fInput = supplyAsync(() -> input);

        CompletableFuture<ResultOne> fResultOne = allOf(fInput)
                .thenApplyAsync(v -> one.run(fInput.join()));

        CompletableFuture<ResultTwo> fResultTwo = allOf(fInput, fResultOne)
                .thenApplyAsync(v -> two.run(fInput.join(), fResultOne.join()));

        CompletableFuture<ResultThree> fResultThree = allOf(fInput, fResultOne)
                .thenComposeAsync(v -> Main.forkJoin(fInput.join().getThreeInput(), three::run)
                        .thenApply(ResultThree::new));

        CompletableFuture<Result> fResult = allOf(fInput, fResultOne, fResultTwo, fResultThree)
                .thenApplyAsync(v -> new Result(
                        fInput.join(), fResultOne.join(), fResultTwo.join(), fResultThree.join()));

        return fResult.join();
    }

    private static <I, O> CompletableFuture<List<O>> forkJoin(List<I> inputs, Function<I, O> aFunction) {
        CompletableFuture<O>[] futures = inputs.stream()
                .map(i -> supplyAsync(() -> aFunction.apply(i)))
                .toArray((IntFunction<CompletableFuture<O>[]>) CompletableFuture[]::new);
        return allOf(futures).thenApply(v -> Arrays.stream(futures)
                .map(CompletableFuture::join)
                .collect(toList()));
    }

    public static void main(String[] args) {
        List<String> threeInput = Stream.of("aap", "noot", "mies", "wim", "zus", "jet", "teun", "vuur", "gijs",
                "lam", "kees", "bok").collect(toList());

        Result result = new Main().run(new Input(threeInput));
        LOGGER.info("Done");
    }
}
