package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;
import com.android.bitglobal.view.UserConfirm;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 *支付验证
 */
public class SafetyPayAuth extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;


    @BindView(R.id.tv_dxtxyz)
    TextView tv_dxtxyz;
    @BindView(R.id.tv_dxtxyz_hint)
    TextView tv_dxtxyz_hint;

    @BindView(R.id.view_switch_dxtxyz)
    SwitchView view_switch_dxtxyz;
    @BindView(R.id.view_switch_ggtxyz)
    SwitchView view_switch_ggtxyz;
    private SubscriberOnNextListener changeGoogleAuthOnNext,paySmsAuthOnNext;
    private ProgressSubscriber progressSubscriber;
    private String operation_dx,operation_gg,authType="2",googleCode="";
    private UserInfo userInfo;
    private UserConfirm userConfirm;
    List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_pay_auth);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initView() {
        userConfirm=new UserConfirm(this);
        userConfirm.tv_user_title1.setText(getString(R.string.user_ggtxyz_gb));
        userConfirm.tv_user_title2.setText(getString(R.string.user_yddlyz_sr));
        userConfirm.ed_user_safePwd.setHint(getString(R.string.user_ggyzm_hint_toast));
        userConfirm.ed_user_safePwd.setInputType(1);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.bnt_user_cancel.setOnClickListener(this);


        tv_head_title.setText(R.string.user_zfyzsz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        if(userInfo.getMobileNumber()==null||userInfo.getMobileNumber().equals("")){
            tv_dxtxyz.setText(R.string.user_yxtxyz);
            tv_dxtxyz_hint.setText(R.string.user_yxtxyz_hint);
        }

        view_switch_dxtxyz.setOnClickListener(this);
        view_switch_ggtxyz.setOnClickListener(this);
        if(userInfo.getPaySmsAuth().equals("1")){
            view_switch_dxtxyz.setOpened(true);
        }else{
            view_switch_dxtxyz.setOpened(false);
        }
        if(userInfo.getPayGoogleAuth().equals("1")){
            view_switch_ggtxyz.setOpened(true);
        }else{
            view_switch_ggtxyz.setOpened(false);
        }
        view_switch_dxtxyz.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                operation_dx="1";
                paySmsAuth();
            }

            @Override
            public void toggleToOff(View view) {
                operation_dx="0";
                paySmsAuth();
            }
        });
        view_switch_ggtxyz.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                if(!userInfo.getGoogleAuth().equals("1")){
                    UIHelper.ToastMessage(context,getString(R.string.user_ggecyz_wkq_toast));
                }else{
                    operation_gg="1";
                    googleCode="";
                    changeGoogleAuth();
                }

            }

            @Override
            public void toggleToOff(View view) {
                userConfirm.show();
            }

        });
        paySmsAuthOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                if(operation_dx.equals("0")){
                    view_switch_dxtxyz.setOpened(false);
                }else{
                    view_switch_dxtxyz.setOpened(true);
                }
            }
        };
        changeGoogleAuthOnNext = new SubscriberOnNextListener() {

            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                if(operation_gg.equals("0")){
                    view_switch_ggtxyz.setOpened(false);
                    userConfirm.dismiss();
                }else{
                    userConfirm.dismiss();
                    view_switch_ggtxyz.setOpened(true);
                }

            }

        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){

        }else{
            finish();
        }
    }
    private void paySmsAuth(){
        progressSubscriber=new ProgressSubscriber(paySmsAuthOnNext,context);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).changeDynamicCodeAuth(progressSubscriber,operation_dx,authType);
    }
    private void changeGoogleAuth(){
        list.clear();
        list.add(operation_gg);
        list.add(authType);
        list.add(googleCode);
        progressSubscriber=new ProgressSubscriber(changeGoogleAuthOnNext,context);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).changeGoogleAuth(progressSubscriber,list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.view_switch_dxtxyz:
                view_switch_dxtxyz.setOpened(view_switch_dxtxyz.isOpened());
                break;
            case R.id.view_switch_ggtxyz:
                view_switch_ggtxyz.setOpened(view_switch_ggtxyz.isOpened());
                break;
            case R.id.bnt_user_cancel:
                view_switch_ggtxyz.setOpened(view_switch_ggtxyz.isOpened());
                userConfirm.dismiss();
                break;
            case R.id.bnt_user_commit:
                googleCode=getText(userConfirm.ed_user_safePwd);
                if(googleCode.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_ggyzm_hint_toast));
                    return;
                }
                operation_gg="0";
                changeGoogleAuth();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
