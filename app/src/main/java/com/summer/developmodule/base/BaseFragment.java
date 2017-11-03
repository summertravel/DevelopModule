package com.summer.developmodule.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.summer.developmodule.http.httputil.SubscriberListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.internal.disposables.ListCompositeDisposable;




public abstract class BaseFragment extends Fragment implements SubscriberListener {
    private ListCompositeDisposable listCompositeDisposable ;
    protected String TAG = this.getClass().getSimpleName();
    protected Context mContext;

    //是否可见状态
    private boolean isVisible;
    //View已经初始化完成
    private boolean isPrepared;
    //是否第一次加载完
    private boolean isFirstLoad = true;

    protected ProgressDialog pd;
    protected Map<String,Object> params = new HashMap<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        this.mContext = getActivity();
        //绑定View
        View rootView = inflater.inflate(getLayoutId(), container, false);
        userBundle(savedInstanceState);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        initView();
        initData();
        //懒加载
        lazyLoad();
        initListener();
    }
    protected void initView() {
    }


    protected void initData() {
    }

    protected void initListener() {
    }

    protected void userBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean("");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    protected abstract int getLayoutId();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    //这个是不在viewpager中的隐藏和显示
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        lazyLoadData();
        isFirstLoad = false;
    }

    protected void lazyLoadData() {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("", isHidden());
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
    public void onDestroy() {
        clear();
        mContext = null;
        super.onDestroy();
    }
    public void setToolbar(Toolbar mToolbar){
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            //隐藏标题栏
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(mContext, clz));
    }


    /**
     * 全局等待对话框
     */
    public void showWaitDialog() {
        if (getActivity()==null||getActivity().isFinishing())
            return;
        getActivity().runOnUiThread(new Runnable() {
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

    public void dismissWaitDialog() {
        if (getActivity()==null||getActivity().isFinishing())
            return;
        getActivity().runOnUiThread(new Runnable() {
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
