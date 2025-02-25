package com.ruyiruyi.rylibrary.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ruyiruyi.rylibrary.R;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, R.style.Dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, R.style.Dialog);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.Dialog);
    }

    protected abstract void onTouchOutside();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 触摸外部弹窗 */
        if (isOutOfBounds(getContext(), event)) {
            onTouchOutside();
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
}