package com.ruyiruyi.rylibrary.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.ruyiruyi.rylibrary.R;

public class CustomEditText extends EditText {
    private EditText edit;
    private Paint mPaint;
    private Rect mBound;
    private int editWidth;
    private int editHeight;
    private int numLength;
    private String numStr;
    private int numSize;

    public CustomEditText(Context context) {
        this(context, (AttributeSet)null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.numLength = 200;
        this.numStr = "0    /" + this.numLength;
        this.numSize = 30;
        this.edit = this;
        this.edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.numLength)});
        this.mPaint = new Paint();
        int colorGray = this.getResources().getColor(R.color.c2);
        this.mPaint.setColor(colorGray);
        this.mPaint.setTextSize((float)this.numSize);
        this.mBound = new Rect();
        this.mPaint.getTextBounds(this.numStr, 0, this.numStr.length(), this.mBound);
        this.edit.setPadding(10, 0, 10, 20);
        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                CustomEditText.this.edit.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                CustomEditText.this.editWidth = CustomEditText.this.edit.getWidth();
                CustomEditText.this.editHeight = CustomEditText.this.edit.getHeight();
            }
        });
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(this.numStr, (float)(this.editWidth - this.mBound.width() - 10), (float)(this.editHeight - this.mBound.height() - 5), this.mPaint);
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        this.editHeight = this.getHeight();
        int num = text.length();
        this.numStr = num + "/" + this.numLength;
        this.postInvalidate();
        this.invalidate();
    }

    public void setEditHint(String str) {
        this.edit.setHint(str);
        this.requestLayout();
    }
}