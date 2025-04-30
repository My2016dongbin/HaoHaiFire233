package com.ruyiruyi.rylibrary.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public abstract class BaseActivity extends Activity {


    private ActionBar a;
    private MyBaseActiviy_Broad oBaseActiviy_Broad;

    public BaseActivity() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        } else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_primary_top));
        }

        //动态注册广播
       oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        IntentFilter intentFilter = new IntentFilter("haohai.haohai.baseActivity");
        registerReceiver(oBaseActiviy_Broad, intentFilter);
    }


    //定义一个广播
    public class MyBaseActiviy_Broad extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {
//接收发送过来的广播内容
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                finish();//销毁BaseActivity
            }
        }
    }

    //在销毁的方法里面注销广播


    protected void setContentView(int var1, int var2) {
        this.setContentView(var1);
        if (var2 != -1) {
            a = ((ActionBar) this.findViewById(var2));
            a.setBackgroundColor(getResources().getColor(R.color.theme_primary));
        }
    }

    protected void setActionBarTitle(ActionBar var1, String var2) {
        if (var1 != null) {
            var1.setTitle(var2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 21) {
            unregisterReceiver(oBaseActiviy_Broad);//注销广播
        }
    }

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    public void hideDialogProgress(ProgressDialog dialog) {
        dialog.dismiss();
    }
}
