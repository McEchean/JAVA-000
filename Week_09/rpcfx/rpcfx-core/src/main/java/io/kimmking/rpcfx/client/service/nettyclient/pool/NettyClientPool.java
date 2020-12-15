package io.kimmking.rpcfx.client.service.nettyclient.pool;

import io.kimmking.rpcfx.client.service.nettyclient.NettyClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.EvictionConfig;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.NoSuchElementException;

@Slf4j
public class NettyClientPool {
    protected GenericObjectPool<NettyClient> internalPool;


    public NettyClient getResource() {
        try {
            return internalPool.borrowObject();
        } catch (NoSuchElementException nse) {
            throw new RuntimeException("Could not get a resource from the pool", nse);
        } catch (Exception e) {
            throw new RuntimeException("Could not get a resource from the pool", e);
        }
    }

    public void returnResourceObject(final NettyClient resource) {
        if (resource == null) {
            return;
        }
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new RuntimeException("Could not return the resource to the pool", e);
        }
    }

    public NettyClientPool(final GenericObjectPoolConfig poolConfig,
                           PooledObjectFactory<NettyClient> factory) {
        initPool(poolConfig, factory);
    }



    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<NettyClient> factory) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }


        this.internalPool = new GenericObjectPool<NettyClient>(factory, poolConfig);
        try {
            this.internalPool.preparePool();
        } catch (Exception e) {
            log.error("preparePool failed:{}",e);
        }
        log.info("preparePool success");
    }



    protected void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not destroy the pool", e);
        }
    }

    public static class NoopEvictionPolicy implements EvictionPolicy {

        @Override
        public boolean evict(EvictionConfig config, PooledObject underTest, int idleCount) {
            return false;
        }
    }

}
