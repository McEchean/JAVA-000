学习笔记

1. 基本实现了三种outboundHandler
    * httpclient4 : 这个主要是老师实现的，自己稍微做了一些无关紧要的改动
    * okHttp : 主要流程也是参考老师的代码实现的
    * nettyclient : 这个实现也是差不多的，但是在实现的时候遇到了一些坑，最后的sync加了之后会一直block，这个是因为自己对netty不理解导致的
2. 实现了一个很简单的filter
    * 增加了一个header -> nio : hello filter
    
3. 实现了随机的router

4. 实现了两种后端服务endpoint提供者
    * default : 主要通过默认的配置来读取endpoint
    * etcd : 通过etcd来实时更新endpoint，简单实现了功能