package com.android.bitglobal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BalanceResult;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-25 16:10
 * 793169940@qq.com
 * 扫描登录或扫描支付
 */
public class LoginOrPayActivity extends SwipeBackActivity implements View.OnClickListener {

    public static final String TYPE_NO_SCAN_PASTE = "noScanAndPaste";
    private int precision = SystemConfig.BALANCE_PRECISION;

    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.tv_home_content)
    TextView tv_home_content;

    @BindView(R.id.bnt_home_qrdl)
    Button bnt_home_qrdl;
    @BindView(R.id.bnt_home_ljdl)
    Button bnt_home_ljdl;
    @BindView(R.id.tv_home_cancel)
    TextView tv_home_cancel;


    @BindView(R.id.ll_home_dl)
    LinearLayout ll_home_dl;
    @BindView(R.id.ll_home_zf)
    LinearLayout ll_home_zf;
    @BindView(R.id.ll_home_zf_mydl)
    LinearLayout ll_home_zf_mydl;
    @BindView(R.id.ll_home_zf_yjdl)
    LinearLayout ll_home_zf_yjdl;

    private String type = "2", uid = "", qrcode = "";
    private String content = "";
    private UserInfo userInfo;
    //private UserAcount userAcount;
    private UserAcountResult userAcountResult;
    private CurrencySetResult currencySetResult;
    private QuickAdapter adapter;
    private List<BalanceResult> list_data = new ArrayList<BalanceResult>();
    private SubscriberOnNextListener mGetActivitysUrlOnNext, loginByQrcodeOnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_pay);
        context = this;
        ButterKnife.bind(this);
        initData();
        initView();
        initView2();
        sfdyc++;
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        content = bundle.getString("content");
        tv_home_content.setText(content);

        tv_head_title.setText(getString(R.string.asset_smewm));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_head_front.setVisibility(View.VISIBLE);
