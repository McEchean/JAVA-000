### Server01 单线程模式
1. 压测结果
    ```java
   Running 1m test @ http://localhost:8808
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency     1.14s    47.94ms   1.19s    98.26%
       Req/Sec     2.04      2.90    10.00     88.29%
     Latency Distribution
        50%    1.15s
        75%    1.15s
        90%    1.16s
        99%    1.18s
     632 requests in 1.00m, 157.26KB read
     Socket errors: connect 157, read 3384, write 17, timeout 0
   Requests/sec:     10.52
   Transfer/sec:      2.62KB
    ```
2. 注意点： 在返回HTTP报文的时候需要加上Content-Length这个头，不然浏览器访问会失败报错，`connect reset by peer`
    ```java
   public void startServer() throws Exception {
           ServerSocket socket = new ServerSocket(8808);
           while(true) {
               Socket accept = socket.accept();
               service(accept);
           }
       }
   
       public void service(Socket s) throws IOException {
           try(OutputStream outputStream = s.getOutputStream()) {
               Thread.sleep(20);
               String str = "HELLO NIO\n";
               PrintWriter print = new PrintWriter(outputStream, true);
               outputStream.write("HTTP/1.1 200 OK\n".getBytes());
               outputStream.write("Content-Type: text/html;charset=utf-8\n".getBytes());
               outputStream.write(String.format("Content-Length: %d\n", str.length()).getBytes());
               outputStream.write("\n".getBytes());
               outputStream.write(str.getBytes());
               outputStream.flush();
               outputStream.close();
               s.close();
           }catch ( IOException | InterruptedException e) {
               e.printStackTrace();
           }
       }
   
       public static void main(String[] args) {
           Server01 s = new Server01();
           try {
               s.startServer();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
      ```
   
### Server02 多线程模式
1. 压测结果
    ```java
   Running 1m test @ http://localhost:8808
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency    22.94ms    4.86ms  98.51ms   97.47%
       Req/Sec    10.89     10.42   130.00     89.77%
     Latency Distribution
        50%   21.93ms
        75%   24.23ms
        90%   25.28ms
        99%   34.99ms
     4791 requests in 1.00m, 2.85MB read
     Socket errors: connect 157, read 135649, write 66, timeout 0
   Requests/sec:     79.72
   Transfer/sec:     48.50KB
    ```
   
### Server03 线程池模式
1. 压测结果
    ```java
   Running 1m test @ http://localhost:8808
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency    39.41ms   23.01ms 140.88ms   94.07%
       Req/Sec    10.44      9.27   100.00     71.35%
     Latency Distribution
        50%   30.96ms
        75%   46.59ms
        90%   51.60ms
        99%  132.49ms
     4383 requests in 1.00m, 2.30MB read
     Socket errors: connect 157, read 105092, write 48, timeout 0
   Requests/sec:     72.93
   Transfer/sec:     39.25KB
    ```
   
### NIO Server
1. 压测结果
    ```java
   Running 1m test @ http://localhost:8808/hello
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency     4.68ms   32.64ms   1.24s    99.34%
       Req/Sec     7.52k     3.46k   25.12k    60.37%
     Latency Distribution
        50%    2.52ms
        75%    2.68ms
        90%    3.09ms
        99%    6.82ms
     5372452 requests in 1.00m, 527.73MB read
     Socket errors: connect 157, read 206, write 0, timeout 0
   Requests/sec:  89393.63
   Transfer/sec:      8.78MB
    ```
   
### 可以看到NIOServer相比于前面几种方案的性能提升是非常巨大的