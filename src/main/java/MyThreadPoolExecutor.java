
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolExecutor extends ThreadPoolExecutor {
    public static final int maxPoolSize = Integer.MAX_VALUE;
    public static final long liveTime = 60;
    public static final TimeUnit unit = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);

    public MyThreadPoolExecutor(int poolSize) {
        super(poolSize, maxPoolSize, liveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        int count = 1;
        if (command.getClass().isAnnotationPresent(Repeat.class)) {
            Repeat annotation = command.getClass().getAnnotation(Repeat.class);
            count = annotation.count();
        }
        for (int i = 0; i < count; i++) {
            super.execute(command);
        }
    }

    public static void main(String[] args) {
        MyThreadPoolExecutor executor = new MyThreadPoolExecutor(20);
        executor.execute(new OneRunnable());
        executor.shutdown();
    }
}
