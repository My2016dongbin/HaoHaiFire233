package com.ruyiruyi.rylibrary.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import java.util.Properties;

import rx.functions.Action1;

public class HomeTabsCell extends LinearLayout implements ViewPager.OnPageChangeListener{

    public static final int CELL_HEIGHT= HomeTabCell.CELL_HEIGHT;
    private Paint paint;
    private Context mContext;
    private ViewPager viewPager;

    public HomeTabsCell(Context context) {
        super(context);
        initView(context);
    }



    public HomeTabsCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeTabsCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {

        if (paint == null) {
            paint = new Paint();
            paint.setColor(0xffd9d9d9);
            paint.setStrokeWidth(2);
        }
        this.mContext = context;
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(CELL_HEIGHT), MeasureSpec.EXACTLY));
    }

    public void addView(int iconResId,CharSequence name){
        addView(iconResId, 0, name);
    }
    public void addView(int iconResId , int selectedIconResId,CharSequence name){
        final HomeTabCell cell = new HomeTabCell(getContext());
        cell.setClickable(true);
        cell.setBackgroundResource(R.drawable.list_selector);
        cell.setTag(getChildCount());
        addView(cell,new LinearLayout.LayoutParams(0, LayoutHelper.WRAP_CONTENT,1));
        cell.setValue(iconResId,selectedIconResId,name);
        RxViewAction.clickNoDouble(cell)
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        if (viewPager != null){
                            int index = Integer.parseInt(cell.getTag().toString());
                            viewPager.setCurrentItem(index);
                            setSelected(index);
                            Properties eventProperties = new Properties();
                            eventProperties.setProperty("home_tabs","tab" + (index + 1));
                        }
                    }
                });

    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);
        setWillNotDraw(false);
    }

    public void setSelected(int index){
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            HomeTabCell cell = (HomeTabCell) getChildAt(i);
            if (cell!=null){
                cell.select(i == index);
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0,1,getWidth(),1,paint);
    }
}
