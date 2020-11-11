import java.util.concurrent.*;

public class h04 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start=System.currentTimeMillis();

        int cores = Runtime.getRuntime().availableProcessors();
        int liveTime = 1000;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(cores,cores,liveTime,
                TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });


        System.out.println("异步计算结果为："+future.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        executor.shutdown();
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
