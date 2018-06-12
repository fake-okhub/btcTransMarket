package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.SplashActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.GetCurrencyResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.ResMsg;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.UserFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import rx.Subscriber;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 * 消息推送
 */
public class SettingActivity extends SwipeBackActivity implements View.OnClickListener {
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.languageText)
    TextView languageText;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.view_switch)
    SwitchView mSwitchView;
    @BindView(R.id.view_switch2)
    SwitchView mSwitchView2;
    @BindView(R.id.changeLanguage)
    RelativeLayout changeLanguage;
    @BindView(R.id.tv_usermp_set_usd)
    TextView mTvUsermpSetUsd;
    @BindView(R.id.rl_usermp_set_currency)
    RelativeLayout mRlSetCurrency;
    private String registrationID;
    private SubscriberOnNextListener mRegistationIDOnNextListener;
    private Subscriber setLanNextListener;
    private boolean isOpened = false, isOpened2 = false;
    private UserInfo userInfo;


    private ArrayList<String> mCurrencys=new ArrayList<>();
    private int choiceWhich;
    private String defaultCurrency;
    boolean isNetErrorClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        context = this;

        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        userInfo  = UserDao.getInstance().getIfon();
        tv_head_title.setText(R.string.user_settings);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        changeLanguage.setOnClickListener(this);
        mRlSetCurrency.setOnClickListener(this);
        findViewById(R.id.tv_test_hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,FeedbackActivity.class));
            }
        });
        getSupportLegalTender();
        if (SystemConfig.SIMPLIFIED_CHINESE.equals(language)) {
            languageText.setText(getString(R.string.zh));
        } else if (Locale.TRADITIONAL_CHINESE.getCountry().equals(language)) {
            languageText.setText(getString(R.string.zh_rTW));
        } else if (SystemConfig.ENGLISH.equals(language)) {
            languageText.setText(getString(R.string.en));
        }

//        isOpened= SharedPreferences.getInstance().getBoolean(userInfo.getUserId() + "push_open",true);
        isOpened= SharedPreferences.getInstance().getBoolean(SharedPreferences.PUSH_OPEN,true);
        mSwitchView.setOpened(isOpened);
        mSwitchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
//                setRegistrationID();
                mSwitchView.setOpened(!mSwitchView.isOpened());
                SharedPreferences.getInstance().putBoolean(SharedPreferences.PUSH_OPEN, mSwitchView.isOpened());
                setJPush(mSwitchView.isOpened());
            }

            @Override
            public void toggleToOff(View view) {
//                setRegistrationID();
                mSwitchView.setOpened(!mSwitchView.isOpened());
                SharedPreferences.getInstance().putBoolean(SharedPreferences.PUSH_OPEN, mSwitchView.isOpened());

                setJPush(mSwitchView.isOpened());
            }
        });
