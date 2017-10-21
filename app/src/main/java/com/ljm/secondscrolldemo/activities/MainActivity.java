package com.ljm.secondscrolldemo.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ljm.secondscrolldemo.R;
import com.ljm.secondscrolldemo.customview.ScrollLinearLayout;
import com.ljm.secondscrolldemo.other.ScrollPositionListener;
import com.ljm.secondscrolldemo.other.ScrollStateValue;

public class MainActivity extends AppCompatActivity {

    private ScrollLinearLayout mScrollLinearLayout;
    private ListView mListView;
    private float viewLayoutHeight;
    private float viewLayoutWidth;
    private LinearLayout mTopLinearLayout;

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mScrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.scroll_ll);
        mListView = (ListView) findViewById(R.id.lst);
        mTopLinearLayout = (LinearLayout) findViewById(R.id.bg_linearlayout);
        //适配不同分辨率机器，调整view宽高
        ViewTreeObserver vto = mTopLinearLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTopLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                viewLayoutWidth = mTopLinearLayout.getWidth();
                viewLayoutHeight = mTopLinearLayout.getHeight();
            }
        });

        mScrollLinearLayout.setOnChangeListener(new ScrollLinearLayout.OnChangeListener() {
            @Override
            public void onChange(float progress) {
                ViewGroup.LayoutParams layoutParams = mTopLinearLayout.getLayoutParams();

                layoutParams.height = (int) (viewLayoutHeight - dpToPixel(getApplicationContext(), progress * 2));
                layoutParams.width = (int) viewLayoutWidth;
                mTopLinearLayout.setLayoutParams(layoutParams);
            }
        });

        int dataCount = 30;
        data = new String[dataCount];
        for (int i = 0; i < dataCount; i++) {
            data[i] = "Item" + i;
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        mListView.setOnScrollListener(new ScrollPositionListener(new ScrollPositionListener.ScrollPositonCallback() {
            @Override
            public void execute(int state) {
                switch (state) {
                    case ScrollStateValue.LISTVIEW_TOP_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_TOP_STATE);
                        break;
                    case ScrollStateValue.LISTVIEW_BOTTOM_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_BOTTOM_STATE);
                        break;
                    case ScrollStateValue.LISTVIEW_FLING_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_FLING_STATE);
                        break;
                    default:
                        break;
                }
            }
        }));
    }

    private int dpToPixel(Context context, float dpValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * m + 0.5);
    }
}



