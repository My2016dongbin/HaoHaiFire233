package com.ruyiruyi.rylibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/8/9 14:29
 */

public class RyLiaTransparentDialog extends ProgressDialog {
    private TextView txt;
    private String content;

    private Context mContext;

    private ImageView pop_close;

    public RyLiaTransparentDialog(Context context, String content) {
        super(context, R.style.myProgressDialog);//去除白色背景
        this.mContext = context;
        this.content = content;
        setCanceledOnTouchOutside(false);
        /*//去除半透明灰色背景
        getWindow().setDimAmount(0f);*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transparent_dialog);
        initView();
        bindView();
    }

    private void bindView() {
        //关闭提示框
        RxViewAction.clickNoDouble(pop_close).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    private void initView() {
        pop_close = (ImageView) findViewById(R.id.pop_close);
        txt = (TextView) findViewById(R.id.txt);

        txt.setText(content);
    }


}
