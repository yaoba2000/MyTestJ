package test.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-11-20.
 */
public class CloseRegister implements AutoCloseable {

    final List<Runnable> tasks = new ArrayList<>();

    public static CloseRegister of() {
        return new CloseRegister();
    }

    public static CloseRegister of(Runnable... tasks) {
        return new CloseRegister().addAll(tasks);
    }

    public CloseRegister addAll(Runnable... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
        return this;
    }

    public CloseRegister add(Runnable task) {
        this.tasks.add(task);
        return this;
    }

    @Override
    public void close() throws Exception {
        List<Runnable> dest = new ArrayList<>(tasks);
        Collections.copy(dest, tasks);
        Collections.reverse(dest);
        for (Runnable task : dest) {
            try {
                task.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
