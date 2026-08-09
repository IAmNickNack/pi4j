package com.pi4j.io.gpio.parallel;

import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputBase;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalInputProviderBase;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A provider for creating digital input instances from this parallel port.
 * <p>
 * The bcm numbering of digital devices created by this provider corresponds to the bits in the parallel port's value.
 * E.g. If the {@link ParallelPort} is mapped to pins 10 and 12, the logical bcm of for the {@link DigitalInput}s
 * creatable via this instance would be `0` and `1` respectively.
 * <p>
 * All {@link DigitalInput}s created by this provider share the same {@link ParallelPort} instance and therefore
 * also the same event source.
 */
public class ParallelPortDigitalInputProvider extends DigitalInputProviderBase {

    private final ParallelPort port;

    public ParallelPortDigitalInputProvider(ParallelPort port) {
        if (port.config().initialDirection() != ParallelPort.Direction.INPUT) {
            throw new IllegalArgumentException("Parallel port must be configured as input");
        }
        this.port = port;
    }

    @Override
    public DigitalInput create(DigitalInputConfig config) {
        return new DigitalInputBase(this, config) {
            private final Map<DigitalStateChangeListener, ListenerReference> listeners = new HashMap<>();

            @Override
            public DigitalState state() {
                var intValue = ((port.read() & config.bcm().mask()) != 0) ? 1 : 0;
                return DigitalState.state(intValue);
            }

            /**
             * Hypothetical implementation of adding listeners to the digital input.
             */
            @Override
            public DigitalInput addListener(DigitalStateChangeListener... listener) {
                ParallelPort.Listener parallelPortListener = event -> {
                    for (var l : listener) {
                        if ((event.value() & config.bcm().mask()) != 0) {
                            l.onDigitalStateChange(new DigitalStateChangeEvent<>(this, state()));
                        }
                    }
                };

                port.addListener(parallelPortListener);

                for (var l : listener) {
                    listeners.put(l, new ListenerReference(l, parallelPortListener));
                }

                return super.addListener(listener);
            }

            /**
             * Hypothetical implementation of removing listeners from the digital input.
             */
            @Override
            public DigitalInput removeListener(DigitalStateChangeListener... listener) {
                for (var l : listener) {
                    var listenerReference = listeners.remove(l);
                    if (listenerReference != null) {
                        port.removeListener(listenerReference.parallelPortListener());
                    }
                }
                return super.removeListener(listener);
            }
        };
    }

    private record ListenerReference(
        DigitalStateChangeListener listener,
        ParallelPort.Listener parallelPortListener
    ) {}
}
