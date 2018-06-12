package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-02 16:50
 * 793169940@qq.com
 * 邮箱注册成功
 */
public class RegisterEmailSuccess extends SwipeBackActivity implements View.OnClickListener{

    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    private String userId,email;
    private TimeCount time;
    public static Activity context;
    private SubscriberOnNextListener sendCodeOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email_success);
//        tintManager.setStatusBarTintResource(R.color.main_login_header_bg);//通知栏所需颜色
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        userId=bundle.getString("userId");
        email=bundle.getString("email");
    }
    private void initView(){
        bnt_commit.setOnClickListener(this);
        btn_head_back.setOnClickListener(this);
        tv_email.setText(email);
        time = new TimeCount(60000, 1000);
        sendCodeOnNext = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                time.start();
            }
        };


    }
    private void reSendEmail(){
        HttpMethods.getInstance(3).reSendEmail(new ProgressSubscriber(sendCodeOnNext, this), userId);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                UIHelper.showLogin(this);
                finish();
                break;
            case R.id.bnt_commit:
                // 开始计时
                reSendEmail();
                break;

        }
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 计时完毕
            bnt_commit.setText(R.string.user_cxfsyj);
            bnt_commit.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            bnt_commit.setClickable(false);//防止重复点击
            bnt_commit.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
        }
    }
}
