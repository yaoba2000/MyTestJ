package net.rtp.util;

public class StringUtil {

    private static char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String convertByteToHexString(byte b[]) {
        if (b == null)
            return "null";
        else
            return convertByteToHexString(b, 0, b.length);
    }

    public static String convertByteToHexString(byte b[], int offset, int len) {
        if (b == null)
            return "null";
        int end = offset + len;
        if (end > b.length)
            end = b.length;
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < end; i++)
            sb.append(hex[(b[i] & 0xf0) >>> 4]).append(hex[b[i] & 0xf]).append(' ');

        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String convertByteToHexStringWithoutSpace(byte b[]) {
        if (b == null)
            return "null";
        else
            return convertByteToHexStringWithoutSpace(b, 0, b.length);
    }

    public static String convertByteToHexStringWithoutSpace(byte b[], int offset, int len) {
        if (b == null)
            return "null";
        int end = offset + len;
        StringBuilder sb = new StringBuilder();
        if (end > b.length)
            end = b.length;
        for (int i = offset; i < end; i++)
            sb.append(hex[(b[i] & 0xf0) >>> 4]).append(hex[b[i] & 0xf]);

        return sb.toString();
    }
}

