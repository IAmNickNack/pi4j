package com.pi4j.plugin.ffm.providers.gpio;

import java.util.Iterator;

/**
 * Utility functions to convert between long value bit masks and offsets representing set bits
 * <p>
 * TODO: This is likely to become relevant at a more generic level, as devices with multiple channels are built out.
 *  This utility should be considered likely to be made public and relocated.
 */
class MaskUtils {

    private MaskUtils() {}

    /**
     * Return the offsets of the set bits in the given mask
     * @param mask the mask value
     * @return an array of offsets
     */
    public static int[] offsets(long mask) {
        var offsets = new int[Long.bitCount(mask)];
        int index = 0;
        for (var offset : MaskIterator.of(mask)) {
            offsets[index++] = offset;
        }
        return offsets;
    }

    /**
     * Return a mask with the bits at the given offsets set
     * @param offsets the offsets of the bits to set
     * @return a mask with the bits at the given offsets set
     */
    public static long mask(int... offsets) {
        long mask = 0;
        for (var offset : offsets) {
            mask |= 1L << offset;
        }
        return mask;
    }

    /**
     * Iterator over the offsets of a mask
     */
    private static class MaskIterator implements Iterator<Integer> {

        private long remaining;

        private MaskIterator(long mask) {
            this.remaining = mask;
        }

        @Override
        public boolean hasNext() {
            return remaining != 0;
        }

        @Override
        public Integer next() {
            int index = Long.numberOfTrailingZeros(remaining);
            remaining &= ~(1L << index);
            return index;
        }

        private static Iterable<Integer> of(long mask) {
            return () -> new MaskIterator(mask);
        }
    }
}
