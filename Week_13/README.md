学习笔记

* kafka集群使用docker搭建，有两个参数要注意
    * KAFKA_LISTENERS：broker监听地址
    * KAFKA_ADVERTISED_LISTENERS：broker注册到zk让用户访问的地址
    * 关于这两个配置项的问题参考了[https://www.jianshu.com/p/26495e334613](https://www.jianshu.com/p/26495e334613)
