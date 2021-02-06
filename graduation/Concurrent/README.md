### 并发
* 线程状态,Thread类的方法
* 线程通信
* 线程安全
---

#### 线程状态
* 一共七种状态：NEW,READY,RUNNING,TIMED_WAIT,WAIT,BLOCK,TERMINATED
* 状态改变：
    * Thread.sleep(): 不释放锁，释放CPU，RUNNING -> READY
    * Thread.yield(): 不释放锁，RUNNING -> READY
    * t.join(): 当前线程不释放锁，**但是会释放 t 这个对象的锁**，具体可以看join方法的实现，它会调用wait方法
    * notify(): 需要配合 synchronized, wait
    
    
#### 线程通信
* notify，wait
* 可继承的ThreadLocal + 对象（？？？）

### 线程安全
* synchronized
* 最大化使用final
* volatile：能不用就不用