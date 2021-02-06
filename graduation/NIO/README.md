### NIO
* Socket了解
* 几种IO模型
* Netty原理

---

### Socket
* socket：是一系列编程接口，用来可以让程序员方便的使用tcp/ip协议
* http：是一种通信协议，位于五层模型的应用层
* tcp/ip：和http一样都是协议， 位于五层模型的传输层

### 几种IO模型的演进
* 阻塞式IO：进入调用之后直接阻塞等待数据准备完全，再继续处理数据
* 非阻塞式IO：进入调用之后直接返回，但是需要定时向内核询问数据是否准备好，当询问到数据准备好则进入下一阶段
* IO复用：轮询这个工作交给内核（select，poll，epoll），可以同时监听多个IO，当其中任何一个IO数据准备好了就通知用户可以来处理数据了
* 信号驱动IO：用的较少
* 异步IO：用的较少

### Netty原理
* 核心对象
    * B: BootStrap
    * E: EventLoop
    * C: Channel
    * H: Handler
* 一个channel就是一个连接
* Boss Eventloop负责 ACCEPT 事件，他在启动的时候向 Selector 注册 ACCPET 事件和 ServerChannel，并且绑定端口
* Work EventloopGroup 是一组 Work Eventloop 的集合，数量默认为 CPU 核心数 *2
* Work Eventloop 本质上是一个线程，他有一个死循环，执行两种事件：READ事件和自己任务队列上面的事件
* Work Eventloop 的任务队列上面的事件是可以在任何地方提交加入（通过 Future/Promise 的 addListener 方法提交任务）
* Channel 是和用户连接的桥梁，往 Channel 写数据就是往用户电脑写数据
* Pipeline 处理时可以选择同步模式还是异步模式，但无论如何最终都要刷写 Buffer ，最后要结束数据流