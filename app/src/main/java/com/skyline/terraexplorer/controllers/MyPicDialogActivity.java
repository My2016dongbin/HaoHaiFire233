package com.skyline.terraexplorer.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.*;

public class MyPicDialogActivity extends com.skyline.terraexplorer.controllers.base.BaseActivity {

    private ImageView iv;
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        imgUrl = "";
        imgUrl = bundle.getString("imgUrl");
        RelativeLayout layout = getLayoutView();
        setContentView(layout);


        //设置窗口对其屏幕宽度
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP;//置顶显示
        window.setAttributes(lp);
    }

    public RelativeLayout getLayoutView() {
        RelativeLayout v = new RelativeLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        iv = new ImageView(this);
        WindowManager.LayoutParams ivlp = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(ivlp);
        Glide.with(this).load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        v.addView(iv);
        return v;
    }
}
