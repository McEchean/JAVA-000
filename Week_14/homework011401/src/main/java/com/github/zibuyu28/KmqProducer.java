package com.github.zibuyu28;

public class KmqProducer<T> {

    private final KmqBroker<T> broker;

    public KmqProducer(KmqBroker<T> broker) {
        this.broker = broker;
    }

    public boolean send(String topic, KmqMessage<T> message) {
        Kmq<T> kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        return kmq.send(message);
    }
}
