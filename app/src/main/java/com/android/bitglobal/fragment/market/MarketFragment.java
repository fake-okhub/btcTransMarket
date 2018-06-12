package com.android.bitglobal.fragment.market;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.adapters.MarketInfoAdapter;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketAndCurrency;
import com.android.bitglobal.entity.MarketDatasResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TradeExchange;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.HomeFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

public class MarketFragment extends BaseFragment {
    @BindView(R.id.listview)
    ListView listview;
    private final String kChartTimeInterval = "1800";                       //图表数据间隔
    private final String kChartDataSize = "48";                          //图表数据总条数
    private final int kChartXAsixLabelInterval = 50;

    private HomeFragment mHomeFragment;
    private Activity context;
    private CurrencySetResult currencySetResult;
    private QuickAdapter<MarketAndCurrency> adapter;
    private String currencyType = "ETH";
    private String exchangeType = "BTC";
    private String symbol_cny = "￥";
    /**
     * 当前列表中展示币list
     */
    private List<MarketAndCurrency> showMarketAndCurrencyList =new ArrayList<>();
    /**
     * 网络返回所有币种list ，用于价格预警展示
     */
    private List<MarketAndCurrency> allMarketAndCurrencyList=new ArrayList<>();

    private MarketInfoAdapter marketInfoAdapter = null;
    private String exchangeFlag = SystemConfig.homeUSDC;
    //记录点击的item
    //private  MarketAndCurrency mMarketAndCurrency = null;

