import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class h01 {
    public static void main(String[] args) {

        long start=System.currentTimeMillis();

        CountDownLatch c = new CountDownLatch(1);

        AtomicInteger result = new AtomicInteger();
        new Thread(()-> {
            result.set(sum());
            c.countDown();
        }).start();

        try {
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("异步计算结果为："+result);
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
