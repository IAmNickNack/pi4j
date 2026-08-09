package com.pi4j.plugin.ffm.providers.parallel;

import com.pi4j.exception.InitializeException;
import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfig;
import com.pi4j.io.gpio.parallel.ParallelPortProvider;
import com.pi4j.plugin.ffm.common.gpio.PinFlag;
import com.pi4j.plugin.ffm.common.gpio.enums.LineAttributeId;
import com.pi4j.plugin.ffm.common.gpio.structs.LineAttribute;
import com.pi4j.plugin.ffm.common.gpio.structs.LineConfig;
import com.pi4j.plugin.ffm.common.gpio.structs.LineConfigAttribute;
import com.pi4j.provider.ProviderBase;

public class FFMParallelPortProvider
    extends ProviderBase<ParallelPortProvider, ParallelPort, ParallelPortConfig>
    implements ParallelPortProvider {


    public FFMParallelPortProvider() {
        super("ffm-gpio-port", "FFM API GPIO Port");
    }

    @Override
    public ParallelPort create(ParallelPortConfig config) {
        return null;
    }

}
