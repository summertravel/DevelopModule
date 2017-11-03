package com.summer.developmodule.http;


import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface HttpService {
    //登录
    @FormUrlEncoded
    @POST()
    Flowable<HttpResult> api(@Path("model") String versionName, @FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST()
    Flowable<ResponseBody> api2(@Path("model") String versionName, @FieldMap Map<String, Object> map);

    @GET()
    Call<ResponseBody> loadFile(@Url String url);

}
