import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CyclicBarrier;

public class Chemistry {
    private final Semaphore o2Semaphore;
    private final Semaphore h2Semaphore;
    private final CyclicBarrier cyclicBarrier;


    public Chemistry() {
        this.o2Semaphore = new Semaphore(1);
        this.h2Semaphore = new Semaphore(2);
        this.cyclicBarrier = new CyclicBarrier(3);
    }

    public void o2(Runnable o2Release) throws InterruptedException {
        o2Semaphore.acquire();
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        o2Release.run();
        o2Semaphore.release();
    }

    public void h2(Runnable h2Release) throws InterruptedException {
        h2Semaphore.acquire();
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        h2Release.run();
        h2Semaphore.release();
    }


    public static void main(String[] args) {
        Chemistry watery = new Chemistry();
        String str = "OOHHHHOOOOHHHHOOHOHOHOHOOHHH";

        Runnable h2Release = () -> System.out.println("H");
        Runnable o2Release = () -> System.out.println("O");

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 'O') {
                new Thread(() -> {
                    try {
                        watery.o2(o2Release);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (c == 'H') {
                new Thread(() -> {
                    try {
                        watery.h2(h2Release);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
