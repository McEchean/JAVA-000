import java.util.concurrent.atomic.AtomicInteger;

public class h07 {
    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        AtomicInteger result = new AtomicInteger();
        new Thread(() -> {
            result.set(sum());
        }).start();
        while(Thread.currentThread().getThreadGroup().activeCount() > 2) {
            Thread.yield();
        }
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
