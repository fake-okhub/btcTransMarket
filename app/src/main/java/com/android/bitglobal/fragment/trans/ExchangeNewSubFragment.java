package com.android.bitglobal.fragment.trans;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.TradeOverviewResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.manager.ExchangeNewSubManager;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.PriceUtils;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.GlideCircleTransform;
import com.android.bitglobal.ui.RecyclerSpace;
import com.android.bitglobal.ui.UIHelper;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;

import static com.android.bitglobal.common.SystemConfig.exchangeBtnTypePressed;


/**
 * Created by joyson on 2017/9/20.
 */

public class ExchangeNewSubFragment extends BaseFragment {

    @BindView(R.id.tv_new_exchange_price)
    TextView tvNewExchangePrice;
    @BindView(R.id.tv_new_exchange_avail_price)
    TextView tvNewExchangeAvailPrice;
    @BindView(R.id.tv_new_exchange_lock_price)
    TextView tvNewExchangeLockPrice;
    @BindView(R.id.tv_new_exchange_rate)
    TextView tvNewExchangeRate;
    @BindView(R.id.btn_legal_tender)
    Button btnLegalTender;
    @BindView(R.id.rcv_new_exchange_icon_list)
    RecyclerView rcvNewExchangeIconList;
    CoinAdapter adapter;
    private Unbinder unbinder;
    List<TradeOverviewResult.CoinBean.SymbolListBeanX> symbolList=new ArrayList<>();
    private String currentType= SystemConfig.USDC;
    Map<String,String> coinUrlMap=new HashMap<>();

