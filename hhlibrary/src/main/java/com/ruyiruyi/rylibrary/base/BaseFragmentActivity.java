package com.ruyiruyi.rylibrary.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;


public class BaseFragmentActivity extends FragmentActivity {



    private ActionBar a;
    private MyBaseActiviy_Broad oBaseActiviy_Broad;

    public BaseFragmentActivity(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            return;
        }else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_primary_top));
        }

        //动态注册广播
        oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        IntentFilter intentFilter = new IntentFilter("qd.haohai.baseActivity");
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
    protected void setContentView(int var1, int var2){
        this.setContentView(var1);
        if (var2 != -1){
            a = ((ActionBar) this.findViewById(var2));
            a.setBackgroundColor(getResources().getColor(R.color.theme_primary));
        }
    }


    protected void setActionBarTitle(ActionBar var1, String var2){
        if (var1 != null){
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

    public void showDialogProgress(ProgressDialog dialog, String message){
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }
    public void hideDialogProgress(ProgressDialog dialog){
        dialog.dismiss();
    }

    /*
  * 用户版Token错误跳转dialog
  * */
/*
    public void showUserTokenDialog(String error) {
        //解绑信鸽手机号
        XGPushManager.delAccount(getApplicationContext(),new DbConfig(getApplicationContext()).getPhone() );
        //反注册
        XGPushManager.unregisterPush(this);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //登录失效 更新本地User信息
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                User user = dbConfig.getUser();
                user.setIsLogin("0");
                DbManager db = dbConfig.getDbManager();

                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {

                }
                //即将跳转登录界面
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        dialog.show();
    }
*/


    /*
    * 判断是否登录 （未登录提示登录跳转）
    * 用法：
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }

    *
    *
    * */
  /*  public boolean judgeIsLogin() {
        if (!new DbConfig(getApplicationContext()).getIsLogin()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
            TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
            error_text.setText("您还没有登录");
            dialog.setTitle("如意如驿");
            dialog.setIcon(R.drawable.ic_logo_login);
            dialog.setView(dialogView);
            dialog.setNegativeButton("暂不登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton("前往登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
            dialog.show();
        }
        return new DbConfig(getApplicationContext()).getIsLogin();
    }*/
}