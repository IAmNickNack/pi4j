package com.pi4j.io.gpio.parallel;

import com.pi4j.io.Bcm;
import com.pi4j.io.gpio.GpioConfig;
import com.pi4j.io.gpio.digital.PullResistance;

import java.util.Map;

/**
 * Configuration for a parallel port
 * @param id the user-provided ID
 * @param name the user-provided name
 * @param description the user-provided description
 * @param bus the bus number / GPIO chip
 * @param bcm the BCM details / pin numbers
 * @param onValue the value to set when the pin is set to HIGH
 * @param pull the pull resistance setting
 * @param debounce the debounce time in microseconds
 * @param initialValue the initial value of the port
 * @param shutdownValue the value applied to the port on shutdown
 * @param initialDirection the initial direction of the port
 */
public record ParallelPortConfig(
    String id,
    String name,
    String description,
    // hardware
    @Override Integer bus,
    @Override Bcm bcm,
    Integer onValue,
    // input
    @Override PullResistance pull,
    @Override Long debounce,
    // output
    Integer initialValue,
    Integer shutdownValue,
    // port-specific
    ParallelPort.Direction initialDirection
) implements GpioConfig {

    @Override
    public int getUniqueIdentifier() {
        return bcm.intMask();
    }

    @Override
    public String provider() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public Map<String, String> properties() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void validate() {
        // this current core codebase doesn't seem to do anything here except check that the ID is non-null
        // `@NonNull` annotations could be used to indicate this requirement, so leaving faults up to the user(?)
        if (id == null) {
            throw new IllegalStateException("ID cannot be null");
        }
    }
}
