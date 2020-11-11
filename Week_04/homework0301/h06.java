import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class h06 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();
        Semaphore s = new Semaphore(1);
        AtomicInteger result = new AtomicInteger();
        new Thread(() -> {
            try {
                s.acquire();
                result.set(sum());
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1);
        s.acquire();
        s.release();
        System.out.println("异步计算结果为："+ result.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