//        mSwitchView2.setOpened(isOpened2);
//        mSwitchView2.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn(View view) {
//                mSwitchView2.toggleSwitch(true);
//                SharedPreferences.getInstance().putBoolean("price_warning", true);
//            }
//
//            @Override
//            public void toggleToOff(View view) {
//                mSwitchView2.toggleSwitch(false);
//                SharedPreferences.getInstance().putBoolean("price_warning", false);
//            }
//        });
        mRegistationIDOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult objectResult) {
                ResMsg res = objectResult.getResMsg();
                String code = res.getCode();
                if (code.equals(SystemConfig.SUCCESS)) {
                    mSwitchView.setOpened(!mSwitchView.isOpened());
                    SharedPreferences.getInstance().putBoolean(SharedPreferences.PUSH_OPEN, mSwitchView.isOpened());
                    //UIHelper.ToastMessage(context,R.string.push_success);
                } else {
                    mSwitchView.setOpened(mSwitchView.isOpened());
                    SharedPreferences.getInstance().putBoolean(SharedPreferences.PUSH_OPEN, mSwitchView.isOpened());
                    //UIHelper.ToastMessage(context,R.string.push_failure);
                }
                setJPush(mSwitchView.isOpened());

            }
        };
        setLanNextListener = new Subscriber<HttpResult>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult httpResult) {

            }

        };
    }

    private void setJPush(boolean isOpen) {
        if (isOpen) {
            JPushInterface.resumePush(context);
        } else {
            JPushInterface.stopPush(context);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.changeLanguage:
                showSelectLanguageDialog();
                break;
            case R.id.rl_usermp_set_currency:
                clickSetCurrency();
                break;
        }
    }

    private void showSelectLanguageDialog() {
        try {
            LayoutInflater inflater = getLayoutInflater();
            View root = inflater.inflate(R.layout.dialog_select_language, null);
            RadioGroup languageRadioGroup = (RadioGroup) root.findViewById(R.id.languageRadioGroup);
            final RadioButton zhRadioButton = (RadioButton) root.findViewById(R.id.zhRadioButton);
            final RadioButton twRadioButton = (RadioButton) root.findViewById(R.id.twRadioButton);
            final RadioButton enRadioButton = (RadioButton) root.findViewById(R.id.enRadioButton);
            if (SystemConfig.SIMPLIFIED_CHINESE.equals(language)) {
                languageRadioGroup.check(zhRadioButton.getId());
            } else if (Locale.TRADITIONAL_CHINESE.getCountry().equals(language)) {
                languageRadioGroup.check(twRadioButton.getId());
            } else if ("EN".equals(language)) {
                languageRadioGroup.check(enRadioButton.getId());
            }
            languageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (zhRadioButton.getId() == checkedId) {
                        //zh
                        SharedPreferences.getInstance().putString(SharedPreferences.APP_LANGUAGE, SystemConfig.SIMPLIFIED_CHINESE);
                        HttpMethods.getInstance(3).setSystemLanguage(setLanNextListener, "CN");
                        languageText.setText(getString(R.string.zh));
                    } else if (twRadioButton.getId() == checkedId) {
                        //TW
                        SharedPreferences.getInstance().putString(SharedPreferences.APP_LANGUAGE, Locale.TRADITIONAL_CHINESE.getCountry());
                        HttpMethods.getInstance(3).setSystemLanguage(setLanNextListener, "HK");
                        languageText.setText(getString(R.string.zh_rTW));
                    } else if (enRadioButton.getId() == checkedId) {
                        //en
                        SharedPreferences.getInstance().putString(SharedPreferences.APP_LANGUAGE, SystemConfig.ENGLISH);
                        HttpMethods.getInstance(3).setSystemLanguage(setLanNextListener, "EN");
                        languageText.setText(R.string.en);
                    }
                    dismiss();
                    finish();
                    UserFragment.isSettingLanguage = true;
                }
            });
            alert(root, true);
        } catch (NullPointerException e) {
            e.printStackTrace();


        }
    }

    private void setRegistrationID() {
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(mRegistationIDOnNextListener, SettingActivity.this);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).setRegistrationID(progressSubscriber, registrationID);
    }


    private void clickSetCurrency() {
        if (mCurrencys.isEmpty()) {
            isNetErrorClick=true;
            UIHelper.ToastMessage(SettingActivity.this, getString(R.string.app_network_interruption_toast));
        } else {
            currencyDialog();
        }
        if (isNetErrorClick){
            getSupportLegalTender();
        }


    }

    //创建设置法币对话框
    private void currencyDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_select_currency, null);
        final RadioGroup currencyRadioG = (RadioGroup) root.findViewById(R.id.radioG_setting_currency);
        for(int i=0;i<mCurrencys.size();i++) {
            String currency= mCurrencys.get(i);
            final RadioButton tempButton = new RadioButton(this);
            tempButton.setBackground(null);   // 设置RadioButton的背景图片  null
            tempButton.setButtonDrawable(getDrawable(android.R.color.transparent));
            //设置RadioButton  drawableLeft 单选状态选择器
            tempButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.language_radio_select), null, null, null);
            //设置RadioButton 与 text 之间的间距
            tempButton.setCompoundDrawablePadding(DeviceUtil.dp2px(context, 20));
            tempButton.setText(currency);
            //设置RadioButton 与 父控件之间的间距
            tempButton.setPadding(DeviceUtil.dp2px(context, 15), DeviceUtil.dp2px(context, 15), 0, 0);
            tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tempButton.setGravity(Gravity.CENTER);
            tempButton.setTag(currency);
            tempButton.setId(1011+i);
            //如果是默认选择的法币，直接选中
            if (currency.equals(defaultCurrency)){
                tempButton.setChecked(true);
//                currencyRadioG.check(tempButton.getId());
            }
            tempButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    //在点击瞬间遍历孩子让其取消点击，然后再选择点击选项。避免视觉误差,暂时还没解决
                    for (int i = 0; i < currencyRadioG.getChildCount(); i++) {
                        RadioButton rb= (RadioButton) currencyRadioG.getChildAt(i);
                        rb.setChecked(false);
                    }
//                    tempButton.setChecked(true);
                    mTvUsermpSetUsd.setText(tempButton.getText());
//                    setNetCurrency((String) tempButton.getText());
                    defaultCurrency=(String) tempButton.getText();
                    SpTools.setlegalTender((String) tempButton.getText());
                }
            });

//            currencyRadioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                    for (int i = 0; i < group.getChildCount(); i++) {
//                        RadioButton rb= (RadioButton) group.getChildAt(i);
//                        rb.setChecked(false);
//                    }
//                }
//            });
            currencyRadioG.addView(tempButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        alert(root, true);
    }

    //设置法币
    private void setNetCurrency(final String choiceItem) {
        HttpMethods.getInstance(3).setLegalTender(new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult httpResult) {
                if (SystemConfig.SUCCESS.equals(httpResult.getResMsg().getCode())) {
                    //说明设置法币成功
                    L.d("设置法币成功");
                }
            }
        }, choiceItem);
//        defaultCurrency=choiceItem;
    }

    //获取支持的折算货币
    private void getSupportLegalTender() {
        defaultCurrency=SpTools.getlegalTender();
        mTvUsermpSetUsd.setText(defaultCurrency);
        HttpMethods.getInstance(3).getSupportLegalTender(new Subscriber<HttpResult<GetCurrencyResult>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                isNetErrorClick=true;
            }

            @Override
            public void onNext(HttpResult<GetCurrencyResult> listHttpResult) {
                isNetErrorClick=false;
                mCurrencys = (ArrayList<String>) listHttpResult.getDatas().getLegal_tender();
//                defaultCurrency = listHttpResult.getDatas().getDefaultX();
//                if (!TextUtils.isEmpty(defaultCurrency)){
//                    mTvUsermpSetUsd.setText(defaultCurrency);
//                    if (!mCurrencys.isEmpty() && mCurrencys.size()>0){
//                        choiceWhich = mCurrencys.indexOf(defaultCurrency);
//                    }
//                }
            }
        });

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
