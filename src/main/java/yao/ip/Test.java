package yao.ip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-8-30.
 */
public class Test {
    public static void main(String[] args) {
        String regex = "/(.*?):(.*)";
        Pattern p = Pattern.compile(regex);
        String t = "/127.0.0.1:8080";
        Matcher m = p.matcher(t);
        while (m.find()) {
            System.out.println(m.group(1));
            System.out.println(m.group(2));
        }
        String[] ipstr = t.replace("/","").split(":");
        System.out.println(ipstr[1]);

    }
}
