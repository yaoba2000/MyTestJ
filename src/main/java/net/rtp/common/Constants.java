package net.rtp.common;

public class Constants {

    /**
     * 包头分隔符
     */
    public static final byte[] DELIMITER = new byte[]{0x30, 0x31, 0x63, 0x64};

    /**
     * 每个包的最大长度
     */
    public static final int MAX_FRAME_LENGTH = 980;
}
