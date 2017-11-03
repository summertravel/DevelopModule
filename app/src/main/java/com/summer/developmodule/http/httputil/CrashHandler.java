package com.summer.developmodule.http.httputil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler instance;
    //程序的Context对象
    private Context mContext;

    private static final String TAG = "CrashHandler";

    private CrashHandler(Context context) {
        init(context);
    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized CrashHandler getInstance(Context context) {
        if (instance == null) {
            instance = new CrashHandler(context);
        }
        return instance;
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        ex.printStackTrace(printWriter);
//        Throwable cause = ex.getCause() ;
//        while (cause != null){
//            cause.printStackTrace(printWriter);
//            cause = cause.getCause() ;
//        }
//        printWriter.close();
//        String result = writer.toString() ;
        limitAppLogCount();
        collectCrashLogInfo(mContext);
        writerCrashLogToFile(ex);
        return true;
    }


    private Map<String, String> crashAppLog = new HashMap<>();

    /**
     * 获取应用信息
     *
     * @param mContext
     */
    private void collectCrashLogInfo(Context mContext) {

        try {
            if (mContext == null)
                return;

            PackageManager packageManager = mContext.getPackageManager();

            if (packageManager != null) {

                PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

                if (packageInfo != null) {

                    String versionName = packageInfo.versionName;
                    String versionCode = "" + packageInfo.versionCode;
                    String packName = packageInfo.packageName;

                    crashAppLog.put("versionName", versionName);
                    crashAppLog.put("versionCode", versionCode);
                    crashAppLog.put("packName", packName);

                }
            }


            /**
             * 获取手机型号，系统版本，以及SDK版本
             */
            crashAppLog.put("手机型号:", Build.MODEL);
            crashAppLog.put("系统版本", "" + Build.VERSION.SDK);
            crashAppLog.put("Android版本", Build.VERSION.RELEASE);

            Field[] fields = Build.class.getFields();

            if (fields != null && fields.length > 0) {

                for (Field field : fields) {

                    if (field != null) {

                        field.setAccessible(true);

                        crashAppLog.put(field.getName(), field.get(null).toString());
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "collectDeviceInfo - " + e.getMessage());
        }
    }

    /**
     * 写入文件中
     *
     * @param ex
     */
    private void writerCrashLogToFile(Throwable ex) {
        try {
            StringBuffer buffer = new StringBuffer();

            if (crashAppLog != null && crashAppLog.size() > 0) {

                for (Map.Entry<String, String> entry : crashAppLog.entrySet()) {

                    buffer.append(entry.getKey() + ":" + entry.getValue() + "\n");
                }
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();

            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }

            printWriter.flush();
            printWriter.close();

            String result = writer.toString();

            buffer.append("Exception:+\n");

            buffer.append(result);

            writerToFile(buffer.toString());
            Log.i(TAG, "Crash：" + result);
        } catch (Exception e) {
            Log.e(TAG, "writerCrashLogToFile - " + e.getMessage());
        }
    }


    private SimpleDateFormat formate;
    //将文件写入

    private void writerToFile(String s) {

        try {
            /**
             * 创建日志文件名称
             */
            String curtTimer = "" + System.currentTimeMillis();
            if (formate == null) {

                formate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            }
            String timer = formate.format(new Date());

            String fileName = "crash-" + timer + "-" + curtTimer + ".log";
            /**
             * 创建文件夹
             */
            File folder = new File(CAHCE_CRASH_LOG);

            if (!folder.exists())
                folder.mkdirs();

            /**
             * 创建日志文件
             */
            File file = new File(folder.getAbsolutePath() + File.separator + fileName);

            if (!file.exists())
                file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();

            sendCrashLogToServer(folder, file);

        } catch (Exception e) {
            Log.e(TAG, "writerToFile - " + e.getMessage());
        }
    }


    private int LIMIT_LOG_COUNT = 10;
    private String CAHCE_CRASH_LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 最大文件数量
     */
    private void limitAppLogCount() {

        try {

            File file = new File(CAHCE_CRASH_LOG);

            if (file != null && file.isDirectory()) {

                File[] files = file.listFiles(new CrashLogFliter());

                if (files != null && files.length > 0) {

                    Arrays.sort(files, comparator);

                    if (files.length > LIMIT_LOG_COUNT) {

                        for (int i = 0; i < files.length - LIMIT_LOG_COUNT; i++) {

                            files[i].delete();
                        }
                    }

                }
            }

        } catch (Exception e) {
            Log.e(TAG, "limitAppLogCount - " + e.getMessage());
        }
    }

//这里限制文件的数量我使用的文件类型过滤和排序
    /**
     * 日志文件按日志大小排序
     */
    private Comparator<File> comparator = new Comparator<File>() {
        @Override
        public int compare(File l, File r) {

            if (l.lastModified() > r.lastModified())
                return 1;
            if (l.lastModified() < r.lastModified())
                return -1;

            return 0;
        }
    };

    /**
     * 过滤.log的文件
     */
    public class CrashLogFliter implements FileFilter {

        @Override
        public boolean accept(File file) {

            if (file.getName().endsWith(".log"))
                return true;
            return false;
        }
    }

    public void sendCrashLogToServer(File folder, File file) {
        //发送服务端
        Log.e("*********", "文件夹:" + folder.getAbsolutePath() + " - " + file.getAbsolutePath() + "");
    }

}
