package com.android.bitglobal.fragment.market;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.market.MarketDetailActivity;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketDatasResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TickerData;
import com.android.bitglobal.entity.TradeOverviewResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.fragment.trans.TransBuyOrSellFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.DataHelper;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.chart.KChartAdapter;
import com.android.bitglobal.view.chart.KChartView;
import com.android.bitglobal.view.chart.KLineEntity;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.github.tifezh.kchartlib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * 某币种详情图表视图
 * Created by Elbert on 17/3/15.
 */
public class ChartFragment extends BaseFragment {

    KChartView mKChartView;
    private View view;
    private View chartSelectTimeView;
//    private LinearLayout mBottomLayout;
    private RadioGroup myRadioGroup;
    private PopupWindow popupWindow;
    private TextView selectTimeText;

    private KChartAdapter mAdapter;

    // 声明控件对象
//    private String kChartTimeInterval = "900";                       //图表数据间隔
    private String kChartTimeInterval = "1800";                       //图表数据间隔
    private final String kChartDataSize = "1440";                          //图表数据总条数
    //定时器
    Timer timer1 = null;  //刷新定时器
    private final int dataRefreshTime1 = 4 * 1000;                         //数据刷新间隔
    private List<Map<String, Object>> titleList = new ArrayList<>();
    List<String> list = new ArrayList<>();
    private List<KLineEntity> marketChartDataLists = new ArrayList<>();
    private Activity context;
    private String currencyType = "ETH";
    private String exchangeType = "BTC";

    @BindView(R.id.image_full)
    ImageView image_full;
    @BindView(R.id.buyBtn)
    Button buyBtn;
    @BindView(R.id.sellBtn)
    Button sellBtn;


    private Unbinder unbinder;

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.market_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = this.getActivity();
        initView();
        return view;
    }

    private void initView() {
        mKChartView = (KChartView)view.findViewById(R.id.activity_kchart_view);
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        mKChartView.setScreenOrientation(true);
        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                L.i("index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
        mKChartView.setOverScrollRange(ViewUtil.Dp2Px(context, 40));
//        mBottomLayout = (LinearLayout) view.findViewById(R.id.activity_kchart_lay);
        selectTimeText = (TextView) view.findViewById(R.id.selectTimeText);
        initTimeMap();
    }

    @OnClick({R.id.selectTimeText, R.id.image_full, R.id.buyBtn, R.id.sellBtn })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_full:
                UIHelper.showKDiagramNewActivity(context, currencyType, exchangeType,
                        ((MarketDetailActivity)getActivity()).getIconUrl());
//                context.finish();
                break;
            case R.id.selectTimeText:
                showOrDismissSelectTimeDialog();
                break;
            case R.id.buyBtn:
                Bundle bundle = new Bundle();
                bundle.putString("currencyType", currencyType);
                bundle.putString("exchangeType", exchangeType);
                bundle.putString("type", TransBuyOrSellFragment.TYPE_TRANS_BUY);
                UIHelper.showTrans(getActivity(), bundle);
                break;
            case R.id.sellBtn:
                Bundle bundle2 = new Bundle();
                bundle2.putString("currencyType", currencyType);
                bundle2.putString("exchangeType", exchangeType);
                bundle2.putString("type", TransBuyOrSellFragment.TYPE_TRANS_SELL);
                UIHelper.showTrans(getActivity(), bundle2);
                break;

        }

    }

    private void initTimeMap() {
        Map<String, Object> map;
//        map = new HashMap<>();
//        map.put("id", "1");
//        map.put("title", getString(R.string.one_min));
//        map.put("time", 60 + "");
//        titleList.add(map);
//        map = new HashMap<>();
//        map.put("id", "13");
//        map.put("title", getString(R.string.three_min));
//        map.put("time", 3 * 60 + "");
//        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "2");
        map.put("title", getString(R.string.five_min));
        map.put("time", 5 * 60 + "");
        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "3");
        map.put("title", getString(R.string.fifteen_min));
        map.put("time", 15 * 60 + "");
        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "4");
        map.put("title", getString(R.string.thirty_min));
        map.put("time", 30 * 60 + "");
        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "5");
        map.put("title", getString(R.string.one_hour));
        map.put("time", 60 * 60 + "");
        titleList.add(map);
