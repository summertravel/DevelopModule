package com.summer.developmodule.base;

import android.app.Application;
import android.os.Handler;

public class AppContext extends Application {
    private static AppContext instance;
    public static String lat = "39.93";
    public static String lng = "116.42";
    public static String city_name = "北京";
    public static Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppContext context() {
        return instance;
    }

}
