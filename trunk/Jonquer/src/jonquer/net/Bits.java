/*
 * @(#)Bits.java 1.00 03/11/09
 *
 * Copyright Jonquer 2009. All rights reserved.
 */

package jonquer.net;

/**
 * A class used to encode and decode bits into a byte array according
 * to the little endian byte order.
 *
 * @author s.bat
 * @version 1.00, 03/11/09
 */
public final class Bits {

    /** Don't let anyone instantiate this class. */
    private Bits() {}


    // -- get/put short --

    /**
     * Reads two bytes from the given byte array at the given index,
     * composing them into a short value according to the
     * little endian byte order.
     *
     * @param b the byte array to read from.
     * @param bi the index at which the bytes will be read.
     *
     * @return the short value at the given index.
     */
    public static short getShort(byte[] b, int bi) {
        return (short) (((b[bi + 1] & 0xff) <<  8) |
                        ((b[bi + 0] & 0xff) <<  0));
    }

    /**
     * Writes two bytes containing the given short value,
     * in the little endian byte order, into the given byte array
     * at the given index.
     *
     * @param b the byte array to write to.
     * @param bi the index at which the bytes will be written.
     * @param x the short value to be written.
     */
    public static void putShort(byte[] b, int bi, int x) {
        b[bi + 1] = (byte) (x >>  8);
        b[bi + 0] = (byte) (x >>  0);
    }


    // -- get/put int --

    /**
     * Reads four bytes from the given byte array at the given index,
     * composing them into an int value according to the
     * little endian byte order.
     *
     * @param b the byte array to read from.
     * @param bi the index at which the bytes will be read.
     *
     * @return the int value at the given index.
     */
    public static int getInt(byte[] b, int bi) {
        return (int) (((b[bi + 3] & 0xff) << 24) |
                      ((b[bi + 2] & 0xff) << 16) |
                      ((b[bi + 1] & 0xff) <<  8) |
                      ((b[bi + 0] & 0xff) <<  0));
    }

    /**
     * Writes four bytes containing the given int value,
     * in the little endian byte order, into the given byte array
     * at the given index.
     *
     * @param b the byte array to write to.
     * @param bi the index at which the bytes will be written.
     * @param x the int value to be written.
     */
    public static void putInt(byte[] b, int bi, int x) {
        b[bi + 3] = (byte) (x >> 24);
        b[bi + 2] = (byte) (x >> 16);
        b[bi + 1] = (byte) (x >>  8);
        b[bi + 0] = (byte) (x >>  0);
    }
}
