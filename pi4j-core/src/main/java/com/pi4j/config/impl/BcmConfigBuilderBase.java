package com.pi4j.config.impl;

import com.pi4j.config.BcmConfig;
import com.pi4j.config.BcmConfigBuilder;
import com.pi4j.config.Config;
import com.pi4j.config.ConfigBuilder;
import com.pi4j.io.Bcm;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>Abstract AddressConfigBuilderBase class.</p>
 *
 * @param <BUILDER_TYPE>
 * @param <CONFIG_TYPE>
 */
public abstract class BcmConfigBuilderBase<
    BUILDER_TYPE extends ConfigBuilder<BUILDER_TYPE, CONFIG_TYPE>,
    CONFIG_TYPE extends Config
    >
    extends ConfigBuilderBase<BUILDER_TYPE, CONFIG_TYPE>
    implements BcmConfigBuilder<BUILDER_TYPE, CONFIG_TYPE> {

    /**
     * PRIVATE CONSTRUCTOR
     */
    protected BcmConfigBuilderBase() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public BUILDER_TYPE bcm(Bcm bcm) {
        var propertyValue = Arrays.stream(bcm.offsets())
            .mapToObj(String::valueOf)
            .collect(Collectors.joining(","));
        this.properties.put(BcmConfig.BCM_KEY, propertyValue);
        return (BUILDER_TYPE) this;
    }
}
