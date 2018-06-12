package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserSelect;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-08-08 11:10
 * 793169940@qq.com
 *安全中心
 */
public class SafetyCenter extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.rl_dlmmsz)
    RelativeLayout rl_dlmmsz;
    @BindView(R.id.rl_zjmmsz)
    RelativeLayout rl_zjmmsz;
    @BindView(R.id.rl_ggyzsz)
    RelativeLayout rl_ggyzsz;

    @BindView(R.id.rl_ssmmsz)
    RelativeLayout rl_ssmmsz;
    @BindView(R.id.rl_dlyzsz)
    RelativeLayout rl_dlyzsz;
    @BindView(R.id.rl_zfyzsz)
    RelativeLayout rl_zfyzsz;
    @BindView(R.id.rl_jyyzsz)
    RelativeLayout rl_jyyzsz;


    private UserInfo userInfo;
    private UserSelect userSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_center);
        context=this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tv_head_title.setText(R.string.user_aqzx);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        rl_dlmmsz.setOnClickListener(this);
        rl_zjmmsz.setOnClickListener(this);
        rl_ggyzsz.setOnClickListener(this);

        rl_ssmmsz.setOnClickListener(this);
        rl_dlyzsz.setOnClickListener(this);
        rl_zfyzsz.setOnClickListener(this);
        rl_jyyzsz.setOnClickListener(this);


        userSelect=new UserSelect(context);
        userSelect.tv_select_title1.setText(R.string.safety_ggrzkq);
//        userSelect.tv_select_title2.setText(R.string.user_ggrzsz);
        userSelect.tv_select_title2.setVisibility(View.GONE);
        userSelect.bnt_select_subject_1.setText(R.string.safety_xgggyz);
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setText(R.string.safety_gbggyz);
        userSelect.bnt_select_subject_2.setOnClickListener(this);
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(!is_token(userInfo)){
            finish();
        }else{

        }
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.rl_dlmmsz:
                UIHelper.showSafetyLoginPwd(context);
                break;
            case R.id.rl_zjmmsz:
                UIHelper.showSafetySafePwd(context);
                break;
            case R.id.rl_ggyzsz:
                userInfo=UserDao.getInstance().getIfon();
                if(userInfo.getGoogleAuth()!=null&&userInfo.getGoogleAuth().equals("1")){
                    userSelect.show();
                }else{
                    UIHelper.showGoogleAuth(context,"1");
                }
                break;
            case R.id.bnt_select_subject_1:
                userSelect.dismiss();
                UIHelper.showGoogleAuth(context,"2");
                break;
            case R.id.bnt_select_subject_2:
                userSelect.dismiss();
                UIHelper.showGoogleAuth(context,"0");
                break;
            case R.id.rl_ssmmsz:
                String user_lock=SharedPreferences.getInstance().getString(SharedPreferences.KEY_USER_LOCK,"");
                if(user_lock.equals("")){
                    UIHelper.showGestureHint(context);
                }else{
                    UIHelper.showGestureSet(context);
                }
                break;
            case R.id.rl_dlyzsz:
                bundle.putString("category","1");
                UIHelper.SafetyLoginOrWithdrawAuth(context,bundle);
                break;
            case R.id.rl_jyyzsz:
                //// 是否设置有资金密码: 0:否,1:是``
                if (userInfo.getIsHadSecurePassword().equals("0")) {
                    UIHelper.ToastMessage(context, context.getString(R.string.asset_wszzjmm_toast));
                    UIHelper.showSafetySafePwd(context);
                    return;
                }
                bundle.putString("category","2");
                UIHelper.SafetyLoginOrWithdrawAuth(context,bundle);
                break;
            case R.id.rl_zfyzsz:
                //// 是否设置有资金密码: 0:否,1:是
                if (userInfo.getIsHadSecurePassword().equals("0")) {
                    UIHelper.ToastMessage(context, context.getString(R.string.asset_wszzjmm_toast));
                    UIHelper.showSafetySafePwd(context);
                    return;
                }
                bundle.putString("category","3");
                UIHelper.SafetyLoginOrWithdrawAuth(context,bundle);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

}
