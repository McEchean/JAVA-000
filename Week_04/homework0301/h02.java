import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class h02 {
    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        AtomicInteger result = new AtomicInteger();
        CyclicBarrier c = new CyclicBarrier(1,() -> {
            System.out.println("异步计算结果为："+result.get());
            System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        });

        new Thread(() -> {
            result.set(sum());
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
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
