package yao.algorithm;

/**
 * Created by Administrator on 2017-6-9.
 */
public class StringArrange {//方法1：递归实现

    /*
     * 参数arrayA:给定字符串的字符数组
     * 参数start:开始遍历字符与其后面各个字符将要进行交换的位置
     * 参数end:字符串数组的最后一位
     * 函数功能：输出字符串数字的各个字符全排列
     */
    public void recursionArrange(char[] arrayA, int start, int end) {
        if (end <= 1)
            return;
        if (start == end) {
            for (int i = 0; i < arrayA.length; i++)
                System.out.print(arrayA[i]);
            System.out.println();
        } else {
            for (int i = start; i <= end; i++) {
                swap(arrayA, i, start);
                recursionArrange(arrayA, start + 1, end);
                swap(arrayA, i, start);
            }
        }

    }

    //交换数组m位置和n位置上的值
    public void swap(char[] arrayA, int m, int n) {
        char temp = arrayA[m];
        arrayA[m] = arrayA[n];
        arrayA[n] = temp;
    }

    public static void main(String[] args) {
        StringArrange test = new StringArrange();
        String A = "abc";
        char[] arrayA = A.toCharArray();
        test.recursionArrange(arrayA, 0, arrayA.length - 1);
    }
}