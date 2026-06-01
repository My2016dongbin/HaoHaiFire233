package com.hht.hsatellitemobile.ui.mqtt;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.db.model.User;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Random;

public class MqttAlarmManager {
    public interface AlarmCallback {
        void onDeviceAlarm(MqttAlarmData alarmData);
    }

    private static final String TAG = "MqttAlarmManager";
    private static final String MQTT_URI = "ws://wx3.ehaohai.com:80/mqtt";
    private static final String MQTT_ACCOUNT = "weixing2";
    private static final String MQTT_PASSWORD = "Haohai!@3$%";
    private static final String ALARM_TOPIC = "/Satellite/Haohai/";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private MqttAsyncClient client;
    private MqttConnectOptions connectOptions;
    private String subscribedTopicId;
    private AlarmCallback callback;

    public void connect(Context context, AlarmCallback alarmCallback) {
        this.callback = alarmCallback;
        User user = new DbConfig(context.getApplicationContext()).getUser();
        if (user == null) {
            Log.e(TAG, "connect: missing login user");
            return;
        }
        String topicId = buildAreaTopicId(user);
        if (client != null && client.isConnected() && topicId.equals(subscribedTopicId)) {
            return;
        }
        disconnect();
        subscribedTopicId = topicId;

        try {
            final String clientId = String.valueOf(new Random().nextInt(999999));
            client = new MqttAsyncClient(MQTT_URI, clientId, new MemoryPersistence());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "connectionLost", cause);
                    reconnect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    handleMessage(topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(MQTT_ACCOUNT);
            connectOptions.setPassword(MQTT_PASSWORD.toCharArray());
            connectOptions.setCleanSession(true);
            connectOptions.setKeepAliveInterval(20);
            connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            client.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "connect success");
                    subscribeAlarmTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "connect failed", exception);
                    if (isFailedAuthentication(exception)) {
                        Log.e(TAG, "connect failed: authentication denied, stop reconnect");
                        return;
                    }
                    reconnect();
                }
            });
            Log.d(TAG, "connect: " + clientId);
        } catch (MqttException e) {
            Log.e(TAG, "connect failed", e);
        }
    }

    public void disconnect() {
        if (client == null) {
            return;
        }
        try {
            if (client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            Log.e(TAG, "disconnect failed", e);
        }
        client = null;
    }

    private void reconnect() {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (client == null || client.isConnected()) {
                    return;
                }
                try {
                    client.connect(connectOptions, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d(TAG, "reconnect success");
                            subscribeAlarmTopic();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.e(TAG, "reconnect failed", exception);
                            if (isFailedAuthentication(exception)) {
                                Log.e(TAG, "reconnect failed: authentication denied, stop reconnect");
                                return;
                            }
                            reconnect();
                        }
                    });
                } catch (MqttException e) {
                    Log.e(TAG, "reconnect failed", e);
                    reconnect();
                }
            }
        }, 3000L);
    }

    private void subscribeAlarmTopic() {
        if (client == null || TextUtils.isEmpty(subscribedTopicId)) {
            return;
        }
        try {
            String topic = ALARM_TOPIC + subscribedTopicId;
            IMqttToken token = client.subscribe(topic, 1);
            Log.d(TAG, "subscribe: " + topic + " token=" + token);
        } catch (MqttException e) {
            Log.e(TAG, "subscribe failed", e);
        }
    }

    private boolean isFailedAuthentication(Throwable exception) {
        return exception instanceof MqttException && ((MqttException) exception).getReasonCode() == 5;
    }

    private void handleMessage(String topic, MqttMessage message) {
        if (topic == null || !topic.contains(ALARM_TOPIC)) {
            return;
        }
        String payload = new String(message.getPayload(), Charset.forName("UTF-8"));
        Log.d(TAG, "messageArrived: " + payload + " topic=" + topic);
        try {
            JSONObject model = new JSONObject(payload);
            final MqttAlarmData alarmData = parseAlarmData(model);
            if (callback != null) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onDeviceAlarm(alarmData);
                        }
                    }
                });
            }
        } catch (JSONException e) {
            Log.e(TAG, "parse message failed", e);
        }
    }

    private MqttAlarmData parseAlarmData(JSONObject model) {
        String alarmId = parseAlarmId(model);
        String timeText = parseAlarmTime(model);
        String content = parseAlarmContent(model);
        String dedupeKey = parseDedupeKey(model, content, timeText, alarmId);
        return new MqttAlarmData(alarmId, "卫星火警推送", timeText, content, dedupeKey, model.toString());
    }

    private String parseAlarmId(JSONObject model) {
        String[] keys = new String[]{"Id", "id", "FireId", "fireId", "FireAlarmId", "fireAlarmId", "FireNo", "fireNo", "linkId"};
        for (String key : keys) {
            String value = safeString(model.opt(key));
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        JSONObject data = model.optJSONObject("data");
        if (data != null) {
            for (String key : keys) {
                String value = safeString(data.opt(key));
                if (!TextUtils.isEmpty(value)) {
                    return value;
                }
            }
        }
        return "";
    }

    private String parseAlarmTime(JSONObject model) {
        String timeText = safeString(model.opt("ObservationDateTime"));
        return timeText.replace("T", " ");
    }

    private String parseAlarmContent(JSONObject model) {
        String formattedAddress = safeString(model.opt("FormattedAddress"));
        String content = (formattedAddress + "发现火情").trim();
        if (!TextUtils.isEmpty(content)) {
            return content;
        }
        return "发现火情";
    }

    private String parseDedupeKey(JSONObject model, String content, String timeText, String alarmId) {
        if (!TextUtils.isEmpty(alarmId)) {
            return "alarm_" + alarmId;
        }
        return "alarm_" + new Random().nextInt(10000);
    }

    private String buildAreaTopicId(User user) {
        return parseTopicId(user.getProvinceNo()) + "/"
                + parseTopicId(user.getCityNo()) + "/"
                + parseTopicId(user.getCountyNo());
    }

    private String parseTopicId(String areaNo) {
        String value = safeString(areaNo);
        if (TextUtils.isEmpty(value) || "null".equals(value)) {
            return "+";
        }
        return value;
    }

    private String safeString(Object value) {
        if (value == null || JSONObject.NULL.equals(value)) {
            return "";
        }
        return String.valueOf(value).trim();
    }
}
