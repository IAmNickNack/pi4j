package com.pi4j.io.gpio.parallel;

import com.pi4j.io.exception.IOException;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputBase;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalOutputProviderBase;
import com.pi4j.io.gpio.digital.DigitalState;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A provider for creating digital output instances from this parallel port.
 * <p>
 * The bcm numbering of digital devices created by this provider corresponds to the bits in the parallel port's value.
 * E.g. If the {@link ParallelPort} is mapped to pins 10 and 12, the logical bcm of for the {@link DigitalOutput}s
 * creatable via this instance would be `0` and `1` respectively.
 */
public class ParallelPortDigitalOutputProvider extends DigitalOutputProviderBase {

    private final ParallelPort port;

    /**
     * An in-memory representation of the parallel port's value.
     * <p>
     * This could possibly be swapped for an explicit read of the port's value before setting the port's value.
     */
    private final AtomicReference<Integer> portValue = new AtomicReference<>(0);

    public ParallelPortDigitalOutputProvider(ParallelPort port) {
        if (port.config().initialDirection() != ParallelPort.Direction.OUTPUT) {
            throw new IllegalArgumentException("Port direction must be OUTPUT");
        }
        this.port = port;
    }

    @Override
    public DigitalOutput create(DigitalOutputConfig config) {
        return new DigitalOutputBase(this, config) {
            @Override
            public DigitalState state() {
                var intValue = ((port.read() & config.bcm().mask()) != 0) ? 1 : 0;
                return DigitalState.state(intValue);
            }

            @Override
            public DigitalOutput state(DigitalState state) throws IOException {
                var intValue = state.isHigh() ? 1 : 0;
                if (intValue != 0) {
                    portValue.getAndUpdate(current -> current | (intValue << config.bcm().offsets()[0]));
                } else {
                    portValue.getAndUpdate(current -> current & ~(1 << config.bcm().offsets()[0]));
                }
                port.write(portValue.get());
                return this;
            }
        };
    }
}
