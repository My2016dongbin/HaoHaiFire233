package com.hht.hsatellitemobile.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;

import rx.functions.Action1;

public class RegisterActivity extends HhBaseActivity {

    private LinearLayout zhengfuLayout;
    private LinearLayout gongsiLayout;
    private LinearLayout gerenLayout;
    private ImageView zhengfuImage;
    private ImageView gongsiImage;
    private ImageView gerenImage;
    public int jigouChoose = 0;  //  0 公司  1政府  2是个人
    public int diquChoose = 0;  //  0 省  1市  2县
    private LinearLayout shengLayout;
    private LinearLayout shiLayout;
    private LinearLayout xianLayout;
    private ImageView shengImage;
    private ImageView shiImage;
    private ImageView xianImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        zhengfuLayout = (LinearLayout) findViewById(R.id.zhengfu_layout);
        gongsiLayout = (LinearLayout) findViewById(R.id.gongsi_layout);
        gerenLayout = (LinearLayout) findViewById(R.id.geren_layout);
        zhengfuImage = (ImageView) findViewById(R.id.zhengfu_choose_image);
        gongsiImage = (ImageView) findViewById(R.id.gongsi_choose_image);
        gerenImage = (ImageView) findViewById(R.id.gerenchoose_image);
        shengLayout = (LinearLayout) findViewById(R.id.sheng_layout);
        shiLayout = (LinearLayout) findViewById(R.id.shi_layout);
        xianLayout = (LinearLayout) findViewById(R.id.xian_layout);
        shengImage = (ImageView) findViewById(R.id.sheng_image);
        shiImage = (ImageView) findViewById(R.id.shi_image);
        xianImage = (ImageView) findViewById(R.id.xian_image);
        RxViewAction.clickNoDouble(shengLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        diquChoose = 0;
                        initDiquChooseImage();
                    }
                });

        RxViewAction.clickNoDouble(shiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        diquChoose = 1;
                        initDiquChooseImage();
                    }
                });

        RxViewAction.clickNoDouble(xianLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        diquChoose = 2;
                        initDiquChooseImage();
                    }
                });

        RxViewAction.clickNoDouble(gongsiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jigouChoose = 0;
                        initJiGouChooseImage();
                    }
                });

        RxViewAction.clickNoDouble(zhengfuLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jigouChoose = 1;
                        initJiGouChooseImage();
                    }
                });

        RxViewAction.clickNoDouble(gerenLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        jigouChoose = 2;
                        initJiGouChooseImage();
                    }
                });
    }

    private void initDiquChooseImage() {
        if (diquChoose == 0){
            shengImage.setImageResource(R.drawable.choose);
            shiImage.setImageResource(R.drawable.choose_no);
            xianImage.setImageResource(R.drawable.choose_no);
        }else if (diquChoose == 1){
            shengImage.setImageResource(R.drawable.choose_no);
            shiImage.setImageResource(R.drawable.choose);
            xianImage.setImageResource(R.drawable.choose_no);
        }else {
            shengImage.setImageResource(R.drawable.choose_no);
            shiImage.setImageResource(R.drawable.choose_no);
            xianImage.setImageResource(R.drawable.choose);
        }
    }

    private void initJiGouChooseImage() {
        if (jigouChoose == 0){  //公司
            gongsiImage.setImageResource(R.drawable.choose);
            zhengfuImage.setImageResource(R.drawable.choose_no);
            gerenImage.setImageResource(R.drawable.choose_no);

        }else if (jigouChoose == 1){ //政府
            gongsiImage.setImageResource(R.drawable.choose_no);
            zhengfuImage.setImageResource(R.drawable.choose);
            gerenImage.setImageResource(R.drawable.choose_no);
        }else {  //个人
            gongsiImage.setImageResource(R.drawable.choose_no);
            zhengfuImage.setImageResource(R.drawable.choose_no);
            gerenImage.setImageResource(R.drawable.choose);
        }
    }
}
