package com.hht.hsatellitemobile.ui.activity.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hht.hsatellitemobile.MainActivity;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.hht.hsatellitemobile.MyApplication;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.db.model.User;
import com.hht.hsatellitemobile.ui.activity.LoginActivity;
import com.tencent.android.tpush.XGPushManager;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import static com.hht.hsatellitemobile.ui.activity.LoginActivity.yingjiTags;


public class HhBaseActivity extends BaseActivity {
    private MyApplication application;
    private BaseActivity oContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hh_base);
        if (application == null) {
            // 得到Application对象
            application = (MyApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }


    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用myApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*
     * 用户版Token错误跳转dialog
     * */
    public void showUserTokenDialog(String error) {
      //  XGPushManager.delAccount(getApplicationContext(),"geyang");
     //   XGPushManager.unregisterPush(this);
        //*/解绑信鸽手机号
        /*XGPushManager.delAccount(getApplicationContext(),new DbConfig(getApplicationContext()).getPhone() );
        //反注册
        XGPushManager.unregisterPush(this);*/

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("慧眼卫星");
        dialog.setIcon(R.mipmap.ic_launcher);
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
              //  JPushInterface.cleanTags(getApplicationContext(),1001);
                //登录失效 更新本地User信息
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                User user = dbConfig.getUser();
                user.setIsLogin("0");
                DbManager db = dbConfig.getDbManager();

                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {

                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                //即将跳转登录界面
            //    finish();

            }
        });
        dialog.show();
    }


    public void outLoginDialog(String msg) {
      //  XGPushManager.delAccount(getApplicationContext(),"geyang");
     //   XGPushManager.unregisterPush(this);
        //*/解绑信鸽手机号
        /*XGPushManager.delAccount(getApplicationContext(),new DbConfig(getApplicationContext()).getPhone() );
        //反注册
        XGPushManager.unregisterPush(this);*/

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(msg);
        dialog.setTitle("慧眼卫星");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent1= new Intent();
                intent1.setAction("out_login");
                sendBroadcast(intent1);

                cleanTags();
                //JPushInterface.cleanTags(getApplicationContext(),1001);
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
              //  finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }

    private void cleanTags() {
        try{
            String username = new DbConfig(this).getUser().getUsername();
            if(username!=null && username.equals("山东省应急管理厅")){
                String[] tags = yingjiTags.split(",");
                for (int i = 0; i < tags.length; i++) {
                    XGPushManager.cleanTags(this,tags[i]);
                }
            }else{
                XGPushManager.cleanTags(this,new DbConfig(this).getUser().getPushTag());
            }
        }catch (Exception e){
            Log.e("Exception", "cleanTags");
        }
    }


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
   /* public boolean judgeIsLogin() {
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
    }
*/
    /*
    * 错误提示dialog
    * */
    public void showMerchantErrorDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("慧眼卫星");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }
}
