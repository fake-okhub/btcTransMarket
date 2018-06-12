package com.android.bitglobal.fragment.trans;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BalanceResult;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.UserAcountDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketDepth;
import com.android.bitglobal.entity.MarketDepthData;
import com.android.bitglobal.entity.TransMarketDepth;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.HomeFragment;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.UserConfirm;
import com.android.bitglobal.view.keyboardView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

public class SellFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener {

    //显示买入eth价格
    @BindView(R.id.tv_trans_jg)
    TextView tv_trans_jg;

    //显示买入eth数量
    @BindView(R.id.tv_trans_sl)
    TextView tv_trans_sl;


    //显示交易额
    @BindView(R.id.tv_trans_jye)
    EditText tv_trans_jye;


    //显示交易额
    @BindView(R.id.tv_trans_jye_show)
    TextView tv_trans_jye_show;

    //最新价格
    @BindView(R.id.tv_currentPrice)
    TextView tv_currentPrice;

    //输入交易价格
    @BindView(R.id.ed_trans_jg)
    EditText ed_trans_jg;

    //输入交易数量
    @BindView(R.id.ed_trans_sl)
    EditText ed_trans_sl;
    //显示可用btc
    @BindView(R.id.show_ky_btc1)
    TextView show_ky_btc1;
    //可用btc
    @BindView(R.id.show_ky_btc2)
    TextView show_ky_btc2;

    //显示可买eth
    @BindView(R.id.tv_trans_km_eth1)
    TextView tv_trans_km_eth1;
    //可买eth
    @BindView(R.id.tv_trans_km_eth2)
    TextView tv_trans_km_eth2;

    //显示冻结
    @BindView(R.id.tv_trans_dj_btc1)
    TextView tv_trans_dj_btc1;

    //冻结
    @BindView(R.id.tv_trans_dj_btc2)
    TextView tv_trans_dj_btc2;


    //显示可用资产eth
    @BindView(R.id.tv_trans_ky_eth1)
    TextView tv_trans_ky_eth1;
    //可用资产eth
    @BindView(R.id.tv_trans_ky_eth2)
    TextView tv_trans_ky_eth2;

    //可卖BTC
    @BindView(R.id.tv_trans_km_btc1)
    TextView tv_trans_km_btc1;
    //显示可卖BTC
    @BindView(R.id.tv_trans_km_btc2)
    TextView tv_trans_km_btc2;


    //显示冻结eth
    @BindView(R.id.tv_trans_dj_eth1)
    TextView tv_trans_dj_eth1;
    //冻结eth
    @BindView(R.id.tv_trans_dj_eth2)
    TextView tv_trans_dj_eth2;


    //显示总资产
    @BindView(R.id.tv_trans_total1)
    TextView tv_trans_total1;
    //总资产
    @BindView(R.id.tv_trans_total2)
    TextView tv_trans_total2;



    //没登录时候显示登录按钮
    @BindView(R.id.trans_ll_mydl)
    LinearLayout trans_ll_mydl;

    //买入按钮
    @BindView(R.id.trans_ll_yjdl)
    LinearLayout trans_ll_yjdl;


    @BindView(R.id.main)
    LinearLayout main;


    @BindView(R.id.listview1)
    ListView listview1;
    @BindView(R.id.listview2)
    ListView listview2;

    @BindView(R.id.btn_exchange_order_buy)
    Button btn_placeOrder;
    @BindView(R.id.btn_placeOrder2)
    Button btn_placeOrder2;

    private UserAcountResult userAcount;
    private BalanceResult mBalanceResult;
    private BalanceResult mBTCBalanceResult;
    private UserInfo userInfo;
    private Activity context;
    private List<String> list=new ArrayList<String>();
    private List<String> list2=new ArrayList<String>();
    private List<MarketDepthData> asks_list=new ArrayList<MarketDepthData>();
    private List<MarketDepthData> bids_list=new ArrayList<MarketDepthData>();
    private List<TransMarketDepth> sList1=new ArrayList<TransMarketDepth>();
    private List<TransMarketDepth> sList2=new ArrayList<TransMarketDepth>();
    private QuickAdapter<MarketDepthData> adapter1,adapter2;
    private String length="10",depth="0",ed_type="0";
    private SubscriberOnNextListener marketDepthOnNext;
    private Subscriber doEntrustOnNext;
    private String type = "0",currencyType,exchangeType,prizeRange,sf_ljjy="0";
    //private String currencyType2="",exchangeTyp2="";
    private String isPlan="0",unitPrice,number,safePwd="";
    private UserConfirm userConfirm;
    private TransCurrency t_Currency;
    //当前币种可用
    private double currencyType_ky=0.00;
    //当前币种可买
    private double currencyType_kma=0.00;
    //兑换币种可用
    private double exchangeType_ky=0.00;
    //兑换币种可卖
    private double exchangeType_kma=0.00;
    //当前币价
    private double currentPrice=0.00;

