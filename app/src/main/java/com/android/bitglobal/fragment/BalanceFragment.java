package com.android.bitglobal.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BalanceResult;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.manager.ExchangeNewSubManager;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.PriceUtils;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.HomeDropdown;
import com.android.bitglobal.view.UserConfirm;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by bitbank on 16/12/25.
 */
public class BalanceFragment extends BaseFragment implements View.OnClickListener{

    private static final int PRECISION = 6;

    private static final int LegalTenderPRECISION = 2;

    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
//    @BindView(R.id.btn_head_front)
//    ImageView btn_head_front;

//    @BindView(R.id.img_head_ico)
//    ImageView img_head_ico;

    @BindView(R.id.nologin_relative)
    LinearLayout nologin_relative;

    @BindView(R.id.login_linearlayout)
    LinearLayout login_linearlayout;

    @BindView(R.id.totalAmount)
    TextView tv_TotalAmount;
    @BindView(R.id.tv_balance_account_symbol)
    TextView tv_balance_account_symbol;

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.sign_up_text)
    TextView signUpText;

    @BindView(R.id.login_text)
    TextView loginText;


    UserConfirm userConfirm;
    CurrencySetResult currencySetResult;

    private UserAcountResult userAcountResult;

    UserInfo userInfo;
    QuickAdapter<BalanceResult> adapter;
    private Unbinder unbinder;

    private String totalAmount;
    private ExchangeNewSubManager coinManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accout, container, false);
        unbinder= ButterKnife.bind(this, view);
        context = getActivity();
        initView();
        initViewData();
        initNetData();
        sfdyc++;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            if(userAcountResult==null){
                handler.postDelayed(runnable, 500);
                return;
            }
        }
    }

    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        currencySetResult= CurrencySetDao.getInstance().getIfon();
        if(is_token(userInfo)){
            login_linearlayout.setVisibility(View.VISIBLE);
            nologin_relative.setVisibility(View.GONE);
             userAcountResult= UserAcountDao.getInstance().getIfon(userInfo.getUserId());

            initNetData();
             MainActivity.getInstance().getUserInfo();
        }else
        {   login_linearlayout.setVisibility(View.GONE);
            nologin_relative.setVisibility(View.VISIBLE);
            tv_head_title.setText(getString(R.string.account_balance_title));
            signUpText.setOnClickListener(this);
            loginText.setOnClickListener(this);
        }
    }

    private void initNetData(){
        Subscriber netSubscriber = new Subscriber<UserAcountResult>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (userAcountResult != null) {
                    totalAmount=userAcountResult.getTotalAmount();
                    String finalAmout=PriceUtils.carryNumber(PriceUtils.getDefNum(userAcountResult.getConvertFund()),LegalTenderPRECISION);
                    tv_TotalAmount.setText(coinManager.getCoinSymbol()+" "+finalAmout);
                    tv_balance_account_symbol.setText(SpTools.getlegalTender());
                }
            }

            @Override
            public void onNext(UserAcountResult userAcountResult) {
                totalAmount=userAcountResult.getTotalAmount();
                String finalAmout=PriceUtils.carryNumber(PriceUtils.getDefNum(userAcountResult.getConvertFund()),LegalTenderPRECISION);
                tv_TotalAmount.setText(coinManager.getCoinSymbol()+" "+finalAmout);
                tv_balance_account_symbol.setText(SpTools.getlegalTender());
            }

        };
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {
            HttpMethods.getInstance(2).getfunds(netSubscriber, SpTools.getlegalTender());
        }
    }

    private void initView() {
//        img_head_ico.setVisibility(View.VISIBLE);
//        tv_head_title.setVisibility(View.VISIBLE);
//        tv_head_title.setText(getString(R.string.user_wdzc));
        coinManager=ExchangeNewSubManager.getInstance();
        userConfirm=new UserConfirm(context);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.bnt_user_commit.setOnClickListener(this);
//        btn_head_front.setVisibility(View.VISIBLE);
//        btn_head_front.setOnClickListener(this);


        adapter = new QuickAdapter<BalanceResult>(context, R.layout.fragment_accout_list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BalanceResult item) {

//                String url=item.getCoinUrl();
//                if(!item.getCoinUrl().equals(""))
//                {
//                    helper.setImageUrl(R.id.img_type, item.getCoinUrl());
//                }
                helper.setText(R.id.propTag,item.getPropTag());
                helper.setText(R.id.propTag_all_text, item.getCoinFullNameEn());
                if("0".equals(deFormat(item.getTotal(), PRECISION))) {
                    helper.setText(R.id.total, "0.000000");
                } else {
                    helper.setText(R.id.total, format(item.getTotal(), PRECISION));
                }
                if("0".equals(deFormat(item.getBalance(), PRECISION))) {
                    helper.setText(R.id.balance, getString(R.string.account_available) + "0.000000");
                } else {
                    helper.setText(R.id.balance, getString(R.string.account_available) + format(item.getBalance(), PRECISION));
                }
                if("0".equals(deFormat(item.getFreeze(), PRECISION))) {
                    helper.setText(R.id.freeze, getString(R.string.account_on_orders) + "0.000000");
                } else {
                    helper.setText(R.id.freeze, getString(R.string.account_on_orders) + format(item.getFreeze(), PRECISION));
                }
                helper.setOnClickListener(R.id.relative_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",item.getPropTag());
                        bundle.putString("totalAmount", totalAmount);
                        bundle.putString("availableAmount",deFormat(item.getBalance(),6));
                        UIHelper.showBalance(getActivity(),bundle);
                    }
                });

                //充值
                helper.setOnClickListener(R.id.linearLayout_recharge, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",item.getPropTag());
                        bundle.putString("totalAmount",totalAmount);
                        bundle.putString("availableAmount",deFormat(item.getBalance(),6));
                        bundle.putInt("tabIndex", UIHelper.BALANCE_RECHARGE);
                        UIHelper.showBalance(getActivity(),bundle);
                    }
                });

                //提现
                helper.setOnClickListener(R.id.linearLayout_withdraw, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (userInfo.getGoogleAuth() != null && userInfo.getGoogleAuth().equals("0")) {
//                            UIHelper.showGoogleAuth(getActivity(), "1");
//                            UIHelper.ToastMessage(context, context.getString(R.string.safety_on_open_google));
//                            return;
//                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",item.getPropTag());
                        bundle.putString("totalAmount",totalAmount);
                        bundle.putString("availableAmount",deFormat(item.getBalance(),6));
                        bundle.putInt("tabIndex", UIHelper.BALANCE_WITHDRAW);
                        UIHelper.showBalance(getActivity(),bundle);

                    }
                });
                LinearLayout detailLinearLayout = helper.getView(R.id.relative_detail);
                Button rechargeButton = helper.getView(R.id.linearLayout_recharge);
                Button withButton = helper.getView(R.id.linearLayout_withdraw);
                //GBC USDC不能点
                if("GBC".equals(item.getPropTag()) || "USDC".equals(item.getPropTag())) {
                    helper.setBackgroundColor(R.id.linearLayout_recharge, ContextCompat.getColor(context, R.color.text_color_light_gray));
                    helper.setBackgroundColor(R.id.linearLayout_withdraw, ContextCompat.getColor(context, R.color.text_color_light_gray));
                    detailLinearLayout.setEnabled(false);
                    rechargeButton.setEnabled(false);
                    withButton.setEnabled(false);
                }else {
                    helper.setBackgroundRes(R.id.linearLayout_recharge, R.drawable.btn_bottom_dark_blue_radius);
                    helper.setBackgroundRes(R.id.linearLayout_withdraw, R.drawable.btn_bottom_dark_blue_radius);
                    detailLinearLayout.setEnabled(true);
                    rechargeButton.setEnabled(true);
                    withButton.setEnabled(true);
                }
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_front:
                HomeDropdown homeDropdown=new HomeDropdown(context, DeviceUtil.dp2px(context,45.0f));
                homeDropdown.show();
                break;
            case R.id.bnt_user_commit:
                if("0".equals(userInfo.getIsHadSecurePassword())){
                    UIHelper.showSafetySafePwd(context);
                }else if(!"2".equals(userInfo.getIdentityAuthStatus())&&!"1".equals(userInfo.getIdentityAuthStatus())){
                    UIHelper.showSafetyIdentityAuth(context);
                }
                userConfirm.dismiss();
                break;
            case R.id.sign_up_text:
                UIHelper.showRegister(context);
             //   UIHelper.showLoginOrRegister(context);
                break;
            case R.id.login_text:
                UIHelper.showLogin(context);
             //   UIHelper.showLoginOrRegister(context);
                break;
        }

    }

    private void initViewData(){
        initData();
//        if(is_token(userInfo)&&userAcountResult!=null) {
//         adapter.replaceAll(userAcountResult.getUserAccount().getBalances());
//        }
    }

    public void refreshList(UserAcountResult userAcountResult) {
        if(is_token(userInfo)&&userAcountResult!=null) {
            adapter.replaceAll(userAcountResult.getUserAccount().getBalances());
        }
    }

    private boolean hidden;

    private int sfdyc=0;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden&&sfdyc>1) {
            initViewData();
        }
        sfdyc++;
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            initViewData();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden&&sfdyc>1) {
            initViewData();
        }
        sfdyc++;
    }

    @Override
    public void onStart() {
        super.onStart();
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
