package com.summer.developmodule.base;

import android.telecom.Call;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    public static String YOUR_TAG = "";
    public final static String APP_ID = "com.llkj.hwxw";
    public static final String TEST_PIC_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508826953&di=5a2e132cb19d20fe7c07a15ccbe268e8&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F17%2F89%2F00%2F14V58PIC9uq_1024.jpg";
    public final static String  USERINFO = APP_ID +"_userinfo";
    public final static String  FGF = "★";
    public final static String  HISTORYRECORD = APP_ID+"historyrecord";
    public final static String  HISTORYRECORD2 = APP_ID+"historyrecord2";
    public final static String  IS_GET_PUSH = APP_ID+"is_get_push";
    public  static final Map<String, Call> downCalls =new HashMap<>();//用来存放各个下载的请求
}
