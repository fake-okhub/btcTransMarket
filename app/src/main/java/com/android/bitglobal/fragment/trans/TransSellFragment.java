package com.android.bitglobal.fragment.trans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BalanceResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketDepth;
import com.android.bitglobal.entity.MarketDepthData;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.ExchangeConfirmDialog;
import com.android.bitglobal.view.SeekBarView;
import com.android.bitglobal.view.UserConfirm;
import com.android.bitglobal.view.keyboardView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/8.
 */

public class TransSellFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_MINUS = 1;
    private static final int TYPE_AMOUNT = 2;
    private static final int TYPE_PRICE = 3;
    public static final int PRICE_EDIT_TEXT_ID = 1;
    public static final String TYPE_TRANS_BUY = "1";
    public static final String TYPE_TRANS_SELL = "0";

    //为了精度显示，小数点后保留6位
    private int mPrecision = 6;
    private int mPrecisionAmount;
    //输入交易价格
    @BindView(R.id.ed_trans_jg)
    EditText ed_trans_jg;

    //输入交易数量
    @BindView(R.id.ed_trans_sl)
    EditText ed_trans_sl;

    @BindView(R.id.totalPriceText)
    TextView totalBtcText;
    @BindView(R.id.percentAmountText)
    TextView percentAmountText;
    @BindView(R.id.availableText)
    TextView availableText;
    @BindView(R.id.availableValueText)
    TextView availableValueText;
    @BindView(R.id.currencyPriceText)
    TextView currencyPriceText;

    @BindView(R.id.priceMinusImage)
    ImageView priceMinusImage;
    @BindView(R.id.priceAddImage)
    ImageView priceAddImage;
    @BindView(R.id.amountMinusImage)
    ImageView amountMinusImage;
    @BindView(R.id.amountAddImage)
    ImageView amountAddImage;


    @BindView(R.id.listview1)
    ListView listview1;
    @BindView(R.id.listview2)
    ListView listview2;

    @BindView(R.id.buyOrSellBtn)
    Button buyOrSellBtn;

    @BindView(R.id.seekBar)
    SeekBarView seekBarView;

    private BalanceResult mBalanceResult;
    private BalanceResult mBTCBalanceResult;

    private UserAcountResult userAcount;
    private UserInfo userInfo;
    private Activity context;
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();


    private List<MarketDepthData> sellList = new ArrayList<>();
    private List<MarketDepthData> buyList = new ArrayList<>();
    private QuickAdapter<MarketDepthData> sellAdapter, buyAdapter;
    private String length = "10", ed_type = "0";
    private SubscriberOnNextListener marketDepthOnNext, doEntrustOnNext;

    private String currencyType = "", exchangeType = "", prizeRange = "";
    //二次确定
    private String sf_ljjy = "0";


    //  | isPlan | String | 是    | 类型 <br/> 1：计划/委托交易  0：立即交易 |
    private String isPlan = "0";

    private String unitPrice, number, safePwd = "";
    private UserConfirm userConfirm;
    private TransCurrency t_Currency;

    //当前币种可用
    private double currencyType_ky = 0.00;
    //当前币种可买
    private double currencyType_kma = 0.00;
    //兑换币种可用
    private double exchangeType_ky = 0.00;
    //兑换币种可卖
    private double exchangeType_kma = 0.00;
    //当前币价
    private double currentPrice = 0.00;

    private keyboardView mkeyboardView;
    private int n = 200, sfxz = 0, djcdj = 0, ddb = 0;

    //买一价格
    private float mPrice_Buy;

    //卖一价格
    private float mPrice_Sell;
    private Unbinder unbinder;
    private ExchangeConfirmDialog mExchangeConfirmDialog;

    private boolean isInitPriceText = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_buy, container, false);
        view.setOnTouchListener(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
        initViewData();
    }

    private void showTotalTextView(UserAcountResult userAcount) {

        if (userAcount == null) {
//            show_ky_btc1.setText(getString(R.string.trans_ky)+exchangeType);
//            show_ky_ll.setVisibility(View.GONE);

//            tv_trans_km_eth1.setText(getString(R.string.trans_km) + currencyType);
//            tv_trans_km_eth2.setText("0.00");
//
//            tv_trans_dj_btc1.setText(getString(R.string.trans_dj) + exchangeType);
//            tv_trans_dj_btc2.setText("0.00");
//
//            tv_trans_ky_eth1.setText(getString(R.string.trans_ky) + currencyType);
//            tv_trans_ky_eth2.setText("0.00");
//
//            tv_trans_km_btc1.setText(getString(R.string.trans_kmm) + exchangeType);
//            tv_trans_km_btc2.setText("0.00");
//
//            tv_trans_dj_eth1.setText(getString(R.string.trans_dj) + currencyType);
//            tv_trans_dj_eth2.setText("0.00");
//
//            tv_trans_total1.setText(getResources().getString(R.string.trans_zzc));
//            tv_trans_total2.setText("0.00");
        } else {

//            show_ky_btc1.setText(getString(R.string.trans_ky)+exchangeType);
            if (!totalBtcText.getText().toString().equals("0.0" + exchangeType)) {
                totalBtcText.setText("0.0 " + exchangeType);
            }
            availableText.setText(getString(R.string.trans_available).replaceAll("#", currencyType));
            availableValueText.setText(format(currencyType_ky, Utils.getPrecisionCurrency(currencyType, exchangeType)));
            buyOrSellBtn.setBackgroundResource(R.drawable.btn_trans_sell_bg);
            buyOrSellBtn.setText(R.string.trans_xhmc);
//            show_ky_ll.setVisibility(View.VISIBLE);

//            tv_trans_km_eth1.setText(getString(R.string.trans_km) + currencyType);
//            tv_trans_km_eth2.setText(currencyType_kma + "");
//
//            tv_trans_dj_btc1.setText(getString(R.string.trans_dj) + exchangeType);
//            if (mBTCBalanceResult != null) {
//                tv_trans_dj_btc2.setText(format(mBTCBalanceResult.getFreeze(), mPrecision));
//            } else {
//                tv_trans_dj_btc2.setText("0.00");
//            }
//
//            tv_trans_ky_eth1.setText(getString(R.string.trans_ky) + currencyType);
//            tv_trans_ky_eth2.setText(currencyType_ky + "");
//
//            tv_trans_km_btc1.setText(getString(R.string.trans_kmm) + exchangeType);
//            tv_trans_km_btc2.setText(exchangeType_kma + "");
//
//            tv_trans_dj_eth1.setText(getString(R.string.trans_dj) + currencyType);

//            if (mBalanceResult != null) {
//                tv_trans_dj_eth2.setText(format(mBalanceResult.getFreeze(), mPrecision));
//            } else {
//                tv_trans_dj_eth2.setText("0.00");
//            }


//            tv_trans_total1.setText(getResources().getString(R.string.trans_zzc));
//            tv_trans_total2.setText(format(userAcount.getTotalAmount(), mPrecision));
        }
        ed_trans_jg.setHint(exchangeType.toUpperCase());
        ed_trans_sl.setHint(currencyType.toUpperCase());
        updatePrecision();

    }

    private void updatePrecision() {
        mPrecision = Utils.getPrecisionCurrency(currencyType, exchangeType);
        mPrecisionAmount = Utils.getPrecisionAmount(currencyType, exchangeType);
    }

    private boolean sfjzgl = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() && sfjzgl) {
            initViewData();
        }
        if (isVisibleToUser && isVisible() && !sfjzgl) {
            sfjzgl = true;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {

        mkeyboardView = MainActivity.getInstance().getKeyboardView();
        userConfirm = new UserConfirm(context);
        userConfirm.bnt_user_commit.setOnClickListener(this);
//        tv_trans_total1.setText(getResources().getString(R.string.trans_zzc));

        priceAddImage.setOnClickListener(this);
        priceMinusImage.setOnClickListener(this);
        amountAddImage.setOnClickListener(this);
        amountMinusImage.setOnClickListener(this);

//        btn_exchange_order_buy.setOnClickListener(this);
//        btn_exchange_order_sell.setOnClickListener(this);
//        tv_currentPrice.setOnClickListener(this);
//        main.setOnClickListener(this);

        seekBarView.setOnChangeListener(new SeekBarView.OnChangeListener() {
            @Override
            public void onProgressChanged(SeekBarView seekBarView, int progress) {
                percentAmountText.setText(progress + "%");
                double maxAmount = 0;
                double price = Double.parseDouble(ed_trans_jg.getText().toString());
                maxAmount = currencyType_ky / price;
                ed_trans_sl.setText((int) (maxAmount * (1 / seekBarView.getMax())));
            }

            @Override
            public void onStartTrackingTouch(SeekBarView seekBarView) {

            }

            @Override
            public void onStopTrackingTouch(SeekBarView seekBarView) {

            }
        });
        availableValueText.setText(format(currencyType_ky, Utils.getPrecisionCurrency(currencyType, exchangeType)));
//        show_ky_ll.setVisibility(View.GONE);

        if (t_Currency != null) {
            prizeRange = t_Currency.getPrizeRange();
            //显示当前币价
//            tv_currentPrice.setText(t_Currency.getExchangeType_symbol()+format(currentPrice));
//            currencyPriceText.setText(format(currentPrice, mPrecision));
//            TransFragment.fragment.setCurrencyPrice(format(currentPrice, mPrecision));
            availableValueText.setText(currencyType_ky + "");
        }

        //显示交易额
        // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+format(currentPrice,mPrecision));
//        tv_trans_jye.setText(format(currentPrice,mPrecision));
        //没登录显示登录按钮
//        trans_ll_mydl.setOnClickListener(this);
//        btn_exchange_order_buy.setVisibility(View.VISIBLE);
//        btn_exchange_order_sell.setVisibility(View.GONE);
        mkeyboardView.buy.setOnClickListener(this);
//        mkeyboardView.setOnClearListener(new keyboardView.OnClearListener() {
//            @Override
//            public void clear(String value) {
//                ed_trans_jg.setText(value);
//                ed_trans_jg.setSelection(ed_trans_jg.getText().length());
//                setKeyboardShow(false);
//            }
//        });

        marketDepthOnNext = new SubscriberOnNextListener<MarketDepth>() {
            @Override
            public void onNext(MarketDepth marketDepth) {
                try {

                    //卖方深度
                    String asks[][] = marketDepth.getAsks();

                    //买方深度
                    String bids[][] = marketDepth.getBids();

                    sellList.clear();
                    if (asks != null && asks.length > 0) {
                        for (int n = 0; asks.length > n; n++) {
                            String str[] = asks[asks.length - n - 1];
                            MarketDepthData marketDepthData = new MarketDepthData();
                            marketDepthData.setId((asks.length - n) + "");
                            marketDepthData.setPrice(str[0]);
                            if (n == 0) {
                                mPrice_Sell = Float.parseFloat(str[0]);
                            }
                            marketDepthData.setNumber(str[1]);
                            sellList.add(marketDepthData);
                        }
                    }
                    buyList.clear();
                    if (bids != null && bids.length > 0) {
                        for (int n = 0; bids.length > n; n++) {

                            String str[] = bids[n];
                            MarketDepthData marketDepthData = new MarketDepthData();
                            marketDepthData.setId((n + 1) + "");
                            marketDepthData.setPrice(str[0]);
                            if (n == 0) {
                                mPrice_Buy = Float.parseFloat(str[0]);
                            }
                            marketDepthData.setNumber(str[1]);
                            buyList.add(marketDepthData);

                        }
                    }

                    Collections.sort(sellList, new Comparator<MarketDepthData>() {
                        @Override
                        public int compare(MarketDepthData o1, MarketDepthData o2) {
                            return Double.compare(Double.valueOf(o1.getPrice()),
                                    Double.valueOf(o2.getPrice()));
                        }
                    });
                    Collections.sort(buyList, new Comparator<MarketDepthData>() {
                        @Override
                        public int compare(MarketDepthData o1, MarketDepthData o2) {
                            return Double.compare(Double.valueOf(o2.getPrice()),
                                    Double.valueOf(o1.getPrice()));
                        }
                    });
                    sellAdapter.clear();
                    sellAdapter.addAll(sellList);
                    buyAdapter.clear();
                    buyAdapter.addAll(buyList);

                    if(isInitPriceText) {
                        setPriceTextDefault();
                        isInitPriceText = false;
                    }

                    if (ddb == 0) {
                        //   listview1.setSelection(listview1.getBottom());
//                        listview1.smoothScrollToPosition(listview1.getCount() - 1);//移动到尾部
                        listview1.setSelection(0);
                        listview2.setSelection(0);
                        ddb = 1;
                    }
                    double currentPrice_ls = Double.parseDouble(marketDepth.getCurrentPrice());
                    String str_clo = "#3a3a3a";
                    if (currentPrice_ls > currentPrice) {
                        str_clo = "#E70101";
//                        HomeFragment.showHeaderAnimation(tv_currentPrice);
                    } else if (currentPrice_ls < currentPrice) {
                        str_clo = "#1BA905";
//                        HomeFragment.showHeaderAnimation(tv_currentPrice);
                    }
                    currentPrice = currentPrice_ls;
//                    tv_currentPrice.setText(t_Currency.getExchangeType_symbol()+format(currentPrice));
//                    tv_currentPrice.setTextColor(Color.parseColor(str_clo));
                    TransFragment.fragment.setCurrencyPrice(format(currentPrice, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    String str_jg = getText(ed_trans_jg);
                    if (str_jg.equals("") || str_jg.equals("0")) {
                        currencyType_kma = Double.parseDouble(format((exchangeType_ky / currentPrice) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        exchangeType_kma = Double.parseDouble(format((currencyType_ky * currentPrice) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    } else {
                        double dou_jg = Double.parseDouble(str_jg);
                        currencyType_kma = Double.parseDouble(format((exchangeType_ky / dou_jg) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        exchangeType_kma = Double.parseDouble(format((currencyType_ky * dou_jg) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    }
                    currencyPriceText.setText(format(currentPrice, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                } catch (Exception e) {

                }
                setTextViewNum();
            }
        };
        sellAdapter = new QuickAdapter<MarketDepthData>(context, R.layout.trans_remote_2_item, sellList) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MarketDepthData item) {
                final int position = helper.getPosition();
//                helper.setText(R.id.tv_title,getResources().getString(R.string.trans_mi)+item.getId());
                helper.setText(R.id.tv_number, format(item.getNumber(), mPrecisionAmount));
                helper.setText(R.id.tv_price, format(item.getPrice(), Utils.getPrecisionCurrency(currencyType, exchangeType))).setTextColorRes(R.id.tv_price, R.color.trans_exchange_sell);
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ed_type = "1";
                        setKeyboardShow(false);
                        String a_number = format(item.getNumber(), Utils.getPrecisionCurrency(currencyType, exchangeType));
                        String a_price = format(item.getPrice(), Utils.getPrecisionCurrency(currencyType, exchangeType));
//                        ed_trans_jg.setText(a_price);
//                        ed_trans_sl.setText(a_number);

                        double b_price = 0;
                        try {
                            b_price = Double.parseDouble(format(a_price, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
//                        ed_trans_jg.setText(a_price);
                        double b_number = 0;
                        for (int n = 0; n <= position; n++) {
                            b_number += Double.parseDouble(format(sellList.get(n).getNumber(), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        }

                        if (b_price > 0) {
                            double maxAmount = exchangeType_ky / b_price;
                            if (Double.compare(b_number, maxAmount) >= 0) {
                                ed_trans_sl.setText(format(maxAmount, mPrecisionAmount));
                            } else {
                                ed_trans_sl.setText(format(b_number, mPrecisionAmount));
                            }
                        }


                        Editable etext = ed_trans_jg.getText();
                        Selection.setSelection(etext, etext.length());

                        etext = ed_trans_sl.getText();
                        Selection.setSelection(etext, etext.length());
                    }
                });
            }
        };

        buyAdapter = new QuickAdapter<MarketDepthData>(context, R.layout.trans_remote_item, buyList) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MarketDepthData item) {
                final int position = helper.getPosition();
//                helper.setText(R.id.tv_title,getResources().getString(R.string.trans_ma)
//                        +item.getId()).setTextColorRes(R.id.tv_title,R.color.text_color_red);
                helper.setText(R.id.tv_number, format(item.getNumber(), mPrecisionAmount));
                helper.setText(R.id.tv_price, format(item.getPrice(), Utils.getPrecisionCurrency(currencyType, exchangeType))).setTextColorRes(R.id.tv_price, R.color.trans_exchange_buy);
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ed_type = "1";
                        setKeyboardShow(false);
                        String a_number = format(item.getNumber(), mPrecisionAmount);
                        String a_price = format(item.getPrice(), Utils.getPrecisionCurrency(currencyType, exchangeType));
                        ed_trans_sl.setText(a_number);
//                        ed_trans_jg.setText(a_price);

                        double b_price = Double.parseDouble(format(a_price, Utils.getPrecisionCurrency(currencyType, exchangeType)));
//                        ed_trans_jg.setText(a_price);
                        double b_number = 0;
                        for (int n = 0; n <= position; n++) {
                            b_number += Double.parseDouble(format(buyList.get(n).getNumber(), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        }
                        if (b_price > 0) {
//                            double maxAmount = currencyType_ky / b_price;
                            double maxAmount = currencyType_ky;
                            if (Double.compare(b_number, maxAmount) >= 0) {
                                ed_trans_sl.setText(format(maxAmount, mPrecisionAmount));
                            } else {
                                ed_trans_sl.setText(format(b_number, mPrecisionAmount));
                            }
                        }

                        Editable etext = ed_trans_jg.getText();
                        Selection.setSelection(etext, etext.length());

                        etext = ed_trans_sl.getText();
                        Selection.setSelection(etext, etext.length());


                    }
                });
            }
        };
        listview1.setAdapter(sellAdapter);
        listview2.setAdapter(buyAdapter);
        ViewTreeObserver observer = listview1.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                try {
                    Message msg = new Message();
                    msg.what = n;
                    msg.arg1 = listview1.findViewById(R.id.ll_trans_item).getHeight();
                    mHandler.sendMessage(msg);
                } catch (Exception e) {

                }
                return true;
            }
        });

        ed_trans_sl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

//                if(!is_token(userInfo)){
//                    UIHelper.showLogin(context);
//                    return false;
//                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ed_type = "1";

                    mkeyboardView.setEditText(ed_trans_sl, mPrecisionAmount);
                    mkeyboardView.processingData(Double.parseDouble(format(currencyType_ky, Utils.getPrecisionCurrency(currencyType, exchangeType))));
                    mkeyboardView.setViewVisible(false);
                    setKeyboardShow(true);
                }


                return false;
            }
        });

        ed_trans_jg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                try {
//                    if(!is_token(userInfo)){
//                        UIHelper.showLogin(context);
//                        return false;
//                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ed_type = "0";
                        mkeyboardView.setEditText(ed_trans_jg, Utils.getPrecisionCurrency(currencyType, exchangeType), format(buyAdapter.getItem(0).getPrice(), Utils.getPrecisionCurrency(currencyType, exchangeType)));
//                        if (StringUtils.isEmpty(ed_trans_jg.getText().toString())) {
//                            ed_trans_jg.setText(format(currentPrice, Utils.getPrecisionCurrency(currencyType, exchangeType)));
//                        }
                        mkeyboardView.processingData(Double.parseDouble(format(currencyType_ky, Utils.getPrecisionCurrency(currencyType, exchangeType))));
                        mkeyboardView.setViewVisible(false);
                        setKeyboardShow(true);
                    }
                } catch (Exception e) {

                }

                return false;
            }
        });
        ed_trans_jg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                bnt_placeOrder();
                String str = s.toString();
                if (!"0".equals(ed_type)) {
                    return;
                }
                if (str.equals(".")) {
                    ed_trans_jg.setText("");
                } else if (str.equals("00") || str.equals("01") || str.equals("02") || str.equals("03") || str.equals("04") || str.equals("05") || str.equals("06") || str.equals("07") || str.equals("08") || str.equals("09")) {
                    // ed_trans_jg.setText("0");
                    ed_trans_jg.setText(format(str.substring(1), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    Editable etext = ed_trans_jg.getText();
                    Selection.setSelection(etext, etext.length());
                } else if (!str.equals("")) {
                    String a_number = ed_trans_sl.getText().toString().trim();
                    if (!a_number.equals("")) {
                        String a_amount = format(Double.parseDouble(a_number) * Double.parseDouble(str), Utils.getPrecisionCurrency(currencyType, exchangeType));
                        //  tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+format(a_amount,Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        totalBtcText.setText(format(a_amount, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    }

                } else {
                    // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
//                    totalPriceText.setText(format(0, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    totalBtcText.setText("0.0 " + exchangeType);
                }


                if (str.equals("") || str.equals("0")) {
                    if (Float.compare(mPrice_Sell, 0.0f) != 0) {
                        currencyType_kma = Double.parseDouble(format((exchangeType_ky / mPrice_Sell) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    } else {
                        currencyType_kma = 0.0000;
                    }
                    if (Float.compare(mPrice_Buy, 0.0f) != 0) {
                        exchangeType_kma = Double.parseDouble(format((currencyType_ky * mPrice_Buy) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    } else {
                        exchangeType_kma = 0.0000;
                    }

                } else {
                    try {
                        double dou_jg = Double.parseDouble(str);
                        currencyType_kma = Double.parseDouble(format((exchangeType_ky / dou_jg) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        exchangeType_kma = Double.parseDouble(format((currencyType_ky * dou_jg) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));

                    } catch (Exception e) {
                        if (Double.compare(currentPrice, 0.0f) != 0) {
                            currencyType_kma = Double.parseDouble(format((exchangeType_ky / currentPrice) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                            exchangeType_kma = Double.parseDouble(format((currencyType_ky * currentPrice) + "", Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        } else {
                            currencyType_kma = 0.0000;
                            exchangeType_kma = 0.0000;
                        }
                    }
                }
                setTextViewNum();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > Utils.getPrecisionCurrency(currencyType, exchangeType)) {
                    s.delete(posDot + Utils.getPrecisionCurrency(currencyType, exchangeType) + 1, posDot + Utils.getPrecisionCurrency(currencyType, exchangeType) + 2);
                }
            }
        });
        ed_trans_sl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                bnt_placeOrder();
                String str = s.toString();
                if (!"1".equals(ed_type)) {
                    return;
                }
                if (str.equals(".")) {
                    ed_trans_sl.setText("");
                } else if (str.equals("00") || str.equals("01") || str.equals("02") || str.equals("03") || str.equals("04") || str.equals("05") || str.equals("06") || str.equals("07") || str.equals("08") || str.equals("09")) {
                    //  ed_trans_sl.setText("0");
                    ed_trans_sl.setText(format(str.substring(1), mPrecisionAmount));
                    Editable etext = ed_trans_sl.getText();
                    Selection.setSelection(etext, etext.length());
                } else if (!str.equals("")) {
                    String a_price = ed_trans_jg.getText().toString().trim();
                    if (!a_price.equals("")) {
                        String a_amount = format(Double.parseDouble(a_price) * Double.parseDouble(str), Utils.getPrecisionCurrency(currencyType, exchangeType));
                        // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+format(a_amount,Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        totalBtcText.setText(format(a_amount, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    }
                } else {
                    if (ed_type.equals("1")) {
                        //tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
//                        totalPriceText.setText(format(0, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                        totalBtcText.setText("0.0 " + exchangeType);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > mPrecisionAmount) {
                    s.delete(posDot + mPrecisionAmount + 1, posDot + mPrecisionAmount + 2);
                }
            }
        });

        mExchangeConfirmDialog = new ExchangeConfirmDialog(context, new ExchangeConfirmDialog.ExchangeConfirmCallback() {
            @Override
            public void onConfirm(String passwd) {
                safePwd = passwd;
                doEntrust();
            }
        });

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                try {
                    int height = msg.arg1;
//                    ViewGroup.LayoutParams params= listview1.getLayoutParams();
//                    params.height=5*height;
//                    listview1.setLayoutParams(params);
//                    listview2.setLayoutParams(params);
                    n++;
                } catch (Exception e) {

                }

            }
        }
    };

    private void setPriceTextDefault() {
        ed_trans_jg.setText(format(buyAdapter.getItem(0).getPrice(), mPrecision));
        ed_trans_jg.setSelection(ed_trans_jg.getText().length());
    }

    private void initData() {
        currencyType = TransFragment.fragment.getCurrencyType().toUpperCase();
        exchangeType = TransFragment.fragment.getExchangeType().toUpperCase();
        t_Currency = TransFragment.fragment.getT_Currency();
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {
            userAcount = UserAcountDao.getInstance().getIfon(userInfo.getUserId());
            if (userAcount == null || userAcount.getUserAccount() == null)
                return;
            for (int i = 0; i < userAcount.getUserAccount().getBalances().size(); i++) {
                if (userAcount.getUserAccount().getBalances().get(i).getPropTag().toUpperCase().equals(exchangeType.toUpperCase())) {
                    mBTCBalanceResult = userAcount.getUserAccount().getBalances().get(i);
                }
                if (userAcount.getUserAccount().getBalances().get(i).getPropTag().toUpperCase().equals(currencyType.toUpperCase())) {
                    mBalanceResult = userAcount.getUserAccount().getBalances().get(i);
                }

            }
            if (mBTCBalanceResult != null) {
                //可用BTC
                exchangeType_ky = Double.parseDouble(format(mBTCBalanceResult.getBalance(), Utils.getPrecisionExchange(currencyType, exchangeType)));
            }

            if (mBalanceResult != null) {
                //可用ETC
                currencyType_ky = Double.parseDouble(format(mBalanceResult.getBalance(), Utils.getPrecisionCurrency(currencyType, exchangeType)));
            }


        }
        for (int n = 1; n < 7; n++) {
            MarketDepthData marketDepthData = new MarketDepthData();
            marketDepthData.setId(n + "");
            marketDepthData.setPrice("--");
            marketDepthData.setNumber("--");
            buyList.add(marketDepthData);
            marketDepthData.setId((6 - n) + "");
            sellList.add(marketDepthData);
        }


    }

    private void setTextViewNum() {
        if (is_token(userInfo)) {
            //    MainActivity.getInstance().getUserInfo();
//            trans_ll_mydl.setVisibility(View.GONE);
//            trans_ll_yjdl.setVisibility(View.VISIBLE);

            if (userAcount == null) {
                handler.postDelayed(runnable, 3000);
                return;
            }

        } else {
//            trans_ll_yjdl.setVisibility(View.GONE);
//            trans_ll_mydl.setVisibility(View.VISIBLE);
            userAcount = null;
            exchangeType_ky = 0.0000;
            currencyType_ky = 0.0000;
            currencyType_kma = 0.0000;
        }
        showTotalTextView(userAcount);

       /* tv_trans_jg.setText(getString(R.string.trans_mr)+currencyType+getString(R.string.trans_jg));
        tv_trans_sl.setText(getString(R.string.trans_mr)+currencyType+getString(R.string.trans_sl));*/
//        tv_trans_jg.setText(exchangeType.toUpperCase());
//        tv_trans_sl.setText(currencyType.toUpperCase());
//        tv_trans_jye_show.setText(exchangeType.toUpperCase());
    }

    public void initViewData() {
        initData();
        marketDepth();
    }

    private void marketDepth() {
        list.clear();
        list.add(length);
        list.add(currencyType);
        list.add(exchangeType);
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(marketDepthOnNext, context);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(1).marketDepth(progressSubscriber, list);
    }

    private void ljjy() {
        int sfyxstk = 0;
        //if("1".equals(userInfo.getIsHadSecurePassword())&&"3".equals(userInfo.getTradeAuthenType())){
        if ("1".equals(userInfo.getSafePwdPeriod())) {
            sfyxstk++;
            userConfirm.ed_user_safePwd.setVisibility(View.VISIBLE);
            userConfirm.tv_user_title2.setText(getString(R.string.trans_nykqzjmmbh_hint));
        } else {
            userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        }

//        double d_unitPrice =Double.parseDouble(unitPrice);
//        double d_prizeRange =Double.parseDouble(prizeRange);
        String transType;

        //type=1买入=0卖出
        transType = getString(R.string.trans_sell);
//        if(sfyxstk>0){
//            userConfirm.show();
//        }else{
//            doEntrust();
//        }
        if ("1".equals(userInfo.getIsHadSecurePassword())) {
            if ("1".equals(userInfo.getSafePwdPeriod())) {
                mExchangeConfirmDialog.setTransPasswdInput(true);
            } else {
                mExchangeConfirmDialog.setTransPasswdInput(false);
            }
            mExchangeConfirmDialog.setTransType(TYPE_TRANS_SELL);
            String tips = getString(R.string.trans_info);
            tips = tips.replace("###", currencyType.toUpperCase());
            tips = tips.replace("##", transType);
            tips = tips.replace("#", unitPrice);
            tips = tips.replace("@", number);
            mExchangeConfirmDialog.show(tips);
        } else {
            showSecurePasswdWarning();
        }
    }

    private void showSecurePasswdWarning() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_exchange_transaction_passwd_notice, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(root);
        dialog.setCancelable(false);
        root.findViewById(R.id.exchange_transaction_passwd_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        root.findViewById(R.id.exchange_transaction_passwd_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showSafetySafePwd(context);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**
     * 进行交易
     */
    private void doEntrust() {
        long timeStamp = System.currentTimeMillis();
        list2.clear();
        list2.add(timeStamp + "");
        list2.add(TYPE_TRANS_SELL);
        list2.add(currencyType);
        list2.add(exchangeType);
        list2.add(isPlan);
        list2.add(unitPrice);
        list2.add(number);
        list2.add(safePwd);
//        Subscriber<HttpResult> doEntrustOnNext = new Subscriber<HttpResult>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
////                handler.postDelayed(runnable22, 1000);
//            }
//
//            @Override
//            public void onNext(HttpResult httpResult) {
//                if (httpResult.getResMsg().getCode().equals("1000")) {
////                    handler.postDelayed(runnable2, 1000);
//                    clear_ed();
//                }
//                UIHelper.ToastMessage(context, httpResult.getResMsg().getMessage());
//            }
//
//        };
        doEntrustOnNext = new SubscriberOnNextListener<HttpResult>() {

            @Override
            public void onNext(HttpResult httpResult) {
                if (httpResult.getResMsg().getCode().equals("1000")) {
//                    handler.postDelayed(runnable2, 1000);
                    clear_ed();
                } else if (httpResult.getResMsg().getCode().equals("1001")) {
                    double maxAmount = exchangeType_ky / currentPrice;
                    if (maxAmount > currencyType_ky) {
                        ed_trans_sl.setText(format(currencyType_ky, Utils.getPrecisionCurrency(currencyType, exchangeType)));
                    }
                }
                UIHelper.ToastMessage(context, httpResult.getResMsg().getMessage());
            }
        };
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(doEntrustOnNext, context);
        HttpMethods.getInstance(1).doEntrust(progressSubscriber, list2);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            initViewData();
        }
    };
//    Runnable runnable2 = new Runnable() {
//        public void run() {
//            handler.postDelayed(runnable3, 1500);
//        }
//    };
//    Runnable runnable22 = new Runnable() {
//        public void run() {
//            handler.postDelayed(runnable3, 1500);
//        }
//    };
//    Runnable runnable3 = new Runnable() {
//        public void run() {
//            if(type.equals("1")) {
//                btn_exchange_order_buy.setVisibility(View.VISIBLE);
//            }else{
//                btn_exchange_order_sell.setVisibility(View.VISIBLE);
//            }
//
//        }
//    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.trans_ll_mydl:
//                UIHelper.showLogin(context);
            //   UIHelper.showLoginOrRegister(context);
//                break;
            case R.id.bnt_user_commit:
                safePwd = getText(userConfirm.ed_user_safePwd);
                if (safePwd.equals("") && userConfirm.ed_user_safePwd.isShown()) {
                    UIHelper.ToastMessage(context, getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                userConfirm.dismiss();
                doEntrust();
                break;
            case R.id.buy:
                mkeyboardView.setVisibility(View.GONE);
                break;
            case R.id.buyOrSellBtn:
                if (!is_token(userInfo)) {
                    UIHelper.showLogin(context);
                    break;
                }
                buyOrSell();
                break;
//            case R.id.tv_currentPrice:
//                ed_trans_jg.setText(format(currentPrice,Utils.getPrecisionCurrency(currencyType, exchangeType)));
//                Editable etext = ed_trans_jg.getText();
//                Selection.setSelection(etext, etext.length());
//                break;
            case R.id.main:
                setKeyboardShow(false);
                break;
            case R.id.priceAddImage:
                ed_trans_jg.setText(format(addOrMinusValue(ed_trans_jg, TYPE_ADD, TYPE_PRICE), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                ed_trans_jg.setSelection(ed_trans_jg.getText().length());
                break;
            case R.id.priceMinusImage:
                ed_trans_jg.setText(format(addOrMinusValue(ed_trans_jg, TYPE_MINUS, TYPE_PRICE), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                ed_trans_jg.setSelection(ed_trans_jg.getText().length());
                break;
            case R.id.amountAddImage:
                ed_trans_sl.setText(format(addOrMinusValue(ed_trans_sl, TYPE_ADD, TYPE_AMOUNT), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                ed_trans_sl.setSelection(ed_trans_sl.getText().length());
                break;
            case R.id.amountMinusImage:
                ed_trans_sl.setText(format(addOrMinusValue(ed_trans_sl, TYPE_MINUS, TYPE_AMOUNT), Utils.getPrecisionCurrency(currencyType, exchangeType)));
                ed_trans_sl.setSelection(ed_trans_sl.getText().length());
                break;
        }
    }

    private double addOrMinusValue(EditText inputEditText, int type, int precisionType) {
        double showDouble;
        if (inputEditText.getText().toString().equals("")) {
            showDouble = 0.0f;
        } else {
            try {
                showDouble = Double.parseDouble(inputEditText.getText().toString());

            } catch (Exception e) {
                showDouble = 0.0f;
            }

        }
        if(type == TYPE_ADD) {
            showDouble = showDouble + getStep(precisionType);
        } else if(type == TYPE_MINUS) {
            if (Double.compare(showDouble, 0) > 0) {
                showDouble = showDouble - getStep(precisionType);
            }
        }
        return showDouble;
    }

    private double getStep(int type) {
        String str = "0.";
        int precision = 0;
        if(type == TYPE_PRICE) {
            precision = Utils.getPrecisionCurrency(currencyType, exchangeType);
        } else if(type == TYPE_AMOUNT) {
            precision = Utils.getPrecisionAmount(currencyType, exchangeType);
        }
        for (int i = 0; i < precision - 1; i++) {
            str += "0";
        }
        str += "1";
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }

    private void buyOrSell() {

        unitPrice = getText(ed_trans_jg);
        number = getText(ed_trans_sl);


        if (unitPrice.equals("")) {
            UIHelper.ToastMessage(context, R.string.trans_jg_hint);
            return;
        }
        if (number.equals("")) {
            UIHelper.ToastMessage(context, R.string.trans_sl_hint);
            return;
        }
        if (Double.parseDouble(unitPrice) == 0) {
            UIHelper.ToastMessage(context, R.string.trans_jg_hint);
            return;
        }
        if (Double.parseDouble(number) == 0) {
            UIHelper.ToastMessage(context, R.string.trans_sl_hint);
            return;
        }
        setKeyboardShow(false);
        //二次确认
        String trans_state = SharedPreferences.getInstance().getString("trans_state", "0");
        if (trans_state.equals("0") || sf_ljjy.equals("0")) {
            ljjy();
        } else {
            if (djcdj == 0) {
//                if (transType.equals("1")) {
//                    btn_exchange_order_buy.setText(R.string.trans_djqr);
//                } else {
//                    btn_exchange_order_sell.setText(R.string.trans_djqr);
//                }
                djcdj = 1;
            } else {
//                bnt_placeOrder();
                ljjy();
            }
        }

    }

//    public void bnt_placeOrder() {
//        djcdj = 0;
//        if (transType.equals("1")) {
//            btn_exchange_order_buy.setText(R.string.trans_xhmr);
//        } else {
//            btn_exchange_order_sell.setText(R.string.trans_xhmc);
//        }
//
//    }

    public void clear_ed() {
        try {
            ed_trans_sl.setText("");
            if(buyList != null && buyList.size() > 0) {
                ed_trans_jg.setText(format(buyList.get(0).getPrice(), mPrecision));
            } else {
                ed_trans_jg.setText("");
            }
            // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
            if (totalBtcText.getText().toString().equals("0.0" + exchangeType)) {
                totalBtcText.setText("0.0 " + exchangeType);
            }
        } catch (Exception e) {

        }

    }

    private void setKeyboardShow(boolean show) {
        try {
            if (mkeyboardView == null)
                return;
            if (show) {
                mkeyboardView.setVisibility(View.VISIBLE);
            } else {
                mkeyboardView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setKeyboardShow(false);
        return false;
    }

}