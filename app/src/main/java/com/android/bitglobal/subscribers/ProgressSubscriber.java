package com.android.bitglobal.subscribers;

import android.content.Context;
import android.util.Log;

import com.android.bitglobal.R;
import com.android.bitglobal.progress.ProgressDialogHandler;
import com.android.bitglobal.progress.ProgressCancelListener;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private Subscriber subscriber;
    private ProgressDialogHandler mProgressDialogHandler;
    private boolean is_progress_show=true;
    private boolean is_unsubscribe=false;
    private boolean is_showMessage=true;
    public boolean is_progress_show() {
        return is_progress_show;
    }

    public void setIs_progress_show(boolean is_progress_show) {
        this.is_progress_show = is_progress_show;
    }

    public boolean is_unsubscribe() {
        return is_unsubscribe;
    }

    public void setIs_unsubscribe(boolean is_unsubscribe) {
        this.is_unsubscribe = is_unsubscribe;
    }

    public boolean is_showMessage() {
        return is_showMessage;
    }

    public void setIs_showMessage(boolean is_showMessage) {
        this.is_showMessage = is_showMessage;
    }

    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }

    public ProgressSubscriber(Subscriber subscriber, Context context) {
        this.subscriber = subscriber;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null&&is_progress_show) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null&&is_progress_show) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        //Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (is_showMessage && context != null) {
            if(!Utils.isNetWorkConnected(context)){
                UIHelper.ToastMessage(context, context.getString(R.string.app_network_interruption_toast));
            }else if (e instanceof SocketTimeoutException) {
                UIHelper.ToastMessage(context, context.getString(R.string.app_network_interruption_toast));
            } else if (e instanceof ConnectException) {
                UIHelper.ToastMessage(context, context.getString(R.string.app_network_interruption_toast));
            } else {
                String[] sourceStrArray = e.getMessage().split("__");
                UIHelper.ToastMessage(context, sourceStrArray[0]);
                Log.e("e.getMessage()",e.getMessage());
            }
        }
        dismissProgressDialog();
        if (subscriber != null) {
            subscriber.onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
        if (subscriber != null) {
            subscriber.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}