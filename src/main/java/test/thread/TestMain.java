package test.thread;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2017-11-20.
 */
public class TestMain {

    public static ConcurrentTerExecutor recvPackDbExecutor = new ConcurrentTerExecutor(3);
    private final static Set<Long> currentMinuteBusSet = new ConcurrentSkipListSet<>();
    public final static BiConsumer<Long, ITask> backServer = recvPackDbExecutor.andThen((busId, task) -> currentMinuteBusSet.add(busId));

    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            backServer.accept((long) 1 % 3, new TestThread("msg1+" + i));
            backServer.accept((long) 2 % 3, new TestThread("msg2+" + i));
            backServer.accept((long) 3 % 3, new TestThread("msg3+" + i));
            backServer.accept((long) 4 % 3, new TestThread("msg4+" + i));
            backServer.accept((long) 5 % 3, new TestThread("msg5+" + i));
            backServer.accept((long) 6 % 3, new TestThread("msg6+" + i));
        }
        System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
        Thread.sleep(5000L);

        int n = 2;
        List<Integer> ss = new ArrayList<>();
        ss.add(1);
        ss.add(2);
        ss.add(3);
        ss.add(4);
        System.out.println(
                Seq.seq(ss).window(0, n - 1)
                        .filter(w -> w.count() == n)
                        .map(w -> w.window().toList())
                        .toList()
        );

        Tuple4<String, String, String, Integer> tuple4 = Tuple.tuple("1", "2", "3", 4);
        System.out.println(tuple4.v4());


        System.out.println(currentMinuteBusSet.toString());
    }
}
