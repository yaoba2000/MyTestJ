package yao.disruptor;

import com.conversantmedia.util.concurrent.DisruptorBlockingQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2017-6-19.
 * 经测试 {@link ArrayBlockingQueue} 比 DisruptorBlockingQueue take()慢了大概1半的时间
 * 2017-7-4 更新到服务器发现多线程写入同步效果没有ArrayBlockingQueue的效果好。有待查找并改进
 * 放入时间一样。
 */
public class TestQueue {
    public static void main(String[] args) throws InterruptedException {
        final int cap = 16777216;
        BlockingQueue<InParkingDataEvent> dbq = new ArrayBlockingQueue<>(cap);
        long start1 = System.currentTimeMillis();
        while (dbq.offer(new InParkingDataEvent())) ;
        long end1 = System.currentTimeMillis();
        System.out.println("test1:" + (end1 - start1) + "ms");
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < dbq.size(); i++) {
            dbq.take();
        }
        long end2 = System.currentTimeMillis();
        System.out.println("test2:" + (end2 - start2) + "ms");
    }
}
