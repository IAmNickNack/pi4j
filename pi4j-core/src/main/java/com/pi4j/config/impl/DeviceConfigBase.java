package com.pi4j.config.impl;

import com.pi4j.config.ConfigBase;
import com.pi4j.config.DeviceConfig;
import com.pi4j.config.exception.ConfigMissingRequiredKeyException;

import java.util.Map;

/**
 * <p>Abstract DeviceConfigBase class.</p>
 */
public abstract class DeviceConfigBase
    extends ConfigBase
    implements DeviceConfig {

    // private configuration variables
    protected Integer device = null;

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected DeviceConfigBase() {
    }

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected DeviceConfigBase(Map<String, String> properties) {
        super(properties);

        // load address property
        if (properties.containsKey(DEVICE_KEY)) {
            this.device = Integer.parseInt(properties.get(DEVICE_KEY));
        } else {
            throw new ConfigMissingRequiredKeyException(DEVICE_KEY);
        }
    }

    public Integer device() {
        return this.device;
    }
}
