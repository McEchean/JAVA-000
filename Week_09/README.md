学习笔记

1. 使用netty这个需求写了比较久，client代码基本是之前的网关作业的，但是请求本地一直报400，增加了请求头还是不行，这个需要在研究一下,作业里面有一个网上参考的nettyclient，要做对比
2. AOP替换动态代理，这个不知道要做啥，动态代理不就是实现AOP的方式吗，有点奇怪，然后这里我根据自己的理解，使用ByteBuddy写了一个生成接口代理类的方式去实现这个代理过程，实现的方式很奇葩。需要看看大家怎么做的

3. 还有一题需要等有时间了在补上了
    * 2020.12.20作业补上
4. 使用dubbo和hmily
    * dubbo在本地启动消费者的时候需要指定qos的port，不然默认的端口和服务提供者端口冲突
    * 起初exchange项目配置了application.yml,启动之后配置无法读取，后来改成 application.yaml文件就可以读取
        * 查了原因是因为配置了`<packaging>pom</packaging>`导致的，把这个改成`<packaging>jar</packaging>`即可，因为子模块直接继承了父模块的这个属性
    * mybatis 无法执行：sql中变量映射方式写错 `{#user_id}` -> `#{user_id}`
    * 注意余额大于等于零判断，发现hmily里面sql的条件是`balance > 0`这个会有问题，会导致出现负值现象，应改为 `balance >= ?`即可