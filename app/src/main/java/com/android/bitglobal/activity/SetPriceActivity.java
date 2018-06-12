package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.entity.CurrencyMarketDepthRealm;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.EntityString;
import com.android.bitglobal.entity.MarketRemindResult;
import com.android.bitglobal.entity.TradeExchange;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.fragment.market.MarketFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.PopupDialog;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-10-08 14:10
 * 793169940@qq.com
 *
 */
public class SetPriceActivity extends SwipeBackActivity implements View.OnClickListener {
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.add_price_image)
    ImageView addPriceImage;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_set_price_nodata)
    TextView tv_set_price_nodata;
//    private String btc_h="",btc_l="",eth_h="",eth_l="",etc_h="",etc_l="",ltc_h="",ltc_l="";
    private QuickAdapter<MarketRemindResult> adapter;
    private List<MarketRemindResult> list_data=new ArrayList<MarketRemindResult>();
    private SubscriberOnNextListener getMarketRemindOnNext;
    private SubscriberOnNextListener postMarketRemindOnNext;
    private SubscriberOnNextListener deleteMarketRemindOnNext;
//    private JSONArray jsonArray;
    private PopupDialog assetList;
    private List<TradeExchange> TradeExchangelist;
    private HashMap<String, String> currencyBtcPriceMap;
    private HashMap<String, String> currencyUsdPriceMap;
    private CurrencySetResult currencySetResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_list);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getMarketRemind();
    }

    private void initData() {
        currencySetResult = CurrencySetDao.getInstance().getIfon();
        currencyBtcPriceMap = (HashMap<String, String>) getIntent().getExtras().getSerializable("currencyBtcPriceMap");
        currencyUsdPriceMap = (HashMap<String, String>) getIntent().getExtras().getSerializable("currencyUsdPriceMap");
    }

    private void initView() {
        tv_head_title.setText(R.string.user_jgyj);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        addPriceImage.setVisibility(View.VISIBLE);
        addPriceImage.setOnClickListener(this);
        listview.setDividerHeight(0);
        adapter = new QuickAdapter<MarketRemindResult>(context, R.layout.activity_setprice) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final MarketRemindResult item) {
//                final int position_i=helper.getPosition();
//                final View view=helper.getView();
//                final SwitchView view_switch=(SwitchView)view.findViewById(R.id.view_switch);
//                final EditText ed_jgsx=(EditText)view.findViewById(R.id.ed_jgsx);
//                final EditText ed_jgxx=(EditText)view.findViewById(R.id.ed_jgxx);
//                CurrencySetRealm currencySet=CurrencySetDao.getInstance().getIfon(item.getCurrencyType());
//                if(!currencySet.getCoinUrl().equals(""))
//                {
//                    helper.setImageUrl(R.id.img_type,currencySet.getCoinUrl());
//                }
//                helper.setText(R.id.tv_name,getString(R.string.app_name)+"-"+item.getCurrencyType());
                helper.setText(R.id.name_text, item.getCurrency().toUpperCase() + "/" + item.getExchange().toUpperCase());
                helper.setText(R.id.price_text, getString(R.string.market_detail_trade_price) + ": " + format(item.getPrice(), Utils.getPrecisionExchange(item.getCurrency(), item.getExchange())));
                helper.setOnClickListener(R.id.delete_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDeleteConfirmDialog(item.getId());
                    }
                });
