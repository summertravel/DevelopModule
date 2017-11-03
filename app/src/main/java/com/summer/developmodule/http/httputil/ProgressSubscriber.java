package com.summer.developmodule.http.httputil;

import android.content.Context;
import android.widget.Toast;

import io.reactivex.subscribers.ResourceSubscriber;

import static com.summer.developmodule.util.AppUtil.checkNet;

public class ProgressSubscriber<T> extends ResourceSubscriber<T> implements ProgressCancelListener {
    private SubscriberListener mSubscriberListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;
    private int httpcode;
    private boolean isShow = true;
    private boolean isNeedCahe = true;

    public ProgressSubscriber(SubscriberListener mSubscriberOnNextListener, Context context, int httpcode) {
        this.mSubscriberListener = mSubscriberOnNextListener;
        this.context = context;
        this.httpcode = httpcode;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(SubscriberListener mSubscriberOnNextListener, Context context, int httpcode, boolean isShow) {
        this.mSubscriberListener = mSubscriberOnNextListener;
        this.context = context;
        this.httpcode = httpcode;
        this.isShow = isShow;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        if (isShow)
            showProgressDialog();
        if (!checkNet(context)) {
            Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
            onComplete();
            mSubscriberListener.onError(httpcode);
        }
        super.onStart();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
//        Logger.e(e.toString());
        e.printStackTrace();
        ApiException a = e instanceof ApiException ? ((ApiException) e) : null;
        if (a != null) {
            if (a.getErrCode() == -2) {
//                UserInfoBean.getInstance().clearUserInfo();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            mSubscriberListener.onError(httpcode);
        }
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        //  Toast.makeText(context, " Completed", Toast.LENGTH_SHORT).showBottom();
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    @Override
    public void onNext(T t) {
        mSubscriberListener.onNext(t, httpcode);
    }

    @Override
    public void onCancelProgress() {

    }

}
