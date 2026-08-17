package com.pi4j.plugin.ffm.providers.parallel;

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfigBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("just an example for now")
class FFMParallelPortProviderTest {

    @Test
    void test() throws Exception {
        var context = Pi4J.newContextBuilder()
            .add(new FFMParallelPortProvider())
            .build();

        var device = context.create(ParallelPortConfigBuilder.newInstance()
            .id("test-gpio")
            .initialDirection(ParallelPort.Direction.OUTPUT)
            .bcm(0)
            .bcm(5)
            .bcm(19)
            .bcm(26)
            .build()
        );

        var virtualProvider = device.digitalOutputProvider();

        var digital0 = virtualProvider.create(DigitalOutputConfigBuilder.newInstance()
            .bcm(0)
            .build()
        );
        var digital1 = virtualProvider.create(DigitalOutputConfigBuilder.newInstance()
            .bcm(1)
            .build()
        );

        // This is an annoyance which could be avoided if initialisation was done during construction.
        device.initialize(context);

        device.write(1);
        Thread.sleep(500);
        device.write(0x2);
        Thread.sleep(500);
        device.write(0x4);
        Thread.sleep(500);
        device.write(0x8);

        Thread.sleep(1000);
        device.write(0);

        digital0.setState(1);
        Thread.sleep(500);
        digital1.setState(1);

        Thread.sleep(1000);
        device.write(0);

        assertNotNull(device);
    }

}