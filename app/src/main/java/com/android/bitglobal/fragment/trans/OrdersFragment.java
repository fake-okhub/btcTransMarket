package com.android.bitglobal.fragment.trans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.trans.ExchangeOrderDetailActivity;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.EntrustTrade;
import com.android.bitglobal.entity.EntrustTradeResult;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.DialogUtils;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.UserConfirm;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrdersFragment extends BaseFragment implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {
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
    private List<String> list = new ArrayList<>();

    private List<EntrustTrade> list_order = new ArrayList<>();
    private EntrustTrade order_record = new EntrustTrade();
    private ProgressSubscriber progressSubscriber;
    private SubscriberOnNextListener getOrdersOnNext, cancelOrderOnNext, cancelAllOrderOnNext;
    private QuickAdapter<EntrustTrade> adapter;
    //type 0 卖出 1 买入 -1 不限制
    private String currencyType, exchangeType, type = "-1", status = "3", orderId, dayIn3 = "1";
    int pageIndex = 1, pageSize = 20, sfjz = 0;
    private UserConfirm userConfirm;
    private TransCurrency t_Currency;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.trans_before_later, container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currencyType = SharedPreferences.getInstance().getString("trans_currencyType", "");
        exchangeType = SharedPreferences.getInstance().getString("trans_exchangeType", "");
        context = getActivity();
    }

    private boolean sfjzgl = false;

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

    private void initView() {
//        cb_qb.setOnClickListener(this);
//        cb_mr.setOnClickListener(this);
//        cb_mc.setOnClickListener(this);
//        btn_del_all.setOnClickListener(this);
        userConfirm = new UserConfirm(context);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.tv_user_title2.setText("");
        userConfirm.tv_user_title1.setText(R.string.trans_cxqbwt_hint);
        mSwipyRefreshLayout.setOnRefreshListener(this);
//        btn_del_all.setVisibility(View.VISIBLE);
        getOrdersOnNext = new SubscriberOnNextListener<EntrustTradeResult>() {
            @Override
            public void onNext(EntrustTradeResult entrustTradeResult) {
                if (pageIndex == 1) {
                    list_order.clear();
                    adapter.clear();
                }
                List<EntrustTrade> tradeList = entrustTradeResult.getEntrustTrades();
//                EntrustTrade entrustTrade = new EntrustTrade();
//                entrustTrade.setComplete_rate(0.5);
//                entrustTrade.setCompleteNumber("0");
//                entrustTrade.setCompleteTotalMoney("0");
//                entrustTrade.setEntrust_total(28.7);
//                entrustTrade.setEntrustId("12715");
//                entrustTrade.setJunjia("0E-8");
//                entrustTrade.setNumber("1");
//                entrustTrade.setStatus("3");
//                entrustTrade.setSubmitTime("1498788638569");
//                entrustTrade.setType("0");
//                entrustTrade.setUnitPrice("0.005");
//                tradeList.add(entrustTrade);
                if (tradeList != null && tradeList.size() > 0) {
                    if (list_order.size() > 0 && list_order.get(0).getEntrustId().equals(tradeList.get(0).getEntrustId())) {

                    } else {
                        pageIndex++;
                        list_order.addAll(tradeList);
                        adapter.replaceAll(list_order);
                    }
                }
                if (tradeList != null && tradeList.size() < 1 && list_order.size() != 0) {
                    sfjz++;
                }
                if (list_order.size() > 0) {
                    tv_no_ts.setVisibility(View.GONE);
                } else {
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
            }
        };
        cancelOrderOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                list_order.remove(order_record);
                adapter.remove(order_record);
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(context, R.string.trans_cxcg);
            }
        };
        cancelAllOrderOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                list_order.clear();
                adapter.clear();
                tv_no_ts.setVisibility(View.VISIBLE);
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(context, R.string.trans_cxcg);
            }
        };
        adapter = new QuickAdapter<EntrustTrade>(getActivity(), R.layout.orders_list_item, list_order) {
            @Override
            protected void convert(BaseAdapterHelper helper, final EntrustTrade item) {
                //Type 0：卖出 1：买入
                final String item_type = item.getType();
                //状态 0：正在进行 1：已取消 2：已成交 3：待成交 -1：计划中
                final String item_status = item.getStatus();
                ProgressBar orderProgressBar = (ProgressBar) helper.getView().findViewById(R.id.ordersProgressBar);
//                helper.setText(R.id.tv_wtsl, t_Currency.getCurrencyType_symbol() + "" + deFormat(item.getNumber()));
                if(Double.parseDouble(item.getCompleteNumber()) > 0) {
                    helper.setVisible(R.id.waitText, false);
                } else {
                    helper.setVisible(R.id.waitText, true);
                }
                try {
//                    helper.setText(R.id.tv_tv_wtjg_title,
//                            getResources().getString(R.string.trans_wtjg) + "("
//                                    + t_Currency.getExchangeType().toUpperCase() + ")");
                    helper.setText(R.id.tv_tv_wtjg_title,
                            getResources().getString(R.string.trans_wtjg).replaceAll("###", exchangeType));
//                    helper.setText(R.id.tv_wtjg, t_Currency.getExchangeType_symbol()
//                            + deFormat(item.getUnitPrice(),
                    //成交价格
                    String entrustPrice= format(item.getUnitPrice(),
                            Utils.getPrecisionExchange(currencyType, exchangeType));
                    helper.setText(R.id.tv_wtjg,entrustPrice);
                    helper.setText(R.id.tv_cjjg_title,
                            getResources().getString(R.string.trans_cjjg).replaceAll("###", currencyType));
                    //成交量
                    String amount=format(item.getNumber(),Utils.getPrecisionAmount(currencyType, exchangeType));
                    helper.setText(R.id.tv_cjjg, amount);
//
//                    helper.setText(R.id.tv_finish_count_title,
//                            getResources().getString(R.string.trans_finished_count) + "("
//                                    + t_Currency.getCurrencyType().toUpperCase() + ")");
                    helper.setText(R.id.tv_finish_count_title,
                            getResources().getString(R.string.trans_finished_count).replaceAll("###", exchangeType));
//                    helper.setText(R.id.tv_finish_count, deFormat(item.getCompleteNumber(),
//                            Utils.getPrecisionAmount()));
                    //不以服务器返回结果作为依据，而是app端的 价格*成交量 作为依据
//                    helper.setText(R.id.tv_finish_count, deFormat(item.getEntrust_total(),
//                            Utils.getPrecisionAmount()));
                    helper.setText(R.id.tv_finish_count, format(item.getEntrust_total(),
                            Utils.getPrecisionExchange(currencyType, exchangeType)));
                    helper.setProgress(R.id.ordersProgressBar, (int) (item.getComplete_rate() * 100));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
//                helper.setBackgroundRes(R.id.ll_bfb, R.color.vertical_line_color);
//                LinearLayout ll_bfb = (LinearLayout) helper.getView().findViewById(R.id.ll_bfb);
//                final double completeNumber_d = Double.parseDouble(item.getCompleteNumber());
//                final double number_d = Double.parseDouble(item.getNumber());
                View view = helper.getView();
//                View view_bfb = view.findViewById(R.id.view_bfb);
                if (item_type.equals("1")) {
                    helper.setText(R.id.tv_mrmc, getString(R.string.trans_mad));
//                    helper.setBackgroundRes(R.id.view_bfb, R.color.trans_exchange_buy);
//                    helper.setTextColorRes(R.id.tv_wtsl, R.color.text_color_red);
//                    helper.setTextColorRes(R.id.tv_wtjg, R.color.trans_exchange_buy);
                    helper.setBackgroundRes(R.id.tv_mrmc, R.drawable.bnt_trans_buy_solid);
                    orderProgressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.orders_progressbar_green_horizontal));
//                    helper.setTextColorRes(R.id.tv_cje, R.color.text_color_red);
//                    helper.setImageResource(R.id.img_type, R.mipmap.ico_type_buy);
                } else {
                    helper.setText(R.id.tv_mrmc, getString(R.string.trans_mid));
//                    helper.setBackgroundRes(R.id.view_bfb, R.color.trans_exchange_sell);
//                    helper.setTextColorRes(R.id.tv_wtsl, R.color.text_color_green);
//                    helper.setTextColorRes(R.id.tv_wtjg, R.color.trans_exchange_sell);
                    helper.setBackgroundRes(R.id.tv_mrmc, R.drawable.bnt_trans_sell_solid);
                    orderProgressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.orders_progressbar_red_horizontal));
