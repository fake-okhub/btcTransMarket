package com.android.bitglobal.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.entity.MarketAndCurrency;
import com.android.bitglobal.entity.TickerData;
import com.android.bitglobal.entity.TradeExchange;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;

/**
 * Created by bitbank on 17/1/7.
 */
public class MarketInfoAdapter extends BaseAdapter {


    //combo
    private ComboLineColumnChartData data;
    private LineChartData Linedata;
    private ColumnChartData Columndata;

    private HashMap datasetLineSet = new HashMap();
    //  private String symbol_cny="฿";
    private String symbol_cny = "";
    private final int kChartXAsixLabelInterval = 50;
    private Activity mContext;
    DecimalFormat df0 = new DecimalFormat("0.00000000");
    DecimalFormat df1 = new DecimalFormat("0.00000");
    DecimalFormat df2 = new DecimalFormat("0.00");
    DecimalFormat df3 = new DecimalFormat("0.000");
    private List<MarketAndCurrency> mMarketList = new ArrayList<>();
    private List<Double> mPrePriceList = new ArrayList<>(Collections.nCopies(10, (double) 0.0f));
    private MarketAndCurrency mMarketAndCurrency = null;
    private LayoutInflater mInflater;
    //当前市场的分类
    private String exchangeFlag = SystemConfig.homeUSDC;
    public MarketInfoAdapter(Activity context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setMarketList(List<MarketAndCurrency> marketList) {
        this.mMarketList = marketList;
    }

    public void setExchangeFlag(String exchangeFlag){
        this.exchangeFlag=exchangeFlag;
    }

    public String getExchangeFlag(){
        return exchangeFlag;
    }

    @Override
    public int getCount() {
        return mMarketList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMarketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 生成图表显示的数据
     *
     * @param data1
     */
    private void createChartDataSet(String[][] data1, String current, final lecho.lib.hellocharts.view.LineChartView chart, int color) {
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        float maxValue = 0;
        float miniValue = 0;
        if (data1 != null && data1.length > 0) {
            for (int j = 0; j < data1.length; ++j) {
                float price = Float.parseFloat(data1[data1.length - j - 1][4]);

                if (price > maxValue || j == 0) {
                    maxValue = price;
                }
                if (price < miniValue || j == 0) {
                    miniValue = price;
                }
                values.add(new PointValue(j, Float.parseFloat(data1[data1.length - j - 1][4])));//设置线的数据
                //subvalue.add(new SubcolumnValue(Float.parseFloat(data1[j][1]), Color.BLUE));// 设置柱子数据并为其设置颜色

                Long time = Long.parseLong(data1[j][0]);
                /*if(j%2==1){
                    axisValues.add(new AxisValue(j).setLabel(sdf.format(new Date(time))));
                }else{
                    axisValues.add(new AxisValue(j).setLabel(""));
                }*/
            }
            values.add(new PointValue(data1.length, Float.parseFloat(current)));//设置线的数据
        }
        Line line = new Line(values);
        line.setColor(color);//设置线颜色
        line.setHasPoints(false);//设置是否显示点
        line.setFilled(false);//设置是否填充
        line.setCubic(false);
        line.setStrokeWidth(1); // 设置线宽
        lines.add(line);
        Linedata = new LineChartData(lines);//将线赋值给linedata
//        Linedata.setAxisXBottom(new Axis(axisValues));
//        Linedata.setAxisYLeft(new Axis().setHasLines(false).setMaxLabelChars(5));
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);
        chart.setLineChartData(Linedata);
        resetViewport(chart, values.size());
        //  chart.setOnClickListener(this);
        chart.setVisibility(View.VISIBLE);
    }

    //修改k线曲线弧度
    private void resetViewport(lecho.lib.hellocharts.view.LineChartView chart, int size) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = v.bottom - (v.bottom * 0.09f);
        v.top = v.top * 1.05f;
        v.left = size - 49;
        v.right = size;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    /**
     * 生成图表显示的数据
     *
     * @param marketChartDatas
     */
    private void createChartDataSet(String[][] marketChartDatas, final LineChartView chartView, final ProgressBar mProgressBar, String index) {
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.55f));
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        // gridPaint.setPathEffect(effects);
        chartView.setBorderSpacing(Tools.fromDpToPx(10))
                .setXLabels(AxisController.LabelPosition.NONE)
//                .setLabelsColor(Color.parseColor("#999999"))
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false)
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String[] labels = new String[0];
        float[] values = new float[0];
        int maxValue = 0;
        int miniValue = 0;
        if (marketChartDatas != null && marketChartDatas.length > 0) {
            labels = new String[marketChartDatas.length];
            values = new float[marketChartDatas.length];
            for (int i = 0; i < marketChartDatas.length; i++) {
                String[] data = marketChartDatas[marketChartDatas.length - 1 - i];

                //1.记录最大值和最小值
                float price = Float.parseFloat(data[4]);
                long time = Long.parseLong(data[0]);
                String timePoint = sdf.format(new Date(time * 1000));
                if (price > maxValue || i == 0) {
                    maxValue = (int) price;
                }
                if (price < miniValue || i == 0) {
                    miniValue = (int) price;
                }

                //2.记录x周要显示坐标
                if (i == 0 || (i + 1) % kChartXAsixLabelInterval == 0) {
                    labels[i] = timePoint;
                } else {
                    labels[i] = "";
                }

                //3.记录坐标y值
                values[i] = price;
            }
        } else {

        }
        //要偶数
        if (miniValue % 2 == 0) {
            miniValue = miniValue - 2;
        } else {
            miniValue = miniValue - 1;
        }
        //要偶数
        if (maxValue % 2 == 0) {
            maxValue = maxValue + 2;
        } else {
            maxValue = maxValue + 1;
        }
        int step = (maxValue - miniValue) / 5;  //计算最大值到最少
        if (step == 0) {
            maxValue = miniValue + 5;
            step = 1;
        } else {
            step = step + 1;
            maxValue = step * 5 + miniValue;
        }
        Log.i("miniValue", "miniValue = " + miniValue);
        Log.i("maxValue", "maxValue = " + maxValue);
        Log.i("step", "step = " + step);
        LineSet dataset = new LineSet(labels, values);
        dataset.setColor(Color.RED).setThickness(Tools.fromDpToPx(2));
        datasetLineSet.put(index, dataset);
        datasetLineSet.put(index + "miniValue", miniValue);
        datasetLineSet.put(index + "maxValue", maxValue);
        datasetLineSet.put(index + "step", step);
        chartView.addData(dataset);
        chartView.setXAxis(false);
        chartView.setAxisBorderValues(miniValue, maxValue, step);
        chartView.show();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        final MarketAndCurrency item = mMarketList.get(i);
        TickerData tickerData=item.getTickerData();
        if(tickerData!=null) {
            item.getTickerData().format();
        }
        int color = 0x0;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.market_list_item, null);
            holder.img_type = (ImageView) convertView.findViewById(R.id.img_type);
