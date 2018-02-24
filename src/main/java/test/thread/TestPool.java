package test.thread;

/**
 * Created by Administrator on 2017-11-16.
 */
public class TestPool {

    public static void main(String[] args) throws Exception {

        ThreadPoolUtil pool = ThreadPoolUtil.init(10);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i <= 100; i++) {
            int finalI = i;
            pool.execute(new Thread(() -> System.out.println(finalI)));

        }

        long elapsedTime = pool.elapsedTime(startTime);
        System.out.println("总耗时:  " + elapsedTime);

      /*  pool.shutdown();
        if (pool.awaitTermination()) {
            System.out.println("规定时间内,线程池终止了不关心是否正常终止");
        } else {
            System.out.println("超时了,仅此而已,不关心线程池终止与否");
        }*/
    }
}
