学习笔记

1. redis.yaml -> redis主从+哨兵模式
    * 偷懒使用了docker的方式，镜像使用了bitnami的redis，一些初始化配置只需要使用环境变量即可，看上去很简洁
    * 也使用过redis的官方镜像，只不过配置主从的时候需要在容器启动之后，进入容器执行 `slaveof redis0 6379` 这个命令即可
    * 哨兵模式配置Jedis的时候有一个配置 setTestOnCreate = true
        * 开始的时候因为是 docker 模式配置了 docker 内的网络，sentinel 获取到的主从的 ip 是 docker 内网ip，导致无法联通，查了报错还以为是这个配置的原因，后来更改了sentinel链接的host，就可以了
        * 查了一下原因：如果设置为true就需要将redis和你的程序放到同一台机器上或者同一局域网上面或者关闭该模式。因为是docker的原因导致这个测试不通
   
    
2. redis-cluster -> 还没有尝试