package com.pi4j.config.impl;

import com.pi4j.config.BcmConfig;
import com.pi4j.config.ConfigBase;
import com.pi4j.config.exception.ConfigMissingRequiredKeyException;
import com.pi4j.io.Bcm;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>Abstract AddressConfigBase class.</p>
 */
public abstract class BcmConfigBase
    extends ConfigBase
    implements BcmConfig {

    // private configuration properties
    protected Bcm bcm = null;

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected BcmConfigBase() {
        super();
    }

    protected BcmConfigBase(Integer bcm) {
        this.bcm = Bcm.fromOffset(bcm);
    }

    protected BcmConfigBase(Bcm bcm) {
        super();
        this.bcm = bcm;
    }

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected BcmConfigBase(Map<String, String> properties) {
        super(properties);

        // load address property
        if (properties.containsKey(BCM_KEY)) {
            var offsets = Arrays.stream(properties.get(BCM_KEY).split(","))
                                .map(String::trim)
                                .mapToInt(Integer::parseInt)
                                .toArray();
            this.bcm = Bcm.fromOffsets(offsets);
        } else {
            throw new ConfigMissingRequiredKeyException(BCM_KEY);
        }
    }

    @Override
    public Integer address() {
        if (bcm == null) {
            throw new IllegalStateException("Bcm has not been set");
        }

        if (bcm.offsets().length != 1) {
            throw new IllegalStateException("Bcm must have a single offset");
        }
        return bcm.offsets()[0];
    }


    public Bcm bcm() {
        return this.bcm;
    }
}
