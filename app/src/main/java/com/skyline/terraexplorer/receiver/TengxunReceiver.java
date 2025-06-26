package com.skyline.terraexplorer.receiver;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.bus.PlanPush;
import com.skyline.terraexplorer.controllers.CheckPlanActivity;
import com.tencent.android.tpush.NotificationAction;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.iflytek.speech.UtilityConfig.CHANNEL_ID;

public class TengxunReceiver extends XGPushBaseReceiver{
    private static final String TAG = TengxunReceiver.class.getSimpleName();

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        Log.e(TAG, "onRegisterResult: ");
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        Log.e(TAG, "onUnregisterResult: " );
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Log.e(TAG, "onSetTagResult: ");
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        Log.e(TAG, "onDeleteTagResult: ");
    }

    @Override
    public void onSetAccountResult(Context context, int i, String s) {
        Log.e(TAG, "onSetAccountResult: ");
    }

    @Override
    public void onDeleteAccountResult(Context context, int i, String s) {
        Log.e(TAG, "onDeleteAccountResult: ");
    }

    /**
     * 消息透传
     * @param context
     * @param xgPushTextMessage
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.e(TAG, "onTextMessage: ");
        String title = xgPushTextMessage.getTitle();
        if(title!=null && title.contains("update")){
            //EventBus.getDefault().post(new DoUpdate());
        }
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
        if (message.getActionType() == NotificationAction.clicked.getType()) {// 通知在通知栏被点击   APP自己处理点击的相关动作
            Log.e(TAG, "onNotificationClickedResult: 通知被点击了" +message.getActivityName());
            Log.e(TAG, "onNotificationClickedResult: 通知被点击了" +message.getCustomContent());
            String id = "";
            String type = "";
            String content = "";
            String time = "";
            try {
                JSONObject jsonObject = new JSONObject(message.getCustomContent());
                id = jsonObject.getString("id");

                EventBus.getDefault().post(new PlanPush(id));
                Log.e(TAG, "onNotificationClickedResult: XGPush " + id );
/*
                Intent intent = new Intent(TEApp.getAppContext(), CheckPlanActivity.class);
                intent.putExtra("plan_id", id);
                TEApp.getAppContext().startActivity(intent);*/

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if(Objects.equals(type, "12")){
                EventBus.getDefault().post(new MainTabChange(2,"oneBody"));
            }else{
                EventBus.getDefault().post(new MainTabChange(2));
            }
            EventBus.getDefault().post(new MessageRefresh());*/


        }
    }

    /**
     * 通知栏接受报警
     * @param context
     * @param xgPushShowedResult
     */
    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.e(TAG, "onNotificationShowedResult: " + xgPushShowedResult.getActivity());
        Log.e(TAG, "onNotificationShowedResult: " + xgPushShowedResult.getContent());
        Log.e(TAG, "onNotificationShowedResult: " + xgPushShowedResult.getTitle());
        Log.e(TAG, "onNotificationShowedResult: " + xgPushShowedResult.getMsgId());
        Log.e(TAG, "onNotificationShowedResult: " + xgPushShowedResult.getNotifactionId());
        Log.e(TAG, "onNotificationShowedResult: customContent" + xgPushShowedResult.getCustomContent());
        String id = "";
        String type = "";
        String content = "";
        String time = "";
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setXiaoMiBadgeNum(1);
        }*/
        try {
            JSONObject jsonObject = new JSONObject(xgPushShowedResult.getCustomContent());
            id = jsonObject.getString("id");
            type = jsonObject.getString("type");
            content = jsonObject.getString("content");
            time = jsonObject.getString("time");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Intent intent = new Intent(context, BackgroundMp3Service.class);
        CommonData.warnType = type;
        context.startService(intent);

        EventBus.getDefault().post(new MessageRefresh());*/

    }



    private String parseMessageType(String finalType) {
        String type = "";
        if(Objects.equals(finalType, "12")){
            type = "报警";
        }else{
            type = "任务";
        }

        return type;
    }

}
