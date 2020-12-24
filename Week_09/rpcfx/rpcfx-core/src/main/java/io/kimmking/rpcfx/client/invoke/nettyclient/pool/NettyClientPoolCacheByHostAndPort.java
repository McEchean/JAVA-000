package io.kimmking.rpcfx.client.invoke.nettyclient.pool;

import io.kimmking.rpcfx.client.invoke.nettyclient.vo.HostAndPortConfig;
import io.kimmking.rpcfx.client.invoke.nettyclient.utils.ModuleUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class NettyClientPoolCacheByHostAndPort {
    private static final Map<String, NettyClientPool> nodes = new HashMap<String, NettyClientPool>();


    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final Lock r = rwl.readLock();
    private static final Lock w = rwl.writeLock();




    public static NettyClientPool getOrCreateNettyClientPoolIfNecessay(HostAndPortConfig hostAndPortConfig,
                                                                       NettyClientPoolConfig nettyClientPoolConfig) {
        w.lock();
        try {
            String nodeKey = getNodeKey(hostAndPortConfig);
            NettyClientPool existingPool = nodes.get(nodeKey);
            if (existingPool != null){
                return existingPool;
            }

            NettyClientFactory factory = new NettyClientFactory(hostAndPortConfig);
            NettyClientPool nodePool = new NettyClientPool(nettyClientPoolConfig,factory);
            nodes.put(nodeKey, nodePool);
            return nodePool;
        } finally {
            w.unlock();
        }
    }
    /**
     * 创建连接池
     * @param hostAndPortConfig
     * @return
     */
    public static NettyClientPool getOrCreateNettyClientPoolIfNecessay(HostAndPortConfig hostAndPortConfig) {
        return getOrCreateNettyClientPoolIfNecessay(hostAndPortConfig, NettyClientPoolConfig.DEFAULT);
    }


    public static String getNodeKey(HostAndPortConfig hnp) {
        return hnp.getHost() + ":" + hnp.getPort();
    }

    public static void init(HostAndPortConfig hostAndPortConfig) {
        log.info("init connection pool between localhost with {}",hostAndPortConfig);
        getOrCreateNettyClientPoolIfNecessay(hostAndPortConfig);
        log.info("succeed to init connection pool between localhost with {}",hostAndPortConfig);
    }

    /**
     * 重载函数
     * @param url
     */
    public static void init(String url) {
        String[] ipAndPort = ModuleUtils.extractIpAndPort(url);
        if (ipAndPort == null) {
            log.warn("failed to extractIpAndPort from url:{}",url);
            return;
        }

        HostAndPortConfig config = new HostAndPortConfig(ipAndPort[0], Integer.valueOf(ipAndPort[1]));
        log.info("init connection pool between localhost with {}", config);
        getOrCreateNettyClientPoolIfNecessay(config);
        log.info("succeed to init connection pool between localhost with {}",config);
    }
}
