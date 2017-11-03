package com.summer.developmodule.http.httputil;

import android.util.Log;


import com.summer.developmodule.base.AppContext;
import com.summer.developmodule.util.AppUtil;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.summer.developmodule.util.AppUtil.checkNet;


/**
 * 拦截器和设置配置信息，设置缓存
 * Created by yujunlong on 2016/12/27.
 */

public class OkHttpUtil {
    /**
     * 拦截器
     * @param type
     * 0 Log信息拦截器 1 缓存机制拦截器 2 公共参数拦截器 3 设置头拦截器
     * @return
     */
    public static Interceptor getInterceptor(int type){
        Interceptor mInterceptor = null;
        switch (type) {
            case 0:
                //Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("OKHTTP", message);
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                mInterceptor = loggingInterceptor;
                break;
            case 1:
                //缓存机制拦截器
                mInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!checkNet(AppContext.context())) {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        if (checkNet(AppContext.context())) {
                            int maxAge = 0;
                            // 有网络时 设置缓存超时时间0个小时
                            response.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                    .build();
                        } else {
                            // 无网络时，设置超时为4周
                            int maxStale = 60 * 60 * 24 * 28;
                            response.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .removeHeader("Pragma")
                                    .build();
                        }
                        return response;
                    }
                };
                break;
            case 2:
                //公共参数拦截器
                mInterceptor =   new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request request;
                    String method = originalRequest.method();
                    Headers headers = originalRequest.headers();
                    HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                            // Provide your custom parameter here
                            .addQueryParameter("token", "")
                            .addQueryParameter("userId", "")
                            .build();
                    request = originalRequest.newBuilder().url(modifiedUrl).build();
                    return chain.proceed(request);
                }
            };
                break;
            case 3:
                //设置头拦截器
                mInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .header("AppType", "TPOS")
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .method(originalRequest.method(), originalRequest.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                };
                break;
            case 4:
                //设置url路径拦截器 会拼接在baseurl的后面
                mInterceptor =   new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request request;
                        String method = originalRequest.method();
                        Headers headers = originalRequest.headers();
                        HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                                // Provide your custom parameter here
                                .addPathSegment("api")
                                .addPathSegment("v"+ AppUtil.getCurrentVersion(AppContext.context()).versionCode+"")
                                .addPathSegment("hwxw")
                                .build();
                        request = originalRequest.newBuilder().url(modifiedUrl).build();
                        return chain.proceed(request);
                    }
                };
                break;
        }
        return mInterceptor;
    }
    //设置http配置信息
    public static void setHttpConfig(OkHttpClient.Builder builder){
        //希望超时时能重连
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
         //错误重连
        builder.retryOnConnectionFailure(true);
    }
    //设置缓存
    public static void setCache(OkHttpClient.Builder builder,Interceptor cacheInterceptor){
        File cacheFile = new File(AppContext.context().getExternalCacheDir(), "lanlingkeji_hwxw");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        builder.cache(cache).addNetworkInterceptor(cacheInterceptor);
    }
}
