package com.ljm.secondscrolldemo.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;

import com.ljm.secondscrolldemo.utills.ScrollStateValue;

/**
 * Created by ljm on 2017/10/20.
 */

public class ScrollLinearLayout extends LinearLayout {

    private int mContentViewState = ScrollStateValue.LISTVIEW_TOP_STATE;
    private ValueAnimator mTransitionAnim;
    private boolean mIsAnimPlay = false;
    private VelocityTracker mVelocityTracker;

    private PathInterpolator easeOutCubic;
    private PathInterpolator easeInCubic;

    private int mLayoutHeightMin = 200;
    private int mLayoutHeightMax = 400;
    private int mLayoutThresHold = 300;

    public ScrollLinearLayout(Context context) {
        this(context, null);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        easeOutCubic = new PathInterpolator(0.215f, 0.61f, 0.355f, 1);
        easeInCubic = new PathInterpolator(0.55f, 0.055f, 0.675f, 0.19f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果动画在播放或ListView在滑动时，不处理事件
        if (mIsAnimPlay
                || mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        //如果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final VelocityTracker vt = mVelocityTracker;

        return super.onTouchEvent(event);
    }
}
