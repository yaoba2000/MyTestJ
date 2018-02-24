package java8.inte;


import com.google.common.base.Optional;

/**
 * Created by Administrator on 2017-11-24.
 */
public class Test {
    interface Foo<F, T> {
        T foo(F from);
    }

    public static void main(String[] args) {
        Foo<String, Integer> sf = Integer::valueOf;
        Integer sf1 = sf.foo("123");
        System.out.println(sf1);

        System.out.println(Integer.parseInt("12345"));

        String pa = "";
        System.out.println(pa.length());

        byte[] byteArray = new byte[30];
        String abc = null;

//        String abcd = Optional.of("str").or(abc);
//        System.out.println(abcd);// returns true
//        System.out.println(abcd);// returns 5
        /*测试new String*/
//        String a = "hello";
//        String b = "he"+new String("llo");
//        System.out.println(a==b);
//        System.out.println(a.equals(b));

        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println(a == b);
        System.out.println(c == d);

    }

}
