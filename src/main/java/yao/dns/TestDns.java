package yao.dns;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017-6-27.
 */
public class TestDns implements Serializable{

    private static final long serialVersionUID = 2324441340879917038L;

    public static void main(String[] args) throws UnknownHostException {
        //获取本机IP地址
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        //获取www.baidu.com的地址
        System.out.println(InetAddress.getByName("www.wtlhs.com.cn"));
        //获取www.baidu.com的真实IP地址
        System.out.println(InetAddress.getByName("www.tcps.com.cn").getHostAddress());
        //获取配置在HOST中的域名IP地址
//        System.out.println(InetAddress.getByName("localhost").getHostAddress());
        //根据IP解析到域名基本没戏
//        InetAddress ia = InetAddress.getByName("60.205.44.8");
//        System.out.println(ia.getHostName());
    }
}
