package com.android.bitglobal.ui.swipebacklayout;

/**
 * xiezuofei
 * 2016-06-24 16:20
 * 793169940@qq.com
 *
 */
public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    public abstract void scrollToFinishActivity();

}
