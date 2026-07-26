package com.pi4j.config;

/**
 * Configuration contract for I/O that is addressed by a named port.
 * Extends the base {@link Config} contract with a single
 * {@code port} property identified by {@link #PORT_KEY}.
 */
public interface PortConfig {

    /**
     * Property key under which the port value is stored in the configuration.
     */
    String PORT_KEY = "port";

    /**
     * Returns the configured port.
     *
     * @return the port identifier, or {@code null} if no port was configured
     */
    String port();

    /**
     * Returns the configured port; an alias for {@link #port()} following the JavaBeans getter naming convention.
     *
     * @return the port identifier, or {@code null} if no port was configured
     */
    default String getPort() {
        return this.port();
    }
}
