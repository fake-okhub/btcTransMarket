package com.android.bitglobal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketDatasResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TickerData;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.DataHelper;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.tool.SpTools;
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

import rx.Subscriber;


/**
 * K线图
 */
public class KDiagramNewActivity extends BaseActivity implements View.OnClickListener {

    KChartView mKChartView;
//    private TextView mPriceTv;
//    private TextView mHighTv;
//    private TextView mLowTv;
//    private TextView mRangeTv;
//    private TextView mVolumeTv;
    private LinearLayout mBottomLayout;
    private RadioGroup myRadioGroup;

    private KChartAdapter mAdapter;


    private String currencyType = "ETH", exchangeType = "BTC";
    private String iconUrl;
    // 声明控件对象
//    private String kChartTimeInterval = "900";                       //图表数据间隔
    private String kChartTimeInterval = "1800";                       //图表数据间隔
    private final String kChartDataSize = "1440";                           //图表数据总条数
    //定时器
    Timer timer1 = null;  //刷新定时器
    private final int dataRefreshTime1 = 4 * 1000;                         //数据刷新间隔
    private List<Map<String, Object>> titleList = new ArrayList<>();
    List<String> list = new ArrayList<>();
    private List<KLineEntity> marketChartDataLists = new ArrayList<>();

    public KDiagramNewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.klinebg);//通知栏所需颜色
        // 设置显示的视图
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kdiagram);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        currencyType = intent.getStringExtra("currencyType");
        exchangeType = intent.getStringExtra("exchangeType");
        iconUrl = intent.getStringExtra("iconUrl");
        mKChartView = (KChartView) findViewById(R.id.activity_kchart_view);
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setCurrencyType(currencyType);
        mKChartView.setExchangeType(exchangeType);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        mKChartView.setScreenOrientation(false);
        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                L.i("index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
        mKChartView.setOverScrollRange(ViewUtil.Dp2Px(this, 40));

        findViewById(R.id.activity_kchart_back).setOnClickListener(this);
//        TextView view = (TextView) findViewById(R.id.activity_kchart_title);
//        view.setText(currencyType.toUpperCase() + "/" + exchangeType.toUpperCase());
//        mPriceTv = (TextView) findViewById(R.id.activity_kchart_price);
//        mHighTv = (TextView) findViewById(R.id.activity_kchart_high);
//        mLowTv = (TextView) findViewById(R.id.activity_kchart_low);
//        mRangeTv = (TextView) findViewById(R.id.activity_kchart_range);
//        mVolumeTv = (TextView) findViewById(R.id.activity_kchart_volume);
        mBottomLayout = (LinearLayout) findViewById(R.id.activity_kchart_lay);
        initBottomLayout();
    }

    private void initBottomLayout() {
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
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.VERTICAL);
        mBottomLayout.addView(myRadioGroup);
        for (int i = 0; i < titleList.size(); i++) {
            Map<String, Object> map1 = titleList.get(i);
            RadioButton radio = new RadioButton(this);
            myRadioGroup.addView(radio);
            radio.setButtonDrawable(R.color.kViewztblack);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
//            radio.setPadding(20, 0, 20, 0);
            // radio.setPadding(left, top, right, bottom)
            radio.setId(1 + i);
            radio.setText(String.format("%s", map1.get("title")));
            radio.setTextSize(16.0f);
            radio.setTextColor(ContextCompat.getColor(this, R.color.white));
            if (i == 2) {
//                radio.setTextColor(getResources().getColor(R.color.kViewzt_yellow));
                radio.setChecked(true);
                radio.setBackgroundColor(ContextCompat.getColor(this, R.color.chart_radio_select));
            } else {
//                radio.setTextColor(getResources().getColor(R.color.kViewztblack));
                radio.setChecked(false);
                radio.setBackgroundColor(ContextCompat.getColor(this, R.color.chart_radio_no_select));
            }

            radio.setTag(map1);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton vradio = (RadioButton) group.getChildAt(i);
                    vradio.setGravity(Gravity.CENTER);
//                    vradio.setPadding(20, 0, 20, 0);
                    if (rb.getId() == vradio.getId()) {
//                        vradio.setTextColor(getResources().getColor(R.color.kViewzt_yellow));
                        vradio.setChecked(true);
                        vradio.setBackgroundColor(ContextCompat.getColor(KDiagramNewActivity.this, R.color.chart_radio_select));
//                        vradio.setBackground(getResources().getDrawable(R.drawable.bg_bottom));
                    } else {
                        vradio.setChecked(false);
                        vradio.setBackgroundColor(ContextCompat.getColor(KDiagramNewActivity.this, R.color.chart_radio_no_select));
                    }

                }
                //           CustomProgress.show(mContext, true, null);
                kChartTimeInterval = titleList.get(checkedId - 1).get("time") + "";
                // indexMarketChart();
                marketList();

            }
        });
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
            //更新图表
            DataHelper.calculate(marketChartDataLists);
            mAdapter.addData(marketChartDataLists);
            if (isfirst) {
                mKChartView.onLeftSide();
                mKChartView.startAnimation();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//        String str_rate=tickerData.getRiseRate();
//        if (str_rate.contains("-")) {
//            mRangeTv.setText(String.format(" RANGE : %s%%", tickerData.getRiseRate()));
//        } else {
//            mRangeTv.setText(" RANGE : +" + tickerData.getRiseRate() + "%");
//        }
//
//        mHighTv.setText(String.format("HIGH : %s", format(tickerData.getHigh(),
//                Utils.getPrecisionPrice(currencyType))));
//        setTextAndColor(format(btc123MarketData.getTicker().getLast(),
//                Utils.getPrecisionPrice(currencyType)),
//                mPriceTv, false);
//        mPriceTv.setText(format(tickerData.getLast(),
//                Utils.getPrecisionPrice(currencyType)));
//
//        mLowTv.setText(String.format("LOW  : %s", format(tickerData.getLow(),
//                Utils.getPrecisionPrice(currencyType))));
//        mVolumeTv.setText(String.format("VOLUME : %s", tickerData.getVol()));

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
            TransTimerTask task1 = new TransTimerTask(handlerOfTrans, 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_kchart_back:
                onBackPressed();
                break;
            default:
                break;
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
    public void onBackPressed() {
//        UIHelper.showMarketDetailActivity(this, currencyType, exchangeType, iconUrl);
        finish();
    }
}