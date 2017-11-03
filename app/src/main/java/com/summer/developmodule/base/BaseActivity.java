package com.summer.developmodule.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.summer.developmodule.http.httputil.SubscriberListener;
import com.summer.developmodule.statusbar.StatusBarCompat;
import com.summer.developmodule.statusbar.StatusBarFontHelper;
import com.summer.developmodule.util.AppManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.internal.disposables.ListCompositeDisposable;


public abstract class BaseActivity extends AppCompatActivity implements SubscriberListener {
    private ListCompositeDisposable listCompositeDisposable ;
    protected String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected ProgressDialog pd;
    protected  Map<String,Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        beforeSetContentView(savedInstanceState);
        setContentView(getLayoutId());

        AppManager.getAppManager().addActivity(this);
        mContext = this;
        afterSetContentView(savedInstanceState);
        setStatus(setStatus2());
        initView();
        initData();
        initListener();

    }
    protected void setStatus(boolean status) {
        if(status){
            StatusBarCompat.compat(this);
            StatusBarFontHelper.setStatusBarMode(this, true);
        }
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    protected void beforeSetContentView(Bundle savedInstanceState) {
    }

    protected void afterSetContentView(Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();
    protected  boolean setStatus2(){
        return true;
    }
    public void setToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            //隐藏标题栏
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    @Override
    protected void onDestroy() {
        clear();
        AppManager.getAppManager().removeActivity(this);
        mContext = null;
        super.onDestroy();
    }

    public ListCompositeDisposable getComp() {
        if (this.listCompositeDisposable == null) {
            this.listCompositeDisposable = new ListCompositeDisposable();
        }
        return listCompositeDisposable;
    }
    protected void clear() {
        if (!getComp().isDisposed()) {
            getComp().clear();
        }
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }


    /**
     * 全局等待对话框
     */
    public void showWaitDialog() {
        //如果ctx等于空或者isFinishing
        if (mContext == null || isFinishing())
            return;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null
                        || !pd.isShowing()) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage("加载中...");
                    pd.setCancelable(true);
                    pd.show();
                }

            }
        });

    }
    /**
     * 全局等待对话框
     */
    public void showWaitDialog(final String remind) {
        //如果ctx等于空或者isFinishing
        if (mContext == null || isFinishing())
            return;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null
                        || !pd.isShowing()) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage(remind);
                    pd.setCancelable(true);
                    pd.show();
                }

            }
        });

    }
    public void dismissWaitDialog() {
        if (mContext == null || isFinishing())
            return;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null
                        && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });

    }
    @Override
    public void onNext(Object o, int httpcode) {

    }

    @Override
    public void onError(int httpcode) {

    }
}
