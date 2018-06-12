package com.android.bitglobal.view.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.chart.BaseKChart;
import com.github.tifezh.kchartlib.chart.draw.BOLLDraw;
import com.github.tifezh.kchartlib.chart.draw.KDJDraw;
import com.github.tifezh.kchartlib.chart.draw.MACDDraw;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.RSIDraw;
import com.github.tifezh.kchartlib.chart.draw.VOLDraw;

/**
 * k线图
 */
public class KChartView extends BaseKChart {

    private MainDraw mainDraw;

    public KChartView(Context context) {
        this(context, null);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addChildDraw("VOL", new VOLDraw(getContext()));
        addChildDraw("MACD", new MACDDraw(getContext()));
//        addChildDraw("KDJ", new KDJDraw(getContext()));
//        addChildDraw("RSI", new RSIDraw(getContext()));
//        addChildDraw("BOLL", new BOLLDraw(getContext()));
        mainDraw = new MainDraw(getContext());
        setMainDraw(mainDraw);
    }

    public void setMainDrawCurrencyType(String currencyType, String exchangeType) {
        mainDraw.setCurrencyType(currencyType);
        mainDraw.setExchangeType(exchangeType);
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

}
