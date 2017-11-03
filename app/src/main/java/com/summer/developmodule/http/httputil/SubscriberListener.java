package com.summer.developmodule.http.httputil;

public interface SubscriberListener<T> {
    void onNext(T t, int httpcode);
    void onError(int httpcode);
}
