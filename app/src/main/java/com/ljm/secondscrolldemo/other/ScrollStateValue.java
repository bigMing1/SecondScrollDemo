package com.ljm.secondscrolldemo.other;

/**
 * Created by ljm on 2017/10/20.
 * ScrollLinearLayout的状态值
 */

public class ScrollStateValue {

    //ListView滑到顶部
    public static final int LISTVIEW_TOP_STATE = 0;
    //ListView在滑动
    public static final int LISTVIEW_FLING_STATE = 1;
    //ListView滑到底部
    public static final int LISTVIEW_BOTTOM_STATE = 2;
    //滚动的最大高度
    public static final int SCROLL_MAX_STATE = 100;
    //滚动的最小进度
    public static final int SCROLL_MIN_STATE = 0;
    //平移的最大的持续时间
    public static final int TRANSITION_DURATION_MAX = 300;
    //滚动的速率阀值
    public static final int SCROLL_VELOCITY_THRESHOLD = 300;
}
