package com.pi4j.io;

import com.pi4j.config.Config;

/**
 * Common configuration contract shared by all Pi4J I/O types.
 * <p>
 * In addition to the generic properties from {@link Config}, it identifies the {@link com.pi4j.provider.Provider}
 * and platform that should service the I/O instance described by this configuration.
 */
public interface IOConfig extends Config {
    /** Configuration property key identifying the target platform. */
    String PLATFORM_KEY = "platform";
    /** Configuration property key identifying the I/O provider. */
    String PROVIDER_KEY = "provider";

    /**
     * Returns the id of the provider that should create the I/O instance for this configuration.
     *
     * @return the configured provider id, or {@code null} if none was specified
     */
    String provider();

    /**
     * Alias for {@link #provider()} following the JavaBeans getter naming convention.
     *
     * @return the configured provider id, or {@code null} if none was specified
     */
    default String getProvider() {
        return provider();
    }

    /**
     * Returns the id of the platform this configuration targets.
     *
     * @return the configured platform id, or {@code null} if none was specified
     */
    String platform();

    /**
     * Alias for {@link #platform()} following the JavaBeans getter naming convention.
     *
     * @return the configured platform id, or {@code null} if none was specified
     */
    default String getPlatform() {
        return platform();
    }
}
