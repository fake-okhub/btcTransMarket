package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.EntityImpl.MACDImpl;
import com.github.tifezh.kchartlib.chart.EntityImpl.VOLImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * Created by Administrator on 2017/7/12.
 */

public class VOLDraw extends BaseDraw<VOLImpl> {

    public VOLDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable VOLImpl lastPoint, @NonNull VOLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawVOL(canvas, view, curX, curPoint.getVolume(), lastPoint.getVolume());
//        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
//        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
//        String text = "";
//        MACDImpl point = (MACDImpl) view.getItem(position);
//        text = "DIF:" + view.formatValue(point.getDif()) + " ";
//        canvas.drawText(text, x, y, ma10Paint);
//        x += ma5Paint.measureText(text);
//        text = "DEA:" + view.formatValue(point.getDea()) + " ";
//        canvas.drawText(text, x, y, ma5Paint);
//        x += ma10Paint.measureText(text);
//        text = "MACD:" + view.formatValue(point.getMacd()) + " ";
//        canvas.drawText(text, x, y, ma20Paint);
    }

    @Override
    public float getMaxValue(VOLImpl point) {
        return point.getVolume();
    }

    @Override
    public float getMinValue(VOLImpl point) {
        return 0;
    }

    /**
     * 画vol
     *  @param canvas
     * @param lastVol
     * @param x
     * @param vol
     */
    private void drawVOL(Canvas canvas, IKChartView view, float x, float vol, float lastVol) {
//        vol = view.getChildY(vol);
        int r = mCandleWidth / 2;
        if(vol >= lastVol) {
            canvas.drawRect(x - r, view.getChildY(vol), x + r, view.getChildY(0), redPaint);
        } else {
            canvas.drawRect(x - r, view.getChildY(vol), x + r, view.getChildY(0), greenPaint);
        }
//        if (vol > view.getChildY(0)) {
//            canvas.drawRect(x - r, view.getChildY(0), x + r, vol, greenPaint);
//        } else {
//            canvas.drawRect(x - r, vol, x + r, view.getChildY(0), redPaint);
//        }
    }

}
