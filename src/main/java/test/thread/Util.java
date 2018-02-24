package test.thread;

/**
 * Created by Administrator on 2017-11-16.
 */
public class Util {

    public static Thread StartDaemonThread(String threadName, ITask task) {//20160413 重构代码是把Thread创建和执行移到这里。
        Thread thread = new Thread(RunnableWithCatchAny(task), threadName);
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    public static Runnable RunnableWithCatchAny(final ITask task) {
        return () -> ExecuteWithCatchAny(task);
    }

    public static void ExecuteWithCatchAny(final ITask task) {//20160425add
        try {
            task.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void ExecuteWithThrowAny(final ITask task) {//20160425add
        try {
            task.run();
        } catch (Exception e) {//只对检查异常进行重新封装，以便能继续上抛
            throw RuntimeExceptionAdapter(e);
        }
    }

    public static RuntimeException RuntimeExceptionAdapter(Exception e) {
        return ExceptionToRuntimeException.of(e);
    }
}
