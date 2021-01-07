学习笔记

1. redis.yaml -> redis主从+哨兵模式
    * 偷懒使用了docker的方式，镜像使用了bitnami的redis，一些初始化配置只需要使用环境变量即可，看上去很简洁
    * 也使用过redis的官方镜像，只不过配置主从的时候需要在容器启动之后，进入容器执行 `slaveof redis0 6379` 这个命令即可
    * 哨兵模式配置Jedis的时候有一个配置 setTestOnCreate = true
        * 开始的时候因为是 docker 模式配置了 docker 内的网络，sentinel 获取到的主从的 ip 是 docker 内网ip，导致无法联通，查了报错还以为是这个配置的原因，后来更改了sentinel链接的host，就可以了
        * 查了一下原因：如果设置为true就需要将redis和你的程序放到同一台机器上或者同一局域网上面或者关闭该模式。因为是docker的原因导致这个测试不通
   
    
2. redis-cluster
    * 配置一个节点得配置文件
    ```shell script
    # 节点端口
    port 6380
    #日志文件
    logfile "log/redis-6380.log"
    # 开启集群模式
    cluster-enabled yes
    # 集群配置文件
    cluster-config-file "data/nodes-6380.conf"
    ```
    * 配置完成之后建好文件夹 log, data
    * 启动脚本 start.sh
    ```shell script
    redis-server conf/redis-6380.conf &
    redis-server conf/redis-6381.conf &
    redis-server conf/redis-6382.conf &
    redis-server conf/redis-6383.conf &
    redis-server conf/redis-6384.conf &
    redis-server conf/redis-6385.conf &
    ```
   * 停止脚本 stop.sh
   ```shell script
    ps -ef|grep redis-server|grep -v grep|awk '{print $2}'|xargs kill -9
    rm -rf log/*
    rm -rf data/*
   ```
   * 启动之后需要配置握手，meet命令  
        * 进入第一个节点执行 `cluster meet 127.0.0.1 6381`
        * 依次执行 6382 6383 6384 6385
   * 集群模式至少三个节点，还有三个节点配置主从
        * 使用命令 `cluster replicate 主节点ID`
   * 分配16383个槽位
     * 命令 `redis-cli -h 127.0.0.1  -p 6380 cluster addslots {0..5461}`
     * 命令 `redis-cli -h 127.0.0.1  -p 6382 cluster addslots {5462..10922}`
     * 命令 `redis-cli -h 127.0.0.1  -p 6384 cluster addslots {10923..16383}`
     
   * 测试得时候redis-cli 一定要加上参数 -c， 不然测试会报错类似： `(error) MOVED 5798 127.0.0.1:7001`
3. 一种更快速得部署方案，使用redis-cli
    * 首先启动 6 个 redis节点
    * 然后执行命令
    ```shell script
    redis-cli --cluster create 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 127.0.0.1:6385 --cluster-replicas 1
    ```
    * 搭建完成