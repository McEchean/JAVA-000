### 串行 GC 模式运行 GCLogAnalysis
1. 执行   
    ```shell script
    java -XX:+UseSerialGC -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:demo.gc.log GCLogAnalysis
    ```
2. 分析
    ```java
   14 2020-10-25T13:52:29.197-0800: 0.472: [GC (Allocation Failure) 2020-10-25T13:52:29.197-0800: 0.472: [DefNew: 157247K->17471K(157248K), 0.0274105 secs] 447900K->351311K(506816K), 0.0275557 secs] [Times: user=0.01 sys=0.01, real=0.03 secs]
   //👆GC时间戳                             / GC原因：对象分配内存失败   / 发生GC时间                         / Yong区发生GC：DefNew（串行GC的Yong区GC）157M -> 174M, 耗时27ms/ 总堆 447M -> 35M（总容量506M），耗时27ms
   15 2020-10-25T13:52:29.242-0800: 0.517: [GC (Allocation Failure) 2020-10-25T13:52:29.242-0800: 0.517: [DefNew: 157167K->157167K(157248K), 0.0000343 secs]2020-10-25T13:52:29.242-0800: 0.517: [Tenured: 333839K->277965K(349568K), 0.0505792 secs] 491007K->277965K(506816K), [Metaspace: 2719K->2719K(1056768K)], 0.0507219 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
   //👆GC时间戳                             / GC原因：对象分配内存失败   / 发生GC时间                      / Yong **没有** 发生GC：DefNew（串行GC的Yong区GC）157M -> 157M, 耗时0.03ms/                   老年代发生GC：333M -> 277M(总容量349M)，耗时50ms       /总堆 491M -> 277M（总容量506M）/ Metaspace区： 2M -> 2M(总容量105M),总耗时50.7ms
   24 2020-10-25T13:52:29.707-0800: 0.981: [Full GC (Allocation Failure) 2020-10-25T13:52:29.707-0800: 0.981: [Tenured: 349506K->342045K(349568K), 0.0447672 secs] 506703K->342045K(506816K), [Metaspace: 2720    K->2720K(1056768K)], 0.0448541 secs] [Times: user=0.04 sys=0.00, real=0.05 secs] 
    ```
   * 可以看出来FullGC 的时候Yong区的内存情况是不清楚的
   * 串行GC时间较大，效率还是不高
   * 增大Xmx之后时间会更长，但是效率变高了（单位时间内干的活更多了）
   
### 并行 GC 模式运行 GCLogAnalysis
1. 执行
    ```shell script
    java -XX:+UseParallelGC -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:demo.gc.log GCLogAnalysis
    ```
2. 分析
    ```java
    6 CommandLine flags: -XX:InitialHeapSize=134217728 -XX:MaxHeapSize=2147483648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC // jdk8默认使用并行GC
    7 2020-10-25T13:07:45.516-0800: 0.115: [GC (Allocation Failure) [PSYoungGen: 33195K->5103K(38400K)] 33195K->12539K(125952K), 0.0062067 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
    //⬆️ 因为加上时间戳 -XX:+PrintGCDateStamps / GC的原因：对象分配空间失败 / YongGC: Yong区大小从33M -> 5M(总容量为38M) / 整体堆从 33M -> 12M(总容量125M) 耗时6ms / CPU时间 用户时间耗时10ms 系统 10ms 暂停时间 10ms
    8 2020-10-25T13:07:45.534-0800: 0.134: [GC (Allocation Failure) [PSYoungGen: 38383K->5119K(71680K)] 45819K->26748K(159232K), 0.0075003 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
    9 2020-10-25T13:07:45.565-0800: 0.165: [GC (Allocation Failure) [PSYoungGen: 71679K->5103K(71680K)] 93308K->47563K(159232K), 0.0105742 secs] [Times: user=0.02 sys=0.01, real=0.01 secs]
    10 2020-10-25T13:07:45.586-0800: 0.185: [GC (Allocation Failure) [PSYoungGen: 71646K->5109K(138240K)] 114107K->71078K(225792K), 0.0117564 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
    11 2020-10-25T13:07:45.598-0800: 0.197: [Full GC (Ergonomics) [PSYoungGen: 5109K->0K(138240K)] [ParOldGen: 65968K->66507K(136192K)] 71078K->66507K(274432K), [Metaspace: 2720K->2720K(1056768K)], 0.0144350 secs] [Times: user=0.02 sys=0.01, real=0.02 secs]
    //⬆️ 因为加上时间戳 -XX:+PrintGCDateStamps / Full GC的原因：自动调优 / YongGC: Yong区大小从5M -> 0M(总容量为138M) / 整体堆 65M -> 66M(总容量125M) 耗时6ms / CPU时间 用户时间耗时10ms 系统 10ms 暂停时间 10ms
    12 2020-10-25T13:07:45.670-0800: 0.270: [GC (Allocation Failure) [PSYoungGen: 132864K->5104K(138240K)] 199371K->109598K(274432K), 0.0214111 secs] [Times: user=0.02 sys=0.02, real=0.02 secs]
    13 2020-10-25T13:07:45.692-0800: 0.292: [Full GC (Ergonomics) [PSYoungGen: 5104K->0K(138240K)] [ParOldGen: 104493K->102164K(194560K)] 109598K->102164K(332800K), [Metaspace: 2720K->2720K(1056768K)], 0.0132609 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
    ```
   * 注意第一个分析的地方：Yong 区空间和堆总空间是一样大小的，这个是因为一开始的时候老年区是没有东西的，所以堆空间和Yong空间一样大小
   * 第二个注意的是：Full GC 的时候，Yong区大小直接变为0M了，全部放到了老年代，老年代的空间经过FullGC之后反而变大了，也是因为Yong区中的对象晋升到老年代
   