//                if(item.getStatus().equals("0")){
////                    helper.setVisible(R.id.ll_xsjg,false);
//                    if(view_switch.isOpened()){
//                        view_switch.toggleSwitch(false);
//                    }
//                }else{
//                    helper.setVisible(R.id.ll_xsjg,true);
//                    if(!view_switch.isOpened()){
//                        view_switch.toggleSwitch(true);
//                    }
//                }
//                view_switch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//                    @Override
//                    public void toggleToOn(View view) {
//                        MarketRemindResult mrr_i=item;
//                        mrr_i.setStatus("1");
//                        list_data.set(position_i,mrr_i);
//                        adapter.replaceAll(list_data);
//                    }
//                    @Override
//                    public void toggleToOff(View view) {
//                        MarketRemindResult mrr_i=item;
//                        mrr_i.setStatus("0");
//                        list_data.set(position_i,mrr_i);
//                        adapter.replaceAll(list_data);
//                    }
//                });
//                ed_jgsx.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        String str_ls=getText(ed_jgsx);
//                        if(item.getCurrencyType().equals("BTC")){
//                            btc_h=str_ls;
//                        }else if (item.getCurrencyType().equals("ETH")) {
//                            eth_h=str_ls;
//                        }else if (item.getCurrencyType().equals("ETC")) {
//                            etc_h=str_ls;
//                        }else if (item.getCurrencyType().equals("LTC")) {
//                            ltc_h=str_ls;
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                ed_jgxx.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        String str_ls=getText(ed_jgxx);
//                        if(item.getCurrencyType().equals("BTC")){
//                            btc_l=str_ls;
//                        }else if (item.getCurrencyType().equals("ETH")) {
//                            eth_l=str_ls;
//                        }else if (item.getCurrencyType().equals("ETC")) {
//                            etc_l=str_ls;
//                        }else if (item.getCurrencyType().equals("LTC")) {
//                            ltc_l=str_ls;
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                if(getText(ed_jgsx).equals("")){
//                    helper.setText(R.id.ed_jgsx,item.getHigh());
//                   /* if(item.getCurrencyType().equals("BTC")){
//                        btc_h=item.getHigh();
//                    }else if (item.getCurrencyType().equals("ETH")) {
//                        eth_h=item.getHigh();
//                    }else if (item.getCurrencyType().equals("ETC")) {
//                        etc_h=item.getHigh();
//                    }else if (item.getCurrencyType().equals("LTC")) {
//                        ltc_h=item.getHigh();
//                    }*/
//                }
//                if(getText(ed_jgxx).equals("")){
//                    helper.setText(R.id.ed_jgxx,item.getLow());
//                    /*if(item.getCurrencyType().equals("BTC")){
//                        btc_l=item.getLow();
//                    }else if (item.getCurrencyType().equals("ETH")) {
//                        eth_l=item.getLow();
//                    }else if (item.getCurrencyType().equals("ETC")) {
//                        etc_l=item.getLow();
//                    }else if (item.getCurrencyType().equals("LTC")) {
//                        ltc_l=item.getLow();
//                    }*/
//                }
            }
        };
        listview.setAdapter(adapter);
        getMarketRemindOnNext = new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                adapter.clear();
                list_data.clear();
