package com.skyline.terraexplorer.mainapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityJianDuBinding;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import rx.functions.Action1;

public class JianDuActivity extends AppCompatActivity {
    private LinearLayout diquLayout;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private static final String TAG = "JianDuActivity";
    private TextView qushitypeView;
    private ActivityJianDuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_jian_du);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jian_du);
        initView();
    }

    private void initView() {
        //diquLayout=findViewById(R.id.diqu_layout);
        qushitypeView=findViewById(R.id.qushitype_view);
        RxViewAction.clickNoDouble(binding.diquLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog();
                    }
                });
    }
    private void showLeibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"青岛市", "市南区","市北区","崂山区","李沧区","城阳区","即墨区","胶州市","西海岸","平度市","莱西市"};

        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("各区市")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //3"全部，0未开始，1执行中，2已结束")
                        if (choose1 == 0) {
                            qushitypeView.setText("青岛市");
                        } else if (choose1 == 1){
                            qushitypeView.setText("市南区");
                        }else if (choose1 == 2){
                            qushitypeView.setText("市北区");
                        }else if (choose1 == 3){
                            qushitypeView.setText("李沧区");
                        }

                    }
                });
        builder.create().show();
    }
}