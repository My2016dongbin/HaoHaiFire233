package com.skyline.terraexplorer.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.controllers.LoginActivity;
import com.skyline.terraexplorer.controllers.TEMainActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.JobOrder;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.multitype.Plan;
import com.skyline.terraexplorer.multitype.PlanOrder;
import com.skyline.terraexplorer.utils.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private String messageWeb;
    private List<JobOrder> jobOrderList;
    public List<Plan> planList;

    private PlanOrder planOrder;
    private DbManager db;
    private DbConfig dbConfig;

    @Override
    public void onReceive(Context context, Intent intent) {
        messageWeb = intent.getExtras().getString(JPushInterface.EXTRA_ALERT);
        Log.e(TAG, "onReceive: " + intent.getExtras().toString() );
        planList = new ArrayList<>();
        jobOrderList = new ArrayList<>();
        dbConfig = new DbConfig(context);
        db = dbConfig.getDbManager();
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
             /*   int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                String extra1 = bundle.getString(JPushInterface.EXTRA_EXTRA);   //推送来的火警ID
                Log.e(TAG, "onReceive:extra1----- " + extra1 );

                //判断启动服务
             //   String isVoice = new DbConfig(context).getUser().getIsVoice();
             //   if ("1".equals(isVoice)) {//用户开启了语音播报
                    Intent intenta = new Intent(context, BackgroundMp3Service.class);
               //     intenta.putExtra("messageWeb", messageWeb);
                    context.startService(intenta);
            //    }

                Intent intent1= new Intent();
                intent1.setAction("jpush_fire");
                intent1.putExtra("sele","新火警");
                intent1.putExtra("fire_id",extra1);
                context.sendBroadcast(intent1);*/

                Intent intent1= new Intent();
                intent1.setAction("job_order");
                context.sendBroadcast(intent1);
                //获取检查计划列表
                initJobOrderIntoDb(context);


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 用户点击打开了通知jpush");
                String fireIdJson = bundle.getString(JPushInterface.EXTRA_EXTRA);   //推送来的火警ID


                DbConfig dbConfig = new DbConfig(context);
                User user = dbConfig.getUser();
                int isLogin = user.getIsLogin();
               /* user.setJpush(true);
                user.setJpushStr(fireIdJson);
                DbManager db = dbConfig.getDbManager();
                db.saveOrUpdate(user);*/


                //打开自定义的Activity
                if (isLogin == 0){
                    Intent i = new Intent(context, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }else {
                    Log.e(TAG, "onReceive: 用户点击了通知  mainactivity正在运行中" );
                    Intent i = new Intent(context, TEMainActivity.class);
                   /* bundle.putString("token",  new DbConfig(context).getUser().getToken());
                    bundle.putString("START_TYPE", "JPUSH");
                    bundle.putString("fire_id",fireIdJson);*/
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                    Intent intent1= new Intent();
                    intent1.setAction("job_order");
                    context.sendBroadcast(intent1);


                }



            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    private void initJobOrderIntoDb(final Context context) {
        List<JobOrder> jobOrderbList = null;
        try {
            jobOrderbList = db.selector(JobOrder.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (jobOrderbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = jobOrderbList.get(jobOrderbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);

            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getUseCheckPlanList");
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(context).getUser().getToken());
        Log.e(TAG, "checkPlan: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:-------checkPlan------ " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        deleteJobOrderInfo();
                        JSONArray data = jsonObject.getJSONArray("data");
                        jobOrderList.clear();
                        Gson gson = new Gson();
                        jobOrderList = gson.fromJson(String.valueOf(data), new TypeToken<List<JobOrder>>(){}.getType());

                        for (int i = 0; i < jobOrderList.size(); i++) {

                            Log.e(TAG, "onSuccess: planinfo" + jobOrderList.get(i).getId() );
                            initJobOrderInfoIntoDb(jobOrderList.get(i).getId());
                        }

                        try {
                            Log.e(TAG, "onSuccess: team11" );
                            db.saveOrUpdate(jobOrderList);

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else {
                        // Toast.makeText(LaunchActivity.this, "暂无检查计划！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
    }
    private void deleteJobOrderInfo() {
        try {

            List<JobOrder> jobOrderDeleteList = db.selector(JobOrder.class)
                    .findAll();
            for (int i = 0; i < jobOrderDeleteList.size(); i++) {
                db.delete(jobOrderDeleteList.get(i).getId());
            }
            List<Plan> planDeleteList = db.selector(Plan.class)
                    .findAll();
            for (int i = 0; i < planDeleteList.size(); i++) {
                db.delete(planDeleteList.get(i).getId());
            }

            List<PlanOrder> planOrderDeleteList = db.selector(PlanOrder.class)
                    .findAll();
            for (int i = 0; i < planOrderDeleteList.size(); i++) {
                db.delete(planOrderDeleteList.get(i).getId());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    private void initJobOrderInfoIntoDb(String planId) {
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getCheckPlanDetail");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("id",planId);
        params.addHeader("Authorization","bearer " + dbConfig.getUser().getToken());
        params.setConnectTimeout(10000);
        Log.e(TAG, "planinfo: " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: planinfo"+ result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    JSONArray resourceJsonArray = data.getJSONArray("resourceList");
                    JSONObject checkPlanObject = data.getJSONObject("checkPlan");
                    Gson gson = new Gson();

                    planOrder = gson.fromJson(String.valueOf(checkPlanObject), new TypeToken<PlanOrder>(){}.getType());
                    planList.clear();
                    planList = gson.fromJson(String.valueOf(resourceJsonArray), new TypeToken<List<Plan>>(){}.getType());
                    for (int i = 0; i < planList.size(); i++) {
                        double lat = planList.get(i).getResourcePosition().getLat();
                        double lng = planList.get(i).getResourcePosition().getLng();
                        planList.get(i).setLat(lat);
                        planList.get(i).setLng(lng);
                    }
                    Log.e(TAG, "onSuccess:planinfo.size= " + planList.size() );

                    try {
                        Log.e(TAG, "onSuccess: team11" );
                        db.saveOrUpdate(planList);
                        db.saveOrUpdate(planOrder);
/*
                        Message message = downHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putInt("pro", 2);
                        b.putInt("what",GO_DOWN);
                        message.setData(b);
                        downHandler.sendMessage(message);*/
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}