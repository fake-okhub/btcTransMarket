package com.android.bitglobal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.user.SafetyIdentityAuth;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.AppVersionResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.UserInfoResult;
import com.android.bitglobal.fragment.BalanceFragment;
import com.android.bitglobal.fragment.HomeFragment;
import com.android.bitglobal.fragment.UserFragment;
import com.android.bitglobal.fragment.trans.ExchangeNewFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.UserConfirm;
import com.android.bitglobal.view.keyboardView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURR_INDEX = "currIndex";
    public static final String INTENT_EXTRA_IS_SETTING_LANGUAGE = "isSettingLanguage";
    public static int currIndex = 0;
    public RadioGroup group;
    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;
    private SubscriberOnNextListener getuserInfoOnNext, getfundsOnNext, mCheckUpdateOnNext,
            mCheckActivitysOnNext, mGetActivitysUrlOnNext;
    private SubscriberOnNextListener mRegistationIDOnNextListener;
    private UserInfo userInfo;
    private int versionCode;
    private boolean mIsHaveNewVersion = false;
    private boolean isSettingLanguage = false;
    private String versionName, mUpdateURL, mDescription, mTitle, mSize, mVersion = "1";
    private boolean mReleased;
    public String framgentSelected = "1", title = "", url = "", isInvolved = "", url2 = "", userOpenId2 = "", activityId = "", shareUrl = "", shareImg = "", shareDes = "", mTopUrl = "";
    private String uid = "";
    private static MainActivity activity;
    public static AlertDialog dialog;

    public static MainActivity getInstance() {
        return activity;
    }

    public String getUrl() {
        return url;
    }

    public String getActivitysTitle() {
        return title;
    }

    public boolean getIsHaveNewVersion() {
        return mIsHaveNewVersion;
    }

    public String getUpdateURL() {
        return mUpdateURL;
    }

    public String getDescription() {
        return mDescription;
    }

    //用来区分推送消类型的提示框  0代表更新dialog
    private String mType = "0";
    private UserConfirm mDialog;
    private String host = "";
    public keyboardView mkeyboardView;

    private UserAcountResult mUserAcountResult = null;

    public UserAcountResult getmUserAcountResult() {
        return mUserAcountResult;
    }

    private BalanceFragment balanceFragment;

    //用来判断活动dialog是否显示过
    private static boolean isShowActivityDialog = false;
    //判断是否其他页面更新
    private boolean isOtherUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        fragmentManager = getSupportFragmentManager();

        initOnNext();
        initData(savedInstanceState);
        initView();
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {
            getUserInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public keyboardView getKeyboardView() {
        return mkeyboardView;
    }

    public void showKeyboardView(boolean show) {
        if (show) {
            mkeyboardView.setVisibility(View.VISIBLE);
        } else {
            mkeyboardView.setVisibility(View.GONE);
        }

    }

    public void showDialog(String type, String msg) {
        mType = type;
//        mDialog = new UserConfirm(activity);
//        mDialog.ed_user_safePwd.setVisibility(View.GONE);
//        mDialog.tv_user_title1.setText(activity.getString(R.string.push_hint));
//        if ("0".equals(mType)) {
//            mDialog.tv_user_title1.setText(activity.getString(R.string.user_bbgx));
//        }
//        mDialog.bnt_user_commit.setOnClickListener(MainActivity.this);
//        mDialog.bnt_user_cancel.setOnClickListener(MainActivity.this);
//        mDialog.tv_user_title2.setText(msg);
//        mDialog.show();
        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_confirm, null);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(root);
        dialog.setCancelable(false);
        TextView description = (TextView) root.findViewById(R.id.dialog_confirm_content);
        TextView cancel = (TextView) root.findViewById(R.id.dialog_confirm_cancel);
        description.setText(msg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView confirm = (TextView) root.findViewById(R.id.dialog_confirm_confirm);
        confirm.setText(getString(R.string.dialog_confirm));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if ("1".equals(type)) {
            confirm.setText(getString(R.string.dialog_close));
            cancel.setVisibility(View.INVISIBLE);
        }
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
    }

    private void initData(Bundle savedInstanceState) {
        host = getIntent().getStringExtra("host");
        isSettingLanguage = getIntent().getBooleanExtra(INTENT_EXTRA_IS_SETTING_LANGUAGE, false);
        fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "ExchangeNewFragment", "AccountFragment", "UserFragment"));
        currIndex = 0;
        if (savedInstanceState != null) {
            currIndex = savedInstanceState.getInt(CURR_INDEX);
            hideSavedFragment();
        }
        userInfo = UserDao.getInstance().getIfon();


    }

    public void getUserInfo() {
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(getuserInfoOnNext, this);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(2).getUserInfo(progressSubscriber);
    }

    public void getfunds() {
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {

            setRegistrationID();
            ProgressSubscriber progressSubscriber = new ProgressSubscriber(getfundsOnNext, this);
            progressSubscriber.setIs_progress_show(false);
            progressSubscriber.setIs_showMessage(false);
            HttpMethods.getInstance(2).getfunds(progressSubscriber, SpTools.getlegalTender());
        }
    }

    private void hideSavedFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    public void initView() {
        mkeyboardView = (keyboardView) findViewById(R.id.keyboardView);
        group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showKeyboardView(false);
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        currIndex = 0;
                        break;
                    case R.id.foot_bar_trans:
                        currIndex = 1;
                        break;
                    case R.id.foot_bar_asset:
                        currIndex = 2;
                        break;
                    case R.id.foot_bar_user:
                        currIndex = 3;
                        break;
                    default:
                        break;
                }
                showFragment();
            }
        });
        showFragment();
        if ("market".equals(host)) {
            currIndex = 0;
            //showFragment();
        }
        if ("exchange".equals(host)) {
            currIndex = 1;
            ((RadioButton) group.findViewById(R.id.foot_bar_home)).setChecked(false);
            ((RadioButton) group.findViewById(R.id.foot_bar_trans)).setChecked(true);
            showFragment();
        }
        if ("scan".equals(host)) {
            AppContext.customScan(MainActivity.getInstance());
        }

        checkUpdateVersion(false);
        //是否打开悬浮窗体
        setIsOpenFloatWindows();

        mRegistationIDOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult objectResult) {
            }
        };
    }

    private void initOnNext() {
        getuserInfoOnNext = new SubscriberOnNextListener<UserInfoResult>() {
            @Override
            public void onNext(UserInfoResult userInfoResult) {
                UserInfo user_info = userInfoResult.getUserInfo();
                user_info.setToken(userInfo.getToken());
                user_info.setIs_online("1");
                UserDao.getInstance().setIfon(user_info);
                userInfo = UserDao.getInstance().getIfon();
                getfunds();
            }
        };
        getfundsOnNext = new SubscriberOnNextListener<UserAcountResult>() {
            @Override
            public void onNext(UserAcountResult userAcountResult) {
                userAcountResult.setUserId(userInfo.getUserId());
                UserAcountDao.getInstance().setIfon(userAcountResult);
                balanceFragment.refreshList(userAcountResult);
                // UserAcount userAcount=userAcountResult.getUserAccount();
                //  userAcount.setUserId(userInfo.getUserId());
                // UserAcountDao.getInstance().setIfon(userAcount);
            }
        };
        mCheckUpdateOnNext = new SubscriberOnNextListener<AppVersionResult>() {
            @Override
            public void onNext(AppVersionResult appVersionResult) {
                try {
                    if (appVersionResult.getVersion() != null) {
                        if (getVersionNameInt(versionName) < getVersionNameInt(appVersionResult.getVersion())) {
                            mUpdateURL = appVersionResult.getUrl();
                            if(appVersionResult.isEnforceUpdate()) {
                                showEnforceUpdateDialog();
                            } else {
                                mIsHaveNewVersion = true;
                                mVersion = appVersionResult.getVersion();
                                mDescription = appVersionResult.getContent();
                                mTitle = appVersionResult.getTitle();
                                mSize = appVersionResult.getSize();
                                mReleased = appVersionResult.isReleased();
                                if (!isSettingLanguage) {
                                    showUpdateDialog();
                                }
                            }
                        } else {
                            if(isOtherUpdate) {
                                UIHelper.ToastMessage(MainActivity.this, R.string.version_isnew);
                                isOtherUpdate = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private int getVersionNameInt(String versionName) {
        String[] versionNameNow = versionName.split("\\.");
        String versionNameNowString = "";
        for(int i = 0; i < versionNameNow.length; i ++) {
            versionNameNowString += versionNameNow[i];
        }
        if(versionNameNow.length == 2) {
            versionNameNowString += "0";
        }
        return Integer.parseInt(versionNameNowString);
    }

    public void showFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return new HomeFragment();
            case 1:
//                return new TransFragment();
                return new ExchangeNewFragment();
            case 2:
                balanceFragment = new BalanceFragment();
                return balanceFragment;
            case 3:
                return new UserFragment();
            default:
                return null;
        }
    }

    public void showUpdateDialog() {
        if (!isOtherUpdate) {
            if (!SharedPreferences.getInstance().getBoolean("APP_NEED_VERSION_UP", true)
                    && SharedPreferences.getInstance().getString("APP_NEED_VERSION_UP_NUM", "").equals(mVersion)) {
                return;
            }
        }
        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_update, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(root);
        dialog.setCancelable(false);
        TextView updateTitleText = (TextView) root.findViewById(R.id.updateTitleText);
        TextView versionText = (TextView) root.findViewById(R.id.versionText);
        TextView sizeText = (TextView) root.findViewById(R.id.sizeText);
        TextView contentText = (TextView) root.findViewById(R.id.contentText);

        updateTitleText.setText(mTitle);
        versionText.setText(getString(R.string.user_update_version) + mVersion);
        sizeText.setText(getString(R.string.user_update_size) + mSize);
        contentText.setText(mDescription);

        root.findViewById(R.id.cancelText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences.getInstance().putBoolean("APP_NEED_VERSION_UP", false);
                SharedPreferences.getInstance().putString("APP_NEED_VERSION_UP_NUM", mVersion);
            }
        });
        root.findViewById(R.id.updateText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mUpdateURL);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showEnforceUpdateDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_confirm, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(root);
        dialog.setCancelable(false);
        TextView contentText = (TextView) root.findViewById(R.id.dialog_confirm_content);
        TextView confirmText = (TextView) root.findViewById(R.id.dialog_confirm_confirm);

        contentText.setText(R.string.user_enforce_update_version);
        confirmText.setText(R.string.user_enforce_update_version_confirm_button);

        root.findViewById(R.id.dialog_confirm_cancel).setVisibility(View.GONE);

        root.findViewById(R.id.dialog_confirm_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mUpdateURL);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        dialog.show();
    }

    public void checkUpdateVersion(boolean isOtherUpdate) {
        PackageInfo info = getVersion();
        if (info != null) {
            versionCode = info.versionCode;
            versionName = info.versionName;
            Log.i("versionCode=versionName", versionCode + "===" + versionName);
        }
        this.isOtherUpdate = isOtherUpdate;
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(mCheckUpdateOnNext, this);
        progressSubscriber.setIs_progress_show(isOtherUpdate);
        HttpMethods.getInstance(3).updateAppVersion(progressSubscriber);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mkeyboardView.getVisibility() == View.VISIBLE) {
                showKeyboardView(false);
                return true;
            }
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void testCall() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 200);
        } else {
            callPhone();
        }
    }

    public void callPhone() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + SystemConfig.TEL));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    callPhone();
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    UIHelper.ToastMessage(activity, R.string.safety_myqkqx_toast);
                }

        }

    }

    private void setIsOpenFloatWindows() {
        if (SharedPreferences.getInstance().getBoolean("is_open_window", false)) {
            Intent intent = new Intent(activity, FloatService.class);
            startService(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnt_user_cancel:
            case R.id.bnt_user_commit:
                mDialog.dismiss();
                switch (mType) {
                    case "0":
                        if (view.getId() == R.id.bnt_user_cancel)
                            break;
                        Uri uri = Uri.parse(mUpdateURL);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                        break;
                    case "2":
                        if (view.getId() == R.id.bnt_user_cancel)
                            break;
                        Intent i = new Intent(activity, SetPriceActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                        break;
                    case "7":
                        if (view.getId() == R.id.bnt_user_cancel)
                            break;
                        Intent mNameProve = new Intent(activity, SafetyIdentityAuth.class);
                        mNameProve.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(mNameProve);
                        break;
                    case "1":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "8":
                    case "9":
                        break;
                }
                break;
        }
    }


    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //UIHelper.ToastMessage(context,getString(R.string.asset_nrwk));
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("content", intentResult.getContents());
                UIHelper.showLoginOrPay(MainActivity.getInstance(), bundle);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setRegistrationID() {
        if (SharedPreferences.getInstance().getBoolean(SharedPreferences.PUSH_OPEN, true)) {
            String registrationID = JPushInterface.getRegistrationID(this);
            ProgressSubscriber progressSubscriber = new ProgressSubscriber(mRegistationIDOnNextListener, this);
            progressSubscriber.setIs_progress_show(false);
            HttpMethods.getInstance(3).setRegistrationID(progressSubscriber, registrationID);
            JPushInterface.resumePush(this);
        } else {
            JPushInterface.stopPush(this);
        }

    }

    protected void onResume() {
        super.onResume();
        try {
            JPushInterface.onResume(this);
        } catch (Exception e) {

        }
    }

    protected void onPause() {
        super.onPause();
        try {
            JPushInterface.onPause(this);
        } catch (Exception e) {

        }

    }


}