### CMS GC 模式运行 GCLogAnalysis
1. 执行
    ```shell script
    java -XX:+UseConcMarkSweepGC -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:demo.gc.log GCLogAnalysis
    ```
2. 分析
    ```java
   2020-10-25T14:51:46.574-0800: [GC (Allocation Failure) 2020-10-25T14:51:46.574-0800: [ParNew: 157248K->17471K(157248K), 0.0324963 secs] 317963K->221557K(506816K), 0.0325372 secs] [Times: user=0.08 sys=0.01, real=0.03 secs]
   //👆 GC时间戳                  / GC原因：对象分配内存失败                                 / Yong区使用，串行GC的多线程版本：157M -> 17M, 耗时：32ms, 总内存 317M->221M
   2020-10-25T14:51:46.607-0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 204086K(349568K)] 221903K(506816K), 0.0001658 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
   // 初始标记
   2020-10-25T14:51:46.607-0800: [CMS-concurrent-mark-start]
   // 并发标记开始
   2020-10-25T14:51:46.611-0800: [CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
   2020-10-25T14:51:46.611-0800: [CMS-concurrent-preclean-start]
   // 并发预清理
   2020-10-25T14:51:46.611-0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
   2020-10-25T14:51:46.611-0800: [CMS-concurrent-abortable-preclean-start]
   2020-10-25T14:51:46.626-0800: [GC (Allocation Failure) 2020-10-25T14:51:46.626-0800: [ParNew2020-10-25T14:51:46.712-0800: [CMS-concurrent-abortable-preclean: 0.001/0.101 secs] [Times: user=0.17 sys=0.02, real=0.10 secs]: 157247K->17472K(157248K), 0.1891631 secs] 361333K->264107K(506816K), 0.1892070 secs] [Times: user=0.32 sys=0.02, real=0.19 secs]
   //期间又发生了一次YongGC
   2020-10-25T14:51:46.815-0800: [GC (CMS Final Remark) [YG occupancy: 17616 K (157248 K)]2020-10-25T14:51:46.815-0800: [Rescan (parallel) , 0.0015473 secs]2020-10-25T14:51:46.817-0800: [weak refs processing, 0.0000221 secs]2020-10-25T14:51:46.817-0800: [class unloading, 0.0003680 secs]2020-10-25T14:51:46.817-0800: [scrub symbol table, 0.0004437 secs]2020-10-25T14:51:46.818-0800: [scrub string table, 0.0001570 secs][1 CMS-remark: 246635K(349568K)] 264251K(506816K), 0.0026044 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
   //最终标记
   2020-10-25T14:51:46.818-0800: [CMS-concurrent-sweep-start]
   //并发清理
   2020-10-25T14:51:46.819-0800: [CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
   2020-10-25T14:51:46.819-0800: [CMS-concurrent-reset-start]
   //并发重置
   2020-10-25T14:51:46.819-0800: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    ```
   * 并发时候线程数占总线程数的1/4，CMS默认启动的回收线程数是（ParallelGCThreads+3）/4
   * 日志刚好对应CMS GC工作的几个阶段

### G1 GC 模式运行 GCLogAnalysis
1. 执行
    ```shell script
    java -XX:+UseG1GC -Xmx1g -Xms1g -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:demo.gc.log GCLogAnalysis
    ```
2. 分析
    ```java
   2020-10-25T23:16:23.555-0800: [GC pause (G1 Evacuation Pause) (young) 687M->409M(1024M), 0.0348857 secs]
   // 👆 GC发生的时间              / GC 停顿 - 疏散停顿（Evacuation Pause）是将活着的对象从一个区域（young or young + old）拷贝到另一个区域的阶段/ yong 代表发生了 Yong GC： 687M -> 409M, 耗时34ms
   2020-10-25T23:16:23.615-0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 553M->444M(1024M), 0.0135810 secs]
   2020-10-25T23:16:23.629-0800: [GC concurrent-root-region-scan-start]
   2020-10-25T23:16:23.629-0800: [GC concurrent-root-region-scan-end, 0.0001752 secs]
   2020-10-25T23:16:23.629-0800: [GC concurrent-mark-start]
   2020-10-25T23:16:23.635-0800: [GC concurrent-mark-end, 0.0052710 secs]
   2020-10-25T23:16:23.635-0800: [GC remark, 0.0015366 secs]
   2020-10-25T23:16:23.637-0800: [GC cleanup 463M->449M(1024M), 0.0009538 secs]
   2020-10-25T23:16:23.638-0800: [GC concurrent-cleanup-start]
   2020-10-25T23:16:23.638-0800: [GC concurrent-cleanup-end, 0.0000248 secs]
   2020-10-25T23:16:23.738-0800: [GC pause (G1 Evacuation Pause) (young)-- 854M->673M(1024M), 0.0253308 secs]
   2020-10-25T23:16:23.764-0800: [GC pause (G1 Evacuation Pause) (mixed) 680M->590M(1024M), 0.0112053 secs]
   2020-10-25T23:16:23.785-0800: [GC pause (G1 Evacuation Pause) (mixed) 652M->603M(1024M), 0.0041611 secs]
    ```
   * G1 GC日志比较复杂，但日志也是符合几个工作阶段的