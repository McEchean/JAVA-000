package io.kimmking.rpcfx.client;

public interface Client {
    <T> T create(final Class<T> t, final String host, final Integer port);
}
