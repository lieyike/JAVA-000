package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

    private final OkHttpClient okHttpClient;
    private final String backendUrl;
    private final ExecutorService proxyService;

    public OkhttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(2, TimeUnit.SECONDS);

        okHttpClient = builder.build();
    }

    public void handle(final FullHttpRequest fullHttpRequest, final ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullHttpRequest.uri();
        proxyService.submit(() -> fetchGet(fullHttpRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        FullHttpResponse httpResponse = null;
        try {
            Request.Builder builder = new Request.Builder();

            for (Map.Entry<String, String> entry : fullRequest.headers()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }



            builder.url(url);
            Request request = builder.build();
            Response okhttpResponse = okHttpClient.newCall(request).execute();

            byte[] body = Objects.requireNonNull(okhttpResponse.body()).bytes();

            httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            httpResponse.headers().set("Content-Type", "application/json");
            httpResponse.headers().setInt("Content-Length", Integer.parseInt(Objects.requireNonNull(okhttpResponse.header("Content-Length"))));

        } catch (Exception e) {
            e.printStackTrace();
            httpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            ctx.close();
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(httpResponse);
                }
            }
            ctx.flush();
        }
    }
}
