package com.pi4j.io.impl;

import com.pi4j.config.DeviceConfig;
import com.pi4j.config.impl.DeviceConfigBase;
import com.pi4j.io.IOConfig;

import java.util.Map;

/**
 */
public class IODeviceConfigBase
    extends DeviceConfigBase
    implements IOConfig, DeviceConfig {

    // private configuration variables
    protected String provider = null;

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IODeviceConfigBase() {
    }

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IODeviceConfigBase(Map<String, String> properties) {
        super(properties);

        // load provider property
        if (properties.containsKey(PROVIDER_KEY)) {
            this.provider = properties.get(PROVIDER_KEY);
        }
    }

    @Override
    public String provider() {
        return this.provider;
    }
}
