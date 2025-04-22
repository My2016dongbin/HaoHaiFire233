package com.ruyiruyi.rylibrary.cell;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruyiruyi.rylibrary.R;

import java.math.BigDecimal;

@SuppressWarnings("ResourceType")
public class NewRatingBar extends LinearLayout {
    private boolean mClickable;
    private int starCount;
    private NewRatingBar.OnRatingChangeListener onRatingChangeListener;
    private float starImageSize;
    private float starPadding;
    public float starStep;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable starHalfDrawable;
    private NewRatingBar.StepSize stepSize;

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void setOnRatingChangeListener(NewRatingBar.OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStepSize(NewRatingBar.StepSize stepSize) {
        this.stepSize = stepSize;
    }

    public NewRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.NewRatingBar);
        this.starImageSize = mTypedArray.getDimension(0, 20.0F);
        this.starPadding = mTypedArray.getDimension(1, 10.0F);
        this.starStep = mTypedArray.getFloat(7, 1.0F);
        this.stepSize = NewRatingBar.StepSize.fromStep(mTypedArray.getInt(8, 1));
        this.starCount = mTypedArray.getInteger(2, 5);
        this.starEmptyDrawable = mTypedArray.getDrawable(3);
        this.starFillDrawable = mTypedArray.getDrawable(4);
        this.starHalfDrawable = mTypedArray.getDrawable(5);
        this.mClickable = mTypedArray.getBoolean(6, true);
        mTypedArray.recycle();

        for(int i = 0; i < this.starCount; ++i) {
            final ImageView imageView = this.getStarImageView();
            imageView.setImageDrawable(this.starEmptyDrawable);
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(NewRatingBar.this.mClickable) {
                        int fint = (int)NewRatingBar.this.starStep;
                        BigDecimal b1 = new BigDecimal(Float.toString(NewRatingBar.this.starStep));
                        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
                        float fPoint = b1.subtract(b2).floatValue();
                        if(fPoint == 0.0F) {
                            --fint;
                        }

                        if(NewRatingBar.this.indexOfChild(v) > fint) {
                            NewRatingBar.this.setStar((float)(NewRatingBar.this.indexOfChild(v) + 1));
                        } else if(NewRatingBar.this.indexOfChild(v) == fint) {
                            if(NewRatingBar.this.stepSize == NewRatingBar.StepSize.Full) {
                                return;
                            }

                            if(imageView.getDrawable().getCurrent().getConstantState().equals(NewRatingBar.this.starHalfDrawable.getConstantState())) {
                                NewRatingBar.this.setStar((float)(NewRatingBar.this.indexOfChild(v) + 1));
                            } else {
                                NewRatingBar.this.setStar((float)NewRatingBar.this.indexOfChild(v) + 0.5F);
                            }
                        } else {
                            NewRatingBar.this.setStar((float)NewRatingBar.this.indexOfChild(v) + 1.0F);
                        }
                    }

                }
            });
            this.addView(imageView);
        }

        this.setStar(this.starStep);
    }

    private ImageView getStarImageView() {
        ImageView imageView = new ImageView(this.getContext());
        LayoutParams layout = new LayoutParams(Math.round(this.starImageSize), Math.round(this.starImageSize));
        layout.setMargins(0, 0, Math.round(this.starPadding), 0);
        imageView.setLayoutParams(layout);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(this.starEmptyDrawable);
        imageView.setMinimumWidth(10);
        imageView.setMaxHeight(10);
        return imageView;
    }

    public void setStar(float rating) {
        if(this.onRatingChangeListener != null) {
            this.onRatingChangeListener.onRatingChange(rating);
        }

        this.starStep = rating;
        int fint = (int)rating;
        BigDecimal b1 = new BigDecimal(Float.toString(rating));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        float fPoint = b1.subtract(b2).floatValue();

        int i;
        for(i = 0; i < fint; ++i) {
            ((ImageView)this.getChildAt(i)).setImageDrawable(this.starFillDrawable);
        }

        for(i = fint; i < this.starCount; ++i) {
            ((ImageView)this.getChildAt(i)).setImageDrawable(this.starEmptyDrawable);
        }

        if(fPoint > 0.0F) {
            ((ImageView)this.getChildAt(fint)).setImageDrawable(this.starHalfDrawable);
        }

    }

    public static enum StepSize {
        Half(0),
        Full(1);

        int step;

        private StepSize(int step) {
            this.step = step;
        }

        public static NewRatingBar.StepSize fromStep(int step) {
            NewRatingBar.StepSize[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                NewRatingBar.StepSize f = var1[var3];
                if(f.step == step) {
                    return f;
                }
            }

            throw new IllegalArgumentException();
        }
    }

    public interface OnRatingChangeListener {
        void onRatingChange(float var1);
    }
}