package com.pi4j.plugin.ffm.providers.gpio;

import java.util.List;

/**
 * Utility record which can be used to represent both the mask for a set of line offsets and the offsets for
 * a mask (whichever is already known)
 * @param offsets The offsets of the GPIO lines.
 * @param mask The mask representing the GPIO lines.
 */
public record FFMGpioLineMask(
    int[] offsets,
    long mask
) {
    /**
     * Constructs a FFMGpioLineMask from a single offset.
     * @param offset The offset of the GPIO line.
     */
    public FFMGpioLineMask(int offset) {
        this(new int[]{offset}, 1L << offset);
    }

    /**
     * Constructs a FFMGpioLineMask from an array of offsets.
     * @param offsets The offsets of the GPIO lines.
     */
    public FFMGpioLineMask(int[] offsets) {
        long mask = 0;
        for (int offset : offsets) {
            mask |= 1L << offset;
        }
        this(offsets, mask);
    }

    /**
     * Constructs a FFMGpioLineMask from a list of offsets.
     * @param offsets The offsets of the GPIO lines.
     */
    public FFMGpioLineMask(List<Integer> offsets) {
        this(offsets.stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Constructs a FFMGpioLineMask from a mask.
     * @param mask The mask representing the GPIO lines.
     */
    public FFMGpioLineMask(long mask) {
        int[] offsets = new int[Long.bitCount(mask)];
        int index = 0;
        for (int i = 0; i < 64; i++) {
            if ((mask & (1L << i)) != 0) {
                offsets[index++] = i;
            }
        }
        this(offsets, mask);
    }

    /**
     * Equality check which avoids comparing the offsets array.
     * @param obj the reference object with which to compare.
     * @return true if the masks are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FFMGpioLineMask that = (FFMGpioLineMask) obj;
        return mask == that.mask;
    }

    @Override
    public String toString() {
        return "FFMGpioLineMask{" +
                "offsets=" + java.util.Arrays.toString(offsets) +
                ", mask=" + mask +
                '}';
    }
}
