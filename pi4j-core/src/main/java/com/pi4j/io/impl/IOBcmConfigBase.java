package com.pi4j.io.impl;

import com.pi4j.config.BcmConfig;
import com.pi4j.config.impl.BcmConfigBase;
import com.pi4j.io.IOConfig;

import java.util.Map;

/**
 */
public class IOBcmConfigBase
    extends BcmConfigBase
    implements IOConfig, BcmConfig {

    // private configuration variables
    protected String provider = null;

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IOBcmConfigBase() {
    }

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected IOBcmConfigBase(Map<String, String> properties) {
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
