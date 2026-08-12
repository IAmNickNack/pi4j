package com.pi4j.test.context;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.exception.Pi4JException;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.Spi;
import com.pi4j.registry.Registry;
import com.pi4j.test.Slf4jStreamBridge;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class ContextTest {

    private static final Logger logger = LoggerFactory.getLogger(ContextTest.class);

    private Context pi4j;

    @BeforeAll
    public void beforeTest() throws Pi4JException {
        pi4j = Pi4J.newContextBuilder().autoDetectMockPlugins().build();
    }

    @AfterAll
    public void afterTest() {
        try {
            pi4j.shutdown();
        } catch (Pi4JException e) { /* do nothing */ }
    }

    @Test
    public void testFactoryContextAcquisition() throws Pi4JException {
        assertNotNull(pi4j);
        logger.info("-------------------------------------------------");
        logger.info("Pi4J CONTEXT <acquired via factory accessor>");
        logger.info("-------------------------------------------------");
        var ps = Slf4jStreamBridge.createPrintStream(logger);
        pi4j.describe().print(ps);
    }

    @Test
    void testDeprecatedFactoryMethodSupport() throws Pi4JException {
        var inputConfig = DigitalInput.newConfigBuilder().id("DIN-3").name("DIN-3").bcm(3);
        var outputConfig = DigitalOutput.newConfigBuilder().id("DOUT-3").name("DOUT-3").bcm(3);
        var i2cConfig = I2C.newConfigBuilder().bus(0).device(0x70).build();
        var pwmConfig = Pwm.newConfigBuilder().id("PWM-3").name("PWM-3").channel(3);
        var spiConfig = Spi.newConfigBuilder().bus(0).build();

        // create I/O instances via deprecated factory methods
        var input = pi4j.din().create(inputConfig);
        var output = pi4j.dout().create(outputConfig);
        var i2c = pi4j.i2c().create(i2cConfig);
        var pwm = pi4j.pwm().create(pwmConfig);
        var spi = pi4j.spi().create(spiConfig);

        Registry registry = pi4j.registry();
        assertAll(
            () -> assertTrue(registry.exists(input.id()), "Should exist: Digital Input by ID"),
            () -> assertTrue(registry.exists(output.id()), "Should exist: Digital Output by ID"),
            () -> assertTrue(registry.exists(pwm.id()), "Should exist: PWM by ID"),
            () -> assertTrue(registry.exists(i2c.id()), "Should exist: I2C by ID"),
            () -> assertTrue(registry.exists(spi.id()), "Should exist: Spi by ID")
        );


        // now shutdown all I/O instances by closing them.
        input.close();
        output.close();
        i2c.close();
        pwm.close();
        spi.close();

        assertAll(
            () -> assertFalse(registry.exists(input.id()), "Should not exist: Digital Input by ID"),
            () -> assertFalse(registry.exists(output.id()), "Should not exist: Digital Output by ID"),
            () -> assertFalse(registry.exists(pwm.id()), "Should not exist: PWM by ID"),
            () -> assertFalse(registry.exists(i2c.id()), "Should not exist: I2C by ID"),
            () -> assertFalse(registry.exists(spi.id()), "Should not exist: SPI by ID")
        );
    }

}