//        btn_head_front.setImageResource(R.mipmap.dropdown_scan_menu);
        btn_head_front.setOnClickListener(this);

        bnt_home_qrdl.setOnClickListener(this);
        bnt_home_ljdl.setOnClickListener(this);
        tv_home_cancel.setOnClickListener(this);
        mGetActivitysUrlOnNext = new SubscriberOnNextListener<ObjectResult>() {
            @Override
            public void onNext(ObjectResult ObjectResult) {
                uid = ObjectResult.getUserOpenId();
                type = "1";
                loginByQrcode();
            }
        };
        loginByQrcodeOnNext = new SubscriberOnNextListener<HttpResult<ObjectResult>>() {
            @Override
            public void onNext(HttpResult<ObjectResult> httpResult) {
                if (SystemConfig.SUCCESS.equals(httpResult.getResMsg().getCode())) {
                    if (!"1".equals(type)) {
                        finish();
                    }
                } else {
                    if ("3".equals(type)) {
                        finish();
                    } else {
                        try {
                            UIHelper.ToastMessage(context, httpResult.getResMsg().getMessage());
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };
        adapter = new QuickAdapter<BalanceResult>(context, R.layout.home_asset_item, list_data) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BalanceResult item) {
//                helper.setText(R.id.tv_home_ky_hint, getString(R.string.scan_login_available)
//                        + " " + format(item.getBalance(), Utils.getPrecisionPrice(item.getPropTag())));
                helper.setText(R.id.tv_home_ky_hint, getString(R.string.scan_login_available)
                        + " " + format(item.getBalance(), precision));
                String propTag = item.getPropTag();
                helper.setText(R.id.tv_home_ky, propTag);
                if(propTag.equals("GBC")) {
                    helper.setTextColor(R.id.tv_home_ky, ContextCompat.getColor(context, R.color.text_color_gray));
                }
                if(propTag.equals("USDC")) {
                    helper.setTextColor(R.id.tv_home_ky, ContextCompat.getColor(context, R.color.text_color_gray));
                }
//                try {
//                    for (CurrencySetRealm csr : currencySetResult.getCurrencySets()) {
//                        if (csr.getCurrency().equals(item.getPropTag())) {
//                            helper.setImageUrl(R.id.img_home_type, csr.getFinanceCoinUrl());
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String propTag = list_data.get(position).getPropTag();
                if(!propTag.equals("GBC") && !propTag.equals("USDC")) {
                    try {
//                    if (userInfo.getGoogleAuth() != null && userInfo.getGoogleAuth().equals("0")) {
//                        UIHelper.showGoogleAuth(LoginOrPayActivity.this, "1");
//                        UIHelper.ToastMessage(context, context.getString(R.string.safety_on_open_google));
//                        return;
//                    }
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType", list_data.get(position).getPropTag());
                        bundle.putString("availableAmount", format(list_data.get(position).getBalance(), precision));
                        bundle.putString("address", content);
                        bundle.putString(TYPE_NO_SCAN_PASTE, TYPE_NO_SCAN_PASTE);
                        UIHelper.showCurrencyWithdrawFromScan(LoginOrPayActivity.this, bundle);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initView2() {
        Map kv = new HashMap();
        kv.put("内容", content + "");
        TCAgent.onEvent(context, "扫一扫", "扫一扫", kv);
        if ((content.indexOf("http://") != -1 || content.indexOf("https://") != -1) && content.indexOf("qrcode=") > 5) {
            qrcode = content.substring(content.lastIndexOf("=") + 1, content.length());
            ll_home_dl.setVisibility(View.VISIBLE);
            ll_home_zf.setVisibility(View.GONE);
            tv_head_title.setText(getString(R.string.home_smdl));
            if (is_token(userInfo)) {
                getOpenId();
            }
        } else {
            ll_home_dl.setVisibility(View.GONE);
            ll_home_zf.setVisibility(View.VISIBLE);
            tv_head_title.setText(getString(R.string.asset_smewm));
        }
        if (is_token(userInfo)) {
            ll_home_zf_mydl.setVisibility(View.GONE);
            ll_home_zf_yjdl.setVisibility(View.VISIBLE);
            userAcountResult = UserAcountDao.getInstance().getIfon(userInfo.getUserId());
            if (userAcountResult != null) {
                list_data.clear();
                for (BalanceResult item : userAcountResult.getUserAccount().getBalances()) {
                    list_data.add(item);
                }


               /* for (AssetsBalance item:userAcount.getBalances()){
                    if(!item.getCurrencyType().equals("CNY")){
                        list_data.add(item);
                    }
                }*/
                adapter.replaceAll(list_data);
            }
        } else {
            ll_home_zf_mydl.setVisibility(View.VISIBLE);
            ll_home_zf_yjdl.setVisibility(View.GONE);
        }
    }

    private void initData() {
        userInfo = UserDao.getInstance().getIfon();
        currencySetResult = CurrencySetDao.getInstance().getIfon();
        if (is_token(userInfo)) {
            userAcountResult = UserAcountDao.getInstance().getIfon(userInfo.getUserId());
            if (userAcountResult == null) {
                handler.postDelayed(runnable, 500);
                return;
            }
            MainActivity.getInstance().getUserInfo();
        }
    }

    private void getOpenId() {
        HttpMethods.getInstance(3).getUserOpenId(new ProgressSubscriber(mGetActivitysUrlOnNext, this));
    }

    private void loginByQrcode() {
        HttpMethods.getInstance(3).loginByQrcode(new ProgressSubscriber(loginByQrcodeOnNext, this), uid, type, qrcode);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            initData();
            initView2();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.tv_home_cancel:
                if (is_token(userInfo)) {
                    type = "3";
                    loginByQrcode();
                } else {
                    finish();
                }
                break;
            case R.id.btn_head_front:
                AppContext.customScan(context);
                break;
            case R.id.bnt_home_qrdl:
                /*if(is_token(userInfo)){
                    type="2";
                    loginByQrcode();
                }else{
                    UIHelper.showLoginOrRegister(context);
                }*/
                break;
            case R.id.bnt_home_ljdl:
                UIHelper.showLogin(context);
                break;

        }

    }

    private int sfdyc = 0;

    @Override
    public void onResume() {
        super.onResume();
        if (sfdyc > 1) {
            initData();
            initView2();
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
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //UIHelper.ToastMessage(context,getString(R.string.asset_nrwk));
            } else {
                content = intentResult.getContents();
                tv_home_content.setText(content);
                initData();
                initView2();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}


