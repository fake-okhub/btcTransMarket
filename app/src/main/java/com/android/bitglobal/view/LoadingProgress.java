package com.android.bitglobal.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.android.bitglobal.R;

public class LoadingProgress {
    public Dialog mDialog;
    private Context mContext;

    public LoadingProgress(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_custom, null);
        mContext = context;
        mDialog = new Dialog(context, R.style.Custom_Progress);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        mDialog.getWindow().setAttributes(lp);
    }

    public void show() {
        // 获取ImageView上的动画背景
//        AnimationDrawable spinner = (AnimationDrawable) mDialog.findViewById(R.id.spinnerImageView)
//                .getBackground();
//        // 开始动画
//        spinner.start();
//        ImageView imageView = (ImageView) mDialog.findViewById(R.id.spinnerImageView);
//        ObjectAnimator animator = ObjectAnimator//
//                .ofFloat(imageView, "rotation", 0.0F, 360.0F)//
//                .setDuration(1500);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setRepeatCount(-1);
//        animator.start();
        if (mDialog != null) {
            try {
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
