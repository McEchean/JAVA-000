### MySQL
* 事务
* 索引
* 分布式事务
---

#### 事务
* 事务可靠性模型 ACID
    * 原子性
    * 一致性
    * 隔离性
    * 持久性
* 日志
    * undo log: 用于保证事物的原子性，对数据直接产生操作的时候，生成一条和操作相反的log用于回滚
    * redo log: 为了保证事物持久性
* 写日志流程
    1. 事物开始
    2. 记录undo log 至 buffer
    3. 记录redo log至buffer
    4. 内存中更改数据
    5. commit，触发redo log刷盘
    6. 记录bin log
    7. 事物结束
    
#### 索引
* hash索引
* b+树索引
* 索引区分度要高
* 聚簇索引：行数据和索引在一起

#### 分布式事务
* XA：需要事务管理器，对业务入侵很小
    - xa_start: 负责开启或者恢复一个事务分支
    - xa_end: 负责取消当前线程与事务分支的关联
    - xa_prepare: 询问RM（资源管理器）是否准备好提交事务分支
    - xa_commit: 通知RM（资源管理器）提交事务分支
    - xa_rollback: 通知RM（资源管理器）回滚事务分支
    - xa_recover: 需要恢复的XA事务
    
* TCC：对业务入侵很大
    * try
    * commit
    * cancel