package io.kimmking.rpcfx.client.invoke.nettyclient.pool;

import org.apache.commons.pool2.impl.BaseGenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class NettyClientPoolConfig extends GenericObjectPoolConfig {
    public NettyClientPoolConfig() {
        /**
         * 如果暂时取不到资源，等2s
         */
        setMaxWaitMillis(2000);
        // defaults to make your life with connection pool easier :)
        setTestWhileIdle(false);
        setMinEvictableIdleTimeMillis(60000);
//        setTimeBetweenEvictionRunsMillis(30000);
        /**
         * 生效的地方见如下处代码：
         * {@link BaseGenericObjectPool#setTimeBetweenEvictionRunsMillis(long)}
         * 默认值为-1，不执行清理idle对象的定时任务
         */
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
        setMaxTotal(2);
        setMinIdle(2);
        /**
         * 检查下我们的client的连接是不是正常的，此时会回调我们的
         * {@link NettyClientFactory#validateObject(org.apache.commons.pool2.PooledObject)}
         */
        setTestOnBorrow(true);
        setEvictionPolicyClassName("NettyClientPool$NoopEvictionPolicy");
    }

    public static final NettyClientPoolConfig DEFAULT = new NettyClientPoolConfig();
}
