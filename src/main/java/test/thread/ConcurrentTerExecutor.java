package test.thread;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

import static test.thread.Util.StartDaemonThread;

/**
 * 多线程同步处理任务，保证同一ID号在同一线程里
 * Created by Administrator on 2017-11-16.
 */
public class ConcurrentTerExecutor implements BiConsumer<Long, ITask> {

    protected final Thread[] threads;

    private final Map<Long, TerContext> terMap = new ConcurrentHashMap<>();
    private final LinkedTable<TerContext> busLinkedTable = new LinkedTable<>(TerContext.class);

    public ConcurrentTerExecutor(int nThread) {
        threads = new Thread[nThread];
        for (int i = 0; i < nThread; i++) {
            threads[i] = StartDaemonThread("ConcurrentTerExecutor.worker" + i, () -> {
                LinkedTable.LinkedNode previousNode = busLinkedTable.headNode();//找到一个节点
                while (true) {
                    LinkedTable<TerContext>.LinkedNode nextWaitingNode = nextWaitingBus(previousNode);
                    if (nextWaitingNode == null) {
                        Thread.sleep(100L);//如果为空等0.1s
                        continue;
                    } else {
                        previousNode = nextWaitingNode;
                    }
                    TerContext ter = nextWaitingNode.get();
                    try (CloseRegister end = CloseRegister.of(ter::leaveQueue)) {
                        TaskWrapper task;
                        while ((task = ter.pollTask()) != null) {
                            try {
                                task.execute();
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * @param previousNode
     * @return 返回下一个等待处理的节点，如果没找到，则返回null
     * @throws InterruptedException
     */
    private LinkedTable<TerContext>.LinkedNode nextWaitingBus(LinkedTable<TerContext>.LinkedNode previousNode) throws InterruptedException {
        return previousNode.loopFindNextNormalNode(node -> node.get().isReadReadly());
    }

    @Override
    public void accept(Long terKey, ITask task) {
        TaskWrapper taskWrap = new TaskWrapper(terKey, task);
        Bus(terKey).push(taskWrap);//这里会返回boolean，暂不使用
    }

    private TerContext Bus(Long busKey) {
        TerContext bus = terMap.computeIfAbsent(busKey, id -> {
            TerContext b = new TerContext(id);
            busLinkedTable.add(b);
            return b;
        });
        return bus;
    }

    //终端类
    private static class TerContext {

        private final Long terId;//终端ID
        private final BlockingQueue<TaskWrapper> queue = new LinkedBlockingQueue<>();
        TaskWrapper previousPollTask = null;//下一个任务
        long pushCount = 0;//计数统计
        long offerCount = 0;//计数统计

        public TerContext(Long busKey) {
            this.terId = busKey;
        }

        public Long getTerId() {
            return terId;
        }

        private boolean push(TaskWrapper task) {
            pushCount++;
            return queue.offer(task);
        }

        TaskWrapper pollTask() {
            TaskWrapper ret = queue.poll();
            if (ret != null) {
                offerCount++;
                this.previousPollTask = ret;
            }
            return ret;
        }

        private boolean using = false;
        private Thread usingThread = null;

        synchronized boolean isReadReadly() {//有待处理数据，且无人占用。
            if (!queue.isEmpty() && using == false) {
                using = true;
                usingThread = Thread.currentThread();
                return true;
            } else {
                return false;
            }
        }

        synchronized void leaveQueue() {
            using = false;
            usingThread = null;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append(getTerId()).append("_").append(using).append("_").append(usingThread).append("_queue:").append(queue.size());
            return buf.toString();
        }


    }

    //任务类
    private static class TaskWrapper {
        private final Long terId;
        private final ITask task;

        TaskWrapper(Long terId, ITask task) {
            this.terId = terId;
            this.task = task;
        }

        void execute() throws Exception {
            task.run();
        }
    }
}
