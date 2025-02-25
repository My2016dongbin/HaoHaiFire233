package com.hht.hsatellitemobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.hht.hsatellitemobile.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import rx.functions.Action1;

public class PicActivity extends BaseActivity {
    private static final String TAG ="PicActivity" ;
    private String pic;
    private PhotoView yitijiImageShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        Intent intent = getIntent();
        pic = intent.getStringExtra("pic");
        Log.e(TAG, "onCreate: "+pic);
        initview();
    }

    private void initview() {
        yitijiImageShow = (PhotoView) findViewById(R.id.yitiji_image_show);
        Glide.with(getApplicationContext()).load(pic)
                .error(R.drawable.ic_no_pic)
                .placeholder(R.drawable.ic_jaizai).into(yitijiImageShow);
        RxViewAction.clickNoDouble(yitijiImageShow)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
    }
}
