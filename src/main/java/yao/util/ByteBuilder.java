package yao.util;

import java.util.Arrays;

/**
 * 字节数组构造器
 *
 */
public class ByteBuilder {

    /**
     *
     */
    protected final int capacity;

    /**
     *
     */
    protected int pos = 0;

    /**
     *
     */
    protected byte[] buf;//自动扩展

    private final BitBuilder bitBuilder = new BitBuilder();

    /**
     *
     */
    public ByteBuilder() {
        this(512);
    }

    /**
     *
     * @param capacity
     */
    public ByteBuilder(int capacity) {
        this.capacity = capacity;
        this.buf = new byte[capacity];
    }

    private void movePos(int bytes) {
        this.pos += bytes;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public ByteBuilder skip(int bytes) {
        need(bytes);
        movePos(bytes);//pos += bytes;
        return this;
    }

    /**
     * 只能修改游标之前的字节，用于逆序输出字节值。可以配合skip(bytes)方法使用。
     *
     * @param index
     * @param l
     * @return
     */
    public ByteBuilder setBack(int index, long l) {
        if (index <= 0) {
            throw new IllegalArgumentException("为了防止忘记移动游标，强制只能修改之前数据");
        }
        buf[pos - index] = (byte) l;
        return this;
    }

    /**
     *
     * @param val
     * @return
     */
    public ByteBuilder append(byte[] val) {
        append(val, 0, val.length);
        return this;
    }

    public ByteBuilder appendBcd(int bytes, long val) {
        //System.out.println("appendBcd("+val);
        this.skip(bytes);
        for (int i = 0; i < bytes; i++) {
            int byteVal = (int) (val % 100);
            int high = byteVal / 10;
            int low = byteVal % 10;
            this.setBack(1 + i, (high << 4) | low);
            val /= 100;
        }
        //System.out.println("byteBuilder:"+this);
        return this;
    }

    /**
     *
     * @param val
     * @param from
     * @param length
     * @return
     */
    public ByteBuilder append(byte[] val, int from, int length) {
        if (length > 0) {
            need(length);//20160216debug need(val.length);
            System.arraycopy(val, from, buf, pos, length);
            movePos(length);//pos+=length;
        }
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteBuilder appendByte(int b) {
        need(1);
        buf[pos] = (byte) b;
        movePos(1);//
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteBuilder appendShort(int b) {
        return appendNumber(2, b);
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteBuilder appendInt(int b) {
        return appendNumber(4, b);
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteBuilder appendLong(long b) {//20160315wanghg DEBUG public ByteBuilder appendLong(int b) {
        return appendNumber(8, b);
    }

    public ByteBuilder appendNumber(int bytes, int value) {
        return appendNumber(bytes, (long) value);
    }

    /**
     * 大端字节顺序
     *
     * @param bytes
     * @param value
     * @return
     */
    public ByteBuilder appendNumber(int bytes, long value) {
        need(bytes);
        this.skip(bytes);
        for (int i = 0; i < bytes; i++) {
            this.setBack(1 + i, (byte) value);//appendByte((byte) value);
            value >>= 8;
        }
        //movePos(bytes);//pos+=bytes;
        return this;
    }

    /**
     *
     * @return
     */
    public byte[] toBytes() {
        return Arrays.copyOf(buf, pos);
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            build.append(Integer.toHexString(buf[i] & 0xFF)).append(' ');
        }
        return build.toString();
    }

    /**
     *
     * @param newBytes
     */
    protected void need(int newBytes) {
        if (pos + newBytes > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length * 2, this.pos + newBytes));//20160216debug 原数组大小翻倍，并不能保证肯定容下新增数据。
        }
    }

    /**
     *
     * @param object
     * @return
     */
    public ByteBuilder append(ToBytesable object) {
        object.toBytes(this);
        return this;
    }

    /**
     * 需要执行commitBits()才可生效。
     *
     * @param bites
     * @param val
     * @return
     */
    public ByteBuilder appendBits(int bites, long val) {
        bitBuilder.appendBits(bites, val);
        return this;
    }

    public ByteBuilder commitBits() {
        if (bitBuilder.size() > 0) {
            this.append(bitBuilder.toBytes());
            bitBuilder.clear();
        }
        return this;
    }

    /**
     * 添加校验字节。算法：对追加的所有字节进行异或。
     */
    public void appendXorByte() {
        //计算并输出校验码
        byte check = 0;
        for (int i = 0; i < pos; i++) {
            check ^= buf[i];
        }
        appendByte(check);
    }

    /**
     *
     */
    @FunctionalInterface
    public static interface ToBytesable {

        /**
         *
         * @param out
         */
        void toBytes(ByteBuilder out);
    }

}
