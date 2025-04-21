package com.hht.hsatellitemobile.ui.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hht.hsatellitemobile.MainActivity;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.ui.activity.TestActivity;
import com.hht.hsatellitemobile.utils.NotifyUtil;

/**
 * Created by 13589 on 2019/8/16.
 */

public class AlarmPointService extends Service {
    private MyReceiver myReceiver;
    private int requestCode = (int) SystemClock.uptimeMillis();

    public AlarmPointService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MsgBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("alarmPointService", "onStartCommand()");
        // 在API11之后构建Notification的方式
        Notification.Builder builder; //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification notification; // 获取构建好的Notification
        String channelId="com.hht.hsatellitemobile.alarm";

        //兼容8.0+
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
     //   NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      //      mChannel = new NotificationChannel(channelId, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
      //       notificationManager.createNotificationChannel(mChannel);
            builder = new Notification.Builder(this.getApplicationContext(),channelId);
            notification = new Notification.Builder(getApplicationContext(), channelId).build();

        }else{
            builder = new Notification.Builder(this.getApplicationContext());
            notification = builder.build();
        }
        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.satellitecon)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("新的火警提示") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("发现最新火警，请查看") //    + 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间*/
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);// 开始前台服务
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.osanwen.nettydemo.NettyService");
        this.registerReceiver(myReceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }


    }

    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, AlarmPointService.class); //销毁时重新启动Service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            this.startForegroundService(localIntent);
        } else {
            this.startService(localIntent);
        }
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知

    }

    private void AlarmNotice(String msg) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("新的火警提示")
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.satellitecon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.satellitecon))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);


    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 广播接收器
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object mm = bundle.get("alarmCount");

       //     Logger.d(mm);
            try {
                //JsonToEntity(mm);
                // String msg = "发现新的火警，共有" + mm.toString() + "条";
                String msg = "发现最新火警，请查看";
                notify_buttom(msg);

            } catch (Exception ex) {
            //    Logger.e(ex.getMessage());
            }

        }
    }

    public class MsgBinder extends Binder {
        public AlarmPointService getAlarmPointService() {
            return AlarmPointService.this;
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    /**
     * 高仿Android更新提醒样式
     */
    private void notify_buttom(String content) {
        //设置想要展示的数据内容
        String ticker = "新卫星热点提示";
        int smallIcon = R.drawable.satellitecon3;
        int lefticon = R.mipmap.android_leftbutton;
        String lefttext = "以后再说";
        Intent leftIntent = new Intent(this,TestActivity.class);
        leftIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //leftIntent.setAction(""+System.currentTimeMillis());
        leftIntent.putExtra("datacancel",0);

        PendingIntent leftPendIntent = PendingIntent.getActivity(this,
                requestCode, leftIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        int righticon = R.mipmap.android_rightbutton;
        String righttext = "查看";
        Intent rightIntent = new Intent(this, MainActivity.class);
        //是否推送信息，如果是推送信息，打开主界面时显示第一条信息

        //rightIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        rightIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        rightIntent.putExtra("isPushMsg", true);

        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                requestCode, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String titile = "发现新的卫星热点";

        //实例化工具类，并且调用接口
        NotifyUtil notify6 = new NotifyUtil(this, 1);
        notify6.notify_button(smallIcon, lefticon, lefttext, leftPendIntent, righticon, righttext, rightPendIntent, ticker, titile, content, true, true, false);
    }
}
