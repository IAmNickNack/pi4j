package com.pi4j.io.gpio;

import com.pi4j.config.BcmConfig;
import com.pi4j.config.BusConfig;
import com.pi4j.io.IOConfig;

/**
 * Configuration contract for {@link Gpio} I/O instances. It combines the {@link IOConfig} identity and
 * lifecycle properties with the GPIO addressing model of {@link BcmConfig} (Broadcom pin number) and the
 * bus addressing of {@link BusConfig}, so a single configuration can fully describe a GPIO pin to its provider.
 */
public interface GpioConfig extends BusConfig, BcmConfig, IOConfig {

    /**
     * Returns the unique identifier used to distinguish GPIO devices, derived from the configured
     * BCM pin number so that each physical GPIO pin maps to a distinct identifier.
     *
     * @return the BCM GPIO pin number serving as this device's unique identifier
     */
    @Override
    default int getUniqueIdentifier() {
        return bcm();
    }
}
