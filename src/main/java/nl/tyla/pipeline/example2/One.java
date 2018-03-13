package nl.tyla.pipeline.example2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class One {
    private static Logger LOGGER = LoggerFactory.getLogger(One.class);

    public ResultOne run(Input input) {
        LOGGER.info("one");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        } catch (InterruptedException ignored) {
        }
        return new ResultOne();
    }
}
