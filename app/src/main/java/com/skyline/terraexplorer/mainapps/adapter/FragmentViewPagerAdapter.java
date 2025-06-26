package com.skyline.terraexplorer.mainapps.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class FragmentViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<Fragment> a;
    private FragmentManager b;
    private int c = 0;
    private com.skyline.terraexplorer.mainapps.adapter.FragmentViewPagerAdapter.OnExtraPageChangeListener d;

    public FragmentViewPagerAdapter(FragmentManager var1, List<Fragment> var2) {
        this.a = var2;
        this.b = var1;
    }

    public int getCount() {
        return this.a.size();
    }

    public boolean isViewFromObject(View var1, Object var2) {
        return var1 == var2;
    }

    public void destroyItem(ViewGroup var1, int var2, Object var3) {
        var1.removeView(((Fragment)this.a.get(var2)).getView());
    }

    public Object instantiateItem(ViewGroup var1, int var2) {
        Fragment var3 = (Fragment)this.a.get(var2);
        if(!var3.isAdded()) {
            FragmentTransaction var4 = this.b.beginTransaction();
            var4.add(var3, var3.getClass().getSimpleName());
            var4.commit();
            this.b.executePendingTransactions();
        }

        if(var3.getView().getParent() == null) {
            var1.addView(var3.getView());
        }

        return var3.getView();
    }

    public int getCurrentPageIndex() {
        return this.c;
    }

    public com.skyline.terraexplorer.mainapps.adapter.FragmentViewPagerAdapter.OnExtraPageChangeListener getOnExtraPageChangeListener() {
        return this.d;
    }

    public void setOnExtraPageChangeListener(com.skyline.terraexplorer.mainapps.adapter.FragmentViewPagerAdapter.OnExtraPageChangeListener var1) {
        this.d = var1;
    }

    public void onPageScrolled(int var1, float var2, int var3) {
        if(null != this.d) {
            this.d.onExtraPageScrolled(var1, var2, var3);
        }

    }

    public void onPageSelected(int var1) {
        ((Fragment)this.a.get(this.c)).onPause();
        if(((Fragment)this.a.get(var1)).isAdded()) {
            ((Fragment)this.a.get(var1)).onResume();
        }

        this.c = var1;
        if(null != this.d) {
            this.d.onExtraPageSelected(var1);
        }

    }

    public void onPageScrollStateChanged(int var1) {
        if(null != this.d) {
            this.d.onExtraPageScrollStateChanged(var1);
        }

    }

    public Fragment getFragmentItem(int var1) {
        return (Fragment)this.a.get(var1);
    }

    public interface OnExtraPageChangeListener {
        void onExtraPageScrolled(int var1, float var2, int var3);

        void onExtraPageSelected(int var1);

        void onExtraPageScrollStateChanged(int var1);
    }
}