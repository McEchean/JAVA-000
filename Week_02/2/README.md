### 在 Xmx256m 参数下进行测试
--- 
1. 串行GC
    * 使用wrk进行压测结果
        ```text
      Running 1m test @ http://localhost:8088/api/hello
        12 threads and 400 connections
        Thread Stats   Avg      Stdev     Max   +/- Stdev
          Latency    14.56ms   45.26ms 729.29ms   94.50%
          Req/Sec     3.51k     1.93k    9.66k    59.26%
        Latency Distribution
           50%    3.89ms
           75%    6.06ms
           90%   14.39ms
           99%  242.67ms
        2129618 requests in 1.00m, 254.25MB read
        Socket errors: connect 157, read 303, write 0, timeout 0
      Requests/sec:  35424.45
      Transfer/sec:      4.23MB
        ``` 
     * 可以看到QPS在35k左右
2. 并行GC
    * 使用wrk进行压测结果
    ```text
   Running 1m test @ http://localhost:8088/api/hello
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency    18.39ms   56.16ms 752.35ms   93.70%
       Req/Sec     3.37k     1.81k   21.29k    64.65%
     Latency Distribution
        50%    4.18ms
        75%    6.41ms
        90%   23.65ms
        99%  283.12ms
     2318366 requests in 1.00m, 276.79MB read
     Socket errors: connect 157, read 249, write 0, timeout 0
   Requests/sec:  38578.88
   Transfer/sec:      4.61MB
    ```
   * 可以看到QPS在38k左右

3. CMS GC
    * 使用wrk进行压测结果
    ```text
   Running 1m test @ http://localhost:8088/api/hello
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency    19.12ms   54.88ms 645.29ms   93.02%
       Req/Sec     3.64k     1.97k    9.75k    63.05%
     Latency Distribution
        50%    3.98ms
        75%    6.17ms
        90%   27.01ms
        99%  302.35ms
     2454176 requests in 1.00m, 293.00MB read
     Socket errors: connect 157, read 150, write 0, timeout 0
   Requests/sec:  40805.85
   Transfer/sec:      4.87MB
    ```
   * 可以看到QPS在40k左右
   
4. G1 GC
    * 使用wrk进行压测结果
    ```text
   Running 1m test @ http://localhost:8088/api/hello
     12 threads and 400 connections
     Thread Stats   Avg      Stdev     Max   +/- Stdev
       Latency    12.32ms   39.64ms 634.66ms   94.92%
       Req/Sec     3.67k     2.03k   12.40k    62.76%
     Latency Distribution
        50%    3.62ms
        75%    5.36ms
        90%   12.11ms
        99%  213.76ms
     2395327 requests in 1.00m, 285.98MB read
     Socket errors: connect 157, read 875, write 0, timeout 0
   Requests/sec:  39864.22
   Transfer/sec:      4.76MB
    ```
   * 可以看到QPS在39k左右
   * 延迟分布上看，延迟也是最少的
   
5. 可以看到小内存下QPS是CMS最好：因为堆空间小，然后请求一多就会分配内存就会急剧上升，会触发多次的FullGC，在这么多次GC中，因为CMS在GC的时候业务线程也在工作，所以会获得更大的QPS


### 在 Xmx4g 参数下进行测试
---
1. 串行GC
    * 使用wrk进行压测结果
        ```text
      Running 1m test @ http://localhost:8088/api/hello
        12 threads and 400 connections
        Thread Stats   Avg      Stdev     Max   +/- Stdev
          Latency    25.53ms  103.77ms   1.45s    95.23%
          Req/Sec     3.27k     1.72k   13.01k    65.02%
        Latency Distribution
           50%    4.32ms
           75%    6.86ms
           90%   27.12ms
           99%  440.49ms
        2229944 requests in 1.00m, 266.23MB read
        Socket errors: connect 157, read 160, write 0, timeout 0
      Requests/sec:  37106.53
      Transfer/sec:      4.43MB
        ``` 
     * 可以看到QPS在37k左右

2. 并行 GC
    * 使用wrk进行压测结果
        ```text
      Running 1m test @ http://localhost:8088/api/hello
        12 threads and 400 connections
        Thread Stats   Avg      Stdev     Max   +/- Stdev
          Latency    20.14ms   64.68ms 845.16ms   93.79%
          Req/Sec     3.64k     1.97k   12.22k    64.93%
        Latency Distribution
           50%    4.26ms
           75%    5.53ms
           90%   20.42ms
           99%  326.97ms
        2453027 requests in 1.00m, 292.87MB read
        Socket errors: connect 157, read 165, write 0, timeout 0
      Requests/sec:  40827.22
      Transfer/sec:      4.87MB
        ``` 
     * 可以看到QPS在40k左右 

3. CMS GC
    * 使用wrk进行压测结果
        ```text
      Running 1m test @ http://localhost:8088/api/hello
        12 threads and 400 connections
        Thread Stats   Avg      Stdev     Max   +/- Stdev
          Latency    14.48ms   45.19ms 743.05ms   94.34%
          Req/Sec     3.80k     2.15k   10.23k    63.24%
        Latency Distribution
           50%    3.76ms
           75%    5.31ms
           90%   15.16ms
           99%  254.84ms
        2511089 requests in 1.01m, 299.80MB read
        Socket errors: connect 157, read 543, write 0, timeout 0
      Requests/sec:  41617.36
      Transfer/sec:      4.97MB
        ``` 
     * 可以看到QPS在41k左右 
     * 延迟分布也可以看出来延迟最少

4. G1 GC
    * 使用wrk进行压测结果
        ```text
      Running 1m test @ http://localhost:8088/api/hello
        12 threads and 400 connections
        Thread Stats   Avg      Stdev     Max   +/- Stdev
          Latency    22.08ms   77.82ms 698.19ms   94.13%
          Req/Sec     4.74k     2.06k   11.95k    65.22%
        Latency Distribution
           50%    3.62ms
           75%    4.40ms
           90%    7.31ms
           99%  434.85ms
        2812567 requests in 1.00m, 335.79MB read
        Socket errors: connect 157, read 377, write 0, timeout 0
      Requests/sec:  46812.54
      Transfer/sec:      5.59MB
        ``` 
     * 可以看到QPS在47k左右 