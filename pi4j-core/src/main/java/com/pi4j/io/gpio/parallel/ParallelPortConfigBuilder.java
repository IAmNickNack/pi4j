package com.pi4j.io.gpio.parallel;

import com.pi4j.config.Config;
import com.pi4j.io.Bcm;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.impl.IOConfigBuilderBase;

import java.util.ArrayList;
import java.util.List;

public class ParallelPortConfigBuilder extends IOConfigBuilderBase<ParallelPortConfigBuilder, ParallelPortConfig> {

    private Integer bus;
    private final List<Integer> bcmPins = new ArrayList<>();
    private Bcm bcm;
    private Integer onValue;
    private ParallelPort.Direction initialDirection = ParallelPort.Direction.INPUT;
    private Integer initialValue;
    private Integer shutdownValue;
    private PullResistance pull;
    private Long debounce;

    ParallelPortConfigBuilder bus(Integer bus) {
        this.bus = bus;
        return this;
    }

    ParallelPortConfigBuilder bcm(Integer bcm) {
        bcmPins.add(bcm);
        return this;
    }

    ParallelPortConfigBuilder bcm(Bcm bcm) {
        this.bcm = bcm;
        return this;
    }

    ParallelPortConfigBuilder initialDirection(ParallelPort.Direction initialDirection) {
        this.initialDirection = initialDirection;
        return this;
    }

    ParallelPortConfigBuilder onValue(Integer onValue) {
        this.onValue = onValue;
        return this;
    }

    ParallelPortConfigBuilder initialValue(Integer initialValue) {
        this.initialValue = initialValue;
        return this;
    }

    ParallelPortConfigBuilder shutdownValue(Integer shutdownValue) {
        this.shutdownValue = shutdownValue;
        return this;
    }

    ParallelPortConfigBuilder pull(PullResistance pull) {
        this.pull = pull;
        return this;
    }

    ParallelPortConfigBuilder debounce(Long debounce) {
        this.debounce = debounce;
        return this;
    }

    @Override
    public ParallelPortConfig build() {
        if (bcmPins.isEmpty() || bcm == null) {
            throw new IllegalArgumentException("BCM pins must be specified");
        }
        if (initialDirection == null) {
            throw new IllegalArgumentException("Initial direction must be specified");
        }
        return new ParallelPortConfig(
            this.id(),
            this.properties.get(Config.NAME_KEY),
            this.properties.get(Config.DESCRIPTION_KEY),
            this.bus,
            (this.bcm != null) ? this.bcm : Bcm.fromOffsets(bcmPins),
            this.onValue,
            this.pull,
            this.debounce,
            this.initialValue,
            this.shutdownValue,
            this.initialDirection
        );
    }
}
