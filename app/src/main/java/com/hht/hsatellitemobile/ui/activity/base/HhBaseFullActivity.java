package com.hht.hsatellitemobile.ui.activity.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hht.hsatellitemobile.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

public class HhBaseFullActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //导航栏沉浸式
        fullScreen(this);
    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /*
 * 用户版Token错误跳转dialog
 * */
   /* public void showUserTokenDialog(String error) {
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
    }*/


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
