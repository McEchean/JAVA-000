package com.github.zibuyu28.router.endpoint;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.github.zibuyu28.util.Prop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdConfigEndpointsProvider implements EndpointsProvider {
    private static final Logger log = LoggerFactory.getLogger(EtcdConfigEndpointsProvider.class);

    public static class F {
        private static EtcdConfigEndpointsProvider INSTANCE = new EtcdConfigEndpointsProvider();
    }

    public static EtcdConfigEndpointsProvider getInstance() {
        return F.INSTANCE;
    }

    private final Client etcdClient;
    private final CopyOnWriteArrayList<String> availableEndpointList;

    private EtcdConfigEndpointsProvider() {
        this.etcdClient = Client.builder()
                .endpoints(Prop.getOrDefault("router.endpoints.etcd.hosts","http://127.0.0.1:2379").split(","))
                .build();
        this.availableEndpointList = new CopyOnWriteArrayList<>();
        try {
            String endpointKey = Prop.getOrDefault("router.endpoints.etcd.configKey");
            List<KeyValue> kvs = this.etcdClient
                    .getKVClient()
                    .get(ByteSequence.fromString(endpointKey)).get().getKvs();
            if(kvs.size() > 1) {
                throw new RuntimeException(String.format("key(%s) not one value", endpointKey));
            }
            kvs.forEach(keyValue -> {
                String value = keyValue.getValue().toStringUtf8();
                List<String> collect = Arrays.stream(value.split(",")).collect(Collectors.toList());
                availableEndpointList.addAll(collect);
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.debug("etcd start error");
        }
        new Thread(() -> {
            Watch.Watcher watcher = this.etcdClient
                    .getWatchClient()
                    .watch(ByteSequence.fromString(Prop.getOrDefault("router.endpoints.etcd.configKey","endpoints-key")));
            try {
                while(true) {
                    watcher.listen().getEvents().forEach(watchEvent -> {
                        KeyValue kv = watchEvent.getKeyValue();
                        log.info("etcd: key({}) changed, event({}) new value({})",
                                kv.getKey().toStringUtf8(), watchEvent.getEventType(), kv.getValue().toStringUtf8());
                        String afterChangeValue = kv.getValue().toStringUtf8();
                        List<String> collect = Arrays.stream(afterChangeValue.split(",")).collect(Collectors.toList());
                        availableEndpointList.addAll(collect);
                        availableEndpointList.retainAll(collect);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public List<String> availableEndpoints() throws Exception {
        return availableEndpointList;
    }
}
