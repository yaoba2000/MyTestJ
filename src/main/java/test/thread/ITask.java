package test.thread;

/**
 * Created by Administrator on 2017-11-16.
 */
public interface ITask {
    void run() throws Exception;

    default void executeWithCatchAny() {
        Util.ExecuteWithCatchAny(this::run);
    }

    default void executeWithThrowAny() {
        Util.ExecuteWithThrowAny(this::run);
    }

    default Runnable toRunnable() {
        return this::executeWithThrowAny;
    }
}
