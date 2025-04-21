package com.ruyiruyi.rylibrary.cell;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

/**
 * 带滚动监听的CoordinatorLayout
 *
 */
public class GradationNoInterceptCollapsingToolbarLayout extends CollapsingToolbarLayout {
    private int downX, downY;
    private int mTouchSlop;

    public interface ScrollViewListener {
        void onScrollChanged(GradationNoInterceptCollapsingToolbarLayout scrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public GradationNoInterceptCollapsingToolbarLayout(Context context) {
        super(context);
    }

    public GradationNoInterceptCollapsingToolbarLayout(Context context, AttributeSet attrs,
                                                       int defStyle) {
        super(context, attrs, defStyle);
    }

    public GradationNoInterceptCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradationNoInterceptCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }



    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                // 判断是否滑动，若滑动就拦截事件
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }*/


}