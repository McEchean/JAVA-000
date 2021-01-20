package com.github.zibuyu28;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class Kmq<T> {

    private final String topic;

    private Integer capacity;

    private KmqMessage<?>[] queue;


    private AtomicInteger index;

    private AtomicInteger canReadAt;

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity < 0 ? 16 : capacity];
        this.index = new AtomicInteger(-1);
        this.canReadAt = new AtomicInteger(-1);
    }

    public String getTopic() {
        return this.topic;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean send(KmqMessage<T> message) {
        if (this.index.incrementAndGet() >= this.capacity) {
            synchronized (this) {
                this.capacity = this.capacity * 2;
                this.queue = Arrays.copyOf(this.queue, this.capacity);
            }
        }
        this.queue[this.index.get()] = message;
        this.canReadAt.incrementAndGet();
        return true;
    }

//    @SuppressWarnings("unchecked")
//    public KmqMessage<T> poll() {
//        if (this.readAt.get() < this.canReadAt.get()) {
//            final int i = this.readAt.incrementAndGet();
//            synchronized (this) {
//                return (KmqMessage<T>) this.queue[i];
//            }
//        }
//        return null;
//    }

    @SuppressWarnings("unchecked")
    public KmqMessage<T> pollWithIndex(Integer index) {
        final int i = this.canReadAt.get();
        if (i >= 0 && index >= 0 && index < i) {
            synchronized (this) {
                return (KmqMessage<T>) this.queue[index];
            }
        }
        return null;
    }
//
//    @SuppressWarnings("unchecked")
//    public KmqMessage<T> poll(long timeout) throws InterruptedException {
//        long start = System.currentTimeMillis();
//        while (canReadAt.get() == -1 || readAt.get() >= canReadAt.get()) {
//            if (System.currentTimeMillis() >= start + timeout) break;
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw e;
//            }
//        }
//        if (System.currentTimeMillis() >= start + timeout)
//            return null;
//        final int i = this.readAt.incrementAndGet();
//        synchronized (this) {
//            return (KmqMessage<T>) this.queue[i];
//        }
//    }
}
