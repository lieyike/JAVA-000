package io.github.kimmking.gateway.router;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.Random;

public class HttpEndpointRouterImpl extends ChannelInboundHandlerAdapter implements HttpEndpointRouter{

    private List<String> endpoints;

    private Random random;

    public HttpEndpointRouterImpl(List<String> endpoints) {
        this.endpoints = endpoints;
        random = new Random();
    }

    @Override
    public String route(List<String> endpoints) {
        int i = random.nextInt(endpoints.size());
        return endpoints.get(i);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        String uri = route(endpoints) + "/" + fullRequest.uri();
        fullRequest.setUri(uri);
        ctx.fireChannelRead(msg);
    }
}