//                removeBtcRemind(versionResult.getMarketReminds());
                if(versionResult.getMarketReminds().isEmpty()){
                    tv_set_price_nodata.setVisibility(View.VISIBLE);
                }else {
                    tv_set_price_nodata.setVisibility(View.GONE);
                }

                list_data.addAll(versionResult.getMarketReminds());
                adapter.replaceAll(list_data);
            }

        };
        postMarketRemindOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                dismiss();
                getMarketRemind();
            }

        };
        deleteMarketRemindOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                dismiss();
                getMarketRemind();
            }

        };

        List<TransCurrency> list = new ArrayList<>();
        TradeExchangelist = MarketFragment.TradeExchangelist;
        TradeExchangelist=getSortCoinList(TradeExchangelist);
        if (TradeExchangelist != null && TradeExchangelist.size() > 0) {
            for (int i = 0; i < TradeExchangelist.size(); i++) {
                TransCurrency transCurrency = new TransCurrency();
                TradeExchange mTradeExchange = TradeExchangelist.get(i);
                for (CurrencySetRealm csr : currencySetResult.getCurrencySets()) {

                    if (csr.getCurrency().toLowerCase().equals(mTradeExchange.getCurrencyType().toLowerCase())) {

                        transCurrency.setCurrencyType(csr.getCurrency());
                        transCurrency.setUrl(csr.getFinanceCoinUrl());
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
//                Map<String, String> price = currencyPriceList.get(i);
//                transCurrency.setBtcPrizeRange(deFormat(price.get(SystemConfig.homeBTC), Utils.getPrecisionPrice(transCurrency.getCurrencyType())));
//                transCurrency.setUsdcPrizeRange(deFormat(price.get(SystemConfig.homeUSDC), Utils.getPrecisionPrice(transCurrency.getCurrencyType())));
                list.add(transCurrency);
            }
        }

        assetList = new PopupDialog(context);
        assetList.createAssetSelectList(list, new PopupDialog.PopupDialogCallback<TransCurrency>() {
            @Override
            public void dataBack(TransCurrency item) {
                assetList.dismiss();
                alertAddPriceDialog(item);
            }
        });
    }

    /**
     * 将List<TradeExchange> 重新排序  usdc在前，btc在后
     * @param list
     * @return
     */
    private List<TradeExchange> getSortCoinList(List<TradeExchange> list){
        List<TradeExchange> tradeExchanges=new ArrayList<>();
        if (!list.isEmpty()){
            for (TradeExchange tradeExchange:list){
                if (SystemConfig.homeUSDC.equals(tradeExchange.getExchangeType())){
                    tradeExchanges.add(tradeExchange);
                }
            }
            for (TradeExchange tradeExchange:list){
                if (SystemConfig.homeBTC.equals(tradeExchange.getExchangeType())){
                    tradeExchanges.add(tradeExchange);
                }
            }
            return tradeExchanges;
        }else {
            return tradeExchanges;
        }
    }
//    private void removeBtcRemind(List<MarketRemindResult> marketReminds)
//    {
//        for (int i=0;i<marketReminds.size();i++)
//        {
//            MarketRemindResult mMarketRemindResult=marketReminds.get(i);
//            if(mMarketRemindResult.getCurrencyType().equals("BTC")||mMarketRemindResult.getCurrencyType().equals("btc"))
//            {
//                marketReminds.remove(i);
//            }
//        }
//
//    }

    private void getMarketRemind() {
        HttpMethods.getInstance(3).getMarketRemind(new ProgressSubscriber(getMarketRemindOnNext,this));
    }

    private void setMarketRemind(MarketRemindResult marketRemindResult, String currencyPrice) {
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(postMarketRemindOnNext,this);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).setMarketRemind(progressSubscriber, marketRemindResult, currencyPrice);
    }

    private void deleteMarketRemind(String marketRemindId) {
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(deleteMarketRemindOnNext,this);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).deleteMarketRemind(progressSubscriber, marketRemindId);
    }

    public boolean is_token(UserInfo userInfo){
        boolean is_token;
        if(userInfo==null||userInfo.getToken()==null||userInfo.getToken().equals("null")||userInfo.getToken().equals("")){
            is_token=false;
        }else{
            is_token=true;
        }
        return is_token;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.add_price_image:
                assetList.show();
                break;
        }

    }

    private void alertAddPriceDialog(final TransCurrency transCurrency) {
        View addPriceView = View.inflate(context, R.layout.dialog_add_price, null);
        TextView priceTypeText = (TextView) addPriceView.findViewById(R.id.price_type_text);
        TextView currencyPriceText = (TextView) addPriceView.findViewById(R.id.currency_price_text);
        TextView okText = (TextView) addPriceView.findViewById(R.id.ok_text);
        TextView cancelText = (TextView) addPriceView.findViewById(R.id.cancel_text);
        final EditText alertPriceText = (EditText) addPriceView.findViewById(R.id.alert_price_text);

        String exchangeType = transCurrency.getExchangeType().toUpperCase();
        String currencyType = transCurrency.getCurrencyType().toUpperCase();
        priceTypeText.setText(currencyType + "/" + exchangeType);
        if(exchangeType.equals(SystemConfig.homeBTC.toUpperCase())) {
            currencyPriceText.setText(getString(R.string.dialog_current_price) + format(currencyBtcPriceMap.get(currencyType),
                    Utils.getPrecisionExchange(currencyType, exchangeType)));
        } else {
            currencyPriceText.setText(getString(R.string.dialog_current_price) + format(currencyUsdPriceMap.get(currencyType),
                    Utils.getPrecisionExchange(currencyType, exchangeType)));
        }
        alertPriceText.setHint(getString(R.string.dialog_alert_price_hint).replaceAll("#", transCurrency.getExchangeType().toUpperCase()));
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alertPriceTextString = alertPriceText.getText().toString().trim();
                if("".equals(alertPriceTextString) || Double.parseDouble(alertPriceTextString) == 0) {
                    UIHelper.ToastMessage(SetPriceActivity.this,
                            getString(R.string.dialog_input_alert_price_toast).replaceAll("#", transCurrency.getExchangeType().toUpperCase()));
                    return;
                }
                MarketRemindResult marketRemindResult = new MarketRemindResult();
                marketRemindResult.setCurrency(transCurrency.getCurrencyType().toUpperCase());
                marketRemindResult.setExchange(transCurrency.getExchangeType().toUpperCase());
                marketRemindResult.setPrice(alertPriceTextString);
                setMarketRemind(marketRemindResult, transCurrency.getPrizeRange());
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        alertPriceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 限制输入金额最多为 limit 位小数
                 */
                int limit = Utils.getPrecisionExchange(transCurrency.getCurrencyType(), transCurrency.getExchangeType());
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > limit) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + limit + 1);
                        alertPriceText.setText(s);
                        alertPriceText.setSelection(s.length());
                    }
