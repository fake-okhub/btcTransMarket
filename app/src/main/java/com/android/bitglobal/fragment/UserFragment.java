package com.android.bitglobal.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.SplashActivity;
import com.android.bitglobal.activity.user.SettingActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.ArticleDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.AppVersionResult;
import com.android.bitglobal.entity.Article;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.UserVipInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.UserSelect;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class  UserFragment extends BaseFragment implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
//    @BindView(R.id.btn_head_front)
//    ImageView btn_head_front;
//    @BindView(R.id.img_head_ico)
//    ImageView img_head_ico;
    @BindView(R.id.ll_user_head)
    LinearLayout ll_user_head;
    @BindView(R.id.img_level)
    ImageView img_level;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;

    @BindView(R.id.tv_user_jf)
    TextView tv_user_jf;

    //安全中心
    @BindView(R.id.rl_aqzx)
    LinearLayout rl_aqzx;

    //新闻公告
    @BindView(R.id.rl_gfgg)
    LinearLayout rl_gfgg;

    @BindView(R.id.tv_user_gfgg)
    TextView tv_user_gfgg;

    @BindView(R.id.rl_bzphsz)
    RelativeLayout rl_bzphsz;

    @BindView(R.id.rl_hlbz)
    RelativeLayout rl_hlbz;

    @BindView(R.id.tv_tel)
    TextView tv_tel;
    @BindView(R.id.tv_versionName)
    TextView tv_versionName;
    @BindView(R.id.versionName_text)
    TextView versionNameText;

    @BindView(R.id.tv_gfgf_title)
    TextView tv_gfgf_title;
    @BindView(R.id.rl_xxts)
    LinearLayout rl_xxts;
    @BindView(R.id.rl_lxkf)
    RelativeLayout rl_lxkf;
    @BindView(R.id.rl_gywm)
    RelativeLayout rl_gywm;
    @BindView(R.id.rl_bbgx)
    LinearLayout rl_bbgx;
    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.is_login_layout)
    LinearLayout isLoginLayout;
    @BindView(R.id.no_login_layout)
    LinearLayout noLoginLayout;
    @BindView(R.id.sign_up_text)
    TextView signUpText;
    @BindView(R.id.login_text)
    TextView loginText;
    @BindView(R.id.vip_level_bar)
    ProgressBar vipLevelBar;
    @BindView(R.id.user_vip_level_text)
    TextView userVipLevelText;
    @BindView(R.id.vip_level_last_text)
    TextView vipLevelLastText;
    @BindView(R.id.vip_level_next_text)
    TextView vipLevelNextText;
    @BindView(R.id.vip_level_last_point_text)
    TextView vipLevelLastPointText;
    @BindView(R.id.vip_level_next_point_text)
    TextView vipLevelNextPointText;
    @BindView(R.id.my_point_text)
    TextView myPointText;
    @BindView(R.id.updateText)
    TextView updateText;

    private UserInfo userInfo;
    private UserSelect userSelect;

    private List<Article> article_list= new ArrayList<Article>();
    private Article article_info;

    private Unbinder unbinder;

    private SubscriberOnNextListener getUserVipInfoNext;

    private String versionName;
    public static boolean isSettingLanguage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder=ButterKnife.bind(this, view);
        context = getActivity();
        initOnNext();
        initData();
        initView();
        sfdyc++;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        article_list=ArticleDao.getInstance().getIfon();
        if(is_token(userInfo)){
            MainActivity.getInstance().getUserInfo();
            btn_exit.setVisibility(View.VISIBLE);
            tv_user_name.setText(userInfo.getUserName());
            isLoginLayout.setVisibility(View.VISIBLE);
            noLoginLayout.setVisibility(View.GONE);
            getUserVipInfo();
        }else{
            btn_exit.setVisibility(View.GONE);
            tv_user_name.setText(R.string.user_dlzc);

            userInfo=null;
            noLoginLayout.setVisibility(View.VISIBLE);
            isLoginLayout.setVisibility(View.GONE);
        }
        if(article_list!=null&&article_list.size()>0) {
            article_info = article_list.get(0);
            tv_gfgf_title.setText(article_info.getTitle());
            tv_user_gfgg.setText("");
        }else{
            article_info=null;
            tv_gfgf_title.setText("");
            tv_user_gfgg.setText(getString(R.string.user_gfgg));
        }
