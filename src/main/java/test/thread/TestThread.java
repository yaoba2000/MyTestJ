package test.thread;

/**
 * Created by Administrator on 2017-11-20.
 */
public class TestThread implements ITask {

    private String msg;

    public TestThread(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            handlePack(msg);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void handlePack(String buf) throws Exception {
        Thread.sleep(10L);
        System.out.println(Thread.currentThread().getName() + ":" + buf);
    }
}
