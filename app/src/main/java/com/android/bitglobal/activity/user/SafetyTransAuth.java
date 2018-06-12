package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.TransMarketDepth;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;
import com.android.bitglobal.view.TransAdapter;
import com.android.bitglobal.view.UserConfirm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 *交易验证
 */
public class SafetyTransAuth extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.sp_zjmmyz)
    Spinner sp_zjmmyz;

    @BindView(R.id.view_switch_jyecyz)
    SwitchView view_switch_jyecyz;

    private String safePwd="";
    private int sfdyc=0,xzdyg,bxstk=0,operation_dx;
    private UserInfo userInfo;
    private UserConfirm userConfirm;
    private TransAdapter arr_adapter;
    private SubscriberOnNextListener useOrCloseSafePwdOnNext;
    private ProgressSubscriber progressSubscriber;
    private List<TransMarketDepth> sList=new ArrayList<TransMarketDepth>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_trans_auth);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initView() {
        userConfirm=new UserConfirm(this);
        userConfirm.tv_user_title1.setText(getString(R.string.user_zjjyyz_gb));
        userConfirm.tv_user_title2.setText(getString(R.string.user_zjjyyz_sr));
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.bnt_user_cancel.setOnClickListener(this);
        tv_head_title.setText(R.string.user_jyyzsz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        String trans_state= SharedPreferences.getInstance().getString("trans_state","0");
        if(trans_state.equals("0")){
            if(view_switch_jyecyz.isOpened()){
                view_switch_jyecyz.toggleSwitch(false);
            }
        }else{
            if(!view_switch_jyecyz.isOpened()){
                view_switch_jyecyz.toggleSwitch(true);
            }
        }
        view_switch_jyecyz.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                if(!view_switch_jyecyz.isOpened()){
                    view_switch_jyecyz.toggleSwitch(true);
                }
                SharedPreferences.getInstance().putString("trans_state","1");
            }

            @Override
            public void toggleToOff(View view) {
                if(view_switch_jyecyz.isOpened()){
                    view_switch_jyecyz.toggleSwitch(false);
                }
                SharedPreferences.getInstance().putString("trans_state","0");
            }
        });
        arr_adapter = new TransAdapter(context,sList,"2");//初始化适配器
        sp_zjmmyz.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                if(sfdyc>0&&bxstk==0){
                    operation_dx=position;
                    if(position==0||position==2){
                        userConfirm.show();
                    }else{
                        useOrCloseSafePwd();
                    }
                }
                bxstk=0;
                sfdyc=1;

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_zjmmyz.setAdapter(arr_adapter);
        xzdyg=Integer.parseInt(userInfo.getSafePwdPeriod());
        sp_zjmmyz.setSelection(xzdyg);

        useOrCloseSafePwdOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                xzdyg=operation_dx;
                userConfirm.dismiss();
            }
        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){

        }else{
            finish();
        }
        String trans_status[]=getResources().getStringArray(R.array.safety_trans_status);
        String trans_status_key[]=getResources().getStringArray(R.array.safety_trans_status_key);

        int n_length=trans_status.length;
        for(int n=0;n<n_length;n++){
            TransMarketDepth mdd=new TransMarketDepth();
            mdd.setId(trans_status_key[n]);
            mdd.setName(trans_status[n]);
            sList.add(mdd);
        }
    }
    private void useOrCloseSafePwd(){
        progressSubscriber=new ProgressSubscriber(useOrCloseSafePwdOnNext,context);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).useOrCloseSafePwd(progressSubscriber,operation_dx+"",safePwd);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_user_commit:
                safePwd=getText(userConfirm.ed_user_safePwd);
                if(safePwd.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                useOrCloseSafePwd();
                break;
            case R.id.bnt_user_cancel:
                bxstk=1;
                userConfirm.dismiss();
                sp_zjmmyz.setSelection(xzdyg);
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
