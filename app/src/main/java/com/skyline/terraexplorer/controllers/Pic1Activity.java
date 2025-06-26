package com.skyline.terraexplorer.controllers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.skyline.terraexplorer.R;

import rx.functions.Action1;

public class Pic1Activity extends BaseActivity {
    private static final String TAG ="Pic1Activity" ;
    private String pic;
    private PhotoView yitijiImageShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic1);
        Intent intent = getIntent();
        pic = intent.getStringExtra("pic");
        Log.e(TAG, "onCreate: "+pic);
        initview();
    }

    private void initview() {
        yitijiImageShow = (PhotoView) findViewById(R.id.yitiji_image_show);
        Glide.with(getApplicationContext()).load(pic).into(yitijiImageShow);
        RxViewAction.clickNoDouble(yitijiImageShow)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
    }
}
