package test.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-11-22.
 */
public class TestArray {
    public static void main(String[] args) {
        List<String> list1 =new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        List<String> list2 =new ArrayList<>(list1);
        Collections.copy(list2, list1);
        Collections.reverse(list2);
        System.out.println(list1.toString());
        System.out.println(list2.toString());

    }
}
