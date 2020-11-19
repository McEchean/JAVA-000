package com.github.zibuyu28.outbound.okhttp;

import com.github.zibuyu28.outbound.HttpOutboundHandler;
import com.github.zibuyu28.outbound.NamedThreadFactory;
import com.github.zibuyu28.router.HttpEndpointRouter;
import com.github.zibuyu28.router.RandomEndpointRouter;
import com.github.zibuyu28.router.RouterFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import okhttp3.*;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
@Lazy
public class OkHttpClientOutboundHandler implements HttpOutboundHandler {

    private final static Logger log = LoggerFactory.getLogger(OkHttpClientOutboundHandler.class);

    private static class F {
        private static OkHttpClientOutboundHandler INSTANCE = new OkHttpClientOutboundHandler();
    }

    private OkHttpClient okClient;


    private ThreadPoolExecutor executor;

    @Autowired
    private RouterFactory routerFactory;

    @PostConstruct
    public void init() {
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        int timeout = 1000;
        int queueSize = 1024;

        this.executor = new ThreadPoolExecutor(
                cores, cores, timeout, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("okhttp-client"),
                new ThreadPoolExecutor.AbortPolicy());

        this.okClient = new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS).build();
    }

    public static OkHttpClientOutboundHandler getInstance() {
        return F.INSTANCE;
    }


    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws Exception {
        log.debug("req uri : {}", fullRequest.uri());

        final String url = availableEndpoint(fullRequest.uri());

        executor.submit(() -> reqToEndpoint(fullRequest,ctx, url));

    }

    private void reqToEndpoint(final FullHttpRequest fullRequest,  final ChannelHandlerContext ctx, final String url) {
        HttpHeaders headers = fullRequest.headers();

        Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .get();
        headers.forEach(item -> reqBuilder.header(item.getKey(), item.getValue()));

        Call call = okClient.newCall(reqBuilder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异步请求失败之后的回调
                onFailedResponse(ctx, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //异步请求成功之后的回调
                if(response.isSuccessful()) {
                    onSuccessResponse(ctx, response);
                }else {
                    onFailedResponse(ctx, new HttpResponseException(response.code(), "response is not success"));
                }
            }
        });
    }

    private void onSuccessResponse(final ChannelHandlerContext ctx, final Response resp) {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(Objects.requireNonNull(resp.body()).bytes()));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(Objects.requireNonNull(resp.header("Content-Length", "0"))));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                if (!HttpUtil.isKeepAlive(response)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
        }

    }

    private void onFailedResponse(final ChannelHandlerContext ctx, Exception e) {
        log.error("failed response, e : {}", e.getMessage());
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(e.getMessage().getBytes()));
        response.headers().setInt("Content-Length", e.getMessage().getBytes().length);
        if (!HttpUtil.isKeepAlive(response)) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.write(response);
        }
        ctx.flush();
    }



    private String availableEndpoint(final String uri) throws Exception {
        String url = routerFactory.getRouter().getEndPoint() + uri;
        if(!url.startsWith("http")) {
            url = "http://" + url;
        }
        return  url;
    }
}
