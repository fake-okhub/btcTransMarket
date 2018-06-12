package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
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
import com.android.bitglobal.entity.GoogleResult;
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
public class SafetyGoogleAuth extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_hint)
    TextView tv_hint;

//    @BindView(R.id.tv_dxyzm)
//    TextView tv_dxyzm;
    @BindView(R.id.ed_ggmy)
    EditText ed_ggmy;
    @BindView(R.id.ed_ggyzm)
    EditText ed_ggyzm;
    @BindView(R.id.ed_dxyzm)
    EditText ed_dxyzm;



    @BindView(R.id.rl_ggmy)
    RelativeLayout rl_ggmy;
    @BindView(R.id.bnt_get_code)
    Button bnt_get_code;
    @BindView(R.id.bnt_fz)
    Button bnt_fz;
    @BindView(R.id.bnt_zt)
    Button bnt_zt;
    @BindView(R.id.btn_update)
    Button btn_update;

    private SubscriberOnNextListener getGoogleSecretOnNext,setGoogleCodeOnNext,userSendCodeOnNext;
    List<String> list=new ArrayList<String>();
    String secret="",tips="",type="",mobileCode="",googleCode="",select_type="";
    private UserInfo userInfo;
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_google);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        loadData();
    }

    private void initView() {
        if(userInfo.getMobileNumber()==null||userInfo.getMobileNumber().equals("")){
//            tv_dxyzm.setText(R.string.user_yxyzm);
            ed_dxyzm.setHint(R.string.user_yxyzm);
        }
        select_type=getIntent().getStringExtra("type");
        if(select_type.equals("1")){
            type="1";
            btn_update.setText(R.string.safety_kqggyz);
        }else if(select_type.equals("2")){
            type="1";
            btn_update.setText(R.string.safety_xgggyz);
        }else if(select_type.equals("0")){
            type="0";
            btn_update.setText(R.string.safety_gbggyz);
            rl_ggmy.setVisibility(View.GONE);
        }
        tv_head_title.setText(R.string.user_set_google);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        bnt_fz.setOnClickListener(this);
        bnt_zt.setOnClickListener(this);
        bnt_get_code.setOnClickListener(this);

        setGoogleCodeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(context,R.string.set_successful_toast);
                finish();
            }

        };
        getGoogleSecretOnNext = new SubscriberOnNextListener<GoogleResult>() {
            @Override
            public void onNext(GoogleResult googleResult) {
                secret=googleResult.getSecret();
                tips=googleResult.getTips();
                ed_ggmy.setText(secret);
                tv_hint.setText(Html.fromHtml(tips));
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
        userInfo=UserDao.getInstance().getIfon();
        if(!is_token(userInfo)){
           finish();
        }else{

        }
    }
    private void loadData() {
        if(!type.equals("0")){
            getGoogleSecret();
        }
    }
    //设置谷歌认证
    private void setGoogleCode() {
        list.clear();
        list.add(secret);
        list.add(type);
        list.add(mobileCode);
        list.add(googleCode);
        HttpMethods.getInstance(3).setGoogleCode(new ProgressSubscriber(setGoogleCodeOnNext, this), list);
    }
    //获取谷歌密钥
    private void getGoogleSecret() {
        HttpMethods.getInstance(3).getGoogleSecret(new ProgressSubscriber(getGoogleSecretOnNext, this));
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
            case R.id.bnt_fz:
                AppContext.getInstance().copy(secret,this);
                break;
            case R.id.bnt_zt:
                googleCode= AppContext.getInstance().paste(this);
                ed_ggyzm.setText(googleCode);
                Editable etext = ed_ggyzm.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.btn_update:
                googleCode=ed_ggyzm.getText().toString().trim();
                if(googleCode.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_ggyzm_hint_toast);
                    return;
                }
                mobileCode=ed_dxyzm.getText().toString().trim();
                if(mobileCode.equals("")){
                    if(userInfo.getMobileNumber()==null||userInfo.getMobileNumber().equals("")){
                        UIHelper.ToastMessage(context,R.string.user_yxyzm_hint_toast);
                    } else {
                        UIHelper.ToastMessage(context,R.string.user_dtyzm_toast);
                    }
                    return;
                }
                setGoogleCode();
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
