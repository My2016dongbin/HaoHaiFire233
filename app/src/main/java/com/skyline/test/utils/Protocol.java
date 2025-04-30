package com.skyline.test.utils;

import android.provider.Settings;

import com.bean.LoginBuf;
import com.bean.MessageBuf;
import com.blankj.utilcode.util.SPUtils;
import com.google.protobuf.ByteString;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.tools.SettingsTool;

public class Protocol {

    public static MessageBuf.JMTransfer.Builder generateSend(int cmd, ByteString byteString) {
        String ANDROID_ID = Settings.System.getString(TEApp.getAppContext().getContentResolver(), Settings.System.ANDROID_ID);
        MessageBuf.JMTransfer.Builder builder = MessageBuf.JMTransfer.newBuilder();
        builder.setVersion("1.0");
        builder.setDeviceId(ANDROID_ID);
        builder.setCmd(cmd);
        builder.setSeq(1234);
        builder.setFormat(1);
        builder.setFlag(1);
        builder.setPlatform("android");
        builder.setPlatformVersion("1.0");
        builder.setToken("abc");
        builder.setAppKey("123");
        builder.setTimeStamp("123456");
        builder.setSign("123");
        builder.setBody(byteString);
        return builder;
    }

    public static MessageBuf.JMTransfer.Builder generateHeartbeat() {
        MessageBuf.JMTransfer.Builder builder = MessageBuf.JMTransfer.newBuilder();
        builder.setVersion("1.0");
        LoginBuf.LoginMessage.LoginSend.Builder loginsend= LoginBuf.LoginMessage.LoginSend.newBuilder();
        String phoneNumber= SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_PhoneNumber);
        if (phoneNumber.equals(null))
        {
            ///
        }
        loginsend.setPhoneNumber(phoneNumber);
        builder.setCmd(10000);
        builder.setBody(loginsend.build().toByteString());

        return builder;
    }
}
