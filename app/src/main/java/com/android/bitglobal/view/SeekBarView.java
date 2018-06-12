package com.android.bitglobal.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.android.bitglobal.R;


/**
 * Created by Administrator on 2017/9/26.
 */

public class SeekBarView extends View {

    //小圆点的半径
    private static float SPOT_CIRCLE_RADIUS;
    //拖动bar的半径
    private static float THUMB_RADIUS;
    //拖动bar的半径
    private static float PROGRESS_BAR_STROKE_WIDTH;
    //总进度数
    private static final int PROGRESS_MAX = 100;
    //小圆点个数
    private static final float SPOT_CIRCLE_AMOUNT = 5;

    //控件宽度
    private float width;
    //控件高度
    private float height;
    //进度条起始x坐标
    private float progressStartX;
    //进度条结束x坐标
    private float progressStopX;
    //进度条长度
    private float progressWidth;
    //进度条y坐标
    private float progressY;
    //每个小圆点圆心之间的距离
    private float perSpotWidth;
    //每刻度之间距离
    private float perProgressWidth;
    //按下x坐标
    private float downX = 0;
    //按下y坐标
    private float downY = 0;
    //抬起x坐标
    private float upX = 0;
    //抬起y坐标
    private float upY = 0;
    //移动x坐标
    private float moveX = 0;
    //移动y坐标
    private float moveY = 0;
    //是否在进度条点击
    private boolean isTouchProgress;
    //是否拖动
    private boolean isSliding = true;
    //当前进度
    private int progress;
    //进度条颜色
    private int progressSecondBarColor;

    //进度条画笔
    private Paint progressBarPaint;
    //进度条画笔
    private Paint progressSecondBarPaint;
    //小圆点画笔
    private Paint spotPaint;
    //拖动bar画笔
    private Paint thumbPaint;

    //监听
    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public int getProgress() {
        return progress;
    }

    public interface OnChangeListener {

        void onProgressChanged(SeekBarView seekBarView, int progress);

        void onStartTrackingTouch(SeekBarView seekBarView);

        void onStopTrackingTouch(SeekBarView seekBarView);

    }

    public SeekBarView(Context context) {
        super(context);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//      默认进度条颜色
//        progressSecondBarColor = ContextCompat.getColor(context, R.color.progress_second_bar_color_green);

        SPOT_CIRCLE_RADIUS = dp2px(5);
        THUMB_RADIUS = dp2px(10);
        PROGRESS_BAR_STROKE_WIDTH = dp2px(4);

        progressBarPaint = new Paint(Paint.DITHER_FLAG);
        progressBarPaint.setAntiAlias(true);
        progressBarPaint.setDither(true);
        progressBarPaint.setStrokeWidth(PROGRESS_BAR_STROKE_WIDTH);
        progressBarPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_bar_color));

        progressSecondBarPaint = new Paint(Paint.DITHER_FLAG);
        progressSecondBarPaint.setAntiAlias(true);
        progressSecondBarPaint.setDither(true);
        progressSecondBarPaint.setStrokeWidth(PROGRESS_BAR_STROKE_WIDTH);
//        progressSecondBarPaint.setColor(progressSecondBarColor);

        spotPaint = new Paint(Paint.DITHER_FLAG);
        spotPaint.setAntiAlias(true);
        spotPaint.setDither(true);
        spotPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_bar_color));
//        spotPaint.setColor(progressSecondBarColor);

        thumbPaint = new Paint(Paint.DITHER_FLAG);
        thumbPaint.setAntiAlias(true);
        thumbPaint.setDither(true);
        thumbPaint.setColor(ContextCompat.getColor(getContext(), R.color.thumb_color));
    }

    public int getMax() {
        return PROGRESS_MAX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = dp2px(180);
        height = dp2px(20);
        //进度条起始x坐标初始化
        progressStartX = THUMB_RADIUS;
        //进度条结束x坐标初始化
        progressStopX = width - THUMB_RADIUS;
        //进度条长度初始化
        progressWidth = width - THUMB_RADIUS * 2;
        //进度条y坐标初始化
        progressY = height / 2;
        //初始化每个小圆点圆心之间的距离
        perSpotWidth = progressWidth / (SPOT_CIRCLE_AMOUNT - 1);
        //计算每个刻度之间的距离
        perProgressWidth = progressWidth / PROGRESS_MAX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画进度条
        canvas.drawLine(progressStartX, progressY, progressStopX, progressY, progressBarPaint);
        //画二级进度条
        canvas.drawLine(progressStartX, progressY, progressStartX + progress * perProgressWidth, progressY, progressSecondBarPaint);
        //小圆点坐标
        float spotRadiusX = progressStartX;
        for(int i = 0; i < SPOT_CIRCLE_AMOUNT; i ++) {
            //如果进度超过小圆点，小圆点变色
            if(progressStartX + perProgressWidth * progress > spotRadiusX) {
                spotPaint.setColor(progressSecondBarColor);
            } else {
                spotPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_bar_color));
            }
//            if(progress == 0) {
//                spotPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_bar_color));
//            }
            canvas.drawCircle(spotRadiusX, progressY, SPOT_CIRCLE_RADIUS, spotPaint);
            if(i == (SPOT_CIRCLE_AMOUNT - 2)) {
                spotRadiusX = progressStopX;
            } else {
                spotRadiusX += perSpotWidth;
            }
        }
        canvas.drawCircle(progressStartX + progress * perProgressWidth, progressY, THUMB_RADIUS, thumbPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(isSliding) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onChangeListener.onStartTrackingTouch(this);
                    downX = event.getX();
                    downY = event.getY();
                    isTouchProgress();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveX = event.getX();
                    moveY = event.getY();
                    //拖动bar不超出进度条范围
                    if(moveX > progressStartX && moveX < progressStopX) {
                        if(isTouchProgress) {
                            setProgress();
                            onChangeListener.onProgressChanged(this, progress);
                            invalidate();
                        }
                    } else if(moveX < progressStartX) {
                        setProgress(0);
                        onChangeListener.onProgressChanged(this, progress);
                    } else if(moveX > progressStopX) {
                        setProgress(PROGRESS_MAX);
                        onChangeListener.onProgressChanged(this, progress);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    upY = event.getY();
                    onChangeListener.onStopTrackingTouch(this);
                    break;
            }
        }
        return true;
    }

    public void setProgress() {
        this.progress = Math.round((moveX - THUMB_RADIUS) / perProgressWidth);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        //当移动到最前端小圆点变色
        invalidate();
    }

    private void isTouchProgress() {
        //点击进度条上部分范围
        float rangeUpY = progressY - THUMB_RADIUS * 2;
        //点击进度条上部分范围
        float rangeDownY = progressY + THUMB_RADIUS * 2;
        //计算点击到圆心距离，判断是否点击在拖动bar内
        if((downX > (progressStartX) && downY < progressStopX) && (downY > rangeUpY && downY < rangeDownY)) {
            isTouchProgress = true;
        } else {
            isTouchProgress = false;
        }
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setSliding(boolean sliding) {
        isSliding = sliding;
    }

    public void setProgressSecondBarColor(int progressSecondBarColor) {
        this.progressSecondBarColor = progressSecondBarColor;
        progressSecondBarPaint.setColor(progressSecondBarColor);
    }
}
