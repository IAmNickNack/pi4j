package com.pi4j.io;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BcmTest {

    private final Long[] masks = IntStream.range(0, 64).mapToLong(i -> 1L << i).boxed().toArray(Long[]::new);

    @Test
    void sanityCheck() {
        var offsets = new int[] { 1 };
        var mask = Bcm.fromOffsets(offsets);
        assertEquals(masks[1], mask.mask());
        assertEquals(2, mask.mask());
    }

    @Test
    void masksSingleBit() {
        for (int i = 0; i < masks.length; i++) {
            var offsets = new int[] { i };
            var maskFromArray = Bcm.fromOffsets(offsets);
            var maskFromInt = Bcm.fromOffset(i);
            assertEquals(masks[i], maskFromArray.mask());
            assertEquals(masks[i], maskFromInt.mask());
            assertEquals(maskFromArray, maskFromInt);
        }
    }

    @Test
    void masksMultipleBits() {
        for (int i = 2; i < masks.length; i++) {
            var offsets = new int[] { i - 2, i };
            var mask = Bcm.fromOffsets(offsets);
            assertEquals(masks[i] | masks[i - 2], mask.mask());
        }
    }

    @Test
    void canConstructFromMask() {
        var maskLong = 0b1010101010101010101010101010101010101010101010101010101010101010L;
        var mask = Bcm.fromMask(maskLong);
        var maskFromOffsets = Bcm.fromOffsets(mask.offsets());
        assertEquals(maskLong, maskFromOffsets.mask());
    }

    @Test
    void canIdentifyOverlap() {
        var mask1 = Bcm.fromOffsets(new int[] { 1, 2, 3 });
        var mask2 = Bcm.fromOffsets(new int[] { 3, 4, 5 });
        var mask3 = Bcm.fromOffsets(new int[] { 6, 7, 8 });

        assertTrue(mask1.conflictsWith(mask2));
        assertFalse(mask1.conflictsWith(mask3));
    }
}