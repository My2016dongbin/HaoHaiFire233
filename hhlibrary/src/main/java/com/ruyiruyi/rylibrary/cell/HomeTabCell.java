package com.ruyiruyi.rylibrary.cell;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

public class HomeTabCell extends LinearLayout {
    public static final int CELL_HEIGHT =AndroidUtilities.dp(150);
    private ImageView iconView;
    private TextView nameView;

    private int iconResId;
    private int selectedIconResId;

    public HomeTabCell(@NonNull Context context) {
        super(context);
        init(context);
    }


    public HomeTabCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeTabCell(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setOrientation(VERTICAL);
        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        addView(iconView, LayoutHelper.createLinear(AndroidUtilities.dp(75),AndroidUtilities.dp(75), Gravity.CENTER_HORIZONTAL,AndroidUtilities.dp(0),AndroidUtilities.dp(10),AndroidUtilities.dp(0),AndroidUtilities.dp(0)));

        nameView = new TextView(context);
        nameView.setTextColor(0xff424242);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        nameView.setLines(1);
        nameView.setMaxLines(1);
        nameView.setSingleLine(true);
        nameView.setGravity(Gravity.CENTER);
        addView(nameView,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,AndroidUtilities.dp(0),AndroidUtilities.dp(0),AndroidUtilities.dp(0),AndroidUtilities.dp(0)));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(CELL_HEIGHT), MeasureSpec.EXACTLY));

    }

    public void setValue(int iconResId , CharSequence name){
        this.iconResId = iconResId;
        this.selectedIconResId = 0;
        this.iconView.setImageResource(iconResId);
        this.nameView.setText(name);
    }

    public void setValue(int iconResId,int selectedIconResId,CharSequence name){
        this.iconResId = iconResId;
        this.selectedIconResId = selectedIconResId;
        this.iconView.setImageResource(iconResId);
        this.nameView.setText(name);
    }

    public void select(boolean isSelected){
        if (isSelected){
            if (selectedIconResId != 0){
                iconView.setImageResource(selectedIconResId);
            }
            //设置选中颜色
            iconView.setColorFilter(getResources().getColor(R.color.theme_primary));
            nameView.setTextColor(getResources().getColor(R.color.theme_primary));
        }else {
            if (selectedIconResId != 0){
                iconView.setImageResource(iconResId);
            }
            iconView.clearColorFilter();
            nameView.setTextColor(0xff424242);
        }
    }
}
