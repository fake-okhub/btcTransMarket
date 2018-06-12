package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-09 13:50
 * 793169940@qq.com
 *安全中心-修改登录密码和资金密码
 */
public class SafetyLoginPwd extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_jmm)
    EditText ed_jmm;
    @BindView(R.id.ed_xmm)
    EditText ed_xmm;
    @BindView(R.id.ed_qrmm)
    EditText ed_qrmm;
    @BindView(R.id.ed_dxyzm)
    EditText ed_dxyzm;
    @BindView(R.id.ed_ggyzm)
    EditText ed_ggyzm;

//    @BindView(R.id.tv_jmm)
//    TextView tv_jmm;
//    @BindView(R.id.tv_xmm)
//    TextView tv_xmm;
//    @BindView(R.id.tv_qrmm)
//    TextView tv_qrmm;
//    @BindView(R.id.tv_dxyzm)
//    TextView tv_dxyzm;

    @BindView(R.id.rl_jmm)
    RelativeLayout rl_jmm;
    @BindView(R.id.rl_ggyzm)
    RelativeLayout rl_ggyzm;
    @BindView(R.id.bnt_zt)
    Button bnt_zt;
    @BindView(R.id.bnt_get_code)
    Button bnt_get_code;
    @BindView(R.id.btn_update)
    Button btn_update;
    String type,isShow="1",isDxyzm="1",isGoole="0";
    private SubscriberOnNextListener updatePwdOnNext,userSendCodeOnNext;
    List<String> list=new ArrayList<String>();
    String oldPwd="",newPwd="",qnewPwd="",mobileCode="",googleCode="";
    private UserInfo userInfo;
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_login);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        type=getIntent().getStringExtra("type");
        if(type!=null&&type.equals("2")){
//            btn_update.setText(R.string.user_zjmmxg);
            tv_head_title.setText(R.string.user_zjmmsz_title);
            ed_qrmm.setHint(R.string.safety_qrzjmm);
//            tv_jmm.setText(R.string.safety_jzjmm);
//            tv_xmm.setText(R.string.safety_xzjmm);
//            tv_qrmm.setText(R.string.safety_qrzjmm);
        }else{
            tv_head_title.setText(R.string.user_set_login_password);
            type="1";

        }
        if(userInfo.getMobileNumber()==null||userInfo.getMobileNumber().equals("")){
            isDxyzm="2";
//            tv_dxyzm.setText(R.string.user_yxyzm);
            ed_dxyzm.setHint(R.string.user_yxyzm_hint);
        }
        bnt_zt.setOnClickListener(this);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        bnt_get_code.setOnClickListener(this);
        if(isShow!=null&&isShow.equals("1")||type.equals("1")){
            rl_jmm.setVisibility(View.VISIBLE);
        }else{
            if(type!=null&&type.equals("2")){
                btn_update.setText(R.string.user_zjmmsz);
            }
            rl_jmm.setVisibility(View.GONE);
        }
        if(isGoole!=null&&isGoole.equals("1")){
            rl_ggyzm.setVisibility(View.VISIBLE);
        }else{
            rl_ggyzm.setVisibility(View.GONE);
        }
        updatePwdOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(context,R.string.set_successful_toast);
                finish();
            }

        };
        time = new TimeCount(60000, 1000);
        userSendCodeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                time.start();
            }
        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
            isShow=userInfo.getIsHadSecurePassword();
            isGoole=userInfo.getGoogleAuth();
        }else{
            finish();
        }
    }
    //修改密码
    private void resetPwd() {
        list.clear();
        list.add(type);
        list.add(oldPwd);
        list.add(newPwd);
        list.add(mobileCode);
        list.add(googleCode);
        HttpMethods.getInstance(3).resetPwd(new ProgressSubscriber(updatePwdOnNext, this), list);
    }
    //用户发送短信
    private void userSendCode(){
        list.clear();
        list.add("14");
        HttpMethods.getInstance(3).userSendCode(new ProgressSubscriber(userSendCodeOnNext, this), list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_get_code:
                userSendCode();
                break;
            case R.id.bnt_zt:
                googleCode= AppContext.getInstance().paste(this);
                ed_ggyzm.setText(googleCode);
                break;
            case R.id.btn_update:
                oldPwd=getText(ed_jmm);
                newPwd=getText(ed_xmm);
                qnewPwd=getText(ed_qrmm);
                mobileCode=ed_dxyzm.getText().toString().trim();
                googleCode=ed_ggyzm.getText().toString().trim();
                if(isShow.equals("1")||type.equals("1")){
                    if(oldPwd.equals("")){
                        UIHelper.ToastMessage(context,R.string.safety_jmm_hint_toast);
                        return;
                    }
                }
                if(newPwd.equals("")){
                    UIHelper.ToastMessage(context,R.string.safety_xmm_hint_toast);
                    return;
                }
                if(!com.android.bitglobal.tool.Utils.check(newPwd))
                {
                    UIHelper.ToastMessage(context,R.string.user_check_pwd_toast);
                    return;
                }
                if(qnewPwd.equals("")){
                    UIHelper.ToastMessage(context,R.string.safety_qrmm_hint_toast);
                    return;
                }
                if(!qnewPwd.equals(newPwd)){
                    UIHelper.ToastMessage(context,R.string.user_lcmmsrbyz_toast);
                    return;
                }
                if(!userInfo.getMobileNumber().equals("")&&mobileCode.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_dxyzm_hint_toast);
                    return;
                }
                if(userInfo.getMobileNumber().equals("")&&mobileCode.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_yxyzm_hint_toast);
                    return;
                }
                if(isGoole.equals("1")&&googleCode.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_ggyzm_hint_toast);
                    return;
                }
                resetPwd();
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
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 计时完毕
            bnt_get_code.setText(R.string.user_hqyzm);
            bnt_get_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            bnt_get_code.setClickable(false);//防止重复点击
            bnt_get_code.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
        }
    }
}
