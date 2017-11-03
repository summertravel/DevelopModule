package com.summer.developmodule.down;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.summer.developmodule.R;
import com.summer.developmodule.http.HttpService;
import com.summer.developmodule.util.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.internal.disposables.ListCompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.summer.developmodule.http.HttpMethods.BASE_URL;


/**
 * Created by zs on 2016/7/8.
 */
public class DownLoadService extends Service {

    private Context mContext;
    private int preProgress = 0;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Retrofit.Builder retrofit;
    private String url, filename;
    public static final Map<String, Call> downCalls = new HashMap<>();//用来存放各个下载的请求
    public final static String APK_DOWN_PATH = Environment.getExternalStorageDirectory() + File.separator + "M_CXKT_DIR";
    public final static int NOTIFICATION_ID_APK = 1000;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        //2个参数一个 url 下载文件名字
        url = intent.getStringExtra("");
        filename = intent.getStringExtra("");
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(filename)) {
            loadFile();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载文件
     */
    private void loadFile() {
        initNotification();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        Call<ResponseBody> responseBodyCall = retrofit.baseUrl(BASE_URL)
                .client(initOkHttpClient())
                .build()
                .create(HttpService.class)
                .loadFile(url);
        downCalls.put(url, responseBodyCall);
        responseBodyCall.enqueue(new FileCallback(new ListCompositeDisposable(), APK_DOWN_PATH, filename) {

            @Override
            public void onSuccess(File file) {
                //Log.e("zs", "请求成功");
                // 安装软件
                cancelNotification();
                if (filename.endsWith(".apk")) {
                    installApk(file);
                }
            }

            @Override
            public void onLoading(long progress, long total) {
                // Log.e("zs", progress + "----" + total);
                if (total == 0) {
                    total = 100;
                }
                updateNotification(progress * 100 / total);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("zs", "请求失败");
                cancelNotification();
            }
        });
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileProvider7.setIntentDataAndType(this,
                install, "application/vnd.android.package-archive", file, true);
        // 执行意图进行安装
        mContext.startActivity(install);
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    public static OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("0%")
                .setContentTitle("appname")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_APK, builder.build());
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFICATION_ID_APK, builder.build());
        }
        preProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID_APK);
        if (downCalls.containsKey(url)) {
            downCalls.remove(url);
        }
    }
}
