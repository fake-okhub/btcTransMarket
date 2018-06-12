package com.android.bitglobal.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CurrencyMarketDepthRealm;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.EntityString;
import com.android.bitglobal.entity.TradeExchange;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.market.MarketFragment;
import com.android.bitglobal.fragment.trans.TransTypeFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.PopupDialog;
import com.android.bitglobal.ui.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TransFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_head_title)
    public TextView tv_head_title;
//    @BindView(R.id.btn_head_front)
//    ImageView btn_head_front;

    @BindView(R.id.img_head_ico_r)
    ImageView img_head_ico_r;
    @BindView(R.id.headRightImage)
    ImageView headRightImage;
    @BindView(R.id.btn_head_back)
    ImageView headBackImage;

    @BindView(R.id.ll_head_title)
    LinearLayout ll_head_title;
    @BindView(R.id.tv_head_price)
    TextView tv_head_price;

    private UserInfo userInfo;

    private static final String TAG = TransFragment.class.getSimpleName();
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;
    private Activity context;
    public static TransFragment fragment;
    private View mView;
    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;
    private TransTypeFragment transTypeFragment;
    public TransCurrency t_Currency;
    private String currencyType = "", exchangeType = "";
    private String prePrice = "";
    private CurrencySetResult currencySetResult;
    private Timer timerTrans = null;  //刷新定时器
    private PopupDialog assetList;
    private List<TradeExchange> TradeExchangelist;
    private SubscriberOnNextListener getfundsOnNext;
    private String coinLogoUrl;
    //private MyCount mc;
    public static final int dataRefreshTime = 5 * 1000;//数据刷新间隔
    public static boolean isPutChangeType = false;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyPrice(String price) {
        Drawable drawable;
        if (StringUtils.isEmpty(price)) {
            return;
        }
//        if (tv_head_price != null && !StringUtils.isEmpty(prePrice)) {
//            tv_head_price.setText(price);
//            int i = prePrice.compareTo(price);
//            if (i > 0) {
//                drawable = ContextCompat.getDrawable(context, R.mipmap.icon_exchange_price_down);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                tv_head_price.setCompoundDrawables(drawable, null, null, null);
//            } else if (i < 0) {
//                drawable = ContextCompat.getDrawable(context, R.mipmap.icon_exchange_price_up);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                tv_head_price.setCompoundDrawables(drawable, null, null, null);
//            }
//        }
        prePrice = price;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public TransCurrency getT_Currency() {
        return t_Currency;
    }

    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trans, null);
        fragment = this;
        context = getActivity();
        unbinder = ButterKnife.bind(this, mView);

        initView();
        //mc = new MyCount(dataRefreshTime, 100);

        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getChildFragmentManager();
        initData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
    }

    private void initData(Bundle savedInstanceState) {
        currencySetResult = CurrencySetDao.getInstance().getIfon();
        transTypeFragment = new TransTypeFragment();
        fragmentTags = new ArrayList<>(Arrays.asList("TransTypeFragment"));
        currIndex = 0;
        if (savedInstanceState != null) {
            currIndex = savedInstanceState.getInt(CURR_INDEX);
            hideSavedFragment();
        }
    }

    private void hideSavedFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    private void initView() {
        tv_head_price.setVisibility(View.GONE);
//        tv_head_price.setOnClickListener(this);
        img_head_ico_r.setVisibility(View.GONE);
//        btn_head_front.setVisibility(View.VISIBLE);
        headRightImage.setVisibility(View.VISIBLE);
//        btn_head_front.setOnClickListener(this);
        ll_head_title.setOnClickListener(this);
        headRightImage.setOnClickListener(this);
        headBackImage.setOnClickListener(this);
        TradeExchangelist = MarketFragment.TradeExchangelist;
        List<TransCurrency> list = new ArrayList<>();
        if (TradeExchangelist != null && TradeExchangelist.size() > 0) {
            for (int i = 0; i < TradeExchangelist.size(); i++) {
                TransCurrency transCurrency = new TransCurrency();
                TradeExchange mTradeExchange = TradeExchangelist.get(i);
                for (CurrencySetRealm csr : currencySetResult.getCurrencySets()) {

                    if (csr.getCurrency().toLowerCase().equals(mTradeExchange.getCurrencyType().toLowerCase())) {

                        transCurrency.setCurrencyType(csr.getCurrency());
                        transCurrency.setUrl(csr.getCoinUrl());
                        transCurrency.setPrizeRange(csr.getPrizeRange());
                        transCurrency.setCurrencyType_symbol(csr.getSymbol());
                        transCurrency.setExchangeType(mTradeExchange.getExchangeType());

                        for (CurrencyMarketDepthRealm cmdr_l : csr.getMarketLength()) {
                            if (csr.getCurrency().equals(cmdr_l.getCurrency())) {
                                String str_length[] = new String[cmdr_l.getOptional().size()];
                                int str_l = 0;
                                for (EntityString es : cmdr_l.getOptional()) {
                                    str_length[str_l] = es.getStr();
                                    str_l++;
                                }
                                transCurrency.setMarketLength(str_length);
                                break;
                            }
                        }

                        for (CurrencySetRealm csr1 : currencySetResult.getCurrencySets()) {
                            if (csr1.getCurrency().toLowerCase().equals(mTradeExchange.getExchangeType().toLowerCase())) {
                                transCurrency.setExchangeType_symbol(csr1.getSymbol());
                                break;
                            }
                        }
                    }
                }


                //  if(currencyType.toLowerCase().equals(transCurrency.getCurrencyType().toLowerCase())&&exchangeType.toLowerCase().equals(transCurrency.getExchangeType().toLowerCase())){
                //    t_Currency=transCurrency;
                // exchangeType=transCurrency.getExchangeType();
                // currencyType=transCurrency.getCurrencyType();
                //  }
                if (i == 0 && t_Currency == null) {
                    if("".equals(SharedPreferences.getInstance().getString("trans_currencyType", "")) && "".equals(SharedPreferences.getInstance().getString("trans_exchangeType", ""))) {
                        t_Currency = transCurrency;
                        exchangeType = transCurrency.getExchangeType();
                        currencyType = transCurrency.getCurrencyType();
                        if(!isPutChangeType) {
                            SharedPreferences.getInstance().putString("trans_currencyType", currencyType);
                            SharedPreferences.getInstance().putString("trans_exchangeType", exchangeType);
                            isPutChangeType = false;
                        }
                    }
                }
                list.add(transCurrency);
            }
        }
        currencyType = SharedPreferences.getInstance().getString("trans_currencyType", "ETC");
        exchangeType = SharedPreferences.getInstance().getString("trans_exchangeType", "BTC");
        tv_head_title.setText(currencyType.toUpperCase() + "/" + exchangeType.toUpperCase());
        if (transTypeFragment != null) {
            transTypeFragment.selectFragment_index();
            transTypeFragment.clear_ed();
        }
/*

        String ExchangeType_symbol="";
        String ExchangeType="";
        for(CurrencySetRealm csr2:currencySetResult.getCurrencySets()){
            if(csr2.getCurrency().equals("BTC")||csr2.getCurrency().equals("btc")){
                ExchangeType_symbol = csr2.getCoinFullNameEn();
                ExchangeType = csr2.getCurrency();
                break;
            }
        }

        for(CurrencySetRealm csr:currencySetResult.getCurrencySets()){
            String currency=csr.getCurrency();
            if(currency.equals("BTC")||currency.equals("btc")) {
                continue;
            }
            if(currency.equals("LTC")||currency.equals("ltc")) {
                continue;
            }
                TransCurrency transCurrency=new TransCurrency();
                transCurrency.setCurrencyType(csr.getCurrency());
                transCurrency.setUrl(csr.getFinanceCoinUrl());
                transCurrency.setPrizeRange(csr.getPrizeRange());
                transCurrency.setCurrencyType_symbol(csr.getCoinFullNameEn());
                transCurrency.setExchangeType(ExchangeType);

                for(CurrencyMarketDepthRealm cmdr_l:csr.getMarketLength()){
                    if(csr.getCurrency().equals(cmdr_l.getCurrency())){
                        String str_length[]=new String[cmdr_l.getOptional().size()];
                        int str_l=0;
                        for(EntityString es:cmdr_l.getOptional()){
                            str_length[str_l]=es.getStr();
                            str_l++;
                        }
                        transCurrency.setMarketLength(str_length);
                    }
                }
                transCurrency.setExchangeType_symbol(ExchangeType_symbol);

                currencyType= SharedPreferences.getInstance().getString("trans_currencyType","ETH");
                exchangeType=SharedPreferences.getInstance().getString("trans_exchangeType","BTC");

                if(currencyType.equals(transCurrency.getCurrencyType())&&exchangeType.equals(transCurrency.getExchangeType())){
                    t_Currency=transCurrency;
                    exchangeType=transCurrency.getExchangeType();
                    currencyType=transCurrency.getCurrencyType();
                }
                list.add(transCurrency);
                  }

*/


//        assetList=new AssetList(context, DeviceUtil.dp2px(context,45.0f));
        assetList = new PopupDialog(context);
        assetList.createAssetSelectList(list, new PopupDialog.PopupDialogCallback<TransCurrency>() {

            @Override
            public void dataBack(TransCurrency item) {
                MainActivity.getInstance().showKeyboardView(false);
                t_Currency = item;
                exchangeType = item.getExchangeType();
                currencyType = item.getCurrencyType();
                SharedPreferences.getInstance().putString("trans_currencyType", currencyType);
                SharedPreferences.getInstance().putString("trans_exchangeType", exchangeType);
                tv_head_title.setText(currencyType.toUpperCase() + "/" + exchangeType.toUpperCase());
                if (transTypeFragment != null) {
                    transTypeFragment.selectFragment_index();
                    transTypeFragment.clear_ed();
                }
                assetList.dismiss();
            }
        });
        showFragment();

        getfundsOnNext = new SubscriberOnNextListener<UserAcountResult>() {
            @Override
            public void onNext(UserAcountResult userAcountResult) {
                String userId = userInfo.getUserId();
                userAcountResult.setUserId(userInfo.getUserId());
                UserAcountDao.getInstance().setIfon(userAcountResult);
                // UserAcount userAcount=userAcountResult.getUserAccount();
                //  userAcount.setUserId(userInfo.getUserId());
                // UserAcountDao.getInstance().setIfon(userAcount);
            }
        };
    }

    private void showFragment() {
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
                return transTypeFragment;
            default:
                return null;
        }
    }

    @Override
    public void onClick(View v) {
        MainActivity.getInstance().showKeyboardView(false);
        switch (v.getId()) {
//            case R.id.btn_head_front:
//                HomeDropdown homeDropdown=new HomeDropdown(context, DeviceUtil.dp2px(context,45.0f));
//                homeDropdown.show();
//                break;
            case R.id.headRightImage:
                for(CurrencySetRealm currencySetRealm : currencySetResult.getCurrencySets()) {
                    if(currencyType.toLowerCase().equals(currencySetRealm.getCurrency().toLowerCase())) {
                        coinLogoUrl = currencySetRealm.getCoinUrl();
                    }
                }
                UIHelper.showMarketDetailActivity(context, currencyType, exchangeType, coinLogoUrl);
                break;
            case R.id.ll_head_title:
                //case R.id.tv_head_title:
                assetList.show();
                break;
            case R.id.btn_head_back:

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (transTypeFragment != null) {
            transTypeFragment.clear_ed();
        }
        stopTimer();
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            startTimer();
            transTypeFragment.showPage(0);
//            if (tv_head_price != null) {
//                tv_head_price.requestFocus();
//            }
        } else {
            if (transTypeFragment != null) {
                transTypeFragment.clear_ed();
            }
            stopTimer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            startTimer();
        }
        initView();
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {

        private Handler mHandler;

        public TransTimerTask(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            this.mHandler.sendMessage(message);
        }
    }

    ;
    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (transTypeFragment != null) {
                    transTypeFragment.selectFragment();
                }
                getfunds();
            }
            super.handleMessage(msg);
        }
    };

    public void getfunds() {
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {

            ProgressSubscriber progressSubscriber = new ProgressSubscriber(getfundsOnNext, context);
            progressSubscriber.setIs_progress_show(false);
            progressSubscriber.setIs_showMessage(false);
            HttpMethods.getInstance(2).getfunds(progressSubscriber, SpTools.getlegalTender());
        }
    }
