package com.hht.hsatellitemobile.tpns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hht.hsatellitemobile.MainActivity;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.jipush.BackgroundMp3Service;
import com.tencent.android.tpush.NotificationAction;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by geyang on 2020/10/16.
 */

public class MessageReceiver extends XGPushBaseReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onSetAccountResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteAccountResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

    }

    @Override
    public void onNotificationClickedResult(Context context, XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        Log.e(TAG, "onNotificationClickedResult: ");
        if (message.getActionType() == NotificationAction.clicked.getType()) {   // 通知在通知栏被点击   APP自己处理点击的相关动作
            //   context.startActivity(new Intent(context, MainActivity.class));
            Log.e(TAG, "onNotificationClickedResult: 通知被点击了" +message.getActivityName());
            Log.e(TAG, "onNotificationClickedResult: 通知被点击了" +message.getCustomContent());

            try {
                JSONObject content = new JSONObject(message.getCustomContent());
                String fire_id = content.getString("id");
                String ob_time = content.getString("time");
                String type = content.getString("type");

                Bundle bundle = new Bundle();
                Intent i = new Intent(context, MainActivity.class);
                bundle.putString("token",  new DbConfig(context).getUser().getToken());
                bundle.putString("START_TYPE", "JPUSH");
                bundle.putString("fire_id",fire_id);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

                Intent intent1= new Intent();
                intent1.setAction("fire_click");
                intent1.putExtra("fire_id",fire_id);
                context.sendBroadcast(intent1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Intent intenta = new Intent(context,BackgroundMp3Service.class);
        context.startService(intenta);
    }
}
