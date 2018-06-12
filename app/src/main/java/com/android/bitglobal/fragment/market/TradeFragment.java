package com.android.bitglobal.fragment.market;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketChartData;
import com.android.bitglobal.entity.MarketChartDataResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.Utils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * 某币种详情交易信息
 * Created by Elbert on 17/3/16.
 */
public class TradeFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;
    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.show_price)
    TextView show_price;
    @BindView(R.id.show_amount)
    TextView show_amount;


    private Unbinder unbinder;
    private Activity context;
    private String currencyType = "ETH";
    private String exchangeType = "BTC";
    private Timer timerTrade = null;  //刷新定时器
    private QuickAdapter<MarketChartData> adapter;

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_trade_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = this.getActivity();
        mSwipyRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        stopTimer();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()) {
            getData();
            show_amount.setText(getString(R.string.market_detail_trade_amount)
                    + "(" + currencyType.toUpperCase() + ")");
            show_price.setText(getString(R.string.market_detail_trade_price)
                    + "(" + exchangeType.toUpperCase() + ")");
            super.setUserVisibleHint(true);
        }
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {

        private Handler mHandler;

        TransTimerTask(Handler handler) {
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
    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            startTimer();
        } else {
            stopTimer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            startTimer();
        }
    }

    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                getData();
            }
            super.handleMessage(msg);
        }

        ;
    };

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timerTrade == null) {
            timerTrade = new Timer();
            TransTimerTask task = new TransTimerTask(handlerOfTrans);
            int dataRefreshTime = 5 * 1000;
            timerTrade.schedule(task, 0, dataRefreshTime);
        }
    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timerTrade != null) {
            timerTrade.cancel();
            timerTrade = null;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        getData();
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (context != null) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mSwipyRefreshLayout != null) {
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

    private void getData() {

        SubscriberOnNextListener subscriber = new SubscriberOnNextListener<HttpResult<MarketChartDataResult>>() {
            @Override
            public void onNext(HttpResult<MarketChartDataResult> marketChartDataResult) {

                mSwipyRefreshLayout.setRefreshing(false);
                List<MarketChartData> mMarketChartDataList = marketChartDataResult.getDatas().getMarketDatas();
                Collections.sort(mMarketChartDataList, new Comparator<MarketChartData>() {
                    @Override
                    public int compare(MarketChartData o1, MarketChartData o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });
                adapter = new QuickAdapter<MarketChartData>(getActivity(), R.layout.market_trade_list_item, mMarketChartDataList) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, final MarketChartData item) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                "HH:mm:ss", Locale.getDefault());
                        if ("buy".equals(item.getType().toLowerCase())) {
                            helper.setText(R.id.list_item_type, "Buy");
                            helper.setTextColor(R.id.list_item_type,
                                    ContextCompat.getColor(context, R.color.green_color));
                        } else {
                            helper.setText(R.id.list_item_type, "Sell");
                            helper.setTextColor(R.id.list_item_type,
                                    ContextCompat.getColor(context, R.color.red_color));
                        }
                        helper.setText(R.id.list_item_time,
                                simpleDateFormat.format(new Date(Long.valueOf(item.getDate()))));
                        helper.setText(R.id.list_item_cur, format(item.getPrice(),
                                Utils.getPrecisionExchange(currencyType, exchangeType)));
                        helper.setText(R.id.list_item_ex, deFormat(item.getAmount(), Utils.getPrecisionAmount(currencyType, exchangeType)));
//                        if (item.getType().toUpperCase().equals("SELL")) {
//                            helper.setBackgroundRes(R.id.list_item_bg, R.color.sell_bg_color);
//                        } else {
//                            helper.setBackgroundRes(R.id.list_item_bg, R.color.buy_bg_color);
//                        }
                        if ((helper.getPosition() % 2) == 1) {
                            helper.setBackgroundColor(R.id.list_item_bg,
                                    Color.rgb(0xf5, 0xf6, 0xf7));
                        } else {
                            helper.setBackgroundColor(R.id.list_item_bg,
                                    Color.rgb(0xff, 0xff, 0xff));
                        }

                    }

                };
                listView.setAdapter(adapter);

            }
        };
        HttpMethods.getInstance(1).getIndexMarketChartTrades(new ProgressSubscriber(subscriber, context), exchangeType, currencyType);
    }
}
