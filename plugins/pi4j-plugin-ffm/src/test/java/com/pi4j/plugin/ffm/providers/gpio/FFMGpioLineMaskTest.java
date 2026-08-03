package com.pi4j.plugin.ffm.providers.gpio;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FFMGpioLineMaskTest {

    private final Long[] masks = IntStream.range(0, 64).mapToLong(i -> 1L << i).boxed().toArray(Long[]::new);

    @Test
    void sanityCheck() {
        var offsets = new int[] { 1 };
        var mask = new FFMGpioLineMask(offsets);
        assertEquals(masks[1], mask.mask());
        assertEquals(2, mask.mask());
    }

    @Test
    void masksSingleBit() {
        for (int i = 0; i < masks.length; i++) {
            var offsets = new int[] { i };
            var maskFromArray = new FFMGpioLineMask(offsets);
            var maskFromInt = new FFMGpioLineMask(i);
            assertEquals(masks[i], maskFromArray.mask());
            assertEquals(masks[i], maskFromInt.mask());
            assertEquals(maskFromArray, maskFromInt);
        }
    }

    @Test
    void masksMultipleBits() {
        for (int i = 2; i < masks.length; i++) {
            var offsets = new int[] { i - 2, i };
            var mask = new FFMGpioLineMask(offsets);
            assertEquals(masks[i] | masks[i - 2], mask.mask());
        }
    }

    @Test
    void canConstructFromMask() {
        var maskLong = 0b1010101010101010101010101010101010101010101010101010101010101010L;
        var mask = new FFMGpioLineMask(maskLong);
        var maskFromOffsets = new FFMGpioLineMask(mask.offsets());
        assertEquals(maskLong, maskFromOffsets.mask());
    }
}