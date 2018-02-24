package yao.string;

import yao.util.HandleUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017-8-7.
 */
public class Test1 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a ="津N12345";
        String str="BDF24E3132333435";

        System.out.println(new String(HandleUtil.HexStringToBytes(str),"GB18030"));

        System.out.println(HandleUtil.byteToString(HandleUtil.StringToByte(a)));
        System.out.println(HandleUtil.StringToByte(a).length);
        System.out.println(new String(a.getBytes()));
        System.out.println(HandleUtil.BytesToHexString(HandleUtil.StringToByte(a)));
        System.out.println(HandleUtil.BytesToHexString(a.getBytes()));
        System.out.println(a.getBytes().length);
    }
}