/*    *//*定义一个倒计时的内部类*//*
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            try {
                if(TransTypeFragment.transBuyOrSellFragment!=null){
                    TransTypeFragment.transBuyOrSellFragment.pb_progressbar.setProgress(0);
                }
            }catch (Exception e){

            }
            try {
                if(TransTypeFragment.trans_sell!=null){
                    TransTypeFragment.trans_sell.pb_progressbar.setProgress(0);
                }
            }catch (Exception e){

            }
            mc.start();

        }
        @Override
        public void onTick(long millisUntilFinished) {
            try {
                if(TransTypeFragment.transBuyOrSellFragment!=null){
                    TransTypeFragment.transBuyOrSellFragment.pb_progressbar.setProgress((int)(dataRefreshTime-millisUntilFinished));
                }
                if(TransTypeFragment.trans_sell!=null){
                    TransTypeFragment.trans_sell.pb_progressbar.setProgress((int)(dataRefreshTime-millisUntilFinished));
                }
            }catch (Exception e){

            }


        }
    }*/

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timerTrans == null) {
            Log.e("trans_startTimer", "trans_startTimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timerTrans = new Timer();
            TransTimerTask task = new TransTimerTask(handlerOfTrans);
            timerTrans.schedule(task, 0, dataRefreshTime);
           /* if(mc!=null)
            mc.start();*/
        }
    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timerTrans != null) {
            Log.e("trans_stoptimer", "trans_stoptimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timerTrans.cancel();
            timerTrans = null;
           /* mc.cancel();*/
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
