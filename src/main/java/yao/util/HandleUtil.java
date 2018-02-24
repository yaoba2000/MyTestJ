/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yao.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class HandleUtil {

    //yy add 20170330新能源电动车 增加 传入值，字节长度，小数点位数，以及偏移量（float）
    public static String checkData(long value, int len, int decimal, int offset) {
        String str = "0";//debug 当传入参数为0的时候是不允许为空的
        switch (len) {
            case 1:
                if (value == 0xff || value == 0xfe) {
                    str = value + "";
                    return str;
                }
                break;
            case 2:
                if (value == 0xffff || value == 0xfffe) {
                    str = value + "";
                    return str;
                }
                break;
            case 4:
                if (value == 0xffffffff || value == 0xfffffffe) {
                    str = value + "";
                    return str;
                }
                break;
            default:
                System.out.println("checkData err!");
        }
        if (decimal != 0 || offset != 0) {//默认此逻辑和减法逻辑共存
            int x = (int) Math.pow(10, decimal);
            str = String.format("%." + decimal + "f", (double) value / x - offset);
        }

        return str;
    }

    //yy add 20170330 传入一个值判断是否是 0xFE 或者是 0xFF
    public static int checkFor(int value, int len) {
        switch (len) {
            case 1:
                if (value == 0xff || value == 0xfe) {
                    return 0;
                }
                break;
            case 2:
                if (value == 0xffff || value == 0xfffe) {
                    return 0;
                }
                break;
            default:
                System.out.println("checkFor err!");
        }
        return value;
    }

    //由于不同操作系统存在字符编码问题，正则判断第一个字符是否是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("(\\d+)(.*)");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //比较两个float大小 如果x<y返回-1；等于返回0；大于返回1
    public static int Compare(float x, float y) {
        return x < y ? -1 : (x > y ? 1 : 0);
    }

    public static void ThreadExecute(Runnable task) {//20160413 重构代码是把Thread创建和执行移到这里。
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static void ThreadStop(Runnable task) {//20160413 重构代码是把Thread创建和执行移到这里。
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.stop();
    }

    //将字符串中某一位设置成1
    public static String setStatus(String s, int pos) {
        if (pos > s.length() || pos < 0) {
            return s;
        }
        return s.substring(0, pos - 1) + "1" + s.substring(pos, s.length());
    }

    public static String formatTime(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = format.format(date);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date convertToDate(String str) {
        try {
            //2016-07-29 15:39:05
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateFormatConvert(String dateStr, String oldFormat, String newFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(oldFormat);
            Date date = format.parse(dateStr);
            format = new SimpleDateFormat(newFormat);
            String str = format.format(date);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过访问url获取返回的字符串
     *
     * @param urlString：url
     * @return 页面返回的字符串
     */
    public static String getReturnData(String urlString) {
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(urlString);
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(10000);
            uc.setReadTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                json.append(line);
            }
            if (json.charAt(0) == '[') {
                json.deleteCharAt(0);
            }
            if (json.charAt(json.length() - 1) == ']') {
                json.deleteCharAt(json.length() - 1);
            }
            in.close();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            return "";
        }
        return json.toString();
    }

    public static String getJarRunTime() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static byte[] StringToByte(String str) {
        ByteBuilder builder = new ByteBuilder();
        byte[] buf = null;
        try {
            buf = str.getBytes("GB18030");
        } catch (UnsupportedEncodingException e) {
            buf = str.getBytes();
            e.printStackTrace();
        }
        builder.append(buf);

        return builder.toBytes();
    }

    //把从数据包中解析出来的字节数组转换成字符串
    public static String byteToString(byte[] data) {

        if (data.length == 0) {
            return null;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                byte[] newData = Arrays.copyOf(data, i);
                try {
                    String str = new String(newData, "GB18030");
                    return str;
                } catch (UnsupportedEncodingException e) {
                    String str = new String(newData);
                    return str;
                }
            }
        }
        try {
            String str = new String(data, "GB18030");
            return str;
        } catch (UnsupportedEncodingException e) {
            String str = new String(data);
            return str;
        }
    }

    public static long getTimeFromBCD(String bcdTime) {
        String time = getTime(bcdTime);

        long timemills = 0;
        if (time == null) {
            return timemills = new Date().getTime();
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            timemills = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timemills;
    }

    public static String getTime(String str) {

        try {
            StringBuilder builder = new StringBuilder();
            builder.append("20").append(str.substring(0, 2)).
                    append("-").append(str.substring(2, 4)).
                    append("-").append(str.substring(4, 6)).
                    append(" ").append(str.substring(6, 8)).
                    append(":").append(str.substring(8, 10)).
                    append(":").append(str.substring(10, 12));

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*构建数据头中消息体属性
     *@ isPwd 是否加密 1是 0否
     *@ isSubPack 是否分包 1是 0否
     *@ isReserve 预留
     */
    public static byte[] createDataProperty(byte[] buf, int isPwd, int isSubPack, int isReserve) {
        BitBuilder b = new BitBuilder();
        b.appendBits(2, isReserve);
        b.appendBit(isSubPack);
        b.appendBits(3, isPwd);
        b.appendBits(10, buf == null ? 0 : buf.length);
        return b.toBytes();

    }
    private static final Map<Long, Integer> serialIdMap = new ConcurrentHashMap<>(); //使用一个map，来保证发送给设备的指令的流水号无问题

    //根据不同的设备，获得当前通道的一个流水号
    public static int getSerialId(long ISUId) {
        int serialId = 0;
        if (serialIdMap.containsKey(ISUId)) {
            serialId = serialIdMap.get(ISUId);
        }

        if (serialId == 65535) {//两字节最大65535
            serialIdMap.put(ISUId, 0);
        } else {
            serialIdMap.put(ISUId, serialId + 1);
        }
        return serialId;
    }

    //从消息体属性中解析消息体长度
    public static int getDataLength(int dataProperty) {
        // int i = dataProperty & 0x01;
        int length = 0;
        for (int i = 0; i < 10; i++) {
            int j = dataProperty >> i & 0x01;
            if (j == 1) {
                length += Math.pow(2, i);
            }
        }
        return length;
    }

    public static boolean matcherOS(String os) {
        Pattern pattern = Pattern.compile("Window", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(os);
        return matcher.find();
    }

    /* 按照标识符的数量截取字符串
     * @ param str 要截取的字符串
     * @ param reg 指定的标识符
     * @ param num 标识符出现的次数
     */
    public static String Sub(String str, String reg, int num) {

        for (int i = 0; i < num; i++) {
            str = str.substring(str.indexOf(reg) + 1);
        }

        return str;
    }

    //从Map中获取数据
    public static Object getObjMap(Map map, Object key) {
        Object obj = map.get(key);
        return obj;
    }

    //获取字符串格式时间
    public static String getFormatTime(Date date) {
        Date temp = null;
        String result = "";
        temp = date == null ? new Date() : date;
        try {
            result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //四舍五入保留float小数几位
    public static float getRetainDecimal(float value, int i) {
        int x = (int) Math.pow(10, i);
        float result = (float) (Math.round(value * x)) / x;
        return result;
    }

    /**
     * 把byte数组转化成2进制字符串
     *
     * @param bArr
     * @return
     */
    public static String BytesToBinaryStr(byte[] bArr) {
        String result = "";
        for (byte b : bArr) {
            result += ByteToBinaryStr(b);
        }
        return result;
    }

    /**
     * 把byte转化成2进制字符串
     *
     * @param b
     * @return
     */
    public static String ByteToBinaryStr(byte b) {
        String result = "";
        byte a = b;
        for (int i = 0; i < 8; i++) {
            byte c = a;
            a = (byte) (a >> 1);//每移一位如同将10进制数除以2并去掉余数。
            a = (byte) (a << 1);
            if (a == c) {
                result = "0" + result;
            } else {
                result = "1" + result;
            }
            a = (byte) (a >> 1);
        }
        return result;
    }

    //yy 20161117 读一个字节带符号位
    public static int readByte(byte[] buf, int pos) {
        byte ret = buf[pos - 1];
        return ret;
    }

    public static int BytesToInt(byte[] buf, int start, int length) {
        int ret = 0;
        for (int i = start; i < start + length; i++) {
            ret <<= 8;
            ret |= (0xff & buf[i]);
        }
        return ret;
    }

    public static long BytesToLong(byte[] buf, int start, int length) {
        long ret = 0;
        for (int i = start; i < start + length; i++) {
            ret <<= 8;
            ret |= (0xff & buf[i]);
        }
        return ret;
    }

    public static int BytesToSignedInt(byte[] buf, int start, int length) {
        int ret = BytesToInt(buf, start, length);
        if ((buf[start] & 0x80) != 0 && length < 4) {//20151111王红广：尽管晦涩但逻辑没问题
            ret -= 1 << (length * 8);
        }
        return ret;
    }

    public static long BytesToSignedLong(byte[] buf, int start, int length) {
        long ret = BytesToLong(buf, start, length);
        if ((buf[start] & 0x80) != 0 && length < 8) {
            ret -= 1L << (length * 8);
        }
        return ret;
    }

    public static long BcdBytesToLong(byte[] buf, int start, int size) {
        String str = BytesToHexString(buf, start, size);
        return Long.parseLong(str);
    }

    public static String BytesToHexString(byte[] buf, int start, int length) {

        StringBuilder out = new StringBuilder();
        for (int i = start; i < start + length; i++) {
            out.append(String.format("%02X", buf[i]));
        }

        return out.toString();
    }

    public static String BytesToHexString(byte[] buf) {
        if (buf == null) {
            return null;
        }
        return BytesToHexString(buf, 0, buf.length);
    }

    public static byte[] LongToBytes10(long num, int length) {
        byte[] buf = new byte[length];
        for (int i = length - 1; i >= 0; i--) {
            int low = (int) (num % 10);
            num /= 10;
            int high = (int) (num % 10);
            num /= 10;
            buf[i] = (byte) ((high << 4) | low);
        }
        return buf;
    }

    public static byte[] IntToBytes(int num, int length) {
        byte[] buf = new byte[length];
        return IntToBytes(num, buf, 0, length);
    }

    public static byte[] IntToBytes(int num, byte[] buf, int start, int length) {
        return LongToBytes(num, buf, start, length);
    }

    public static byte[] LongToBytes(long num, int length) {
        byte[] buf = new byte[length];
        return LongToBytes(num, buf, 0, length);
    }

    public static byte[] LongToBytes(long num, byte[] buf, int start, int length) {
        for (int i = start + length - 1; i >= start; i--) {
            buf[i] = (byte) (num & 0xff);
            num >>>= 8;
        }
        return buf;
    }

    public static byte[] loadBinFile(String filename) {
        byte[] buf = new byte[1024 * 128];
        FileInputStream out = null;
        try {
            out = new FileInputStream(filename);
            int len = out.read(buf);
            out.close();

            byte[] buf2 = new byte[len];
            System.arraycopy(buf, 0, buf2, 0, len);
            return buf2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            logger.log(Level.ERROR, this.getClass().getSimpleName(), e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toHexString(byte[] buf, int start, int len) {
        //wang return new HexBytes(buf, start, len).getHexString();
        //2008.11.19.xuanhua
        StringBuilder buffer = new StringBuilder();
        char[] ch = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = start; i < start + len && start + len <= buf.length; i++) {
            int b = (int) buf[i];
            int high = ((b >>> 4) & 0x0f);
            int low = (b & 0x0f);
            buffer.append(ch[high]);
            buffer.append(ch[low]);
        }
        return buffer.toString();
    }

    private static int hexCharToVal(char ch) {
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        } else if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        } else if (ch >= '0' && ch <= '9') {
            return ch - '0';
        } else {
            return 0;
        }
    }

    public static byte[] HexStringToBytes(String hexStr) {
        int bytelen = (hexStr.length() + 1) / 2;
        byte[] buf = new byte[bytelen];
        for (int i = 0; i < bytelen; i++) {
            int high = hexCharToVal(hexStr.charAt(i * 2));
            int low = (i == bytelen - 1 && hexStr.length() + 1 == bytelen * 2) ? 0 : hexCharToVal(hexStr.charAt(i * 2 + 1));
            buf[i] = (byte) ((high << 4) | (low & 0x0f));
        }
        return buf;//new HexBytes(hexString).getBytes();
    }

    public static String DateToSqlStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date SqlDateStrToDate(String sqlDateStr) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sqlDateStr, new ParsePosition(0));
    }
    private static int createStringIndex = 0;

    public static String createRandString(int min, int max) {
        char[] table = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int rand = ((createStringIndex++) % table.length);
        int len = max; //wanghg tmp
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char rand2 = table[rand];
            buf.append(new Character(rand2));
        }
        return buf.toString();
    }
    static int count = 1;

    public static int getRandom() {
        count++;
        if (count < 0) {
            count = 0;
        }
        return count;
    }
    static int countl = 1;

    public static long getRandomLong() {
        return countl++;
    }

    public static void zipFile(String name, boolean deleteOldFile) {
        String logFilename = name + ".log";

        String timestamp = new SimpleDateFormat("yyyy-MM-dd.hh-mm-ss").format(new Date(System.currentTimeMillis()));
        String zipFilename = logFilename + timestamp + ".zip";
        final int MAX_ZIP_BUFFER_SIZE = 4096;
        final int MAX_FILE_BUFFER_SIZE = 1024;
        try {
            FileOutputStream fsOut = new FileOutputStream(zipFilename);
            ZipOutputStream osZip = new ZipOutputStream(
                    new BufferedOutputStream(fsOut, MAX_ZIP_BUFFER_SIZE));
            String strEntry = logFilename;
            ZipEntry ze = new ZipEntry(strEntry);
            osZip.putNextEntry(ze);

            FileInputStream fsIn = new FileInputStream(logFilename);
            BufferedInputStream bsIn
                    = new BufferedInputStream(fsIn, MAX_ZIP_BUFFER_SIZE);

            byte[] buf = new byte[MAX_ZIP_BUFFER_SIZE];
            while (true) {
                int nRead = bsIn.read(buf, 0, buf.length);
                if (nRead <= 0) {
                    break;
                }
                osZip.write(buf, 0, nRead);
            }

            osZip.flush();
            osZip.closeEntry();
            osZip.close();
            bsIn.close();

            if (deleteOldFile) {
                File oldFile = new File(logFilename);
                oldFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double AnggpsToAngrad(long gpsPackageVal) {
        long du = gpsPackageVal / 10000000;
        long fentmp = (gpsPackageVal % 10000000);
        double fen = fentmp / 100000D;
        double angdeg = du + fen / 60D;
        return angdeg;//wangtest Math.toRadians(angdeg);
    }

    public static long AngradToAnggps(double angrad) {
        double angdeg = angrad; //wangtest Math.toDegrees(angrad);
        long angdegD = (long) angdeg;
        angdeg -= (long) angdeg;
        return angdegD * 10000000 + (long) (angdeg * 60D * 100000D);
    }

    public static double GetDistance(double swd, double sjd, double ewd, double ejd) {
        double a = Math.acos(Math.cos(ewd - swd) * Math.cos(ejd - sjd));
        double distance = a * 6371 * 1000;
        return distance;
    }
//	public static double GetDistanceOld(double swd, double sjd, double ewd, double ejd) {
//		double x = Math.cos(swd) * Math.cos(sjd);
//		double y = Math.cos(swd) * Math.sin(sjd);
//		double z = Math.sin(swd);
//		double x2 = Math.cos(ewd) * Math.cos(ejd);
//		double y2 = Math.cos(ewd) * Math.sin(ejd);
//		double z2 = Math.sin(ewd);
//		double a = Math.acos(x * x2 + y * y2 + z * z2);
//		double distance = a * 6371 * 1000;
//		return distance;
//	}

    public static void Close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 与Long.decode()区别：前导0不再解析成8进制（BUG根源）；增加0o八进制前导符；增加0b二进制前导符号
     *
     * @param nm
     * @return
     * @throws NumberFormatException
     */
    public static Integer decodeInt(String nm) throws NumberFormatException {//20141204add
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Integer result;

        if (nm.length() == 0) {
            throw new NumberFormatException("Zero length string");
        }
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        } else if (firstChar == '+') {
            index++;
        }

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (nm.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (nm.startsWith("0o", index) || nm.startsWith("0O", index)) {
            index += 2;
            radix = 8;
        } else if (nm.startsWith("0b", index) || nm.startsWith("0B", index)) {
            index += 2;
            radix = 2;
//        } else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
//            index++;
//            radix = 8;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index)) {
            throw new NumberFormatException("Sign character in wrong position");
        }

        try {
            result = Integer.valueOf(nm.substring(index), radix);
            result = negative ? Integer.valueOf(-result.intValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Integer.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                    : nm.substring(index);
            result = Integer.valueOf(constant, radix);
        }
        return result;
    }

    /**
     * 与Long.decode()区别: 前导0不再解析成8进制（BUG根源）；增加0o八进制前导符；增加0b二进制前导符号
     *
     * @param nm
     * @return
     * @throws NumberFormatException
     */
    public static Long decodeLong(String nm) throws NumberFormatException {//20141204add
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Long result;

        if (nm.length() == 0) {
            throw new NumberFormatException("Zero length string");
        }
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        } else if (firstChar == '+') {
            index++;
        }

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (nm.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (nm.startsWith("0o", index) || nm.startsWith("0O", index)) {
            index += 2;
            radix = 8;
        } else if (nm.startsWith("0b", index) || nm.startsWith("0B", index)) {
            index += 2;
            radix = 2;
//        } else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
//            index++;
//            radix = 8;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index)) {
            throw new NumberFormatException("Sign character in wrong position");
        }

        try {
            result = Long.valueOf(nm.substring(index), radix);
            result = negative ? Long.valueOf(-result.longValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Long.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                    : nm.substring(index);
            result = Long.valueOf(constant, radix);
        }
        return result;
    }

    // yy 20170323 add 增加字节转换成时间long型
    public static Long byteToTime(byte[] buf) {
        int start = 0;
        int year = BytesToInt(buf, start, 1) + 2000;
        int month = BytesToInt(buf, start + 1, 1);
        int day = BytesToInt(buf, start + 2, 1);
        int hour = BytesToInt(buf, start + 3, 1);
        int miniute = BytesToInt(buf, start + 4, 1);
        int second = BytesToInt(buf, start + 5, 1);

        long val = year;
        val = val * 100 + month;
        val = val * 100 + day;
        val = val * 100 + hour;
        val = val * 100 + miniute;
        val = val * 100 + second;

        return val;
    }

    //yy 20170323 add long转byte
    public static byte[] timeToByte(long val) {
        int second = (int) (val % 100);
        val /= 100;
        int miniute = (int) (val % 100);
        val /= 100;
        int hour = (int) (val % 100);
        val /= 100;
        int day = (int) (val % 100);
        val /= 100;
        int month = (int) (val % 100);
        val /= 100;
        int year = (int) (val % 100);
        val /= 100;

        byte[] bodyBuf = new byte[6];
        IntToBytes(year, bodyBuf, 0, 1);
        IntToBytes(month, bodyBuf, 1, 1);
        IntToBytes(day, bodyBuf, 2, 1);
        IntToBytes(hour, bodyBuf, 3, 1);
        IntToBytes(miniute, bodyBuf, 4, 1);
        IntToBytes(second, bodyBuf, 5, 1);

        return bodyBuf;
    }

}