//            holder.img_market_fh = (ImageView) convertView.findViewById(R.id.img_market_fh);
            holder.tv_currency_name = (TextView) convertView.findViewById(R.id.tv_currency_name);
            holder.tv_currency_english_name = (TextView) convertView.findViewById(R.id.tv_currency_english_name);
            holder.tv_currentPrice = (TextView) convertView.findViewById(R.id.tv_currentPrice);
            holder.tv_currentPrice_symbol = (TextView) convertView.findViewById(R.id.tv_currentPrice_symbol);
//            holder.ll_market_rate = (LinearLayout) convertView.findViewById(R.id.ll_market_rate);
//            holder.tv_market_jj = (TextView) convertView.findViewById(R.id.tv_market_jj);
            holder.tv_market_rate = (TextView) convertView.findViewById(R.id.tv_market_rate);
            holder.tv_24xszgj = (TextView) convertView.findViewById(R.id.tv_24xszgj);
            holder.tv_24xszdj = (TextView) convertView.findViewById(R.id.tv_24xszdj);
            holder.ll_market_detail = (FrameLayout) convertView.findViewById(R.id.ll_market_detail);
//            holder.ll_market_info = (FrameLayout) convertView.findViewById(R.id.ll_market_info);
            holder.linechart2 = (lecho.lib.hellocharts.view.LineChartView) convertView.findViewById(R.id.linechart2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            String url = item.getCurrencySet().getCoinUrl();
            if (!StringUtils.isEmpty(url)) {
                Picasso.with(mContext)
                        .load(url)
                        .into(holder.img_type);
            }

            double price = Double.valueOf(item.getTickerData().getLast());
            holder.tv_currentPrice_symbol.setText(getCurrentSymbol());
            String currency=item.getCurrencySet().getCurrency();
            holder.tv_currentPrice.setText(getCurrentPrice(price,currency));
//            if (Double.compare(price, mPrePriceList.get(i)) > 0) {
//                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_green);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.tv_currentPrice.setCompoundDrawables(drawable, null, null, null);
//            } else if (Double.compare(price, mPrePriceList.get(i)) < 0) {
//                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_red);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                holder.tv_currentPrice.setCompoundDrawables(drawable, null, null, null);
//            }
            mPrePriceList.set(i, price);

            if (item.getTickerData().getRiseRate() != null
                    && !StringUtils.isEmpty(item.getTickerData().getRiseRate())) {
                int compare = Float.compare(Float.valueOf(item.getTickerData().getRiseRate()), 0.0f);
                if (compare > 0) {
                    color = ContextCompat.getColor(mContext, R.color.ll_market_rate_green_color);
//                color = Color.parseColor("#e34b51");
                    String str = "+ " + item.getTickerData().getRiseRate() + "%";
                    holder.tv_market_rate.setText(str);
                    holder.tv_market_rate.setBackgroundResource(R.drawable.bnt_trans_buy_solid);

                } else if (compare < 0) {
                    color = ContextCompat.getColor(mContext, R.color.ll_market_rate_red_color);
                    String str = item.getTickerData().getRiseRate() + "%";
                    holder.tv_market_rate.setText(splitStr(str));
                    holder.tv_market_rate.setBackgroundResource(R.drawable.bnt_trans_sell_solid);
                } else {
                    color = ContextCompat.getColor(mContext, R.color.ll_market_rate_gray_color);
                    holder.tv_market_rate.setText("0.00%");
                    holder.tv_market_rate.setBackgroundResource(R.drawable.bnt_trans_no_change_solid);
                }
            } else {
                color = ContextCompat.getColor(mContext, R.color.ll_market_rate_gray_color);
                holder.tv_market_rate.setText("--%");
                holder.tv_market_rate.setBackgroundResource(R.drawable.bnt_trans_no_change_solid);
            }
            holder.tv_currency_name.setText(item.getCurrencySet().getCurrency());
            holder.tv_currency_english_name.setText(item.getCoinFullNameEn());
            holder.linechart2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TradeExchange mTradeExchange = split(item.getSymbol());
//                UIHelper.showKDiagramActivity(mContext, mTradeExchange.getCurrencyType(), mTradeExchange.getExchangeType());
                    UIHelper.showMarketDetailActivity(mContext, mTradeExchange.getCurrencyType(),
                            mTradeExchange.getExchangeType(), item.getCurrencySet().getCoinUrl());
                }
            });
            holder.ll_market_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMarketAndCurrency = item;
                    TradeExchange mTradeExchange = split(item.getSymbol());