//        if(!MainActivity.getInstance().getIsHaveNewVersion()) {
//            updateText.setText(getString(R.string.version_isnew));
//        }
        versionNameText.setText("V" + Utils.getPackageInfo(context).versionName);
    }
    private void initView() {
//        img_head_ico.setVisibility(View.VISIBLE);
        tv_head_title.setText(getString(R.string.user_wdxx));

//        btn_head_front.setVisibility(View.VISIBLE);
//        btn_head_front.setOnClickListener(this);
        ll_user_head.setOnClickListener(this);

        rl_aqzx.setOnClickListener(this);
        rl_bzphsz.setOnClickListener(this);
        rl_hlbz.setOnClickListener(this);
        rl_gfgg.setOnClickListener(this);
        rl_xxts.setOnClickListener(this);
        rl_lxkf.setOnClickListener(this);
        rl_gywm.setOnClickListener(this);
        rl_bbgx.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        loginText.setOnClickListener(this);
        isLoginLayout.setOnClickListener(this);
        tv_tel.setText(SystemConfig.TEL);

        PackageInfo info= AppContext.getInstance().getVersion();
        if(info==null){
            tv_versionName.setText("");
        }else{
            versionName=info.versionName;
            tv_versionName.setText(versionName);
        }
        userSelect=new UserSelect(context);
        userSelect.tv_select_title1.setText(getString(R.string.user_dskfz));
        userSelect.tv_select_title2.setText(getString(R.string.user_dskfz_dsfs));
        userSelect.ll_select_subject_3.setVisibility(View.VISIBLE);
        userSelect.ll_select_subject_4.setVisibility(View.VISIBLE);
        userSelect.bnt_select_subject_1.setText("BTC");
        userSelect.bnt_select_subject_2.setText("ETH");
        userSelect.bnt_select_subject_3.setText("ETC");
        userSelect.bnt_select_subject_4.setText("LTC");
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setOnClickListener(this);
        userSelect.bnt_select_subject_3.setOnClickListener(this);
        userSelect.bnt_select_subject_4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
//            case R.id.btn_head_front:
//                HomeDropdown homeDropdown=new HomeDropdown(context, DeviceUtil.dp2px(context,45.0f));
//                homeDropdown.show();
//                break;
            case R.id.ll_user_head:
                if(!is_token(userInfo)){
                    UIHelper.showLogin(context);
                    //UIHelper.showLoginOrRegister(context);
                }
                break;
            case R.id.rl_xxts:
//                if(is_token(userInfo)){
                    UIHelper.showSetting(context);
//                }else{
//                    UIHelper.showLogin(context);
//                }
                break;
            case R.id.rl_aqzx:
                if(is_token(userInfo)){
                    UIHelper.showSafetyCenter(context);
                }else{
                    UIHelper.showLogin(context);
                    //UIHelper.showLoginOrRegister(context);
                }
                break;
            case R.id.rl_lxkf:
                MainActivity.getInstance().testCall();
                break;
            case R.id.rl_bbgx:
                MainActivity.getInstance().checkUpdateVersion(true);
//                if(MainActivity.getInstance().getIsHaveNewVersion()) {
//                    SharedPreferences.getInstance().putBoolean("APP_NEED_VERSION_UP", true);
//                    MainActivity.getInstance().showUpdateDialog();
//                } else {
//                    UIHelper.ToastMessage(context,R.string.version_isnew);
//                }
                break;
            case R.id.rl_gfgg:
                if(article_info!=null){
                    String article_title=article_info.getTitle();
                    String article_link=article_info.getLink();
                    ArticleDao.getInstance().delect(article_info);
                    bundle.putString("title",article_title);
                    bundle.putString("url",article_link);
                }else{
                    bundle.putString("title",getString(R.string.user_gfgg));
                    bundle.putString("url",SystemConfig.getBlog());
                }
                bundle.putString("type","1");
//                UIHelper.showWeb(context,bundle);
                UIHelper.showAnnouncementList(context);
                break;
            case R.id.rl_gywm:
                bundle.putString("title",getResources().getString(R.string.user_gywm));
                bundle.putString("url", SystemConfig.getAbout());
                bundle.putString("type","1");
                UIHelper.showWeb(context,bundle);
                break;
            case R.id.bnt_commit:
                UIHelper.showFloatingWindow(context);
                break;
            case R.id.sign_up_text:
                UIHelper.showRegister(context);
                break;
            case R.id.login_text:
                UIHelper.showLogin(context);
                break;
            case R.id.is_login_layout:
                UIHelper.showUserVip(context);
                break;
            case R.id.btn_exit:
                showLogoutDialog();
                break;


        }

    }
    private int sfdyc=0;
    private boolean hidden;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden&&sfdyc>1) {
            initData();
        }
        sfdyc++;
    }

    private void showLogoutDialog(){
//        TextView tv = new TextView(context);
//        tv.setText(R.string.user_exit_title);    //内容
//        tv.setTextSize(DeviceUtil.px2sp(context,8));//字体大小
//        tv.setPadding(30, 20, 10, 10);//位置
////        tv.setTextColor(Color.parseColor("#ffffff"));//颜色

        //确认注销的对话框
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(R.string.user_exit_title)
//        .setCustomTitle(tv)
        .setPositiveButton(R.string.user_exit_no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        }).setNegativeButton(R.string.user_exit_yes, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                UserDao.getInstance().exit();
                UIHelper.ToastMessage(context, R.string.log_out_toast);
                initData();
                initView();
            }
        }).create();
        dialog.show();
        //利用反射调用隐藏代码，设置标题大小
        try {

            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object alertController = mAlert.get(dialog);

            Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);

            TextView title = (TextView) mTitleView.get(alertController);
            title.setTextSize(DeviceUtil.sp2px(context,23));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public void getUserVipInfo(){
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(getUserVipInfoNext, context);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(2).getUserVipInfo(progressSubscriber);
    }

    private void initOnNext() {
        getUserVipInfoNext = new SubscriberOnNextListener<UserVipInfo>(){
            @Override
            public void onNext(UserVipInfo userVipInfo) {
                vipLevelLastText.setText(getString(R.string.user_vip) + " " + userVipInfo.getCurrentRate());
                vipLevelNextText.setText(getString(R.string.user_vip) + " " + userVipInfo.getNextRate());
                vipLevelLastPointText.setText((int) userVipInfo.getCurrentRateBeginPoint() + "");
                myPointText.setText(getString(R.string.user_account_level_details_my_points) + (int)userVipInfo.getCurrentPoints());
                vipLevelNextPointText.setText((int) userVipInfo.getNextRateBeginPoint() + "");
                vipLevelBar.setMax(new BigDecimal(userVipInfo.getNextRateBeginPoint() - userVipInfo.getCurrentRateBeginPoint()).intValue());
                if(!userVipInfo.isFull()) {
                    userVipLevelText.setText(getString(R.string.user_vip) + " " + userVipInfo.getCurrentRate());
                    vipLevelBar.setProgress(new BigDecimal(userVipInfo.getCurrentPoints() - userVipInfo.getCurrentRateBeginPoint()).intValue());
                } else {
                    userVipLevelText.setText(getString(R.string.user_vip) + " " + (userVipInfo.getCurrentRate() + 1));
                    vipLevelBar.setProgress(vipLevelBar.getMax());
                }
            }
        };
    };

    @Override
    public void onResume() {
        super.onResume();
        if(isSettingLanguage) {
            isSettingLanguage = false;
            Intent intent = new Intent(context, SplashActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra(MainActivity.INTENT_EXTRA_IS_SETTING_LANGUAGE, true);
            startActivity(intent);
            context.finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                    System.exit(0);
        }
        if (!hidden&&sfdyc>1) {
            initData();
        }
        sfdyc++;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
