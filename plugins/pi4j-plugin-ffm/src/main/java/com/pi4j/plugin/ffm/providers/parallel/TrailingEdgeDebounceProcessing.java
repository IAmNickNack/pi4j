package com.pi4j.plugin.ffm.providers.parallel;

import com.pi4j.io.gpio.MaskUtils;
import com.pi4j.plugin.ffm.common.gpio.PinEventProcessing;
import com.pi4j.plugin.ffm.common.gpio.structs.LineEvent;
import com.pi4j.plugin.ffm.providers.gpio.EventWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

/**
 * Debounces GPIO events by retaining the latest event in each debounce interval and dispatching
 * it when a later event passes the interval or the event watcher reports a polling timeout.
 * <p>
 * This implementation is Copilot's attempt at replicating the original {@link EventWatcher} functionality.
 */
public class TrailingEdgeDebounceProcessing implements PinEventProcessing {

    private final long debounceNs;
    private final long mask;
    private final PinEventProcessing callback;
    private final LongSupplier nanoTime;

    private final LineEvent[] pendingEvent;
    private final long[] lastEventReceivedTimeNs;

    /**
     * @param debounceNs the debounce interval in nanoseconds
     * @param callback the callback to process the debounced events
     */
    public TrailingEdgeDebounceProcessing(long debounceNs, long mask, PinEventProcessing callback) {
        this(debounceNs, mask, callback, System::nanoTime);
    }

    /**
     * Package-private constructor for testing purposes, allowing injection of a custom time source.
     * @param debounceNs the debounce interval in nanoseconds
     * @param callback the callback to process the debounced events
     * @param nanoTime the time source to use for measuring intervals
     */
    TrailingEdgeDebounceProcessing(long debounceNs, long mask, PinEventProcessing callback, LongSupplier nanoTime) {
        this.debounceNs = debounceNs;
        this.mask = mask;
        this.callback = callback;
        this.nanoTime = nanoTime;
        this.pendingEvent = new LineEvent[Long.bitCount(mask)];
        this.lastEventReceivedTimeNs = new long[Long.bitCount(mask)];
    }

    @Override
    public void process(List<LineEvent> events) {
        if (events.isEmpty()) {
            this.flush();
            return;
        }

        List<LineEvent> readyEvents = new ArrayList<>();
        for (LineEvent event : events) {
            int offset = (int) MaskUtils.packValueByMask(event.offset(), mask);
            if (offset < 0 || offset >= pendingEvent.length) {
                continue; // Ignore events outside the mask
            }

            if (pendingEvent[offset] != null && event.timestampNs() - pendingEvent[offset].timestampNs() >= debounceNs) {
                readyEvents.add(pendingEvent[offset]);
            }

            pendingEvent[offset] = event;
            lastEventReceivedTimeNs[offset] = nanoTime.getAsLong();
        }

        if (!readyEvents.isEmpty()) {
            callback.process(readyEvents);
        }
    }

    /**
     * Called when the event watcher's poll times out. Dispatches the pending event if the debounce
     * interval has elapsed since an event was received.
     */
    private void flush() {
        List<LineEvent> readyEvents = new ArrayList<>();
        for (int i = 0; i < pendingEvent.length; i++) {
            if (pendingEvent[i] != null && nanoTime.getAsLong() - lastEventReceivedTimeNs[i] >= debounceNs) {
                LineEvent event = pendingEvent[i];
                pendingEvent[i] = null;
                lastEventReceivedTimeNs[i] = 0;
                readyEvents.add(event);
            }
        }
        if (!readyEvents.isEmpty()) {
            callback.process(readyEvents);
        }
    }
}
