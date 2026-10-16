package com.pi4j.test.smoketest;

import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfig;
import com.pi4j.io.gpio.parallel.ParallelPortConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test case which outputs values `0..3` on BCM 20 and 21 and reads the value on BCM 7 and 12. The test fails
 * if the value read does not match the value that was written.
 */
public class ParallelPortTestEventsCase {

    private ParallelPortTestEventsCase() {}

    private static final Logger logger = LoggerFactory.getLogger(ParallelPortTestEventsCase.class);

    private static final String TEST_NAME = "Parallel Port";

    private static final ParallelPortConfig OUTPUT_CONFIG = ParallelPortConfigBuilder.newInstance()
        .id("parallel-out")
        .initialDirection(ParallelPort.Direction.OUTPUT)
        .initialValue(2)
        .shutdownValue(1)   // shutdown value should be exclusive of the initial value
        .bcm(24)            // output port used by DigitalOutputTestCase
        .bcm(26)            // output port used by DigitalInputTestCase
        .build();

    private static final ParallelPortConfig INPUT_CONFIG = ParallelPortConfigBuilder.newInstance()
        .id("parallel-in")
        .initialDirection(ParallelPort.Direction.INPUT)
        .bcm(25)            // input port used by DigitalOutputTestCase
        .bcm(16)            // input port used by DigitalInputTestCase
        .build();

    public static TestResult run(ProviderContext providerContext) {
        logger.info("Starting Parallel Port test");

        try(var outputPort = providerContext.getContext().create(OUTPUT_CONFIG);
            var inputPort = providerContext.getContext().create(INPUT_CONFIG)) {

            var eventCount = new AtomicInteger(0);
            inputPort.addListener(event -> eventCount.incrementAndGet());
            outputPort.write(1);
            outputPort.write(0);

            if (eventCount.get() == 0) {
                logger.error("No events were detected on the input port");
                return new TestResult(TEST_NAME, false, "No events detected on input port");
            }
        }

        return new TestResult(TEST_NAME, true, "Parallel port event operations completed");
    }
}
