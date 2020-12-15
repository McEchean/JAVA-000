package io.kimmking.rpcfx.client.service.nettyclient.pool;

import io.kimmking.rpcfx.client.service.nettyclient.NettyClient;
import io.kimmking.rpcfx.client.service.nettyclient.vo.HostAndPortConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 对象池的工厂
 */
@Slf4j
public class NettyClientFactory implements PooledObjectFactory<NettyClient> {
    private HostAndPortConfig hostAndPortConfig;

    public NettyClientFactory(HostAndPortConfig hostAndPortConfig) {
        this.hostAndPortConfig = hostAndPortConfig;
    }

    @Override
    public PooledObject<NettyClient> makeObject() throws Exception {
        NettyClient nettyClient = new NettyClient(hostAndPortConfig);
        try {
            nettyClient.initConnection();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        if (!nettyClient.isbIsConnectionOk()) {
            log.error("new nettyClient failed.hostAndPortConfig:{}", hostAndPortConfig);
            throw new RuntimeException("连接失败");
        }
        log.info("makeObject,thread:{}",Thread.currentThread().getName());

        return new DefaultPooledObject<NettyClient>(nettyClient);
    }

    @Override
    public void destroyObject(PooledObject<NettyClient> pooledObject) throws Exception {

        log.info("destroyObject,thread:{}",Thread.currentThread().getName());
        NettyClient nettyClient = pooledObject.getObject();
        nettyClient.close();
    }

    /**
     * 会自动重连，我们总是认为有效
     * 具体的逻辑在：{@link GenericObjectPool#borrowObject(long)}
     * 里面会调用validateObject方法，如果返回false，就会调用destroyObject去干掉我们的对象
     *
     * 但是获取对象不会直接失败，会继续while循环，拿到下一个object，继续这个步骤
     * @param pooledObject
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<NettyClient> pooledObject) {
        log.info("validateObject,thread:{}",Thread.currentThread().getName());
        NettyClient nettyClient = pooledObject.getObject();
        if (nettyClient.isbIsConnectionOk()) {
            return true;
        }

        return false;
    }

    @Override
    public void activateObject(PooledObject<NettyClient> pooledObject) throws Exception {
        log.info("activateObject,thread:{}",Thread.currentThread().getName());

    }

    @Override
    public void passivateObject(PooledObject<NettyClient> pooledObject) throws Exception {
        log.info("passivateObject,thread:{}",Thread.currentThread().getName());
    }
}
