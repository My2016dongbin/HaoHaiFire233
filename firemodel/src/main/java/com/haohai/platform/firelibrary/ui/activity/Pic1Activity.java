package com.haohai.platform.firelibrary.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.haohai.platform.firelibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;

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
