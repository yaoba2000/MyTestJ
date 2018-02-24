package yao.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-6-22.
 */
public class TestMap {
    public static void main(String[] args) {
        Map<String, User> map = new HashMap<>();
        User user1 = new User();
        user1.setId(1);
        user1.setName("yao");
        User user2 = new User();
        user2.setId(1);
        user2.setName("yao");
        System.out.println(user2 == user1);
        System.out.println(user2.equals(user1));

//        String a = new String("111");
//        String b = new String("111");
        String a = "111";
        String b = new String("111");
        System.out.println(a == b);
        System.out.println(a.equals(b));

        StringBuilder s1 = new StringBuilder("111");
        StringBuilder s2 = new StringBuilder("111");
        StringBuffer s3 = new StringBuffer("111");
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println("s hashcode:" + user2.hashCode());
        char[] ac = new char[]{'a'};
        System.out.println(31+ac[0]);

        System.out.println("----------------------");

        Map<String,String> cmap = new HashMap<>();
        cmap.put("1","a");
        cmap.put("2","b");
        System.out.println("cmap:"+cmap);
    }
}
