package com.skyline.test.plan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by miao on 2017/5/13 16:16.
 */

public class PlanTabAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public PlanTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /** 返回一个用于显示ViewPager页面的Fragment */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /** 返回ViewPager页面数量 */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /** 返回ViewPager页面的标题，PagerSlidingTabStrip会调用这个方法获取标题并显示出来 */
    @Override
    public CharSequence getPageTitle(int position) {
        String simpleName = fragments.get(position).getClass().getSimpleName();
        if(simpleName.equals("LocalFragment")) {
            return "本地";
        }else {
            return "在线";
        }
    }
}
