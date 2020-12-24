package io.kimmking.rpcfx.client.codec;

public interface Codec<T,A,B> {

    public T encode(A o);

    public B decode(T o);
}
