package com.github.zibuyu28;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class KmqBroker<T> { // Broker+Connection

    public static final int CAPACITY = 10000;

    private final Map<String, Kmq<T>> kmqMap = new ConcurrentHashMap<>(64);

    private final ConcurrentHashMap<KmqConsumer<T>, Map<String, Integer>> offsetMap = new ConcurrentHashMap<>();

    public void createTopic(String name) {
        kmqMap.putIfAbsent(name, new Kmq<>(name, CAPACITY));
    }

    public Kmq<T> findKmq(String topic) {
        return this.kmqMap.get(topic);
    }

    public KmqMessage<T> poll(KmqConsumer<T> consumer, String topic) {
        final Kmq<T> kmq = findKmq(topic);
        if (kmq == null) return null;
        final Map<String, Integer> consumerTopicOffsetMap = this.offsetMap.get(consumer);
        if(consumerTopicOffsetMap == null) {
            final KmqMessage<T> tKmqMessage = kmq.pollWithIndex(0);
            if(tKmqMessage != null) {
                ConcurrentHashMap<String, Integer> m = new ConcurrentHashMap<>();
                m.put(topic, 0);
                this.offsetMap.put(consumer, m);
            }
            return tKmqMessage;
        }
        final Integer index = consumerTopicOffsetMap.get(topic);
        if(index == null) {
            final KmqMessage<T> tKmqMessage = kmq.pollWithIndex(0);
            if(tKmqMessage != null) {
                consumerTopicOffsetMap.put(topic, 0);
            }
            return tKmqMessage;
        } else {
            final KmqMessage<T> tKmqMessage = kmq.pollWithIndex(index + 1);
            if(tKmqMessage != null) {
                consumerTopicOffsetMap.put(topic, index+1);
            }
            return tKmqMessage;
        }
    }

    public KmqProducer<T> createProducer() {
        return new KmqProducer<>(this);
    }

    public KmqConsumer<T> createConsumer() {
        return new KmqConsumer<>(this);
    }

}
