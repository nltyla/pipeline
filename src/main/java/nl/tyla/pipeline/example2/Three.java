package nl.tyla.pipeline.example2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

class Three {
    private static Logger LOGGER = LoggerFactory.getLogger(Three.class);

    String run(String string) {
        LOGGER.info("three");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
        } catch (InterruptedException ignored) {
        }
        // change number to increase the likelihood of exceptions
        if (Math.random() < 0.005) {
            throw new RuntimeException("Aargh: " + string);
        }
        return string.toUpperCase();
    }
}
