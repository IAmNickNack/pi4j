package com.pi4j.plugin.ffm.providers.parallel;

import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfig;
import com.pi4j.io.gpio.parallel.ParallelPortProvider;
import com.pi4j.provider.ProviderBase;

/**
 * FFM GPIO Parallel Port Provider.
 */
public class FFMParallelPortProvider
    extends ProviderBase<ParallelPortProvider, ParallelPort, ParallelPortConfig>
    implements ParallelPortProvider {


    public FFMParallelPortProvider() {
        super("ffm-gpio-port", "FFM API GPIO Port");
    }

    @Override
    public ParallelPort create(ParallelPortConfig config) {
        return new FFMParallelPort(this, config);
    }
}
