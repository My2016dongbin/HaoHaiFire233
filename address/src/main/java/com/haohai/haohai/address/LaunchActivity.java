package com.haohai.haohai.address;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.haohai.haohai.address.db.DbConfig;
import com.haohai.haohai.address.db.model.User;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class LaunchActivity extends BaseActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    private ImageView launchImage;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            goHome();
        }
    };

    // private SettingDialogPermision dialog_per;
    //获取手机类型
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    private AlertDialog.Builder builder;
    private ComponentName componentName = null;
    public boolean isHasPermission = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //初始化xUtils
        x.Ext.init(getApplication());
        x.Ext.setDebug(true);

        launchImage = (ImageView) findViewById(R.id.launch_image);

        //权限获取
        requestPower();

        //jumpStartInterface(this);







      /*  Glide.with(this).load(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(launchImage);*/

    }

    private void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
                Toast.makeText(LaunchActivity.this, "1111", Toast.LENGTH_SHORT).show();
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
            }
        } else {
            //   handler.sendEmptyMessageDelayed(GO_NEXT, GO_NEXT_TIME);
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       /* Log.e(TAG, "onRequestPermissionsResult:requestCode --" + requestCode);

        Log.e(TAG, "onRequestPermissionsResult: permissions--" + permissions.toString());
        Log.e(TAG, "onRequestPermissionsResult:  permissions.length--" +  permissions.length);
        Log.e(TAG, "onRequestPermissionsResult: grantResults--" + grantResults.toString());
        Log.e(TAG, "onRequestPermissionsResult: grantResults.length--" + grantResults.length);
*/
        for (int i = 0; i < permissions.length; i++) {

            Log.e(TAG, "onRequestPermissionsResult: permissions------" + permissions[i]);
        }


        if (requestCode == 1) {

            boolean isPremission = true;
            for (int i = 0; i < grantResults.length; i++) {
                Log.e(TAG, "onRequestPermissionsResult: permissions++++++" + grantResults[i]);
                if (grantResults[i] == -1) {
                    isPremission = false;
                }
                //  Log.e(TAG, "onRequestPermissionsResult: permissions++++++" +  grantResults[i]);
            }

            if (isPremission) {          //有权限
                handler.sendEmptyMessageDelayed(0, 3000);
            } else {
                judgePower();
            }
        }
    }

    private void judgePower() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();

        }
    }

    private void goHome() {
        User user = new DbConfig(this).getUser();
        if (user == null){
            User user1 = new User(1, "1", "Test", "123456", "11", "", "http://27.223.18.10:10172", "5", "1", "0");
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();
            try {
                db.saveOrUpdate(user1);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        //获得位置服务
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this, "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //跳转至授权页面
    public void jumpStartInterface(final Context context) {
        final Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());

            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                //componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
                //componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.ui.ram.RamActivity");// Permission Denial not exported from uid 1000，不允许被其他程序调用
                componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                //componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");//锁屏清理
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");//跳自启动管理
                //SettingOverlayView.show(context);
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                //componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");//跳转到手机管家
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.SmartBGActivity");//跳转到后台管理页面
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 将用户引导到系统设置页面
                if (Build.VERSION.SDK_INT >= 9) {
                    Log.e("HLQ_Struggle", "APPLICATION_DETAILS_SETTINGS");
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }

            builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("打开自启动权限")
                    .setMessage("为了能够及时的发现火警消息，请前往权限界面，打开自启动权限").setPositiveButton("确定前往", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            intent.setComponent(componentName);
                            context.startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            handler.sendEmptyMessageDelayed(0, 3000);
                        }
                    });
            builder.create().show();

         /*
            if (getMobileType().equals("Xiaomi")) {
                showtip();//显示弹窗（**特别注意**）
            }
            if (getMobileType().equals("samsung")){
            //    new SettingOverlayView().show(context);//显示悬浮窗
            }*/

        } catch (Exception e) {//抛出异常就直接打开设置页面
            Log.e("HLQ_Struggle", e.getLocalizedMessage());
            //  intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    //小米手机显示弹窗
    private void showtip() {
        try {
            // dialog_per=new SettingDialogPermision(context, R.style.CustomDialog4);
            //dialog_per.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);//注意这里改成吐司类型
            //dialog_per.show();
            Log.e("HLQ_Struggle", "显示弹窗");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HLQ_Struggle", "没有显示弹窗" + e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.sendEmptyMessageDelayed(0, 3000);
    }
}
