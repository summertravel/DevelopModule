package com.summer.developmodule.http;

import android.content.Context;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.summer.developmodule.BuildConfig;
import com.summer.developmodule.http.httputil.ApiException;
import com.summer.developmodule.http.httputil.OkHttpUtil;
import com.summer.developmodule.http.httputil.ProgressSubscriber;
import com.summer.developmodule.http.httputil.SubscriberListener;


import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpMethods {
    public static final int HTTP_SUCCESS = 1;
    public static final String BASE_URL="";
    private Retrofit retrofit;
    private HttpService httpService;
    private OkHttpClient okhttpclient;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        retrofit = getRetrofit(getOkHttpClient(), BASE_URL);
        httpService = retrofit.create(HttpService.class);
    }

    public OkHttpClient getOkHttpClient() {
        if (okhttpclient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            OkHttpUtil.setHttpConfig(httpClientBuilder);
            if (BuildConfig.DEBUG) {
                //设置log拦截器
                httpClientBuilder.addInterceptor(OkHttpUtil.getInterceptor(0));
            }
            okhttpclient = httpClientBuilder.build();
        }
        return okhttpclient;
    }


    public Retrofit getRetrofit(OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    //添加线程管理并订阅
    private void toSubscribe(Flowable o, Subscriber s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Function<HttpResult<T>, T> {
        @Override
        public T apply(HttpResult<T> httpResult) throws Exception {
            if (httpResult.state != HTTP_SUCCESS) {//0失败 1成功，2 异常
                throw new ApiException(httpResult.state, httpResult.message);
            }
            if (httpResult.data == null) {
                httpResult.data = (T) "";
            }
            return httpResult.data;
        }
    }

    public void api(Context c, ListCompositeDisposable compositeSubscription, SubscriberListener subscriberOnNextListener,
                    Map<String, Object> params, String model) {
        api(c, compositeSubscription, subscriberOnNextListener, params, model, true);
    }

    public void api(Context c, ListCompositeDisposable compositeSubscription, SubscriberListener subscriberOnNextListener,
                    Map<String, Object> params, String model, boolean isShow) {

        params.put("pageSize", 10);
        Flowable observable = httpService.api(model, params)
                .map(new HttpResultFunc());
        ResourceSubscriber subscriber = new ProgressSubscriber(subscriberOnNextListener, c, -1, isShow);
        toSubscribe(observable, subscriber);
        compositeSubscription.add(subscriber);
    }

    public void api2(Context c, ListCompositeDisposable compositeSubscription, SubscriberListener<ResponseBody> subscriberOnNextListener,
                     Map<String, Object> params, String model, boolean isShow) {

        params.put("pageSize", 10);
        Flowable<ResponseBody> observable = httpService.api2(model, params);
        ResourceSubscriber subscriber = new ProgressSubscriber(subscriberOnNextListener, c, -1, isShow);
        toSubscribe(observable, subscriber);
        compositeSubscription.add(subscriber);

    }

}
