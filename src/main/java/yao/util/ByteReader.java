/*
 * Tcps Protocol808_2015 Server
 * created by wanghg at 2015.04.08
 *
 */
package yao.util;

import java.util.Arrays;

/**
 * 如果数据已经读完，则获取到默认字节值0
 *
 * @author wanghg
 */
public class ByteReader {

    final byte[] buf;
    int pos = 0;
    int bits = 0;//

    /**
     *
     * @param buf
     * @param from
     * @param count
     * @return
     */
    public static ByteReader of(byte[] buf, int from, int count) {
        buf = Arrays.copyOfRange(buf, from, from + count);
        return new ByteReader(buf);
    }

    /**
     *
     * @param buf
     */
    public ByteReader(byte[] buf) {
        this.buf = buf;
//        System.out.println("" + Arrays.toString(buf));
    }

    /**
     *
     * @return
     */
    public int readUnsignedByte() {
        return readByte() & 0xFF;
    }

    /**
     * (大端字节顺序)
     *
     * @param bytes
     * @return
     */
    public int readNumber(int bytes) {
        int ret = 0;
        for (int i = 0; i < bytes; i++) {
            ret <<= 8;
            ret |= readUnsignedByte();
        }
        return ret;
    }

    public long readNumberLong(int bytes) {
        long ret = 0;
        for (int i = 0; i < bytes; i++) {
            ret <<= 8;
            ret |= readUnsignedByte();
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public byte readByte() {
        int now = this.pos;
        byte ret = now >= buf.length ? 0 : buf[now];
        pos++;
        return ret;
    }

    public byte[] readBytes(int bodyLen) {
        if (bodyLen == 0) {//20170327 yy add
            return null;
        }
        byte[] ret = new byte[bodyLen];
        for (int i = 0; i < bodyLen; i++) {
            ret[i] = readByte();
        }
        return ret;
    }

    public byte[] readRemainBytes() {
        if (pos < buf.length) {
            return Arrays.copyOfRange(buf, pos, buf.length);
        } else {
            return new byte[0];
        }
    }

    /**
     *
     * @param bytes 负值：绝对值是头的长度；正值：定长数据块的字节数。0：剩余
     * @return
     */
    public ByteReader readByteReader(int bytes) {
        int blockBytes;
        if (bytes < 0) {
            blockBytes = this.readNumber(Math.abs(bytes));
        } else if (bytes == 0) {
            blockBytes = this.remainBytesCount();
        } else {
            blockBytes = bytes;
        }
        byte[] valueBytes = this.readBytes(blockBytes);
        return new ByteReader(valueBytes);
    }

    public int remainBytesCount() {
        return Math.max(0, buf.length - pos);
    }

    /**
     *
     * @return
     */
    byte bitByte = 0;

    public int readBit() {
        if (bits == 0) {
            bitByte = readByte();
            bits = 8;
        }
        int ret = (bitByte >> (bits - 1)) & 0x01;
        bits--;
        return ret;
    }

    public int readBits(int bits) {
        int ret = 0;
        for (int i = 0; i < bits; i++) {
            ret <<= 1;
            ret |= readBit();
        }
        return ret;
    }

    public int readRemainBits() {
        return readBits(this.bits);
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(pos).append('/').append(buf.length).append(':');
        for (int i = 0; i < buf.length; i++) {
            if (i == pos) {
                build.append(">");
            }
            build.append(Integer.toHexString(buf[i] & 0xFF)).append(' ');
        }
        return build.toString();
    }

//    public long readBcd(int bytes) {
//        byte[] buf = new byte[bytes];
//        for (int i = 0; i < bytes; i++) {
//            buf[i] = this.readByte();
//        }
//        ByteReader r = new ByteReader(buf);
//        long ret = 0;
//        for (int i = 0; i < bytes * 2; i++) {
//            int d = r.readBits(4);
//            System.out.println("bit " + d);
//            ret *= 10;
//            ret += (d % 10);
//        }
//        return ret;
//    }
    public long readBcd(int bytes) {
        long ret = 0;
        for (int i = 0; i < bytes * 2; i++) {
            int d = this.readBits(4);
            //System.out.println("bit " + d);
            ret *= 10;
            ret += (d % 10);
        }
        return ret;
    }

    public static void main(String[] args) {
        ByteReader r = new ByteReader(new byte[]{(byte) 0x89, (byte) 0x86, 0x00, 0x21, 0x19, 0x15, 0x76, 0x01, 0x11, 0x75});
        long v = r.readBcd(10);
        System.out.println("" + v);
    }

}
