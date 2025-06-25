package com.haohai.platform.fireforestplatform.ui.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ruyiruyi.rylibrary.service.BackgroundMp3Service;
import com.tencent.android.tpush.NotificationAction;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by geyang on 2020/11/24.
 *
 * //推送类型：1：人员上报火警任务下发,2：林业一体机报警任务下发,3：卫星火警任务下发,4：海洋报警任务下发,5国土报警任务下发
 //11：发现新人员上报火警，12：发现林业一体机新火警,13：发现卫星新火警,14：发现海洋新火警,15：发现国土新报警
 */

public class TengxunReceiver extends XGPushBaseReceiver{
    private static final String TAG = TengxunReceiver.class.getSimpleName();


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

    /**
     * 消息点击回调
     * @param context
     */
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

            //type 2一体机林业 3一体机国土    4是卫星
            try {
                JSONObject content = new JSONObject(message.getCustomContent());
                String fire_id = content.getString("id");
                String ob_time = content.getString("time");
                String type = content.getString("type");
                Intent intent= new Intent();
                intent.setAction("fire_weixing_tengxun");
                intent.putExtra("sele","新火警");
                intent.putExtra("fire_id",fire_id);
                intent.putExtra("ob_time",ob_time);
                intent.putExtra("type",type);
                context.sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 通知栏接受报警
     * @param context
     */
    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult message) {

        JSONObject content = null;
        try {
            content = new JSONObject(message.getCustomContent());
            String fire_id = content.getString("id");
            String ob_time = content.getString("time");
            String type = content.getString("type");
            Log.e(TAG, "onNotificationShowedResult: " +type);
            Intent intenta = new Intent(context,BackgroundMp3Service.class);
            intenta.putExtra("type",type);
            context.startService(intenta);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "" );
    }
}