//        map = new HashMap<>();
//        map.put("id", "6");
//        map.put("title", getString(R.string.two_hour));
//        map.put("time", 2 * 60 * 60 + "");
//        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "7");
        map.put("title", getString(R.string.four_hour));
        map.put("time", 4 * 60 * 60 + "");
        titleList.add(map);
//        map = new HashMap<>();
//        map.put("id", "8");
//        map.put("title", getString(R.string.six_hour));
//        map.put("time", 6 * 60 * 60 + "");
//        titleList.add(map);
//        map = new HashMap<>();
//        map.put("id", "9");
//        map.put("title", getString(R.string.twelve_hour));
//        map.put("time", 12 * 60 * 60 + "");
//        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "10");
        map.put("title", getString(R.string.one_day));
        map.put("time", 24 * 60 * 60 + "");
        titleList.add(map);
//        map = new HashMap<>();
//        map.put("id", "11");
//        map.put("title", getString(R.string.three_day));
//        map.put("time", 3 * 24 * 60 * 60 + "");
//        titleList.add(map);
        map = new HashMap<>();
        map.put("id", "12");
        map.put("title", getString(R.string.one_week));
        map.put("time", 7 * 24 * 60 * 60 + "");
        titleList.add(map);
    }


    private void showOrDismissSelectTimeDialog() {
        chartSelectTimeView = View.inflate(context, R.layout.dialog_chart_select_time, null);
        LinearLayout chartLayout = (LinearLayout) chartSelectTimeView.findViewById(R.id.activity_kchart_lay);
        chartLayout.setBackgroundResource(R.drawable.chart_border);
        myRadioGroup = new RadioGroup(context);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(
                selectTimeText.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.VERTICAL);
        chartLayout.addView(myRadioGroup);
        for (int i = 0; i < titleList.size(); i++) {
            Map<String, Object> map1 = titleList.get(i);
            RadioButton radio = new RadioButton(context);
            myRadioGroup.addView(radio);
            radio.setButtonDrawable(R.color.kViewztblack);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            radio.setPadding(20, 10, 20, 10);
            // radio.setPadding(left, top, right, bottom)
            radio.setId(1 + i);
            radio.setText(String.format("%s", map1.get("title")));
            radio.setTextSize(12.0f);
            if (i == 2) {
                radio.setTextColor(getResources().getColor(R.color.kViewzt_yellow));
                radio.setChecked(true);
                radio.setBackground(getResources().getDrawable(R.drawable.bg_bottom));
            } else {
                radio.setTextColor(getResources().getColor(R.color.kViewztblack));
                radio.setChecked(false);
                radio.setBackground(null);
            }
            radio.setTag(map1);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) chartSelectTimeView.findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton vradio = (RadioButton) group.getChildAt(i);
                    vradio.setGravity(Gravity.CENTER);
                    vradio.setPadding(20, 10, 20, 10);
                    if (rb.getId() == vradio.getId()) {
                        vradio.setTextColor(getResources().getColor(R.color.kViewzt_yellow));
                        vradio.setChecked(true);
                        vradio.setBackground(getResources().getDrawable(R.drawable.bg_bottom));
                    } else {
                        vradio.setTextColor(getResources().getColor(R.color.kViewztblack));
                        vradio.setChecked(false);
                        vradio.setBackground(null);
                    }

                }
                //           CustomProgress.show(mContext, true, null);
                kChartTimeInterval = titleList.get(checkedId - 1).get("time") + "";
                // indexMarketChart();
                selectTimeText.setText(rb.getText());
                marketList();
                popupWindow.dismiss();

            }
        });
        if(popupWindow == null) {
            popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(chartSelectTimeView);
        }
        if(popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(selectTimeText);
        }
    }

    public void dismissPopupWindow() {
        if(popupWindow != null) {
            if(popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    /**
     * 生成图表显示的数据
     */
    private void createChartDataSet(String[][] marketChartDatas) {
        boolean isfirst = false;
        if (marketChartDataLists.size() == 0) {
            isfirst = true;
        }
        if (marketChartDatas != null && marketChartDatas.length > 0) {
            marketChartDataLists.clear();
            for (String[] data : marketChartDatas) {
                KLineEntity mMarketChartData = new KLineEntity();
                mMarketChartData.Date = data[0];
                mMarketChartData.Open = Float.parseFloat(data[3]);
                mMarketChartData.Close = Float.parseFloat(data[4]);
                mMarketChartData.High = Float.parseFloat(data[5]);
                mMarketChartData.Low = Float.parseFloat(data[6]);
                mMarketChartData.Volume = Float.parseFloat(data[7]);
//                mMarketChartData.setTime(Long.parseLong(data[0]));
              /*  mMarketChartData.setOpenId(data[1]);
                mMarketChartData.setCloseId(data[2]);*/
//                mMarketChartData.setOpenPrice(Double.parseDouble(data[3]));
//                mMarketChartData.setClosePrice(Double.parseDouble(data[4]));
//                mMarketChartData.setHighPrice(Double.parseDouble(data[5]));
//                mMarketChartData.setLowPrice(Double.parseDouble(data[6]));
//                mMarketChartData.setVol(Double.parseDouble(data[7]));
                marketChartDataLists.add(0, mMarketChartData);
            }
//            List<KLineEntity> newMarketChartDataLists = new ArrayList<>();
//            for(int i = 0; i < marketChartDataLists.size(); i ++) {
//                newMarketChartDataLists.add(marketChartDataLists.get(marketChartDataLists.size() - 1 - i));
//            }
            //更新图表
            DataHelper.calculate(marketChartDataLists);
            mAdapter.addData(marketChartDataLists);
            if (isfirst) {
                mKChartView.onLeftSide();
                mKChartView.startAnimation();
            }
        }
    }

    private void marketList() {
        Subscriber subscriber = new Subscriber<HttpResult<TickerArrayResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<TickerArrayResult> tickerArrayResultHttpResult) {
                if (tickerArrayResultHttpResult.getDatas().getMarketDatas() != null
                        && tickerArrayResultHttpResult.getDatas().getMarketDatas().size() > 0) {
                    mKChartView.setCurrentPrice(Double.parseDouble(tickerArrayResultHttpResult.getDatas().getMarketDatas().get(0).getTicker().getLast()));
                    mKChartView.setCurrencyType(currencyType);
                    mKChartView.setExchangeType(exchangeType);
                    mKChartView.setMainDrawCurrencyType(currencyType, exchangeType);
                    String[][] tline = tickerArrayResultHttpResult.getDatas().getMarketDatas().get(0).getTline();
                    createChartDataSet(tline);
                    MarketDatasResult btc123MarketData = tickerArrayResultHttpResult.getDatas().getMarketDatas().get(0);
                    setTextView(btc123MarketData);
                }

            }
        };
        // UIHelper.ToastMessage(mContext,"exchangeType:"+exchangeType+",currencyType:"+currencyType);
        String currencyTypes = "['" + currencyType + "']";
        HttpMethods.getInstance(1).getTickerArray(subscriber, exchangeType,
                currencyTypes, kChartTimeInterval, kChartDataSize,SpTools.getlegalTender());

    }

    public void setTextView(MarketDatasResult btc123MarketData) {
        TickerData tickerData=btc123MarketData.getTicker();
        if(tickerData!=null){
            tickerData.format();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();

    }


    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                marketList();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timer1 == null) {
            timer1 = new Timer();
            ChartFragment.TransTimerTask task1 = new ChartFragment.TransTimerTask(handlerOfTrans, 1);
            timer1.schedule(task1, 0, dataRefreshTime1);
        }

    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timer1 != null) {
            timer1.cancel();
            timer1 = null;
        }
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {
        private int code;
        private Handler mHandler;

        TransTimerTask(Handler handler, int code) {
            this.mHandler = handler;
            this.code = code;
        }

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = code;
            this.mHandler.sendMessage(message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
