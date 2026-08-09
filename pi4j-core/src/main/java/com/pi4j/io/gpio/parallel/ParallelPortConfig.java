package com.pi4j.io.gpio.parallel;

import com.pi4j.io.Bcm;
import com.pi4j.io.IOConfig;
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
    @Override String id,
    @Override String name,
    @Override String description,
    Integer bus,
    Bcm bcm,
    Integer onValue,
    // input
    PullResistance pull,
    Long debounce,
    // output
    Integer initialValue,
    Integer shutdownValue,
    // port
    ParallelPort.Direction initialDirection
) implements IOConfig {

    @Override
    public int getUniqueIdentifier() {
        return bcm.intMask();
    }

    @Override
    public String provider() {
        // TODO: question if this is needed. I think this is only required for name-based component scan
        //  (which is likely redundant)
        return ParallelPortProvider.class.getName();
    }

    @Override
    public Map<String, String> properties() {
        // TODO: question if this is needed. It seems unnecessary if configs don't read/write properties
        // For now, just fail if this is called
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void validate() {
        // The current core codebase doesn't seem to do anything here except check that the ID is non-null
        // Either (or both) more idiomatic solutions could be applied:
        // - @NonNull` annotations could be used to indicate this requirement, so leaving faults up to the user(?)
        // - Record initialisation could validate this after construction.
        // TODO: Check if validation is relevant
        if (id == null) {
            throw new IllegalStateException("ID cannot be null");
        }
    }
}
