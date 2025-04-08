package com.ruyiruyi.rylibrary.cell;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import rx.functions.Action1;

public class ActionBar extends FrameLayout {

    private  FrameLayout content;
    private  TextView titleView;
    private ImageView backButtonImageView;
    private TextView titleTextView;
    private TextView subTitleTextView;
    private View actionModeTop;
 //   private ActionBarMenu menu;
   // private ActionBarMenu actionMode;
    private boolean occupyStatusBar;
    private boolean allowOverlayTitle;
    private CharSequence lastTitle;
    private boolean castShadows;
    protected boolean isSearchFieldVisible;
    protected int itemsBackgroundResourceId;
    private boolean isBackOverlayVisible;
    public ActionBar.ActionBarMenuOnItemClick actionBarMenuOnItemClick;
    private int extraHeight;
    private static Paint paint;
    private boolean needDivider;
    private boolean isSmallStyle;
    private ImageView imageView;
    public OnRightImageClick listener;
    private TextView rightView;
    private FrameLayout backgroundLayout;

    public void setListener(OnRightImageClick listener) {
        this.listener = listener;
    }

    public void setActionBarMenuOnItemClick(ActionBar.ActionBarMenuOnItemClick var1){
        this.actionBarMenuOnItemClick = var1;
    }

    public void onBackItemClick(){
        if (this.actionBarMenuOnItemClick != null){
            this.actionBarMenuOnItemClick.onItemClick(-1);
        }
    }


    public ActionBar(Context context) {
        super(context);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        content = new FrameLayout(context);
        content.setBackgroundColor(0xff283345);
        addView(content,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,200));

        backgroundLayout = new FrameLayout(context);
        content.addView(backgroundLayout, LayoutHelper.createFrame(200,LayoutHelper.MATCH_PARENT,Gravity.CENTER_VERTICAL,20,0,0,0));

        backButtonImageView = new ImageView(context);
        backButtonImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        backButtonImageView.setImageResource(R.drawable.ic_back);
        backgroundLayout.addView(backButtonImageView, LayoutHelper.createFrame(60,60,Gravity.CENTER_VERTICAL,20,40,0,0));

        RxViewAction.clickNoDouble(backgroundLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ActionBar.this.actionBarMenuOnItemClick.onItemClick(-1);
                    }
                });


        titleView = new TextView(context);
        titleView.setTextSize(17);
        titleView.setText("");
        titleView.setTextColor(Color.WHITE);
        content.addView(titleView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER,0,40,0,0));

        imageView = new ImageView(context);
        imageView.setVisibility(GONE);
        content.addView(imageView,LayoutHelper.createFrame(60,60,Gravity.CENTER_VERTICAL|Gravity.RIGHT,0,40,40,0));
        RxViewAction.clickNoDouble(imageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ActionBar.this.actionBarMenuOnItemClick.onItemClick(-2);
                       // listener.onRightItemClickListener();
                    }
                });

        rightView = new TextView(context);
        rightView.setVisibility(GONE);
        rightView.setTextColor(Color.WHITE);

        content.addView(rightView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER_VERTICAL|Gravity.RIGHT,0,40,20,0));
        RxViewAction.clickNoDouble(rightView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ActionBar.this.actionBarMenuOnItemClick.onItemClick(-3);
                    }
                });
    }

    public void setBackground(int color){
        content.setBackgroundColor(color);
    }
    public void setRightImage(int imageId){
        imageView.setVisibility(VISIBLE);
        imageView.setImageResource(imageId);
    }
    public void setRightView(String rightStr){
        rightView.setText(rightStr);
        rightView.setVisibility(VISIBLE);
    }

    public void setColor(int color){
        content.setBackgroundColor(color);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }
    public void setShowBackView(boolean isShow){
        if (isShow){
            backButtonImageView.setVisibility(VISIBLE);
        }else {
            backButtonImageView.setVisibility(GONE);
        }
    }


    public static class ActionBarMenuOnItemClick{
        public ActionBarMenuOnItemClick(){

        }
        public void onItemClick(int var1){}

      // public void onRightClick(){}
    }

    public interface OnRightImageClick{
        void onRightItemClickListener();
    }




}