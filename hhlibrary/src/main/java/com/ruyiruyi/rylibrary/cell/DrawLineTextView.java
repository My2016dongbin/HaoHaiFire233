package com.ruyiruyi.rylibrary.cell;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lenovo on 2019/1/7.
 */

public class DrawLineTextView extends TextView {
    public DrawLineTextView(Context context) {
        this(context, null);
    }

    public DrawLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }
}
