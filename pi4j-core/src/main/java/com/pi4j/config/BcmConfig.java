package com.pi4j.config;

import com.pi4j.io.Bcm;

/**
 * Configuration contract for I/O instances that are addressed by a Broadcom (BCM) GPIO pin number,
 * such as digital inputs/outputs and PWM pins. The {@code bcm} value identifies the physical SoC
 * GPIO pin the I/O is bound to.
 */
public interface BcmConfig {
    /**
     * Property key under which the legacy "address" value is stored in the configuration properties map.
     *
     * @deprecated use {@link BcmConfig#BCM_KEY} instead.
     * <p>
     * Since "address" has lead to many confusions while configuring IOs,
     * this value is deprecated and will be removed in a future release.
     * Use the correct config related to the IO type.
     */
    @Deprecated(forRemoval = true)
    String ADDRESS_KEY = "address";

    /**
     * Returns the configured pin number under its legacy "address" name.
     *
     * @return the BCM GPIO pin number, or {@code null} if not configured
     * @deprecated use {@link #bcm()} instead.
     * <p>
     * Since "address" has lead to many confusions while configuring IOs,
     * this value is deprecated and will be removed in a future release.
     * Use the correct config related to the IO type.
     */
    @Deprecated(forRemoval = true)
    default Integer address() {
        return this.bcm().intMask();
    }

    /**
     * Returns the configured pin number under its legacy "address" name.
     *
     * @return the BCM GPIO pin number, or {@code null} if not configured
     * @deprecated use {@link #getBcm()} instead.
     * <p>
     * Since "address" has lead to many confusions while configuring IOs,
     * this value is deprecated and will be removed in a future release.
     * Use the correct config related to the IO type.
     */
    @Deprecated(forRemoval = true)
    default Integer getAddress() {
        return this.address();
    }

    /**
     * Property key under which the BCM pin number is stored in the configuration properties map.
     */
    String BCM_KEY = "bcm";

    /**
     * Returns the Broadcom (BCM) GPIO pin number this I/O is bound to.
     *
     * @return the BCM GPIO pin number, or {@code null} if not configured
     */
    Bcm bcm();

    /**
     * Returns the Broadcom (BCM) GPIO pin number this I/O is bound to.
     *
     * @return the BCM GPIO pin number, or {@code null} if not configured
     */
    default Bcm getBcm() {
        return this.bcm();
    }
}
