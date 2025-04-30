package com.ruyiruyi.rylibrary.ui;

import android.view.MotionEvent;

import com.ruyiruyi.rylibrary.ui.cell.WheelView;

public final class WheelViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final WheelView wheelView;

    public WheelViewGestureListener(WheelView wheelview) {
        wheelView = wheelview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
