package com.ruyiruyi.rylibrary.image.ui;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.DrawableUtils;
import com.ruyiruyi.rylibrary.R;

public class ImageProgressBarDrawable extends Drawable {
    private final Paint mPaint = new Paint(1);
    private int mBackgroundColor = -2147483648;
    private int mColor = -6381922;
    private int mPadding;
    private int mBarWidth;
    private int mLevel = 0;
    private boolean mHideWhenZero = false;

    public ImageProgressBarDrawable(Context context) {
        Resources resources = context.getResources();
        this.mPadding = resources.getDimensionPixelOffset(R.dimen.ryry_images_progress_bar_padding);
        this.mBarWidth = resources.getDimensionPixelOffset(R.dimen.ryry_images_progress_bar_width);
    }

    public void setColor(int color) {
        if(this.mColor != color) {
            this.mColor = color;
            this.invalidateSelf();
        }

    }

    public int getColor() {
        return this.mColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        if(this.mBackgroundColor != backgroundColor) {
            this.mBackgroundColor = backgroundColor;
            this.invalidateSelf();
        }

    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setPadding(int padding) {
        if(this.mPadding != padding) {
            this.mPadding = padding;
            this.invalidateSelf();
        }

    }

    public boolean getPadding(Rect padding) {
        padding.set(this.mPadding, this.mPadding, this.mPadding, this.mPadding);
        return this.mPadding != 0;
    }

    public void setBarWidth(int barWidth) {
        if(this.mBarWidth != barWidth) {
            this.mBarWidth = barWidth;
            this.invalidateSelf();
        }

    }

    public int getBarWidth() {
        return this.mBarWidth;
    }

    public void setHideWhenZero(boolean hideWhenZero) {
        this.mHideWhenZero = hideWhenZero;
    }

    public boolean getHideWhenZero() {
        return this.mHideWhenZero;
    }

    protected boolean onLevelChange(int level) {
        this.mLevel = level;
        this.invalidateSelf();
        return true;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(this.mPaint.getColor());
    }

    public void draw(Canvas canvas) {
        if(!this.mHideWhenZero || this.mLevel != 0) {
            this.drawBar(canvas, 10000, this.mBackgroundColor);
            this.drawBar(canvas, this.mLevel, this.mColor);
        }
    }

    private void drawBar(Canvas canvas, int level, int color) {
        Rect bounds = this.getBounds();
        int length = (bounds.width() - 2 * this.mPadding) * level / 10000;
        int xpos = bounds.left + this.mPadding;
        int ypos = bounds.bottom - this.mPadding - this.mBarWidth;
        this.mPaint.setColor(color);
        canvas.drawRect((float)xpos, (float)ypos, (float)(xpos + length), (float)(ypos + this.mBarWidth), this.mPaint);
    }
}

