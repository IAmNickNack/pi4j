package com.pi4j.plugin.mock.provider.gpio.parallel;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.exception.IOAlreadyExistsException;
import com.pi4j.io.exception.IOBoundsException;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfigBuilder;
import com.pi4j.io.gpio.parallel.ParallelPortDigitalOutputProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockParallelPortProviderTest {

    @Nested
    class OutputProviderReadWriteTests {

        private final Context context = Pi4J.newContextBuilder()
            .add(new MockParallelPortProvider())
            .build();

        private final ParallelPort port = context.create(ParallelPortConfigBuilder.newInstance()
            .initialDirection(ParallelPort.Direction.OUTPUT)
            .id("mock-parallel-port")
            .bcm(1)
            .bcm(3)
            .build()
        );

        @AfterEach
        void afterEach() {
            port.write(0);
            assertEquals(0, port.read());
        }

        @Test
        void canWriteDigitalValue() {
            var outputProvider = port.digitalOutputProvider();
            var output = outputProvider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(0)
                .build()
            );
            output.state(DigitalState.HIGH);
            assertEquals(DigitalState.HIGH, output.state());
            assertEquals(1, port.read());
        }

        @Test
        void canWriteMultipleDigitalOutputs() {
            var outputProvider = port.digitalOutputProvider();
            var output0 = outputProvider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(0)
                .build()
            );

            output0.state(DigitalState.HIGH);
            assertEquals(DigitalState.HIGH, output0.state());
            assertEquals(1, port.read());

            var output1 = outputProvider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(1)
                .build()
            );

            output1.state(DigitalState.HIGH);
            assertEquals(DigitalState.HIGH, output1.state());
            assertEquals(3, port.read());
        }

        @Test
        void canRewriteDigitalValues() {
            port.write(3);
            var outputProvider = port.digitalOutputProvider();
            var output0 = outputProvider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(0)
                .build()
            );
            output0.state(DigitalState.LOW);
            assertEquals(DigitalState.LOW, output0.state());
            assertEquals(2, port.read());
        }
    }

    @Nested
    class InputProviderReadTests {

        private final Context context = Pi4J.newContextBuilder()
            .add(new MockParallelPortProvider())
            .build();

        private final ParallelPort port = context.create(ParallelPortConfigBuilder.newInstance()
            .initialDirection(ParallelPort.Direction.OUTPUT)
            .id("mock-parallel-port")
            .bcm(1)
            .bcm(3)
            .build()
        );

        @Test
        void canReadDigitalValue() {
            port.write(3);
            var inputProvider = port.digitalInputProvider();
            var input = inputProvider.create(DigitalInputConfigBuilder.newInstance()
                .bcm(1)
                .build()
            );
            assertEquals(DigitalState.HIGH, input.state());

            port.write(1);
            assertEquals(DigitalState.LOW, input.state());
        }
    }

    @Nested
    class OutputProviderAvailabilityTests {
        private final Context context = Pi4J.newContextBuilder()
            .add(new MockParallelPortProvider())
            .build();

        private final ParallelPort port = context.create(ParallelPortConfigBuilder.newInstance()
            .initialDirection(ParallelPort.Direction.OUTPUT)
            .id("mock-parallel-port")
            .bcm(1)
            .bcm(3)
            .build()
        );

        @Test
        void canCreateValidDigitalOutput() {
            var provider = new ParallelPortDigitalOutputProvider(port);
            var output0 = provider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(1)
                .build()
            );
            assertNotNull(output0);

            var output1 = provider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(3)
                .build()
            );
            assertNotNull(output1);
        }

        @Test
        void cannotCreateInvalidDigitalOutput() {
            var provider = new ParallelPortDigitalOutputProvider(port);
            assertThrows(IOBoundsException.class, () -> provider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(2)
                .build()
            ));
        }

        @Test
        void cannotRecreateTheSameOffset() {
            var provider = new ParallelPortDigitalOutputProvider(port);
            provider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(1)
                .build()
            );
            assertThrows(IOAlreadyExistsException.class, () -> provider.create(DigitalOutputConfigBuilder.newInstance()
                .bcm(1)
                .build()
            ));
        }
    }
}