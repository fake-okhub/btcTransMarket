package com.android.bitglobal.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by joyson on 2017/8/14.
 */

public class NoScrollViewpager extends ViewPager {
    public NoScrollViewpager(Context context) {
        super(context);
    }

    public NoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isPagerCanScroll = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagerCanScroll && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagerCanScroll && super.onInterceptTouchEvent(event);
    }

    /**
     * 设置 viewpager 是否屏蔽滑动事件
     * @param pagerCanScroll true要屏蔽  false 不屏蔽
     */
    public void setPagerCanScroll(boolean pagerCanScroll) {
        isPagerCanScroll = pagerCanScroll;
    }
}
