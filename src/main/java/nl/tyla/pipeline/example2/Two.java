package nl.tyla.pipeline.example2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

class Two {
    private static Logger LOGGER = LoggerFactory.getLogger(Two.class);

    ResultTwo run(Input input, ResultOne resultOne) {
        LOGGER.info("two");

        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 3000));
        } catch (InterruptedException ignored) {
        }
        return new ResultTwo();
    }
}