    public static List<TradeExchange> TradeExchangelist = new ArrayList<TradeExchange>();

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
        SharedPreferences.getInstance().putString("currencyType", currencyType);
        if (mHomeFragment != null)
            mHomeFragment.setHeadTitle();
    }

    public void setHomeContext(HomeFragment homeFragment) {
        this.mHomeFragment = homeFragment;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
        SharedPreferences.getInstance().putString("exchangeType", exchangeType);
        if (mHomeFragment != null)
            mHomeFragment.setHeadTitle();
    }

    public void setExchangeFlag(String exchangeFlag){
        SystemConfig.homeDef=exchangeFlag;
        this.exchangeFlag=exchangeFlag;
    }
    public String getExchangeFlag(){
        return exchangeFlag;
    }

    TextView tv_rate_hint;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_market, container, false);
        unbinder = ButterKnife.bind(this, mView);
        currencySetResult = CurrencySetDao.getInstance().getIfon();
        if (currencySetResult != null) {
            currencyType = currencySetResult.getCurrencySets().get(1).getCurrency();
            setCurrencyType(currencyType);
        }
        marketInfoAdapter = new MarketInfoAdapter(this.getActivity());
        if (getUserVisibleHint()) {
            marketlist();
        }
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
    }

    //c拆分交易兑换币
    private TradeExchange splitTradeExchange(String symbol) {
        TradeExchange mTradeExchange = null;
        if (StringUtils.isEmpty(symbol)) {
            return new TradeExchange();
        }
        if (symbol.length() > 0) {
            mTradeExchange = new TradeExchange();
            String[] names = symbol.split("_");
            if (names.length > 0) {
                mTradeExchange.setCurrencyType(names[0]);
                mTradeExchange.setExchangeType(names[1]);
            }
        }
        return mTradeExchange;
    }

    private void initView() {
        tv_rate_hint = new TextView(getContext());
        tv_rate_hint.setVisibility(View.GONE);
        tv_rate_hint.setGravity(Gravity.CENTER);
        tv_rate_hint.setPadding(0, 15, 0, 15);
//        listview.addFooterView(tv_rate_hint);
        listview.setAdapter(marketInfoAdapter);
        //添加顶部和底部item的分割线
        listview.addHeaderView(new ViewStub(getActivity()));
        listview.addFooterView(new ViewStub(getActivity()));
        listview.setItemsCanFocus(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //listview中添加了headerView，此处  position-1
                MarketAndCurrency mMarketAndCurrency = showMarketAndCurrencyList.get(i-1);
                TradeExchange mTradeExchange = splitTradeExchange(mMarketAndCurrency.getSymbol());
                UIHelper.showMarketDetailActivity(context, mTradeExchange.getCurrencyType(),
                        mTradeExchange.getExchangeType(), mMarketAndCurrency.getCurrencySet().getCoinUrl());
            }
        });
    }

    //保存交易兑换币
    private void split(String symbol) {
        String[] names = symbol.split("_");
        if (names.length > 0) {
            TradeExchange mTradeExchange = new TradeExchange();
            mTradeExchange.setCurrencyType(names[0]);
            mTradeExchange.setExchangeType(names[1]);
            TradeExchangelist.add(mTradeExchange);
        }
    }

    private String getExchangeType(String symbol){
        String[] names = symbol.split("_");
        String exchangeType="";
        if (names.length > 0) {
            exchangeType=names[1];
        }
        return exchangeType;
    }

    private void initData(List<MarketDatasResult> marketDatas) {
        showMarketAndCurrencyList.clear();
        TradeExchangelist.clear();
        allMarketAndCurrencyList.clear();

        MarketAndCurrency marketAndCurrency=new MarketAndCurrency();
        for (int i = 0; i < marketDatas.size(); i++) {
            MarketDatasResult marketDatasResult = marketDatas.get(i);
            split(marketDatasResult.getSymbol());
            String exchangeType= getExchangeType(marketDatasResult.getSymbol());
            if (exchangeType.equals(getExchangeFlag())){
                marketAndCurrency = getMarketAndCurrency(marketDatasResult);
                showMarketAndCurrencyList.add(marketAndCurrency);
            }
            allMarketAndCurrencyList.add(marketAndCurrency);
        }
        marketInfoAdapter.setMarketList(showMarketAndCurrencyList);
        marketInfoAdapter.setExchangeFlag(getExchangeFlag());
        marketInfoAdapter.notifyDataSetChanged();
    }

    @NonNull
    private MarketAndCurrency getMarketAndCurrency(MarketDatasResult marketDatasResult) {
        MarketAndCurrency marketAndCurrency = new MarketAndCurrency();
        marketAndCurrency.setTickerData(marketDatasResult.getTicker());
        marketAndCurrency.setTline(marketDatasResult.getTline());
        marketAndCurrency.setcName(marketDatasResult.getcName());
        marketAndCurrency.setCoinName(marketDatasResult.getCoinName());
        marketAndCurrency.setExeByRate(marketDatasResult.getExeByRate());
        marketAndCurrency.setMoneyType(marketDatasResult.getMoneyType());
        marketAndCurrency.setSymbol(marketDatasResult.getSymbol());
        marketAndCurrency.setCoinFullNameEn(marketDatasResult.getCoinFullNameEn());
        marketAndCurrency.setType(marketDatasResult.getType());
        for (CurrencySetRealm currencySetRealm : currencySetResult.getCurrencySets()) {
            if (marketDatasResult.getCoinName().equals(currencySetRealm.getCurrency())) {
                marketAndCurrency.setCurrencySet(currencySetRealm);

               /* if(marketInfoAdapter.getMarketAndCurrency()!=null)
                {
                    if(marketInfoAdapter.getMarketAndCurrency().getCurrencySet().getCurrency().equals(currencySetRealm.getCurrency()))
                    {
                        marketAndCurrency.setIs_visible("1");
                    }else
                    {
                        marketAndCurrency.setIs_visible("0");
                    }
                }else
                {
                    if(i==0)
                    {
                        marketAndCurrency.setIs_visible("1");
                    }else
                    {
                        marketAndCurrency.setIs_visible("0");
                    }
                }*/


            }
        }
        return marketAndCurrency;
    }

    public void initDataMarketlist() {
        if (getUserVisibleHint()) {
            marketlist();
        }
    }


    private void marketlist() {
        Subscriber subscriber = new Subscriber<HttpResult<TickerArrayResult>>() {
            @Override
            public void onCompleted() {
                // UIHelper.ToastMessage(context,"获取成功！");
            }

            @Override
            public void onError(Throwable e) {
//                UIHelper.ToastMessage(context, "获取行情失败！");
            }

            @Override
            public void onNext(HttpResult<TickerArrayResult> tickerArrayResultHttpResult) {
                // UIHelper.ToastMessage(context,"解析数据！");
                initData(tickerArrayResultHttpResult.getDatas().getMarketDatas());
            }
        };


        HttpMethods.getInstance(1).getTickerArray(subscriber, kChartTimeInterval, kChartDataSize, SpTools.getlegalTender());

    }

    public List<MarketAndCurrency> getShowMarketAndCurrencyList() {
        return showMarketAndCurrencyList;
    }

    public List<MarketAndCurrency> getAllMarketAndCurrencyList() {
        return allMarketAndCurrencyList;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*   @Override
       public void onClick(View v) {
          // UIHelper.showKDiagramActivity(MarketFragment.this.getActivity(),currencyType,exchangeType);
       }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()) {
            initDataMarketlist();
            marketlist();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
