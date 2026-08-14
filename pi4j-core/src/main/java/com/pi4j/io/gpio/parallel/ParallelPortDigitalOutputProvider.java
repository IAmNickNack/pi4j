package com.pi4j.io.gpio.parallel;

import com.pi4j.io.Bcm;
import com.pi4j.io.exception.IOAlreadyExistsException;
import com.pi4j.io.exception.IOBoundsException;
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
    private final AtomicReference<Integer> portValue;

    /**
     * The BCM offsets that are currently in use by this provider.
     */
    private Bcm inUseOffsets = Bcm.fromMask(0);

    public ParallelPortDigitalOutputProvider(ParallelPort port) {
        this.port = port;
        this.portValue = new AtomicReference<>(port.read());
    }

    @Override
    public DigitalOutput create(DigitalOutputConfig config) {
        if ((config.bcm().mask() & port.config().bcm().packed()) == 0) {
            throw new IOBoundsException(config.bcm().intMask(), 0, (int) port.config().bcm().packed());
        }

        if ((inUseOffsets.mask() & config.bcm().mask()) != 0) {
            throw new IOAlreadyExistsException(config.bcm().intMask());
        }

        inUseOffsets = inUseOffsets.or(config.bcm());

        return new DigitalOutputBase(this, config) {
            @Override
            public DigitalState state() {
                var intValue = ((port.read() & config.bcm().mask()) != 0) ? 1 : 0;
                return DigitalState.state(intValue);
            }

            @Override
            public DigitalOutput state(DigitalState state) throws IOException {
                if (port.getDirection() != ParallelPort.Direction.OUTPUT) {
                    throw new IOException("Port direction is not set to OUTPUT");
                }
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
