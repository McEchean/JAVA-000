package com.github.zibuyu28;

import java.util.HashMap;

public class KmqMessage<T> {

    private HashMap<String,Object> headers;

    private T body;

    public HashMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public KmqMessage(HashMap<String, Object> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        return "KmqMessage{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
