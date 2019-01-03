package java8.inte;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017-11-27.
 */
public class Java8Sort {

    /**
     * java8 Hashmap 排序
     * @param args
     */
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> mp = new ConcurrentHashMap<>();
        mp.put("20171029140111", "001");
        mp.put("20171030140111", "002");
        mp.put("20171129140111", "003");
        //找到第一个
        String result = mp.entrySet().stream().sorted(Map.Entry.comparingByKey()).findFirst().map(x -> x.getValue()).get();
        System.out.println(result);
        //测试
    }
}