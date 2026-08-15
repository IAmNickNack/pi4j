package com.pi4j.plugin.mock.provider.gpio.parallel;

import com.pi4j.io.IOBase;
import com.pi4j.io.gpio.MaskUtils;
import com.pi4j.io.gpio.parallel.ParallelPort;
import com.pi4j.io.gpio.parallel.ParallelPortConfig;
import com.pi4j.io.gpio.parallel.ParallelPortProvider;

public class MockParallelPort
    extends IOBase<ParallelPort, ParallelPortConfig, ParallelPortProvider>
    implements ParallelPort {

    private int value;

    private Direction direction;

    /**
     * Creates a new GPIO I/O instance bound to the given provider and configuration.
     *
     * @param provider the {@link ParallelPortProvider} that creates and backs this I/O instance
     * @param config   the {@link ParallelPortConfig} describing this I/O, including its BCM pin numbers
     */
    public MockParallelPort(ParallelPortProvider provider, ParallelPortConfig config) {
        super(provider, config);
        this.direction = config.initialDirection();
        this.value = config.initialValue();
    }

    @Override
    public void write(int value) {
        if (this.direction == Direction.OUTPUT) {
            this.value = value & (int) MaskUtils.packed(config.mask());
        }
    }

    @Override
    public int read() {
        return this.value;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public ParallelPort addListener(Listener listener) {
        return this;
    }

    @Override
    public ParallelPort removeListener(Listener listener) {
        return this;
    }
}
