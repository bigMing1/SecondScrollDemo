package com.ljm.secondscrolldemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ljm.secondscrolldemo.R;
import com.ljm.secondscrolldemo.customview.ScrollLinearLayout;

public class MainActivity extends AppCompatActivity {

    private ScrollLinearLayout mScrollLinearLyaout;
    private LinearLayout mLinearLayout;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mScrollLinearLyaout = (ScrollLinearLayout) findViewById(R.id.scroll_ll);
        mLinearLayout = (LinearLayout) findViewById(R.id.bg_linearlayout);
        mListView = (ListView) findViewById(R.id.lst);
    }

}
