package com.github.zibuyu28;

public class KmqConsumer<T> {

    private final KmqBroker<T> broker;

    private String topic;

    public KmqConsumer(KmqBroker<T> broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        final Kmq<T> kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        this.topic = topic;
    }

    public KmqMessage<T> poll() {
        return this.broker.poll(this, this.topic);
    }

}
