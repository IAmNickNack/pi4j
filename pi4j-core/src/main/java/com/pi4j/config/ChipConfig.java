package com.pi4j.config;

/**
 * Configuration contract for I/O instances that are addressed by a chip number, for example the
 * GPIO chip exposed by the Linux GPIO character-device interface. The {@code chip} value selects
 * which controller chip the I/O belongs to.
 */
public interface ChipConfig {
    /**
     * Property key under which the chip number is stored in the configuration properties map.
     */
    String CHIP_KEY = "chip";

    /**
     * Returns the chip number this I/O is assigned to.
     *
     * @return the chip number, or {@code null} if not configured
     */
    Integer chip();

    /**
     * Returns the chip number this I/O is assigned to.
     *
     * @return the chip number, or {@code null} if not configured
     */
    default Integer getChip() {
        return this.chip();
    }
}
