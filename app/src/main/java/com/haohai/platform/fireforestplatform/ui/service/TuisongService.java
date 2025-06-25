package com.haohai.platform.fireforestplatform.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.LinkedHashSet;
import java.util.Set;

public class TuisongService extends Service {
    private static final String TAG = TuisongService.class.getSimpleName();

    public TuisongService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        User user = new DbConfig(this).getUser();
        super.onCreate();
        Set<String> tagSet = new LinkedHashSet<String>();
        tagSet.add(user.getGridNo());
        tagSet.add(user.getId());
        tagSet.add(user.getGroupId());
        XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
        //开启华为推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: 已创建");
    }
}
