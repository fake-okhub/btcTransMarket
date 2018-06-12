package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.UserInfoResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserConfirm;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;


/**
 * xiezuofei
 * 2016-12-30 14:10
 * 793169940@qq.com
 * 用户管理
 */
public class UserManage extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

    @BindView(R.id.ll_dqyh)
    LinearLayout ll_dqyh;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_userNmae)
    TextView tv_userNmae;
    @BindView(R.id.tv_tc)
    TextView tv_tc;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.listview)
    ListView listView;
    private UserInfo userInfo;
    private RealmResults<UserInfo> list_user;
    private QuickAdapter<UserInfo> adapter;
    private SubscriberOnNextListener getuserInfoOnNext;
    private String token="",userId="";
    private UserConfirm userConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.user_manage);
        ButterKnife.bind(this);
        context=this;
        initView();
        initData();
    }

    private void initView() {
        userConfirm=new UserConfirm(context);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.tv_user_title1.setText(getString(R.string.user_sqygq));
        userConfirm.tv_user_title2.setText(getString(R.string.user_sqygq_sfcxhq));
        userConfirm.bnt_user_commit.setText(getString(R.string.user_sqygq_cxhq));

        tv_head_title.setText(R.string.user_manage_zhgl);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_head_front.setVisibility(View.VISIBLE);
        btn_head_front.setOnClickListener(this);

        btn_head_front.setImageResource(R.mipmap.icon_user_tjyh);
        tv_tc.setOnClickListener(this);
        adapter = new QuickAdapter<UserInfo>(this, R.layout.user_manage_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, UserInfo item) {
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(200,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                final String item_userId=item.getUserId();
                final String item_token=item.getToken();
                String s_name=item.getRealName();
                if(s_name==null||"".equals(s_name)){
                    s_name=getString(R.string.user_wname);
                }
                helper.getView().findViewById(R.id.item_right).setLayoutParams(lp2);
                helper.setText(R.id.tv_userId,getString(R.string.user_id)+item.getUserName()).
                        setText(R.id.tv_userNmae,getString(R.string.user_name)+s_name);
                helper.setOnClickListener(R.id.item_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserDao.getInstance().delect(item_userId);
                        initData();
                        UIHelper.ToastMessage(context,R.string.user_czcg_toast);
                    }
                });
                helper.setOnClickListener(R.id.tv_qh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userId=item_userId;
                        token=item_token;
                        getUserInfo();
                    }
                });
            }

        };
        listView.setAdapter(adapter);
        getuserInfoOnNext= new SubscriberOnNextListener<HttpResult<UserInfoResult>>() {
            @Override
            public void onNext(HttpResult<UserInfoResult> httpResult) {
                Log.e("httpResult",httpResult+"");
                String msg_code=httpResult.getResMsg().getCode();
                String msg_mes=httpResult.getResMsg().getMessage();
                if(SystemConfig.SUCCESS.equals(msg_code)){
                    UserDao.getInstance().change(userId);
                    initData();
                    UIHelper.ToastMessage(context,R.string.user_czcg_toast);
                }else{
                    userConfirm.show();
                }
            }
        };
    }
    private void getUserInfo(){
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(getuserInfoOnNext,this);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(3).getUserInfo2(progressSubscriber,token,userId);
    }
    private void initData() {
        userInfo=UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
            String s_name=userInfo.getRealName();
            if(s_name==null||"".equals(s_name)){
                s_name=getString(R.string.user_wname);
            }
            ll_dqyh.setVisibility(View.VISIBLE);
            tv_userId.setText(getString(R.string.user_id)+userInfo.getUserName());
            tv_userNmae.setText(getString(R.string.user_name)+s_name);
        }else{
            ll_dqyh.setVisibility(View.GONE);
        }
        list_user= UserDao.getInstance().getUsers();
        adapter.clear();
        adapter.addAll(list_user);
        if(list_user.size()>0){
            tv_hint.setVisibility(View.GONE);
        }else{
            tv_hint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_user_commit:
                userConfirm.dismiss();
                UIHelper.showLogin(context);
                break;
            case R.id.btn_head_front:
                UIHelper.showLogin(MainActivity.getInstance());
                break;
            case R.id.tv_tc:
                UserDao.getInstance().exit();
                initData();
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
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
