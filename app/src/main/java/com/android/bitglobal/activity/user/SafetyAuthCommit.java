package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-11-08 17:10
 * 793169940@qq.com
 *安全设置-确认
 */
public class SafetyAuthCommit extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.safetyMessageText)
    TextView safetyMessageText;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;


    @BindView(R.id.rl_asset_zjmm)  View rl_asset_zjmm;
    @BindView(R.id.rl_asset_dxyzm) View rl_asset_dxyzm;
    @BindView(R.id.rl_asset_yxyzm) View rl_asset_yxyzm;
    @BindView(R.id.rl_asset_ggyzm) View rl_asset_ggyzm;


    @BindView(R.id.ed_zjmm)  EditText ed_zjmm;
    @BindView(R.id.ed_dxyzm) EditText ed_dxyzm;
    @BindView(R.id.ed_yxyzm) EditText ed_yxyzm;
    @BindView(R.id.ed_ggyzm) EditText ed_ggyzm;

    @BindView(R.id.bnt_get_dxyzm) Button bnt_get_dxyzm;
    @BindView(R.id.bnt_get_yxyzm) Button bnt_get_yxyzm;
    @BindView(R.id.bnt_get_ggyzm) Button bnt_get_ggyzm;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;


    private UserInfo userInfo;
    private String type="",category="",safePwd="",dynamicCode="",googleCode="";
    private String needSafePwd,needMobileCode,needEmailCode,needGoogleCode;
    private SubscriberOnNextListener changeAuthOnNext,userSendCodeOnNext;
    private List<String> list=new ArrayList<String>();
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_auth_commit);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(R.string.user_aqyz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        bnt_get_ggyzm.setOnClickListener(this);
        bnt_get_dxyzm.setOnClickListener(this);
        bnt_get_yxyzm.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        type=bundle.getString("type");
        category=bundle.getString("category");
        needSafePwd=bundle.getString("needSafePwd");
        needMobileCode=bundle.getString("needMobileCode");
        needEmailCode=bundle.getString("needEmailCode");
        needGoogleCode=bundle.getString("needGoogleCode");

        String info = getString(R.string.safety_message);
        String and = getString(R.string.safety_and);
        if("0".equals(needSafePwd)){
            rl_asset_zjmm.setVisibility(View.GONE);
        } else {
            info += getString(R.string.user_zjmm) + and;
        }
        if("0".equals(needMobileCode)){
            rl_asset_dxyzm.setVisibility(View.GONE);
        } else {
            info += getString(R.string.user_dxyzm) + and;
        }
        if("0".equals(needEmailCode)){
            rl_asset_yxyzm.setVisibility(View.GONE);
        } else {
            info += getString(R.string.user_yxyzm) + and;
        }
        if("0".equals(needGoogleCode)){
            rl_asset_ggyzm.setVisibility(View.GONE);
        }else {
            info += getString(R.string.user_ggyzm);
        }
        if(info.lastIndexOf(and) == info.length() - 1) {
            info = info.substring(0, info.length() - 1);
        }
        safetyMessageText.setText(info.toUpperCase());
        time = new TimeCount(60000, 1000);
        userSendCodeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                time.start();
            }
        };
        changeAuthOnNext = new SubscriberOnNextListener<CurrencyWithdrawResult>() {
            @Override
            public void onNext(CurrencyWithdrawResult currencyWithdrawResult) {
                if(currencyWithdrawResult==null){
                    MainActivity.getInstance().getUserInfo();
                    finish();
                    if (SafetyLoginOrWithdrawAuth.context!=null) {
                        SafetyLoginOrWithdrawAuth.context.finish();
                    }
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
    private void changeAuth(){
        list.clear();
        list.add(type);
        list.add(category);
        list.add(safePwd);//safePwd
        list.add(dynamicCode);//dynamicCode
        list.add(googleCode);//googleCode
        HttpMethods.getInstance(3).changeSafetyAuth(new ProgressSubscriber(changeAuthOnNext,this),list);
    }
    //用户发送短信
    private void userSendCode(){
        list.clear();
        //安全设置
        list.add("14");
        HttpMethods.getInstance(3).userSendCode(new ProgressSubscriber(userSendCodeOnNext, this), list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_get_ggyzm:
                if(!AppContext.getInstance().paste(context).equals("")){
                    googleCode=AppContext.getInstance().paste(context);
                    ed_ggyzm.setText(googleCode);
                }
                Editable etext = ed_ggyzm.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.bnt_get_dxyzm:
                userSendCode();
                break;
            case R.id.bnt_get_yxyzm:
                userSendCode();
                break;
            case R.id.bnt_commit:
                if(needSafePwd.equals("1")){
                    safePwd=getText(ed_zjmm);
                    if(safePwd.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                        return;
                    }
                }
                if(needMobileCode.equals("1")){
                    dynamicCode=getText(ed_dxyzm);
                    if(dynamicCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_dxyzm_hint_toast));
                        return;
                    }
                }
                if(needEmailCode.equals("1")){
                    dynamicCode=getText(ed_yxyzm);
                    if(dynamicCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_yxyzm_hint_toast));
                        return;
                    }
                }
                if(needGoogleCode.equals("1")){
                    googleCode=getText(ed_ggyzm);
                    if(googleCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_ggyzm_hint_toast));
                        return;
                    }
                }
                changeAuth();
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
            if(needMobileCode.equals("1")){
                bnt_get_dxyzm.setText(R.string.user_hqyzm);
                bnt_get_dxyzm.setClickable(true);
            }
            if(needEmailCode.equals("1")){
                bnt_get_yxyzm.setText(R.string.user_hqyzm);
                bnt_get_yxyzm.setClickable(true);
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            if(needMobileCode.equals("1")){
                bnt_get_dxyzm.setClickable(false);//防止重复点击
                bnt_get_dxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
            }
            if(needEmailCode.equals("1")){
                bnt_get_yxyzm.setClickable(false);//防止重复点击
                bnt_get_yxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
            }

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
