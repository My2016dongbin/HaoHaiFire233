package com.ruyiruyi.rylibrary.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.rylibrary.bus.MessagePush;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.vsg.trustaccess.sdks.VSGService;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Objects;

public class CommonUtil {
    public String msgTokenDown = "账号在其他设备登录，您已被登出";
    public String msgTimeDown = "登录时长超过15分钟，您已被登出";
    public String msgActiveDown = "应用长时间不活跃，您已被登出";
    public void tokenDown(Context context){

        VSGService.getInstance().logout(context, null);
        CommonData.vpnState = false;

        DbConfig dbConfig = new DbConfig(context);
        User user = dbConfig.getUser();
        user.setIsLogin(0);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessagePush(msgTokenDown));
    }
    public void timeDown(Context context){
        Log.e("", "timeDown: " );

        VSGService.getInstance().logout(context, null);
        CommonData.vpnState = false;

        DbConfig dbConfig = new DbConfig(context);
        User user = dbConfig.getUser();
        user.setIsLogin(0);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessagePush(msgTimeDown));
    }
    public void activeDown(Context context){
        Log.e("", "activeDown: " );

        VSGService.getInstance().logout(context, null);
        CommonData.vpnState = false;

        DbConfig dbConfig = new DbConfig(context);
        User user = dbConfig.getUser();
        user.setIsLogin(0);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessagePush(msgActiveDown));
    }
    public static String strNull(String str){
        String returnS = "";
        if(str!=null && !Objects.equals(str, "null")){
            returnS = str;
        }

        int index = returnS.indexOf(".");
        if(index != -1){
            returnS = returnS.substring(0,index+2);
        }
        return returnS;
    }
}
