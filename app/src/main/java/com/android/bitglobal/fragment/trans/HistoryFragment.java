package com.android.bitglobal.fragment.trans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.activity.trans.TransActivity;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.EntrustTrade;
import com.android.bitglobal.entity.EntrustTradeResult;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryFragment extends BaseFragment implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
    private Activity context;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;
    @BindView(R.id.listview)
    ListView listView;
//    @BindView(R.id.cb_qb)
//    CheckBox cb_qb;
//    @BindView(R.id.cb_mr)
//    CheckBox cb_mr;
//    @BindView(R.id.cb_mc)
//    CheckBox cb_mc;
//    @BindView(R.id.btn_del_all)
//    Button btn_del_all;
    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    private UserInfo userInfo;
    private List<String> list=new ArrayList<String>();

    private List<EntrustTrade> list_order=new ArrayList<EntrustTrade>();
    private ProgressSubscriber progressSubscriber;
    private SubscriberOnNextListener getOrdersOnNext;
    private QuickAdapter<EntrustTrade> adapter;
    private String currencyType,exchangeType,type="-1",status="2",dayIn3="1";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private TransCurrency t_Currency;
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.trans_before_later, container, false);
        unbinder=ButterKnife.bind(this, mView);
        return mView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currencyType = SharedPreferences.getInstance().getString("trans_currencyType", "");
        exchangeType = SharedPreferences.getInstance().getString("trans_exchangeType", "");
        context = getActivity();

    }
    private boolean sfjzgl=false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()&&sfjzgl) {
            initData();
            clearData();
            if (!is_token(userInfo)) {
                list_order.clear();
                adapter.clear();
                tv_no_ts.setVisibility(View.VISIBLE);
            }
        }
        if (isVisibleToUser && isVisible()&&!sfjzgl) {
            initData();
            initView();
            clearData();
            sfjzgl=true;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    private void initView(){
//        cb_qb.setOnClickListener(this);
//        cb_mr.setOnClickListener(this);
//        cb_mc.setOnClickListener(this);
//        btn_del_all.setVisibility(View.INVISIBLE);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        getOrdersOnNext = new SubscriberOnNextListener<EntrustTradeResult>() {
            @Override
            public void onNext(EntrustTradeResult entrustTradeResult) {
                if(pageIndex==1&&dayIn3.equals("1")){
                    list_order.clear();
                    adapter.clear();
                }
                List<EntrustTrade> tradeList=entrustTradeResult.getEntrustTrades();
                if (tradeList!=null&&tradeList.size()>0){
                    if(list_order.size()>0&&list_order.get(0).getEntrustId().equals(tradeList.get(0).getEntrustId())){

                    }else{
                        pageIndex++;
                        list_order.addAll(tradeList);
                        if ((tradeList==null&&dayIn3.equals("1")) ||(pageSize >tradeList.size()&&dayIn3.equals("1"))) {

                        }else{
                            adapter.replaceAll(list_order);
                        }
                    }
                    adapter.replaceAll(list_order);
                }else{
                    if(dayIn3.equals("0")){
                        adapter.replaceAll(list_order);
                    }
                }
                if(list_order.size()>0||dayIn3.equals("1")){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
                if ((tradeList==null&&dayIn3.equals("1")) ||(pageSize >tradeList.size()&&dayIn3.equals("1"))) {
                    dayIn3 = "0";
                    pageIndex = 1;
                    getOrders();
                }
                if (tradeList!=null && tradeList.size() < 1&&list_order.size()!=0) {
                    sfjz++;
                }
            }
        };

        adapter = new QuickAdapter<EntrustTrade>(getActivity(), R.layout.history_list_item, list_order) {
            @Override
            protected void convert(BaseAdapterHelper helper, final EntrustTrade item) {
                //Type 0：卖出 1：买入
                final String item_type=item.getType();
                //状态 0：正在进行 1：已取消 2：已成交 3：待成交 -1：计划中
                try {
                    if(item_type.equals("1")){
                        helper.setText(R.id.historyTypeText, getString(R.string.trans_mad));
                        helper.setBackgroundRes(R.id.historyTypeText, R.drawable.bnt_trans_buy_solid);
//                        helper.setText(R.id.entrustTotalText, "- " +  format(item.getEntrust_total(),
//                                Utils.getPrecisionExchange(currencyType, exchangeType)));
                        helper.setText(R.id.entrustTotalText, "- " +  format(item.getCompleteTotalMoney(),
                                Utils.getPrecisionExchange(currencyType, exchangeType)));
//                        helper.setText(R.id.entrustAmountText, "+ " + format(item.getNumber(),
//                                Utils.getPrecisionAmount(currencyType)) + " " + t_Currency.getCurrencyType().toUpperCase());
                        helper.setText(R.id.entrustAmountText, "+ " + format(item.getCompleteNumber(),
                                Utils.getPrecisionAmount(currencyType, exchangeType)) + " " + currencyType.toUpperCase());
                        helper.setTextColor(R.id.entrustAmountText, ContextCompat.getColor(context,
                                R.color.analysis_green));
                    }else{
                        helper.setText(R.id.historyTypeText, getString(R.string.trans_mid));
                        helper.setBackgroundRes(R.id.historyTypeText, R.drawable.bnt_trans_sell_solid);
//                        helper.setText(R.id.entrustTotalText, "+ " +  format(item.getEntrust_total(),
//                                 Utils.getPrecisionExchange(currencyType, exchangeType)));
                        helper.setText(R.id.entrustTotalText, "+ " +  format(item.getCompleteTotalMoney(),
                                Utils.getPrecisionExchange(currencyType, exchangeType)));
//                        helper.setText(R.id.entrustAmountText, "- " + format(item.getNumber(),
//                                Utils.getPrecisionAmount(currencyType)) + " " + t_Currency.getCurrencyType().toUpperCase());
                        helper.setText(R.id.entrustAmountText, "- " + format(item.getCompleteNumber(),
                                Utils.getPrecisionAmount(currencyType, exchangeType)) + " " + currencyType.toUpperCase());
                        helper.setTextColor(R.id.entrustAmountText, ContextCompat.getColor(context,
                                R.color.analysis_red));
                    }
                    helper.setText(R.id.timeText, StringUtils.dateFormat1(item.getSubmitTime()));
                    helper.setText(R.id.avgPriceText, " " + format(item.getJunjia(),
                            Utils.getPrecisionExchange(currencyType, exchangeType)));
                    helper.setText(R.id.orderPriceText, " " + format(item.getUnitPrice(),
                            Utils.getPrecisionExchange(currencyType, exchangeType)));
                    helper.setText(R.id.btcText, " " + exchangeType.toUpperCase());
                    helper.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            UIHelper.showExchangeOrderDetail(getActivity(), item,
//                                    t_Currency.getCurrencyType(), t_Currency.getExchangeType(), null);
                            UIHelper.showExchangeOrderDetail(getActivity(), item,
                                    currencyType, exchangeType, null);
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

        };
        listView.setAdapter(adapter);
    }
    private void initData() {
//        currencyType= ((TransActivity)getActivity()).getCurrencyType();
//        exchangeType= ((TransActivity)getActivity()).getExchangeType();
//        t_Currency= ((TransActivity)getActivity()).getT_Currency();
        userInfo = UserDao.getInstance().getIfon();
    }

//    public void setCurrencyType(String currencyType) {
//        this.currencyType = currencyType;
//    }
//
//    public void setExchangeType(String exchangeType) {
//        this.exchangeType = exchangeType;
//    }

    public void setT_Currency(TransCurrency t_Currency) {
        this.t_Currency = t_Currency;
    }

    public void  clearData(){
        pageIndex=1;
        dayIn3="1";
        sfjz=0;
        getOrders();
    }
    public void getOrders(){
        initData();
        if(is_token(userInfo)){
            list.clear();
            list.add(type);
            list.add(currencyType);
            list.add(exchangeType);
            list.add(dayIn3);
            list.add(status);
            list.add(pageIndex+"");
            list.add(pageSize+"");
            progressSubscriber=new ProgressSubscriber(getOrdersOnNext, context);
            progressSubscriber.setIs_progress_show(false);
            progressSubscriber.setIs_showMessage(false);
            HttpMethods.getInstance(1).entrustRecord(progressSubscriber,list);
        }else{
            list_order.clear();
            if(adapter!=null){
                adapter.clear();
            }
            if (tv_no_ts != null) {
                tv_no_ts.setVisibility(View.VISIBLE);
            }
            //UIHelper.showLogin(context);
        }
    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if(swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                getOrders();
            }else{
                UIHelper.ToastMessage(context,R.string.trans_wgdsjl_toast);
            }
        }
        try{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (context== null) {
                        return;
                    }else{
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mSwipyRefreshLayout != null) {
                                    mSwipyRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    }

                }
            }, 2000);
        }catch (Exception e){

        }

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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.cb_mc:
//                cb_mc.setChecked(true);
//                if(cb_mc.isChecked()&&type!="0"){
//                    cb_mr.setChecked(false);
//                    cb_qb.setChecked(false);
//                    type="0";
//                    clearData();
//                }
//                break;
//            case R.id.cb_mr:
//                cb_mr.setChecked(true);
//                if(cb_mr.isChecked()&&type!="1"){
//                    cb_mc.setChecked(false);
//                    cb_qb.setChecked(false);
//                    type="1";
//                    clearData();
//                }
//                break;
//            case R.id.cb_qb:
//                cb_qb.setChecked(true);
//                if(cb_qb.isChecked()&&type!="-1"){
//                    cb_mc.setChecked(false);
//                    cb_mr.setChecked(false);
//                    type="-1";
//                    clearData();
//                }
//                break;
        }
    }
}