//                UIHelper.showKDiagramActivity(mContext,mTradeExchange.getCurrencyType(),mTradeExchange.getExchangeType());
                    UIHelper.showMarketDetailActivity(mContext, mTradeExchange.getCurrencyType(),
                            mTradeExchange.getExchangeType(), item.getCurrencySet().getCoinUrl());
                }
            });
            double vol_a = 0.00;
            String dw_str = "";
            try {
                vol_a = Double.parseDouble(item.getTickerData().getVol());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (SystemConfig.getLanguageEnv().equals("cn")) {
                if (vol_a > 100000000) {
                    vol_a = vol_a / 100000000;
                    dw_str = mContext.getResources().getString(R.string.market_dw_y);
                } else if (vol_a > 1000000) {
                    vol_a = vol_a / 1000000;
                    dw_str = mContext.getResources().getString(R.string.market_dw_bw);
                } else if (vol_a > 10000) {
                    vol_a = vol_a / 10000.0;
                    dw_str = mContext.getResources().getString(R.string.market_dw_w);
                } else if (vol_a > 1000) {
                    vol_a = vol_a / 1000.0;
                    dw_str = mContext.getResources().getString(R.string.market_dw_q);
                }
            } else {
                if (vol_a > 100000000) {
                    vol_a = vol_a / 1000000000;
                    dw_str = mContext.getResources().getString(R.string.market_dw_y);
                } else if (vol_a > 1000000) {
                    vol_a = vol_a / 1000000;
                    dw_str = mContext.getResources().getString(R.string.market_dw_bw);
                } else if (vol_a > 1000) {
                    vol_a = vol_a / 1000.0;
                    dw_str = mContext.getResources().getString(R.string.market_dw_q);
                }
            }

//            String str = mContext.getResources().getString(R.string.market_24_high) + " " + deFormat(item.getTickerData().getHigh(), Utils.getPrecisionPrice(item.getCurrencySet().getCurrency()));
            String str = item.getTickerData().getLegal_tender();
            holder.tv_24xszgj.setText(str);
            // +"฿"
//            str = mContext.getResources().getString(R.string.vol) + " " + deFormat(item.getTickerData().getLow(), Utils.getPrecisionPrice(item.getCurrencySet().getCurrency()));
            str = mContext.getResources().getString(R.string.market_vol);
            holder.tv_24xszdj.setText(str + ((MainActivity)mContext).getVolText(item.getTickerData().getTotalBtcNum()) + getCurrentMarket());
            // holder.tv_24xscjl.setText(deFormat(vol_a,2)+dw_str+item.getCurrencySet().getCoinFullNameEn());

            createChartDataSet(item.getTline(), item.getTickerData().getLast(), holder.linechart2, color);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * 返回当前价格,根据不同市场，返回不同尾数，usdc 下不同币种不同尾数，btc 6位
     * @return
     */
    private String getCurrentPrice(double price,String currency){
        switch (getExchangeFlag()){
            case SystemConfig.homeUSDC:
                return format(price, Utils.getPrecisionExchange(currency, exchangeFlag.toUpperCase()));
            case SystemConfig.homeBTC:
                return format(price, Utils.getPrecisionExchange(currency, exchangeFlag.toUpperCase()));
            default:
                return format(price, Utils.getPrecisionExchange(currency, exchangeFlag.toUpperCase()));
        }
    }

    private String getCurrentSymbol(){
        switch (getExchangeFlag()){
            case SystemConfig.homeUSDC:
                return "$ ";
            case SystemConfig.homeBTC:
                return "฿ ";
            default:
                return "$ ";
        }
    }

    /**
     * 根据市场，返回市场标志
     * @return
     */
    private String getCurrentMarket(){
        switch (getExchangeFlag()){
            case SystemConfig.homeUSDC:
                return " USDC";
            case SystemConfig.homeBTC:
                return " BTC";
            default:
                return " USDC";
        }
    }

    public String format(double num, int type) {
        String str = "0.";
        for (int i = 0; i < type; i++) {
            str += "0";
        }
        try {
            str = StringUtils.formatDouble(num,type);
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }

//    public MarketAndCurrency getMarketAndCurrency() {
//        return mMarketAndCurrency;
//    }

    public String deFormat(String str) {
        try {
            str = df1.format(Double.parseDouble(str));
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String deFormat(String str, int type) {
        try {
            if (type == 2) {
                str = df2.format(Double.parseDouble(str));
            } else if (type == 3) {
                str = df3.format(Double.parseDouble(str));
            } else if (type == 8) {
                str = df0.format(Double.parseDouble(str));
            } else {
                str = df1.format(Double.parseDouble(str));
            }

        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String deFormat(double dou, int type) {
        String str = "";
        try {
            if (type == 2) {
                str = df2.format(dou);
            } else if (type == 3) {
                str = df3.format(dou);
            } else if (type == 8) {
                str = df0.format(dou);
            } else {
                str = df1.format(dou);
            }
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    //c拆分交易兑换币
    private TradeExchange split(String symbol) {
        TradeExchange mTradeExchange = null;
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

    /**
     *对 服务器传过来的  -2.3拆分 重新组合 - 2.3
     */
    private String splitStr(String minusStr){
        StringBuilder sb = new StringBuilder();
        String result;
        if (minusStr.startsWith("-")){
            String minus = minusStr.substring(0,1);
            String num=minusStr.substring(1,minusStr.length());
            sb.append(minus);
            sb.append(" ");
            sb.append(num);
            result=sb.toString();
        }else {
            result=minusStr;
        }
        return result;
    }
/*    @Override
    public void onClick(View view) {
*//*
        if(mMarketAndCurrency==null)
        {
            mMarketAndCurrency =mMarketList.get(0);
        }

        TradeExchange mTradeExchange=split(mMarketAndCurrency.getCoinFullNameEn());
        UIHelper.ToastMessage(mContext,"1111111111111111exchangeType:"+mTradeExchange.getExchangeType()+",111111111111currencyType:"+mTradeExchange.getCurrencyType());
        UIHelper.showKDiagramActivity(mContext,mTradeExchange.getCurrencyType(),mTradeExchange.getExchangeType());*//*
      //  UIHelper.showKDiagramActivity(mContext,currencyType,mMarketAndCurrency.getCurrencySet().getCurrency());
    }*/

    static class ViewHolder {

        public ImageView img_type;
        public TextView tv_currency_name;
        public TextView tv_currency_english_name;
        public TextView tv_currentPrice_symbol;
        public TextView tv_currentPrice;
//        public TextView tv_market_jj;
        public TextView tv_market_rate;
//        public ImageView img_market_fh;


//        public LinearLayout ll_market_rate;
        public FrameLayout ll_market_detail;
//        public FrameLayout ll_market_info;
        public TextView tv_24xszgj;
        public TextView tv_24xszdj;
        public lecho.lib.hellocharts.view.LineChartView linechart2;
    }
}
