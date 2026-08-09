package com.pi4j.io;

import java.util.List;

/**
 * Utility record which can be used to represent both the mask for a set of line offsets and the offsets for
 * a mask (whichever is already known).
 * @param offsets The offsets of the GPIO lines.
 * @param mask The mask representing the GPIO lines.
 */
public record Bcm(
    int[] offsets,
    long mask
) {
    /**
     * This could be lossy if the mask contains bits set beyond the range of an int. I.e. pin numbers > 31.
     * This should probably be discouraged and the API not provide this function. It is currently included
     * for backwards compatibility with {@link com.pi4j.config.Config#getUniqueIdentifier()}
     * @return the mask as an int
     */
    public int intMask() {
        return (int) mask;
    }

    /**
     * Constructs a FFMGpioLineMask from a single offset.
     * @param offset The offset of the GPIO line.
     */
    public static Bcm fromOffset(int offset) {
        return new Bcm(new int[]{offset}, 1L << offset);
    }

    /**
     * Constructs a FFMGpioLineMask from an array of offsets.
     * @param offsets The offsets of the GPIO lines.
     */
    public static Bcm fromOffsets(int[] offsets) {
        long mask = 0;
        for (int offset : offsets) {
            mask |= 1L << offset;
        }
        return new Bcm(offsets, mask);
    }

    /**
     * Constructs a FFMGpioLineMask from a list of offsets.
     * @param offsets The offsets of the GPIO lines.
     */
    public static Bcm fromOffsets(List<Integer> offsets) {
        return fromOffsets(offsets.stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Constructs a FFMGpioLineMask from a mask.
     * @param mask The mask representing the GPIO lines.
     */
    public static Bcm fromMask(long mask) {
        int[] offsets = new int[Long.bitCount(mask)];
        int index = 0;
        for (int i = 0; i < 64; i++) {
            if ((mask & (1L << i)) != 0) {
                offsets[index++] = i;
            }
        }
        return new Bcm(offsets, mask);
    }

    /**
     * Operation to return the intersection of two BCM masks.
     * @param other the value to compare against
     * @return non-null if an intersection of the masks exists, null otherwise
     */
    public Bcm and(Bcm other) {
        var result = this.mask & other.mask;
        if (result != 0) {
            return Bcm.fromMask(result);
        } else {
            return null;
        }
    }

    /**
     * Checks if there is a conflict between two BCM masks.
     * @param other the value to compare against
     * @return true if there is a conflict, false otherwise
     */
    public boolean conflictsWith(Bcm other) {
        return this.and(other) != null;
    }

    /**
     * Packs the offsets into a long value. Has the effect of generating a mask with `offsets.length` bits set, where
     * all bits set to 1. E.g. The mask `1101` representing pins 0, 2, and 3 becomes `111`.
     * @return a packed representation of the pin configuration
     */
    public long pack() {
        long out = 0;
        for (int i = 0; i < offsets.length; i++) {
            out |= 1L << i;
        }
        return out;
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
        Bcm that = (Bcm) obj;
        return mask == that.mask;
    }

    /**
     * Returns the hash code of the mask.
     * <p>
     * {@link mask} is already a numeric representation of {@link offsets}. Hashing the array again is unnecessary.
     * @return the hash code of the mask.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(mask);
    }

    @Override
    public String toString() {
        return "FFMGpioLineMask{" +
                "offsets=" + java.util.Arrays.toString(offsets) +
                ", mask=" + mask +
                '}';
    }
}
