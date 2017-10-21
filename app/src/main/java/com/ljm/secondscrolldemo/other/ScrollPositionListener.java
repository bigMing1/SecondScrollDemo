package com.ljm.secondscrolldemo.other;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by ljm on 2017/10/21.
 */

public class ScrollPositionListener implements AbsListView.OnScrollListener {

    public interface ScrollPositonCallback {
        public void execute(int state);
    }

    private ScrollPositonCallback mCallback;

    public ScrollPositionListener(ScrollPositonCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getFirstVisiblePosition() == 0) {
                View v = view.getChildAt(0);
                int y = v.getTop();
                if (y == view.getTop()) {
                    mCallback.execute(ScrollStateValue.LISTVIEW_TOP_STATE);
                    return;
                }
            } else if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = view.getChildAt(view.getChildCount() - 1);
                int y = v.getBottom();
                if (y == view.getBottom()) {
                    mCallback.execute(ScrollStateValue.LISTVIEW_BOTTOM_STATE);
                    return;
                }
            }
            mCallback.execute(ScrollStateValue.LISTVIEW_FLING_STATE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