//                } else {
//                    if(s.length() > limit) {
//                        s = s.toString().subSequence(0, limit);
//                        alertPriceText.setText(s);
//                        alertPriceText.setSelection(s.length());
//                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alert(addPriceView, false);
    }

    private void alertDeleteConfirmDialog(final String id) {
        View deleteConfirmDialog = View.inflate(context, R.layout.dialog_delete_confirm, null);
        TextView okText = (TextView) deleteConfirmDialog.findViewById(R.id.ok_text);
        TextView cancelText = (TextView) deleteConfirmDialog.findViewById(R.id.cancel_text);

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarketRemind(id);
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        alert(deleteConfirmDialog, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        jsonArray=new JSONArray();
//        try {
//            for(int n=0;n<list_data.size();n++){
//                MarketRemindResult mrr=list_data.get(n);
//                JSONObject job=new JSONObject();
//                String currencyType_ls=mrr.getCurrencyType();
//                String status_ls=mrr.getStatus();
//
//                if(currencyType_ls.equals("BTC")){
//                    Log.e("btc_h=btc_l",btc_h+"=="+btc_l);
//                    /*if(btc_h.equals("")||btc_l.equals("")){
//                        job.put("low","");
//                        job.put("high","");
//                        job.put("status","0");
//                    }else{
//                        job.put("low",btc_l);
//                        job.put("high",btc_h);
//                        job.put("status",status_ls);
//                    }*/
//                    job.put("low",btc_l);
//                    job.put("high",btc_h);
//                    job.put("status",status_ls);
//                }
//                if (currencyType_ls.equals("ETH")) {
//                    Log.e("eth_h=eth_l",eth_h+"=="+eth_l);
//                    /*if(eth_h.equals("")||eth_l.equals("")){
//                        job.put("low","");
//                        job.put("high","");
//                        job.put("status","0");
//                    }else{
//                        job.put("low",eth_l);
//                        job.put("high",eth_h);
//                        job.put("status",status_ls);
//                    }*/
//                    job.put("low",eth_l);
//                    job.put("high",eth_h);
//                    job.put("status",status_ls);
//                }
//                if (currencyType_ls.equals("ETC")) {
//                    Log.e("etc_h=etc_l",etc_h+"=="+etc_l);
//                    /*if(etc_h.equals("")||etc_l.equals("")){
//                        job.put("low","");
//                        job.put("high","");
//                        job.put("status","0");
//                    }else{
//                        job.put("low",etc_l);
//                        job.put("high",etc_h);
//                        job.put("status",status_ls);
//                    }*/
//                    job.put("low",etc_l);
//                    job.put("high",etc_h);
//                    job.put("status",status_ls);
//                }
//                if (currencyType_ls.equals("LTC")) {
//                    Log.e("ltc_h=ltc_l",ltc_h+"=="+ltc_l);
//                    /*if(ltc_h.equals("")||ltc_l.equals("")){
//                        job.put("low","");
//                        job.put("high","");
//                        job.put("status","0");
//                    }else{
//                        job.put("low",ltc_l);
//                        job.put("high",ltc_h);
//                        job.put("status",status_ls);
//                    }*/
//                    job.put("low",ltc_l);
//                    job.put("high",ltc_h);
//                    job.put("status",status_ls);
//                }
//                job.put("currencyType",currencyType_ls);
//                jsonArray.put(job);
//
//            }
//            setMarketRemind();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}

