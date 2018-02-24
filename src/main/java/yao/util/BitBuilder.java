package yao.util;

import java.util.Arrays;
import java.util.BitSet;

public class BitBuilder {

    private final BitSet bitSet = new BitSet();
    private int bitPos = 0;

    public BitBuilder appendBits(int bits, int val) {
        for (int i = bits - 1; i >= 0; i--) {
            appendBit((val >> i) & 0x01);
        }
        return this;
    }

    public BitBuilder appendBits(int bits, long val) {
        for (int i = bits - 1; i >= 0; i--) {
            appendBit((int) (val >> i) & 0x01);
        }
        return this;
    }

    public BitBuilder appendBit(int val) {
        bitSet.set(bitPos++, (val & 0x01) != 0);
        return this;
    }

    public BitBuilder appendBit(boolean val) {
        return appendBit(val ? 1 : 0);
    }

    public void clear() {
        bitSet.clear();
    }

    public int size() {
        return bitPos;
    }

    public byte[] toBytes() {
        int bytes = (bitPos + 7) / 8;
        byte[] buf = new byte[bytes];
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i)) {
                final int bytePos = i / 8;
                final int bitPos = i % 8;
                buf[bytePos] |= (1 << (7 - bitPos));
            }
        }
        return buf;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bitPos; i++) {
            if (i > 0 && i % 8 == 0) {
                buf.append(' ');
            }
            buf.append(bitSet.get(i) ? 1 : 0);
        }
        if (bitPos % 8 != 0) {
            final int remainBits = 8 - (bitPos % 8);
            for (int i = 0; i < remainBits; i++) {
                buf.append('_');
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        BitBuilder bb = new BitBuilder();
        bb.appendBits(64, 0b10101010_11111110_00000000_11111111_10101010_11111110_00000000_11111111L);
        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));
//        bb.appendBits(32, 0b10101010_11111110_00000000_11111111);
//        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));

//        bb.appendBits(3, 1);
//        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));
//        bb.appendBits(6, 15);
//        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));
//        bb.appendBits(7, 15);
//        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));
//        bb.appendBits(7, 15);
//        System.out.println("" + bb + ", " + Arrays.toString(bb.toBytes()));
    }

}
