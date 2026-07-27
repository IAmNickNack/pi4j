package com.pi4j.io.impl;

import com.pi4j.config.PortConfig;
import com.pi4j.config.impl.PortConfigBase;
import com.pi4j.io.IOConfig;

import java.util.Map;

/**
 */
public class IOPortConfigBase
    extends PortConfigBase
    implements PortConfig, IOConfig {

    // private configuration variables
    protected String provider = null;
    protected String platform = null;

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IOPortConfigBase() {
    }

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IOPortConfigBase(Map<String, String> properties) {
        super(properties);

        // load provider property
        if (properties.containsKey(PROVIDER_KEY)) {
            this.provider = properties.get(PROVIDER_KEY);
        }

        // load platform property
        if (properties.containsKey(PLATFORM_KEY)) {
            this.platform = properties.get(PLATFORM_KEY);
        }
    }

    @Override
    public String platform() {
        return this.platform;
    }

    @Override
    public String provider() {
        return this.provider;
    }
}
