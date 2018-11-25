package yao.arrylist;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017-7-5.
 * 转成list方式
 */
public class TestList implements Serializable{


    private static final long serialVersionUID = 7942610063131990096L;

    public static void main(String[] args) {
        List<String> stooges = Arrays.asList("Larry", "Moe", "Curly");
        System.out.println(stooges);
        int sum = 4 << 2;
        System.out.println(sum);
    }
}
