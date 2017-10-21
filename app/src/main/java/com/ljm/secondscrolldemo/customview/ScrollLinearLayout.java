package com.ljm.secondscrolldemo.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;

import com.ljm.secondscrolldemo.other.ScrollStateValue;

/**
 * Created by ljm on 2017/10/20.
 */

public class ScrollLinearLayout extends LinearLayout {

    private ViewGroup.LayoutParams mLayoutParams;
    private float mLastY = 0;
    private float mDeltY = 0;
    private float mCurrentVel = 0;

    private OnChangeListener mListener;
    private float currentScrollOffset = 0;//当前滚动偏移量

    private int mContentViewState = ScrollStateValue.LISTVIEW_TOP_STATE;
    private ValueAnimator mTranslationAnim;
    private boolean mIsAnimPlay = false;
    private VelocityTracker mVelocityTracker;

    private PathInterpolator easeOutCubic;

    private int mLayoutHeightMin = 0;
    private int mLayoutHeightMax = 0;
    private int mLayoutThresHold = 0;
    private int mCurrentScrollState = ScrollStateValue.SCROLL_MIN_STATE;

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

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public void setContentViewState(int state) {
        mContentViewState = state;
    }

    private void init() {
        mLayoutHeightMin = dpToPixel(200);
        mLayoutThresHold = dpToPixel(300);
        mLayoutHeightMax = dpToPixel(400);
        easeOutCubic = new PathInterpolator(0.215f, 0.61f, 0.355f, 1);
    }

    public interface OnChangeListener {
        public void onChange(float progress);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TAG", "ScrollLinearLayout的onTouchEvent执行...");
        //如果动画在播放或ListView在滑动时，不处理事件,不接受此事件的其它事件（move,up）
        if (mIsAnimPlay
                || mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        //获取VelocityTracker实例对象
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final VelocityTracker vt = mVelocityTracker;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                mDeltY = event.getY() - mLastY;
                handlerMove(mDeltY);
                vt.computeCurrentVelocity(1000);
                mCurrentVel = vt.getYVelocity();
                break;
            case MotionEvent.ACTION_UP:
                if (null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                startAnim(mCurrentVel);
                break;
            case MotionEvent.ACTION_CANCEL:
                if (null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("TAG", "ScrollLinearLayout的onIntercepTouchEvent执行...");
        if (mIsAnimPlay) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastY = ev.getY();
            mDeltY = 0;
        } else {
            mDeltY = ev.getY() - mLastY;
        }
        if (currentScrollOffset == ScrollStateValue.SCROLL_MIN_STATE) {
            return true;
        }
        if (currentScrollOffset == ScrollStateValue.SCROLL_MAX_STATE
                && mContentViewState == ScrollStateValue.LISTVIEW_TOP_STATE && mDeltY > 0) {
            return true;
        }
        if (mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void handlerMove(float delty) {
        mLayoutParams = getLayoutParams();
        mLayoutParams.height = (int) (mLayoutParams.height - delty);
        mLayoutParams.height = mLayoutParams.height > mLayoutHeightMax ? mLayoutHeightMax :
                mLayoutParams.height;
        mLayoutParams.height = mLayoutParams.height < mLayoutHeightMin ? mLayoutHeightMin :
                mLayoutParams.height;
        setLayoutParams(mLayoutParams);
        float progress = (float) (mLayoutParams.height - mLayoutHeightMin) * 100
                / (mLayoutHeightMax - mLayoutHeightMin);
        if (mListener != null && currentScrollOffset != progress) {
            mListener.onChange(progress);
        }
        currentScrollOffset = progress;
    }

    private void startAnim(float veloctity) {
        mIsAnimPlay = true;
        int startValue = 0;
        int endValue = 0;
        if (Math.abs(veloctity) < ScrollStateValue.SCROLL_VELOCITY_THRESHOLD) {
            if (mLayoutParams.height > mLayoutThresHold) {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMax;
            } else {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMin;
            }
        } else {
            if (mCurrentScrollState == ScrollStateValue.SCROLL_MIN_STATE) {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMax;
            } else {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMin;
            }
        }
        if (mTranslationAnim != null) {
            mTranslationAnim.cancel();
        }
        mTranslationAnim = ValueAnimator.ofInt(startValue, endValue);
        long duration = (long) ((float) Math.abs(startValue - endValue)
                / (float) Math.abs(mLayoutHeightMax - mLayoutThresHold) * ScrollStateValue.TRANSITION_DURATION_MAX);
        mTranslationAnim.setDuration(duration);
        mTranslationAnim.setInterpolator(easeOutCubic);
        mTranslationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLayoutParams.height = (int) animation.getAnimatedValue();
                setLayoutParams(mLayoutParams);
                float progress = (float) (mLayoutParams.height - mLayoutHeightMin)
                        * 100 / (mLayoutHeightMax - mLayoutHeightMin);

                if (mListener != null && currentScrollOffset != progress) {
                    mListener.onChange(progress);
                }
                currentScrollOffset = progress;
                invalidate();
            }
        });
        mTranslationAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimPlay = false;
                mCurrentScrollState = (int) currentScrollOffset;
            }
        });
        mTranslationAnim.start();
    }

    public int dpToPixel(float dpValue) {
        float m = getResources().getDisplayMetrics().density;
        return (int) (dpValue * m + 0.5);
    }
}
