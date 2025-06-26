package com.skyline.test.plan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.skyline.terraexplorer.R;
import com.skyline.test.others.PagerSlidingTabStrip;

import java.util.ArrayList;

public class PlanManagementActivity extends MatchParentActivity1 implements View.OnClickListener {

    private RelativeLayout back;
    private static final String TAG = "PlanManagementActivity";
    private PagerSlidingTabStrip tabs;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_plan_management);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.plan_back);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_plan_psts);
        vp = (ViewPager) findViewById(R.id.activity_plan_view_pager);
    }
    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        final LocalFragment localFragment = new LocalFragment();
        final OnlineFragment onlineFragment = new OnlineFragment();
        localFragment.setOnLocalDeleteListener(new LocalFragment.OnLocalDeleteListener() {
            @Override
            public void delete() {
                onlineFragment.initView();
                onlineFragment.initData();
            }
        });
        onlineFragment.setonDownloadListener(new OnlineFragment.OnDownloadListener() {
            @Override
            public void download() {
                localFragment.initView();
                localFragment.initData();
            }
        });
        fragments.add(localFragment);
        fragments.add(onlineFragment);
        vp.setAdapter(new PlanTabAdapter(getSupportFragmentManager(),
                fragments));
        tabs.setViewPager(vp); // 把ViewPager和Tabs进行关联
        initTabs();
    }


    private void initListener() {
       back.setOnClickListener(this);
    }

    /**
     * 初始化tab样式
     */
    private void initTabs() {
        tabs.setBackgroundColor(Color.parseColor("#131313"));			// 容器的背景
//        tabs.setTabBackground(0x55FF0000, 0x00000000);  // tab的背景：按下、正常状态的背景颜色
        tabs.setTabBackground(0xff888888, 0x00131313);  // tab的背景：按下、正常状态的背景颜色
        tabs.setTabTextColor(Color.WHITE, Color.WHITE);	// tab的字体颜色：选择、正常状态的字体颜色
        int size= (int)this.getResources().getDimension(R.dimen.tab_size);
        tabs.setTextSize(size);							// tab的字体大小
        tabs.setIndicatorColor(Color.GRAY);			// 滑动指示线的颜色
        tabs.setIndicatorHeight(6);						// 滑动指标器的高
        tabs.setUnderlineColor(Color.TRANSPARENT);				// tabs底部完整宽的线的颜色
        tabs.setUnderlineHeight(2);						// tabs底部完整宽的线的高
        tabs.setDividerColor(Color.WHITE);				// tab之间的分隔线的颜色
        tabs.setDividerPadding(8);						// 分隔线的顶部和底部的padding
        tabs.setTabPaddingLeftRight(8);					// 设置tab左右两边的padding
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plan_back:
                finish();
                break;
        }
    }
}
