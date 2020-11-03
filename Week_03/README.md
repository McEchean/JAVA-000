学习笔记

1. 基本实现了三种outboundHandler
    * httpclient4 : 这个主要是老师实现的，自己稍微做了一些无关紧要的改动
    * okHttp : 主要流程也是参考老师的代码实现的
    * nettyclient : 这个实现也是差不多的，但是在实现的时候遇到了一些坑，最后的sync加了之后会一直block，这个是因为自己对netty不理解导致的 目前这个client还是有问题，弄了好久没弄好，主要还是对netty不理解导致的，准备等这看老师的标准答案了，
2. 实现了一个很简单的filter
    * 增加了一个header -> nio : hello filter
    
3. 实现了随机的router

4. 实现了两种后端服务endpoint提供者
    * default : 主要通过默认的配置来读取endpoint
    * etcd : 通过etcd来实时更新endpoint，简单实现了功能

5. 一些多实现的可以通过配置来实现配置，示例：
    ```java
   #filter=
   
   #inbound
   inbound.listen.port=8989
   #outbound
   outbound.type=nettyclient
   
   ##router random.etcd
   #router.type=random
   #router.endpoints.type=etcd
   #router.endpoints.etcd.hosts=http://127.0.0.1:2379
   #router.endpoints.etcd.configKey=test-endpoint
   
   #router random.default
   router.type=random
   router.endpoints.type=default
   router.endpoints.default.hosts=127.0.0.1:8088,localhost:8088
    ```
   
6. client性能测试
    * httpclient
        ```java
        Running 30s test @ http://127.0.0.1:8989/api/hello
          4 threads and 40 connections
          Thread Stats   Avg      Stdev     Max   +/- Stdev
            Latency    18.57ms   57.37ms 727.08ms   94.26%
            Req/Sec     2.21k   661.91     3.51k    69.56%
          Latency Distribution
             50%    4.22ms
             75%    8.18ms
             90%   43.67ms
             99%  305.20ms
          259215 requests in 30.06s, 12.36MB read
        Requests/sec:   8622.32
        Transfer/sec:    421.01KB
        ```
    * okhttp
        ```java
        Running 30s test @ http://127.0.0.1:8989/api/hello
          4 threads and 40 connections
          Thread Stats   Avg      Stdev     Max   +/- Stdev
            Latency     5.05ms   10.20ms 199.78ms   97.92%
            Req/Sec     2.55k   464.28     3.40k    77.51%
          Latency Distribution
             50%    3.89ms
             75%    5.41ms
             90%    7.08ms
             99%   39.29ms
          303536 requests in 30.03s, 23.74MB read
        Requests/sec:  10106.11
        Transfer/sec:    809.28KB
        ```
      
    * nettyclient
        ```java
        Running 30s test @ http://127.0.0.1:8989/api/hello
          4 threads and 40 connections
          Thread Stats   Avg      Stdev     Max   +/- Stdev
            Latency    25.32ms   18.80ms 181.88ms   85.76%
            Req/Sec   347.80    141.25   780.00     71.13%
          Latency Distribution
             50%   20.68ms
             75%   30.22ms
             90%   45.23ms
             99%  103.58ms
          8391 requests in 30.09s, 393.33KB read
          Socket errors: connect 0, read 8392, write 0, timeout 0
        Requests/sec:    278.88
        Transfer/sec:     13.07KB
        ```
    * 可以看出nettyclient的性能是有问题的，这个不知道问题是什么