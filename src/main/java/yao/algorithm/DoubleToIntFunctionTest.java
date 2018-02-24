package yao.algorithm;

/**
 * Created by Administrator on 2017-6-7.
 */

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.DoubleToIntFunction;

public class DoubleToIntFunctionTest {

    public static void main(String[] args) {
        DoubleToIntFunction df = (x) -> {
            return (int) (x * 10) + 2;
        };
        System.out.println(df.applyAsInt(3.14));
        BinaryOperator<Integer> bi = BinaryOperator.minBy(Comparator.reverseOrder());//选出两个数中最大的
        System.out.println(bi.apply(2, 3));
    }
}
