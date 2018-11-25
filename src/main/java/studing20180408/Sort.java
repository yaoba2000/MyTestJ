package studing20180408;

public class Sort {
    public static void main(String[] args) {
        int a[] = {90, 5, 1, 2, 6, 7, 17, 18, 23, 10};
        sort1(a);
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
    }

    /**
     * 冒泡排序
     * 算法规则： 由于算法每次都将一个最大的元素往上冒，我们可以将待排序集合(0...n)看成两部分，一部分为(k..n)的待排序unsorted集合，
     * 另一部分为(0...k)的已排序sorted集合，每一次都在unsorted集合从前往后遍历，选出一个数，如果这个数比其后面的数大，则进行交换。
     * 完成一轮之后，就肯定能将这一轮unsorted集合中最大的数移动到集合的最后，并且将这个数从unsorted中删除，移入sorted中。
     *
     * @param args
     */
    public static void sort1(int[] args) {
        for (int i = args.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                //保证在相邻的两个数中比较选出最大的并且进行交换(冒泡过程)
                if (args[j] > args[j + 1]) {
                    int temp = args[j];
                    args[j] = args[j + 1];
                    args[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     * 算法规则： 将待排序集合(0...n)看成两部分，在起始状态中，一部分为(k..n)的待排序unsorted集合，另一部分为(0...k)的已排序sorted集合,
     * 在待排序集合中挑选出最小元素并且记录下标i，若该下标不等于k，那么 unsorted[i] 与 sorted[k]交换 ，一直重复这个过程，直到unsorted集合中元素为空为止。
     *
     * @param args
     */
    public static void sort2(int[] args) {
        int len = args.length;
        for (int i = 0, k = 0; i < len; i++, k = i) {
            // 在这一层循环中找最小
            for (int j = i + 1; j < len; j++) {
                // 如果后面的元素比前面的小，那么就交换下标，每一趟都会选择出来一个最小值的下标
                if (args[k] > args[j]) k = j;
            }
            if (i != k) {
                int tmp = args[i];
                args[i] = args[k];
                args[k] = tmp;
            }
        }
    }
}
