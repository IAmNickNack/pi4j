package com.pi4j.io.gpio.digital;

import com.pi4j.io.gpio.GpioConfigBuilder;

/**
 * Fluent builder contract for assembling a {@link DigitalConfig}, extending the generic
 * {@link GpioConfigBuilder} with the digital-specific BCM pin and on-state settings.
 *
 * @param <BUILDER_TYPE> the concrete builder type, used as the self-referencing return type for chaining
 * @param <CONFIG_TYPE> the {@link DigitalConfig} type produced by this builder
 */
public interface DigitalConfigBuilder<BUILDER_TYPE extends DigitalConfigBuilder<BUILDER_TYPE, CONFIG_TYPE>, CONFIG_TYPE extends DigitalConfig>
    extends GpioConfigBuilder<BUILDER_TYPE, CONFIG_TYPE> {

    /**
     * Sets the {@link DigitalState} to be treated as the logical "on" state for the configuration being built.
     *
     * @param state the state that should map to "on"
     * @return this builder for method chaining
     */
    BUILDER_TYPE onState(DigitalState state);
}
