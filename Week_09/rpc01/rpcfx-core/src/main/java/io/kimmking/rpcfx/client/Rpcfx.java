package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HTTP;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    private static CloseableHttpAsyncClient httpclient = createHttpClient();

    private static CloseableHttpAsyncClient createHttpClient() {
        CloseableHttpAsyncClient  httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
        return httpclient;
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(serviceClass);
        proxyFactory.addAdvice(new MethodInterceptor() {
            public final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                try {
                    RpcfxRequest request = new RpcfxRequest();
                    request.setServiceClass(serviceClass.getName());
                    request.setMethod(methodInvocation.getMethod().getName());
                    request.setParams(methodInvocation.getArguments());

                    RpcfxResponse response = post(request, url);

                    if(response.isStatus()) {
                        // 这里判断response.status，处理异常
                        // 考虑封装一个全局的RpcfxException
                        return JSON.parse(response.getResult().toString());
                    } else {
                        throw new RpcfxException("Failed to call server");
                    }

                } catch (Exception e) {
                    throw new RpcfxException(e);
                }
            }

            private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
                String reqJson = JSON.toJSONString(req);
                System.out.println("req json: " + reqJson);

                // 1.可以复用client
                // 2.尝试使用httpclient或者netty client
                final HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                StringEntity stringEntity = new StringEntity(reqJson, ContentType.APPLICATION_JSON);
                httpPost.setEntity(stringEntity);
                AtomicReference<HttpResponse> atomicReference = new AtomicReference<>();
                CountDownLatch countDownLatch = new CountDownLatch(1);
                httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(final HttpResponse endpointResponse) {
                        try {
                            atomicReference.set(endpointResponse);
                            countDownLatch.countDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }

                    @Override
                    public void failed(final Exception ex) {
                        httpPost.abort();
                        ex.printStackTrace();
                    }

                    @Override
                    public void cancelled() {
                        httpPost.abort();
                    }
                });
                try {
                    countDownLatch.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpResponse httpResponse = atomicReference.get();

                String result = new BufferedReader(new InputStreamReader( httpResponse.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
//                OkHttpClient client = new OkHttpClient();
//                final Request request = new Request.Builder()
//                        .url(url)
//                        .post(RequestBody.create(JSONTYPE, reqJson))
//                        .build();
//                String respJson = client.newCall(request).execute().body().string();
                System.out.println("resp json: " + result);
                return JSON.parseObject(result, RpcfxResponse.class);
            }
        });

        return (T) proxyFactory.getProxy();
    }

//     不是太理解这里AOP的意思切点是在调用接口实现的方法??
//     还是切入接口在UserService findId() 在那里设置
//
//        return proxyFactory.getProxy();
//        // 0. 替换动态代理 -> AOP
//        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));


    public static class RpcfxInvocationHandler implements InvocationHandler {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            RpcfxResponse response = post(request, url);

            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException

            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: " + reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();
            System.out.println("resp json: " + respJson);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }
    }
}