//                    helper.setTextColorRes(R.id.tv_cje, R.color.text_color_green);
//                    helper.setImageResource(R.id.img_type, R.mipmap.ico_type_sell);
                }
                if (item_status.equals("2")) {
//                    ll_bfb.setVisibility(View.INVISIBLE);
//                    helper.setVisible(R.id.ll_time, true);
                    helper.setVisible(R.id.img_del, false);
                } else {
//                    ll_bfb.setVisibility(View.VISIBLE);
                    helper.setVisible(R.id.img_del, true);
//                    helper.setVisible(R.id.ll_time, false);

                    helper.setOnClickListener(R.id.img_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtils.showOrderCancelDialog(getActivity(), new DialogUtils.OnDialogCallback() {
                                @Override
                                public void onCancel() {
                                    order_record = item;
                                    orderId = item.getEntrustId();
                                    cancelOrder();

                                }
                            });

                        }
                    });
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("currencyType", currencyType);
//                        bundle.putString("exchangeType", exchangeType);
//                        bundle.putString("orderId", item.getEntrustId());
//                        bundle.putString("number", item.getNumber());
//                        bundle.putString("unitPrice", item.getUnitPrice());
//                        bundle.putString("completeNumber", item.getCompleteNumber());
                        UIHelper.showExchangeOrderDetail(getActivity(), item,
                                currencyType, exchangeType,
                                new ExchangeOrderDetailActivity.OnExchangeOrderDetailCallback() {
                                    @Override
                                    public void onCancel() {
                                        clearData();
                                    }
                                });
                    }
                });