    private Context mContext;
    private TradeOverviewResult.CoinBean coinBean;
    public boolean isSubPageToOnResume;
    private ExchangeNewSubManager manager;
    private String currentRatePrice="0";
//    private TextView tvNewExchangeRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_new_exchange_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initRecyclerView();
        initLocalData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isSubPageToOnResume=true;
        initNetData();
        backToThisPageRefreshData();
    }

    /**
     * 依附于viewpager的fragment之间显示、隐藏调用的方法
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            backToThisPageRefreshData();
        }
    }

    private void backToThisPageRefreshData(){
        checkLegalTenderType();
        judgeBtnIsVisible();
        if (this.coinBean!=null){
            formatCoin(this.coinBean);
        }
    }

    private void initView() {
        mContext=getActivity();
        SystemConfig.exchangeBtnType=SystemConfig.exchangeBtnTypeNormal;

        manager=ExchangeNewSubManager.getInstance();
        btnLegalTender.setBackgroundResource(R.drawable.btn_legal_coin_symbol_bg_normal);
        btnLegalTender.setText(manager.getCoinSymbol());
        btnLegalTender.setTextColor(mContext.getResources().getColor(R.color.background_color2));
        judgeBtnIsVisible();
    }

    private void initRecyclerView() {
        rcvNewExchangeIconList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        RecyclerSpace recyclerSpace=new RecyclerSpace(DeviceUtil.dp2px(getActivity(),0.5f),getResources().getColor(R.color.vertical_line_color));
        rcvNewExchangeIconList.addItemDecoration(recyclerSpace);
        adapter= new CoinAdapter();
//        adapter.setNewData(testAddData());
        rcvNewExchangeIconList.setAdapter(adapter);
//        setFooterView(rcvNewExchangeIconList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //空数据item无法点击
                if (symbolList.size()%3==2 && position==symbolList.size()-1){
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("currencyType",
                        ((TradeOverviewResult.CoinBean.SymbolListBeanX)adapter.getItem(position)).getPropTag().toUpperCase());
                if("USD".equals(currentType)) {
                    bundle.putString("exchangeType", "USDC");
                } else {
                    bundle.putString("exchangeType", currentType);
                }
                UIHelper.showTrans(getActivity(), bundle);
            }
        });
    }


    private void setFooterView(RecyclerView view){
        View recycFootView = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_new_exchange_footview, view, false);
        tvNewExchangeRate = (TextView) recycFootView.findViewById(R.id.tv_new_exchange_rate);
        tvNewExchangeRate.setVisibility(View.INVISIBLE);
        adapter.setFooterView(recycFootView);
    }

    /**
     * 从数据库中获取货币 coinUrl
     */
    private void initLocalData() {
        CurrencySetResult currencySetResult = CurrencySetDao.getInstance().getIfon();
        if (!currencySetResult.getCurrencySets().isEmpty()){
            for (CurrencySetRealm currency:currencySetResult.getCurrencySets()){
                coinUrlMap.put(currency.getCurrency(),currency.getCoinUrl());
            }
        }
    }

    public void initNetData() {
        //解析网络前，现将缓存信息加载进来。
        initResultData(SpTools.getTradeOverview());
        Subscriber subscriber = new Subscriber<HttpResult<TradeOverviewResult>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult<TradeOverviewResult> tradeOverviewResult) {
                TradeOverviewResult datas = tradeOverviewResult.getDatas();
                initResultData(datas);
                SpTools.setTradeOverView(datas);
            }
        };
        HttpMethods.getInstance(2).getTradeOverview(subscriber,UserDao.getInstance().getUserId(),UserDao.getInstance().getToken());
    }

    private void initResultData(TradeOverviewResult result) {
        if (result!=null){
            switch (getCurrentType()){
                case SystemConfig.USDC:
                    TradeOverviewResult.CoinBean usdc = result.getUSDC();
                    formatCoin(usdc);
                    break;
                case SystemConfig.BTC:
                    TradeOverviewResult.CoinBean btc = result.getBTC();
                    formatCoin(btc);
                    break;
            }
        }
    }

    /**
     * 根据服务器返回，格式化界面显示
     * @param usdc
     */
    private void formatCoin(TradeOverviewResult.CoinBean usdc) {
        currentRatePrice=format(manager.getCurrentExchangeRate(usdc),manager.EXCHANGE_RATE_PRECISION,false);
        String curentRate=getJoinRateString(currentRatePrice);
        baseBtnStateToChangeCoinSymbol(usdc);
        String avail=getString(R.string.trans_exchange_avaliable)+getAvailableTag()+ manager.getCoinHoldNum(getCurrentType(),usdc.getUsableFund(),true,true);
        tvNewExchangeAvailPrice.setText(avail);
        tvNewExchangeLockPrice.setText(manager.getCoinHoldNum(getCurrentType(),usdc.getFreezeFund(),true,true));
        symbolList = usdc.getSymbolList();
        //汇率保持4位小数，不补零
        tvNewExchangeRate.setText(curentRate);
        symbolList=manager.addEmptyBeans(symbolList);
        adapter.setNewData(symbolList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据法币按钮状态 界面 改变法币标识
     */
    private void baseBtnStateToChangeCoinSymbol(TradeOverviewResult.CoinBean usdc){
        if (usdc==null){
            return;
        }
        this.coinBean=usdc;
        String convertFund=usdc.getConvertFund();
        switch (SystemConfig.exchangeBtnType){
            case SystemConfig.exchangeBtnTypeNormal://默认情况
                String currentPriceBefore = format(convertFund,manager.getCoinPricePrecision(getCurrentType()),true);
                //对位数进行进位  添加  ,
                String currentPrice = PriceUtils.carryNumber(currentPriceBefore,manager.getCoinPricePrecision(getCurrentType()));
                tvNewExchangePrice.setText(getDefaultPageSymbol()+currentPrice);
                break;
            case exchangeBtnTypePressed://按压情况
                String ratePrice=PriceUtils.mul(convertFund,currentRatePrice);
                String pressPrice= format(ratePrice,manager.getCoinPricePrecision(getCurrentType()),true);
                String finalPrice=PriceUtils.carryNumber(pressPrice,manager.getCoinPricePrecision(SpTools.getlegalTender()));
                tvNewExchangePrice.setText(manager.getCoinSymbol()+" "+finalPrice);
                break;
        }

        adapter.notifyDataSetChanged();
    }


    /**
     * 根据法币按钮状态 界面 改变recyclerview item
     */
    private String[] baseBtnStateToChangeRecyclerviewItem(TradeOverviewResult.CoinBean.SymbolListBeanX bean){
        String[] strings=new String [4];
        //当前价格
        String lastPrice= bean.getLastPrice();
        //持有量
        String holdCount = bean.getHoldCount();
        //合计资金
        String totalFund=bean.getTotalFund();
        //返回该界面时，若上个界面已经 对 法币按钮进行press ，且当前界面为usdc ，且缓存法币为usd
        // 防止recyclerview item页面刷新
        boolean isRefreshRecycItem=SystemConfig.exchangeBtnTypePressed.equals(SystemConfig.exchangeBtnType)
                && SystemConfig.USDC.equals(getCurrentType())
                && SystemConfig.USD.equals(SpTools.getlegalTender());
        if (isRefreshRecycItem){
            return refreshNormalItem(bean, strings);
        }else {
            switch (SystemConfig.exchangeBtnType){
                case SystemConfig.exchangeBtnTypeNormal://默认情况
                    return refreshNormalItem(bean, strings);

                case SystemConfig.exchangeBtnTypePressed://按压情况
                    if (lastPrice.contains(manager.emptypecialsTag)){
                        strings[0]="";
                        strings[1]="";
                        strings[2]="";
                        strings[3]="";
                    }else {
                        String afterRateLastPrice=PriceUtils.mul(lastPrice,currentRatePrice);
                        //法币，保留两位不补零,将当前缓存法币传递进去
                        int precision= Utils.getPrecisionExchange(SpTools.getlegalTender(),getCurrentType());
                        String afterDecimalsDigit=format(afterRateLastPrice,precision,true);
                        strings[0]=manager.getCoinSymbol()+" ";
                        strings[1]=afterDecimalsDigit;
                        String holdSum=manager.getCoinHoldNum(bean.getPropTag(),holdCount,false,false);
                        strings[2]=holdSum;
                        //总金额为：当前持有量 * 价格；为了精确度用界面展示的数值相乘，而没用服务器返回字段
                        String totalMoney= PriceUtils.mul(afterDecimalsDigit,holdSum);
                        strings[3]=manager.getCoinSymbol()+" "+format(totalMoney,precision,true);
                    }

                    return strings;
                default:
                    return strings;
            }
        }

    }

    /**
     * 刷新 法币btn 为 Normal情况下的列表
     * @param bean
     * @param strings
     * @return
     */
    private String[] refreshNormalItem(TradeOverviewResult.CoinBean.SymbolListBeanX bean, String[] strings) {
        //说明是空数据
        if (bean.getLastPrice().contains(manager.emptypecialsTag)){
            strings[0]="";
            strings[1]="";
            strings[2]="";
            strings[3]="";
        }else {
            //说明是正常数据
            int precision= Utils.getPrecisionExchange(bean.getPropTag(),getCurrentType());
            String firstPrice=format(bean.getLastPrice(),precision,true);
            strings[0]=getDefaultPageSymbol();
            strings[1]=firstPrice;
            strings[2]=manager.getCoinHoldNum(bean.getPropTag(),bean.getHoldCount(),false,false);
            strings[3]=getDefaultPageSymbol()+format(PriceUtils.mul(firstPrice,strings[2]),precision,true);
        }
        return strings;
    }


    /**
     * 拼接：1 USD = 6.453 CNY
     */
    private String getJoinRateString(String currentRate){
        String currentUsdOrBtc="USDC";
        switch (getCurrentType()){
            case SystemConfig.USDC:
                currentUsdOrBtc = "USDC";
                break;
            case SystemConfig.BTC:
                currentUsdOrBtc = "BTC";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("1 ");
        sb.append(currentUsdOrBtc);
        sb.append(" = ");
        sb.append(currentRate);
        sb.append(" ");
        sb.append(SpTools.getlegalTender());
        return sb.toString();
    }

    private String getAvailableTag(){
        switch (getCurrentType()){
            case SystemConfig.USDC:
                return " USDC: ";
            case SystemConfig.BTC:
                return " BTC: ";
            default:
                return " USDC: ";
        }
    }

    /**
     * 法币为usd情况下，不显示法币按钮,不显示汇率text
     */
    private void judgeBtnIsVisible(){
        if (SystemConfig.USDC.equals(getCurrentType())){
            if (SystemConfig.USD.equals(SpTools.getlegalTender())){
                btnLegalTender.setVisibility(View.INVISIBLE);
                textRateVisible();
                btnLegalTender.setEnabled(false);
            }else {
                btnLegalTender.setVisibility(View.VISIBLE);
                textRateVisible();
                btnLegalTender.setEnabled(true);
            }
        }else {
            btnLegalTender.setVisibility(View.VISIBLE);
            textRateVisible();
            btnLegalTender.setEnabled(true);
        }
    }

    private String getDefaultPageSymbol(){
        switch (getCurrentType()){
            case SystemConfig.USDC:
                return "$ ";
            case SystemConfig.BTC:
                return "฿ ";
            default:
                return "$ ";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_legal_tender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_legal_tender:
                clickLegalTender();
                break;
        }
    }

    private void clickLegalTender() {
        switch (SystemConfig.exchangeBtnType){
            case SystemConfig.exchangeBtnTypeNormal:
                SystemConfig.exchangeBtnType= exchangeBtnTypePressed;
                clickLegalTenderToPressed();
                break;
            case exchangeBtnTypePressed:
                SystemConfig.exchangeBtnType=SystemConfig.exchangeBtnTypeNormal;
                clickLegalTenderToNormal();
                break;
        }
    }

    private void checkLegalTenderType(){
        switch (SystemConfig.exchangeBtnType){
            case SystemConfig.exchangeBtnTypeNormal:
                clickLegalTenderToNormal();
                break;
            case exchangeBtnTypePressed:
                clickLegalTenderToPressed();
                break;
            default:
                clickLegalTenderToNormal();
                break;
        }
    }

    private void clickLegalTenderToNormal(){
        baseBtnStateToChangeCoinSymbol(this.coinBean);
        tvNewExchangeRate.setVisibility(View.INVISIBLE);
        btnLegalTender.setBackgroundResource(R.drawable.btn_legal_coin_symbol_bg_normal);
        btnLegalTender.setText(manager.getCoinSymbol());
        btnLegalTender.setTextColor(mContext.getResources().getColor(R.color.background_color2));
    }

    private void clickLegalTenderToPressed(){
        baseBtnStateToChangeCoinSymbol(this.coinBean);
        tvNewExchangeRate.setVisibility(View.VISIBLE);
        btnLegalTender.setBackgroundResource(R.drawable.btn_legal_coin_symbol_bg_press);
        btnLegalTender.setText(manager.getCoinSymbol());
        btnLegalTender.setTextColor(mContext.getResources().getColor(R.color.white));
    }

    /**
     * 根据btnLegalTender按压状态，是否显示 汇率 文本
     */
    private void textRateVisible(){
        if (SystemConfig.USDC.equals(getCurrentType()) && SystemConfig.USD.equals(SpTools.getlegalTender())){
            tvNewExchangeRate.setVisibility(View.INVISIBLE);
        }else {
            switch (SystemConfig.exchangeBtnType){
                case SystemConfig.exchangeBtnTypeNormal:
                    tvNewExchangeRate.setVisibility(View.INVISIBLE);
                    break;
                case exchangeBtnTypePressed:
                    tvNewExchangeRate.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    class CoinAdapter extends BaseQuickAdapter<TradeOverviewResult.CoinBean.SymbolListBeanX, BaseViewHolder> {
        public CoinAdapter() {
            super(R.layout.item_recycler_new_exchange_item,symbolList);
        }

        @Override
        protected void convert(final BaseViewHolder viewHolder, TradeOverviewResult.CoinBean.SymbolListBeanX bean) {
            String coinUrl = coinUrlMap.get(bean.getPropTag());
//            coinUrl=(TextUtils.isEmpty(coinUrl))?"https://s.bitglobal.com/statics/img/common/eth.png":coinUrl;
            //同时传userid和token说明服务端验证通过
            if (manager.isNotLogin()){
                viewHolder.setText(R.id.tv_icon_title, bean.getPropTag())
                        .setText(R.id.tv_recyc_item_symbol, baseBtnStateToChangeRecyclerviewItem(bean)[0])
                        .setText(R.id.tv_icon_current_price, baseBtnStateToChangeRecyclerviewItem(bean)[1])
                        .setVisible(R.id.view_icon_current_price_interval,true)
                        //未登录不显示下面条目
                        .setVisible(R.id.tv_icon_num,false)
                        .setVisible(R.id.tv_icon_total_price,false);
            }else {
                viewHolder.setText(R.id.tv_icon_title, bean.getPropTag())
                        .setText(R.id.tv_recyc_item_symbol, baseBtnStateToChangeRecyclerviewItem(bean)[0])
                        .setText(R.id.tv_icon_current_price, baseBtnStateToChangeRecyclerviewItem(bean)[1])
                        .setVisible(R.id.view_icon_current_price_interval,false)
                        .setVisible(R.id.tv_icon_num,true)
                        .setVisible(R.id.tv_icon_total_price,true)
                        .setText(R.id.tv_icon_num, baseBtnStateToChangeRecyclerviewItem(bean)[2])
                        .setText(R.id.tv_icon_total_price, baseBtnStateToChangeRecyclerviewItem(bean)[3]);
            }

            Glide.with(mContext).load(coinUrl).crossFade().transform(new GlideCircleTransform(mContext))
                    .into((ImageView) viewHolder.getView(R.id.iv_new_exchange_coin_brand));
        }

    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }
}