    private keyboardView mkeyboardView;
    private int n=200,sfxz=0,djcdj=0,ddb=0;
    private float mPrice_Buy;
    private float mPrice_sell;
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.trans_sell_layout, container, false);
        view.setOnTouchListener(this);
        unbinder=ButterKnife.bind(this,view);
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
    private boolean sfjzgl=false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()&&sfjzgl) {
            initViewData();
        }
        if (isVisibleToUser && isVisible()&&!sfjzgl) {
            sfjzgl=true;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    private void initView(){

        mkeyboardView= MainActivity.getInstance().getKeyboardView();


        userConfirm=new UserConfirm(context);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        //tv_trans_zzc.setText(getResources().getString(R.string.trans_zzc));
        btn_placeOrder.setOnClickListener(this);
        btn_placeOrder2.setOnClickListener(this);
        tv_currentPrice.setOnClickListener(this);
        main.setOnClickListener(this);
        if(t_Currency!=null)
        {
            prizeRange=t_Currency.getPrizeRange();
            tv_currentPrice.setText(t_Currency.getExchangeType_symbol()+deFormat(currentPrice));
        }

      //  tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+deFormat(currentPrice,6));
          tv_trans_jye.setText(deFormat(currentPrice,6));

        trans_ll_mydl.setOnClickListener(this);
        if(type.equals("1")) {
            btn_placeOrder.setVisibility(View.VISIBLE);
            btn_placeOrder2.setVisibility(View.GONE);
            mkeyboardView.buy.setOnClickListener(this);
        }else{
            btn_placeOrder.setVisibility(View.GONE);
            btn_placeOrder2.setVisibility(View.VISIBLE);
        }

        marketDepthOnNext = new SubscriberOnNextListener<MarketDepth>() {
            @Override
            public void onNext(MarketDepth marketDepth) {
                try{

                    String asks[][]=marketDepth.getAsks();
                    String bids[][]=marketDepth.getBids();

                    asks_list.clear();
                    if (asks != null && asks.length > 0) {
                        for (int n = 0; asks.length > n; n++) {
                            String str[] = asks[asks.length-n-1];
                            MarketDepthData marketDepthData=new MarketDepthData();
                            marketDepthData.setId((asks.length-n)+"");
                            marketDepthData.setPrice(str[0]);
                            if(n==0)
                            {
                                mPrice_Buy=Float.parseFloat(str[0]);
                            }
                            marketDepthData.setNumber(str[1]);
                            asks_list.add(marketDepthData);
                        }
                    }
                    bids_list.clear();
                    if (bids != null && bids.length > 0) {
                        for (int n = 0; bids.length > n; n++) {
                            String str[] = bids[n];
                            MarketDepthData marketDepthData=new MarketDepthData();
                            marketDepthData.setId((n+1)+"");
                            marketDepthData.setPrice(str[0]);
                            if(n==0)
                            {
                                mPrice_sell=Float.parseFloat(str[0]);
                            }
                            marketDepthData.setNumber(str[1]);
                            bids_list.add(marketDepthData);

                        }
                    }

                    adapter1.clear();
                    adapter1.addAll(asks_list);
                    adapter2.clear();
                    adapter2.addAll(bids_list);

                    if(ddb==0){
                      //  listview1.setSelection(listview1.getBottom());
                        listview1.smoothScrollToPosition(listview1.getCount() - 1);//移动到尾部
                        listview2.setSelection(0);
                        ddb=1;
                   }
                    double currentPrice_ls=Double.parseDouble(marketDepth.getCurrentPrice());
                    String str_clo="#3a3a3a";
                    if(currentPrice_ls>currentPrice){
                        str_clo="#E70101";
                        HomeFragment.showHeaderAnimation(tv_currentPrice);
                    }else if(currentPrice_ls<currentPrice){
                        str_clo="#1BA905";
                        HomeFragment.showHeaderAnimation(tv_currentPrice);
                    }
                    currentPrice=currentPrice_ls;
                    tv_currentPrice.setText(t_Currency.getExchangeType_symbol()+deFormat(currentPrice));
                    tv_currentPrice.setTextColor(Color.parseColor(str_clo));
                    String str_jg=getText(ed_trans_jg);

                    if(str_jg.equals("")||str_jg.equals("0")){
                        currencyType_kma=Double.parseDouble(deFormat((exchangeType_ky/currentPrice)+"",6));
                        exchangeType_kma=Double.parseDouble(deFormat((currencyType_ky*currentPrice)+"",6));
                    }else{
                        double dou_jg=Double.parseDouble(str_jg);
                        currencyType_kma=Double.parseDouble(deFormat((exchangeType_ky/dou_jg)+"",6));
                        exchangeType_kma=Double.parseDouble(deFormat((currencyType_ky*dou_jg)+"",6));
                    }


                }catch (Exception e){

                }
            }
        };
        adapter1 = new QuickAdapter<MarketDepthData>(context, R.layout.trans_remote_item,asks_list) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MarketDepthData item) {
                final int position=helper.getPosition();
                helper.setText(R.id.tv_title,getResources().getString(R.string.trans_mi)+item.getId());
                helper.setText(R.id.tv_number,deFormat(item.getNumber()));
                helper.setText(R.id.tv_price,deFormat(item.getPrice()));
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setKeyboardShow(false);
                        String a_number=deFormat(item.getNumber(),0);
                        String a_price=deFormat(item.getPrice(),0);
                        ed_trans_jg.setText(a_price);
                        ed_trans_sl.setText(a_number);

                        double b_price=Double.parseDouble(deFormat(a_price));
                        ed_trans_jg.setText(a_price);
                        double b_number=0;
                        for(int n=position;n<=asks_list.size()-1;n++){
                            b_number+=Double.parseDouble(deFormat(asks_list.get(n).getNumber()));
                        }
                        if(type.equals("1")){
                            if(b_number*b_price>exchangeType_ky){
                                if(exchangeType_ky>0){
                                    ed_trans_sl.setText(deFormat(exchangeType_ky/b_price,3));
                                }else{
                                    ed_trans_sl.setText("0");
                                }
                            }else{
                                ed_trans_sl.setText(deFormat(b_number,3));
                            }
                        }else{
                            if(b_number>currencyType_ky){
                                ed_trans_sl.setText(deFormat(currencyType_ky,3));
                            }else{
                                ed_trans_sl.setText(deFormat(b_number,3));
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
        adapter2 = new QuickAdapter<MarketDepthData>(context, R.layout.trans_remote_item,bids_list) {
            @Override
            protected void convert(BaseAdapterHelper helper,final MarketDepthData item) {
                final int position=helper.getPosition();
                helper.setText(R.id.tv_title,getResources().getString(R.string.trans_ma)+item.getId()).setTextColorRes(R.id.tv_title,R.color.text_color_red);
                helper.setText(R.id.tv_number,deFormat(item.getNumber()));
                helper.setText(R.id.tv_price,deFormat(item.getPrice()));
                helper.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setKeyboardShow(false);
                        String a_number=deFormat(item.getNumber(),0);
                        String a_price=deFormat(item.getPrice(),0);
                        ed_trans_sl.setText(a_number);
                        ed_trans_jg.setText(a_price);

                        double b_price=Double.parseDouble(deFormat(a_price));
                        ed_trans_jg.setText(a_price);
                        double b_number=0;
                        for(int n=0;n<=position;n++){
                            b_number+=Double.parseDouble(deFormat(bids_list.get(n).getNumber()));
                        }
                        if(type.equals("1")){
                            if(b_number*b_price>exchangeType_ky){
                                if(exchangeType_ky>0){
                                    ed_trans_sl.setText(deFormat(exchangeType_ky/b_price,3));
                                }else{
                                    ed_trans_sl.setText("0");
                                }
                            }else{
                                ed_trans_sl.setText(deFormat(b_number,3));
                            }
                        }else{
                            if(b_number>currencyType_ky){
                                ed_trans_sl.setText(deFormat(currencyType_ky,3));
                            }else{
                                ed_trans_sl.setText(deFormat(b_number,3));
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
        listview1.setAdapter(adapter1);
        listview2.setAdapter(adapter2);
     //   handler.postDelayed(runnable4, 0);
        ViewTreeObserver observer = listview1.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                try {
                    Message msg = new Message();
                    msg.what = n;
                    msg.arg1 = listview1.findViewById(R.id.ll_trans_item).getHeight();
                    mHandler.sendMessage(msg);
                }catch (Exception e){

                }
                return true;
            }
        });

        ed_trans_sl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(!is_token(userInfo)){
                    UIHelper.showLogin(context);
                    return false;
                }


                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    ed_type="1";

                    mkeyboardView.setEditText(ed_trans_sl, 6, type);
                    if(type.equals("1")) {
                        mkeyboardView.setBuyOrSell(true);
                        mkeyboardView.processingData(Double.parseDouble(deFormat(exchangeType_kma,3)));
                    }else{
                        mkeyboardView.setBuyOrSell(false);
                        mkeyboardView.processingData(Double.parseDouble(deFormat(currencyType_ky,3)));
                    }
                    mkeyboardView.setViewVisible(true);
                    setKeyboardShow(true);
                }


                return false;
            }
        });

        ed_trans_jg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                try {
                    if(!is_token(userInfo)){
                        UIHelper.showLogin(context);
                        return false;
                    }
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                        ed_type = "0";
                        mkeyboardView.setEditText(ed_trans_jg, 6, type);
                        if (type.equals("1")) {
                            mkeyboardView.setBuyOrSell(true);
                            mkeyboardView.processingData(Double.parseDouble(deFormat(exchangeType_kma, 3)));
                        } else {
                            mkeyboardView.setBuyOrSell(false);
                            mkeyboardView.processingData(Double.parseDouble(deFormat(currencyType_ky, 3)));
                        }
                        mkeyboardView.setViewVisible(false);

                        setKeyboardShow(true);
                    }
                }catch (Exception e)
                {

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
                bnt_placeOrder();
                String str=s.toString();
                if (str.equals(".")) {
                    ed_trans_jg.setText("");
                }else if(str.equals("00")||str.equals("01")||str.equals("02")||str.equals("03")||str.equals("04")||str.equals("05")||str.equals("06")||str.equals("07")||str.equals("08")||str.equals("09")){
                   // ed_trans_jg.setText("0");
                    ed_trans_jg.setText(str.substring(1));
                    Editable etext = ed_trans_jg.getText();
                    Selection.setSelection(etext, etext.length());
                } else if (!str.equals("")) {
                    String a_number=ed_trans_sl.getText().toString().trim();
                    if(!a_number.equals("")){
                        String a_amount=deFormat(Double.parseDouble(a_number)*Double.parseDouble(str));
                       // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+deFormat(a_amount,6));
                        tv_trans_jye.setText(deFormat(a_amount,6));
                    }

                }else{
                   // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
                    tv_trans_jye.setText("0.00");
                }

                if(str.equals("")||str.equals("0")){
                    currencyType_kma=Double.parseDouble(deFormat((exchangeType_ky/mPrice_sell)+"",6));
                    exchangeType_kma=Double.parseDouble(deFormat((currencyType_ky*mPrice_Buy)+"",6));

                }else{
                    try {
                        double dou_jg=Double.parseDouble(str);
                        currencyType_kma=Double.parseDouble(deFormat((exchangeType_ky/dou_jg)+"",6));
                        exchangeType_kma=Double.parseDouble(deFormat((currencyType_ky*dou_jg)+"",6));

                    }catch (Exception e){
                        currencyType_kma=Double.parseDouble(deFormat((exchangeType_ky/currentPrice)+"",6));
                        exchangeType_kma=Double.parseDouble(deFormat((currencyType_ky*currentPrice)+"",6));
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_trans_sl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bnt_placeOrder();
                String str=s.toString();
                if (str.equals(".")) {
                    ed_trans_sl.setText("");
                }else if(str.equals("00")||str.equals("01")||str.equals("02")||str.equals("03")||str.equals("04")||str.equals("05")||str.equals("06")||str.equals("07")||str.equals("08")||str.equals("09")){
                    ed_trans_sl.setText("0");
                    ed_trans_sl.setText(str.substring(1));
                    Editable etext = ed_trans_sl.getText();
                    Selection.setSelection(etext, etext.length());
                } else if (!str.equals("")) {
                    String a_price=ed_trans_jg.getText().toString().trim();
                    if(!a_price.equals("")){
                         String a_amount=deFormat(Double.parseDouble(a_price)*Double.parseDouble(str));
                        // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+deFormat(a_amount,6));
                        tv_trans_jye.setText(deFormat(a_amount,6));
                    }
                }else{
                    if(ed_type.equals("1")){
                      //  tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
                        tv_trans_jye.setText("0.00");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void showTotalTextView(UserAcountResult userAcount)
    {
        if(userAcount==null)
        {
            show_ky_btc1.setText(getString(R.string.trans_ky)+exchangeType);
            show_ky_btc2.setText("0.00");

            tv_trans_km_eth1.setText(getString(R.string.trans_km)+currencyType);
            tv_trans_km_eth2.setText("0.00");

            tv_trans_dj_btc1.setText(getString(R.string.trans_dj)+exchangeType);
            tv_trans_dj_btc2.setText("0.00");

            tv_trans_ky_eth1.setText(getString(R.string.trans_ky)+currencyType);
            tv_trans_ky_eth2.setText("0.00");

            tv_trans_km_btc1.setText(getString(R.string.trans_kmm)+exchangeType);
            tv_trans_km_btc2.setText("0.00");

            tv_trans_dj_eth1.setText(getString(R.string.trans_dj)+currencyType);
            tv_trans_dj_eth2.setText("0.00");

            tv_trans_total1.setText(getResources().getString(R.string.trans_zzc));
            tv_trans_total2.setText("0.00");
        }else
        {

            show_ky_btc1.setText(getString(R.string.trans_ky)+exchangeType);
            show_ky_btc2.setText(exchangeType_ky+"");

            tv_trans_km_eth1.setText(getString(R.string.trans_km)+currencyType);
            tv_trans_km_eth2.setText(currencyType_kma+"");

            tv_trans_dj_btc1.setText(getString(R.string.trans_dj)+exchangeType);
            if(mBTCBalanceResult!=null)
            {
                tv_trans_dj_btc2.setText(deFormat(mBTCBalanceResult.getFreeze(),6));
            }else
            {
                tv_trans_dj_btc2.setText("0.00");
            }


            tv_trans_ky_eth1.setText(getString(R.string.trans_ky)+currencyType);
            tv_trans_ky_eth2.setText(currencyType_ky+"");

            tv_trans_km_btc1.setText(getString(R.string.trans_kmm)+exchangeType);
            tv_trans_km_btc2.setText(exchangeType_kma+"");

            tv_trans_dj_eth1.setText(getString(R.string.trans_dj)+currencyType);
            if(mBalanceResult!=null)
            {
                tv_trans_dj_eth2.setText(deFormat(mBalanceResult.getFreeze(),6));
            }else
            {
                tv_trans_dj_eth2.setText("0.00");
            }


            tv_trans_total1.setText(getResources().getString(R.string.trans_zzc));
            tv_trans_total2.setText(deFormat(userAcount.getTotalAmount(),6));
        }

    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    int height =  msg.arg1;
                    ViewGroup.LayoutParams params= listview1.getLayoutParams();
                    params.height=5*height;
                    listview1.setLayoutParams(params);
                    listview2.setLayoutParams(params);
                    n++;
                }catch (Exception e){

                }
            }
        }
    };
    private void initData() {
        currencyType=TransFragment.fragment.getCurrencyType().toUpperCase();
        exchangeType= TransFragment.fragment.getExchangeType().toUpperCase();
        t_Currency= TransFragment.fragment.getT_Currency();
        userInfo = UserDao.getInstance().getIfon();

        if(is_token(userInfo)) {
            userAcount = UserAcountDao.getInstance().getIfon(userInfo.getUserId());
            for(int i=0;i<userAcount.getUserAccount().getBalances().size();i++)
            {
                if(userAcount.getUserAccount().getBalances().get(i).getPropTag().toUpperCase().equals(exchangeType.toUpperCase()))
                {
                    mBTCBalanceResult = userAcount.getUserAccount().getBalances().get(i);
                }
                if(userAcount.getUserAccount().getBalances().get(i).getPropTag().toUpperCase().equals(currencyType.toUpperCase()))
                {
                    mBalanceResult = userAcount.getUserAccount().getBalances().get(i);
                }

            }

            if(mBTCBalanceResult!=null)
            {
                //可用BTC
                exchangeType_ky = Double.parseDouble(deFormat(mBTCBalanceResult.getBalance()));
            }

            if(mBalanceResult!=null)
            {
                //可用ETC
                currencyType_ky =Double.parseDouble(deFormat(mBalanceResult.getBalance()));
            }



        }
        for(int n=1;n<6;n++){
            MarketDepthData marketDepthData=new MarketDepthData();
            marketDepthData.setId(n+"");
            marketDepthData.setPrice("--");
            marketDepthData.setNumber("--");
            bids_list.add(marketDepthData);
            marketDepthData.setId((6-n)+"");
            asks_list.add(marketDepthData);
        }


    }
    public void initViewData() {
        initData();
        if(is_token(userInfo)){
            //    MainActivity.getInstance().getUserInfo();
            trans_ll_mydl.setVisibility(View.GONE);
            trans_ll_yjdl.setVisibility(View.VISIBLE);

            if(userAcount==null){
                handler.postDelayed(runnable, 500);
                return;
            }

        }else{
            trans_ll_yjdl.setVisibility(View.GONE);
            trans_ll_mydl.setVisibility(View.VISIBLE);

            exchangeType_ky=0.0000;
            currencyType_ky=0.0000;
            currencyType_kma=0.0000;
        }
        showTotalTextView(userAcount);

       /* tv_trans_jg.setText(getString(R.string.trans_mr)+currencyType+getString(R.string.trans_jg));
        tv_trans_sl.setText(getString(R.string.trans_mr)+currencyType+getString(R.string.trans_sl));*/
        tv_trans_jg.setText(exchangeType.toUpperCase());
        tv_trans_sl.setText(currencyType.toUpperCase());
        tv_trans_jye_show.setText(exchangeType.toUpperCase());
        marketDepth();

    }

    private void marketDepth(){
        list.clear();
        list.add(length);
        list.add(currencyType);
        list.add(exchangeType);
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(marketDepthOnNext, context);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(1).marketDepth(progressSubscriber,list);
    }
    private void ljjy(){
        int sfyxstk=0;
       // if("1".equals(userInfo.getSafePwdPeriod())&&"3".equals(userInfo.getTradeAuthenType())){
        if("1".equals(userInfo.getSafePwdPeriod())){
            sfyxstk++;
            userConfirm.ed_user_safePwd.setVisibility(View.VISIBLE);
            userConfirm.tv_user_title2.setText(getString(R.string.trans_nykqzjmmbh_hint));
        }else{
            userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        }

        double d_unitPrice =Double.parseDouble(unitPrice);
        double d_prizeRange =Double.parseDouble(prizeRange);

        if(type.equals("1")){
            userConfirm.tv_user_title1.setText(getString(R.string.trans_qrmr)+" "+number+" "+currencyType+" ？");
            if(d_unitPrice>currentPrice*(d_prizeRange+1)){
                sfyxstk++;
                userConfirm.tv_user_title2.setText(getString(R.string.trans_nmrjgyjj_hint)+(d_prizeRange*100)+"%");
            }
        }else{
            userConfirm.tv_user_title1.setText(getString(R.string.trans_qrmc)+" "+number+" "+currencyType+" ？");
            if(d_unitPrice<currentPrice*(1-d_prizeRange)){
                sfyxstk++;
                userConfirm.tv_user_title2.setText(getString(R.string.trans_nmcjdyjj_hint)+(d_prizeRange*100)+"%");
            }
        }
        if(sfyxstk>0){
            userConfirm.show();
        }else{
            doEntrust();
        }
    }


    private void doEntrust(){
        long timeStamp=System.currentTimeMillis();
        list2.clear();
        list2.add(timeStamp+"");
        list2.add(type);
        list2.add(currencyType);
        list2.add(exchangeType);
        list2.add(isPlan);
        list2.add(unitPrice);
        list2.add(number);
        list2.add(safePwd);
        doEntrustOnNext  = new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handler.postDelayed(runnable22, 1000);
            }

            @Override
            public void onNext(HttpResult httpResult) {
                if(httpResult.getResMsg().getCode().equals("1000")){
                    handler.postDelayed(runnable2, 1000);
                    clear_ed();
                }else{
                    UIHelper.ToastMessage(context,httpResult.getResMsg().getMessage());
                    handler.postDelayed(runnable22, 1000);
                }
            }

        };
        HttpMethods.getInstance(1).doEntrust(doEntrustOnNext,list2);
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            initViewData();
        }
    };
    Runnable runnable2 = new Runnable() {
        public void run() {
            handler.postDelayed(runnable3, 1500);
        }
    };
    Runnable runnable22 = new Runnable() {
        public void run() {
            handler.postDelayed(runnable3, 1500);
        }
    };
    Runnable runnable3 = new Runnable() {
        public void run() {
            if(type.equals("1")) {
                btn_placeOrder.setVisibility(View.VISIBLE);
            }else{
                btn_placeOrder2.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trans_ll_mydl:
                UIHelper.showLogin(context);
               // UIHelper.showLoginOrRegister(context);
                break;
            case R.id.bnt_user_commit:
                safePwd=getText(userConfirm.ed_user_safePwd);
                if(safePwd.equals("")&&userConfirm.ed_user_safePwd.isShown()){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                userConfirm.dismiss();
                doEntrust();
                break;
            case R.id.buy:
                sf_ljjy="0";
                buyOrSell();
                break;
            case R.id.btn_exchange_order_buy:
            case R.id.btn_placeOrder2:
                sf_ljjy="1";
                buyOrSell();
                break;
            case R.id.tv_currentPrice:
                ed_trans_jg.setText(deFormat(currentPrice,3));
                Editable etext = ed_trans_jg.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.main:
                setKeyboardShow(false);
                break;
        }
    }

    private void buyOrSell()
    {
        
        unitPrice=getText(ed_trans_jg);
        number=getText(ed_trans_sl);



        if(unitPrice.equals("")){
            UIHelper.ToastMessage(context,R.string.trans_jg_hint);
            return;
        }
        if(number.equals("")){
            UIHelper.ToastMessage(context,R.string.trans_sl_hint);
            return;
        }
        if(Double.parseDouble(unitPrice)==0){
            UIHelper.ToastMessage(context,R.string.trans_jg_hint);
            return;
        }
        if(Double.parseDouble(number)==0){
            UIHelper.ToastMessage(context,R.string.trans_sl_hint);
            return;
        }
        setKeyboardShow(false);
        String trans_state= SharedPreferences.getInstance().getString("trans_state","0");
        if(trans_state.equals("0")||sf_ljjy.equals("0")){
            ljjy();
        }else{
            if(djcdj==0){
                if(type.equals("1")) {
                    btn_placeOrder.setText(R.string.trans_djqr);
                }else{
                    btn_placeOrder2.setText(R.string.trans_djqr);
                }
                djcdj=1;
            }else{
                bnt_placeOrder();
                ljjy();
            }
        }

    }

    public void bnt_placeOrder() {
        djcdj=0;
        if(type.equals("1")) {
            btn_placeOrder.setText(R.string.trans_xhmr);
        }else{
            btn_placeOrder2.setText(R.string.trans_xhmc);
        }

    }
    public void clear_ed() {
        try {
            ed_trans_sl.setText("");
            ed_trans_jg.setText("");
           // tv_trans_jye.setText(getResources().getString(R.string.trans_jye)+" "+t_Currency.getExchangeType_symbol()+"0.00");
            tv_trans_jye.setText("0.00");
        }catch (Exception e){

        }

    }

    private void setKeyboardShow(boolean show)
    {
        try {
            if(mkeyboardView==null)
                return;
            if(show)
            {
                mkeyboardView.setVisibility(View.VISIBLE);
            }else
            {
                mkeyboardView.setVisibility(View.GONE);
            }
        }catch (Exception e)
        {
        }

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setKeyboardShow(false);
        return false;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