//                if (completeNumber_d >= number_d) {
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    view_bfb.setLayoutParams(layoutParams);
//                } else if (completeNumber_d <= 0) {
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
//                    view_bfb.setLayoutParams(layoutParams);
//                } else {
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
//                    WindowManager mw = (WindowManager) context
//                            .getSystemService(Context.WINDOW_SERVICE);
//                    Display d = mw.getDefaultDisplay();
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    int width = (int) ((d.getWidth()) * (completeNumber_d / (number_d * 1.00)));
//                    if (width < 10) {
//                        width = 10;
//                    }
//                    layoutParams.width = width;
//                    view_bfb.setLayoutParams(layoutParams);
//                }

            }

        };
        listView.setAdapter(adapter);
    }

    private void initData() {
//        currencyType = TransFragment.fragment.getCurrencyType();
//        exchangeType = TransFragment.fragment.getExchangeType();
//        t_Currency = TransFragment.fragment.getT_Currency();
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

    public void clearData() {
        pageIndex = 1;
        dayIn3 = "1";
        sfjz = 0;
        getOrders();
    }

    private void cancelAllOrders() {
        if (is_token(userInfo)) {
            list.clear();
            list.add(type);
            list.add(currencyType);
            list.add(exchangeType);
            ProgressSubscriber progressSubscriber=new ProgressSubscriber(cancelAllOrderOnNext, context);
            progressSubscriber.setIs_progress_show(false);
            progressSubscriber.setIs_showMessage(false);
            HttpMethods.getInstance(1).cancelAllOrders(progressSubscriber, list);
        } else {
            UIHelper.showLogin(context);
            //  UIHelper.showLoginOrRegister(context);
        }
    }

    private void cancelOrder() {
        list.clear();
        list.add(currencyType);
        list.add(exchangeType);
        list.add(orderId);
        HttpMethods.getInstance(1).cancelOrder(new ProgressSubscriber(cancelOrderOnNext, context), list);
    }

    public void getOrders() {
        initData();
        if (is_token(userInfo)) {
            list.clear();
            list.add(type);
            list.add(currencyType);
            list.add(exchangeType);
            list.add(dayIn3);
            list.add(status);
            list.add(pageIndex + "");
            list.add(pageSize + "");
            progressSubscriber = new ProgressSubscriber(getOrdersOnNext, context);
            progressSubscriber.setIs_progress_show(false);
            progressSubscriber.setIs_showMessage(false);
            HttpMethods.getInstance(1).entrustRecord(progressSubscriber, list);
        } else {
            list_order.clear();
            if (adapter != null) {
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
        if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
            clearData();
        } else {
            if (sfjz == 0) {
                getOrders();
            } else {
                UIHelper.ToastMessage(context, R.string.trans_wgdsjl_toast);
            }
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (context == null) {
                        return;
                    } else {
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
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onStart() {
        super.onStart();
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
//            case R.id.btn_del_all:
//                if (list_order.size()<1){
//                    UIHelper.ToastMessage(context,R.string.trans_wwtsj);
//                }else{
//                    userConfirm.show();
//                }
//
//                break;
            case R.id.bnt_user_commit:
                userConfirm.dismiss();
                cancelAllOrders();
                break;
        }
    }
}
