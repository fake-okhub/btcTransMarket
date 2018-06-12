package com.android.bitglobal.fragment.market;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.CoinPriceFromOther;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.Utils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/6.
 */

public class OthersFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.tv_others_no_data)
    TextView tvNodata;

    private Unbinder unbinder;
    private Activity context;
    private String currencyType = "ETH";
    private String exchangeType = "BTC";
    private Timer timerTrade = null;  //刷新定时器
    private QuickAdapter<CoinPriceFromOther> adapter;

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_others_fragment, container, false);
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
            OthersFragment.TransTimerTask task = new OthersFragment.TransTimerTask(handlerOfTrans);
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
        Subscriber subscriber = new Subscriber<HttpResult<List<CoinPriceFromOther>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                //网络请求不是200时显示
                setNoDataTextVisible(true);
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpResult<List<CoinPriceFromOther>> listHttpResult) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (listHttpResult.getDatas()!= null && listHttpResult.getDatas().size() != 0){
                    setNoDataTextVisible(false);
                    adapter = new QuickAdapter<CoinPriceFromOther>(getActivity(), R.layout.market_others_list_item, listHttpResult.getDatas()) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, final CoinPriceFromOther item) {
                            helper.setText(R.id.fromText, item.getFrom());
                            helper.setText(R.id.currencyFiatPriceText, item.getLast_price_legal_tender());
                            helper.setText(R.id.currencyPriceText, format(item.getLast_price(),
                                    Utils.getPrecisionExchange(currencyType, exchangeType)));
                            if(item.getVol() != null) {
                                helper.setText(R.id.volText, getString(R.string.market_others_vol) + getVolText(item.getVol()) + " " + currencyType.toUpperCase());
                            }

                        }
                    };
                    listView.setAdapter(adapter);
                } else {
                    setNoDataTextVisible(true);
                }
            }
        };
        HttpMethods.getInstance(2).getCoinPriceFromOther(subscriber, currencyType + "_" + exchangeType, SpTools.getlegalTender());
    }

    /**
     * 设置无数据是否要显示
     * @param isVisible true 显示 false 不显示
     */
    private void setNoDataTextVisible(boolean isVisible){
        if (isVisible()){
            tvNodata.setVisibility(isVisible?View.VISIBLE:View.GONE);
        }
    }

}
