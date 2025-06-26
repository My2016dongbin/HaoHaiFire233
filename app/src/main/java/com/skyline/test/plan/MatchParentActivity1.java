package com.skyline.test.plan;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by miao on 2017/5/16 9:24.
 * 支持全屏的可用于FragmentActivity的界面
 */

public class MatchParentActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void setContentView (int layoutResID)
    {
        super.setContentView(layoutResID);
        getWindow().setBackgroundDrawable(null);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void setContentView (View view)
    {
        super.setContentView(view);
        getWindow().setBackgroundDrawable(null);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void setContentView (View view, ViewGroup.LayoutParams layoutParams)
    {
        super.setContentView(view,layoutParams);
        getWindow().setBackgroundDrawable(null);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
