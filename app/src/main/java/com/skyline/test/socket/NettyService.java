package com.skyline.test.socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.bean.MessageBuf;
import com.bean.PlottingItem;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.controllers.TEMainActivity;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.test.utils.Constance;
import com.skyline.test.utils.Protocol;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static android.content.ContentValues.TAG;

/**
 *
 * Created by LiuSaibao on 11/17/2016.
 */
public class NettyService extends Service implements NettyListener {

    private NetworkReceiver receiver;
    private static String sessionId = null;

    private ScheduledExecutorService mScheduledExecutorService;
    private void shutdown() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


        receiver = new NetworkReceiver();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        // 自定义心跳，每隔20秒向服务器发送心跳包
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                //byte[] requestBody = {(byte) 0xFE, (byte)0xED, (byte)0xFE, 5};
                NettyClient.getInstance().sendMsgToServer(Protocol.generateHeartbeat().build(), new ChannelFutureListener() {    //3
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {
                            //
                            Log.d(TAG, "operationComplete: 写入成功");

                        } else {
                            Log.d(TAG, "operationComplete: 写入失败");

                        }
                    }
                });
            }
        }, 20, 20, TimeUnit.SECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NettyClient.getInstance().setListener(this);
        connect();
        return START_NOT_STICKY;
    }

    @Override
    public void onMessageResponse(Object byteBuf) {
        if (byteBuf instanceof MessageBuf.JMTransfer)
        {
            MessageBuf.JMTransfer jm= (MessageBuf.JMTransfer)byteBuf;
            Intent intent=new Intent();
            intent.putExtra("count", jm);
            intent.setAction("com.osanwen.nettydemo.NettyService");
            sendBroadcast(intent);
            Log.d(TAG, "onMessageResponse: "+jm.toString());
            if (jm.getCmd()==30000)
            {

            }

        }


    }




    @Override
    public void onServiceStatusConnectChanged(int statusCode) {		//连接状态监听
        if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {
            //authenticData();
        } else {

        }
    }

    /**
     * 认证数据请求
     */




    private void handle(int t, int i, int f) {
        // TODO 实现自己的业务逻辑
    }

    private void connect(){


        if (!NettyClient.getInstance().getConnectStatus()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyClient.getInstance().connect();//连接服务器
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        shutdown();
        NettyClient.getInstance().setReconnectNum(0);
        NettyClient.getInstance().disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                        || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    connect();
                }
            }
        }
    }
}
