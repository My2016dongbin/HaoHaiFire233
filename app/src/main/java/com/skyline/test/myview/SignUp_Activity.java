package com.skyline.test.myview;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bean.LoginBuf;
import com.bean.MessageBuf;
import com.bean.SignUpBuf;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.controllers.ProjectsActivity;
import com.skyline.terraexplorer.controllers.SettingsActivity;
import com.skyline.terraexplorer.models.LocalBroadcastManager;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.test.entity.User;
import com.skyline.test.socket.NettyClient;
import com.skyline.test.socket.NettyListener;
import com.skyline.test.utils.Constance;
import com.skyline.test.utils.Protocol;
import com.skyline.test.utils.TimeUtil;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class SignUp_Activity extends MatchParentActivity {

    private static final String TAG = "SignUp_Activity";
    EditText userName=null;
    EditText dep=null;
    EditText email=null;
    EditText phoneNumber=null;
    EditText pwd=null;
    EditText confirmPwd=null;
    Button signUp=null;

    TextView login=null;
    private MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUI();
        myReceiver=new MyReceiver();
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.osanwen.nettydemo.NettyService");
        SignUp_Activity.this.registerReceiver(myReceiver,filter);
       // LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,new IntentFilter("com.osanwen.nettydemo.NettyService"));
    }
    private  void initUI()
    {
        userName=(EditText)findViewById(R.id.input_name);
        dep=(EditText)findViewById(R.id.input_address);
        email=(EditText)findViewById(R.id.input_email);
        phoneNumber=(EditText)findViewById(R.id.input_mobile);
        pwd=(EditText)findViewById(R.id.input_password);
        confirmPwd=(EditText)findViewById(R.id.input_reEnterPassword);
        signUp=(Button)findViewById(R.id.btn_signup);
        login=(TextView) findViewById(R.id.link_login);
        phoneNumber.setText(SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_PhoneNumber));
        userName.setText(SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_Name));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录界面
               // ActivityUtils.startActivity(Login_Activity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       SignUp_Activity.this.unregisterReceiver(myReceiver);
    }

    /**
     * 注册
     */
    private  void signUp()
    {

        if (!NettyClient.getInstance().getConnectStatus())
        {
            ToastUtils.showShort("当前服务器不可用");
            return;
        }
        SignUpBuf.SignMessage.SignSend.Builder signSend= SignUpBuf.SignMessage.SignSend.newBuilder();
        signSend.setUserName(userName.getText().toString());
        signSend.setDep(dep.getText().toString());
        signSend.setPhoneNumber(phoneNumber.getText().toString());
        signSend.setPwd(pwd.getText().toString());
        signSend.setProfession("hu");
        signSend.setRegisterTime(TimeUtils.getNowString());
        NettyClient.getInstance().sendMsgToServer(Protocol.generateSend(4, signSend.build().toByteString()), new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess())
                {
                    //成功
                }
                else
                {
                    //失败
                }
            }
        });
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            MessageBuf.JMTransfer mm = (MessageBuf.JMTransfer) bundle.get("count");
            Log.d(TAG, "onReceive: " +mm.getCmd());
            if (mm.getCmd() == 4) {
                try {
                    SignUpBuf.SignMessage.SignRecieve signRecieve = SignUpBuf.SignMessage.SignRecieve.parseFrom(mm.getBody());
                    if (signRecieve.getFlagValue() == 0) {
                        SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).put(Constance.USER_Name,userName.getText().toString());
                        SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).put(Constance.USER_PhoneNumber,phoneNumber.getText().toString());
                        SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).put(Constance.USERREGISTER,1);
                      // ActivityUtils.startActivity(SignUp_Activity.this,SettingsActivity.class);
                        //登录成功跳转到gis界面
//                     Intent intents = TEApp.getAppContext().getPackageManager()
//                                .getLaunchIntentForPackage(TEApp.getAppContext() .getPackageName());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        TEApp.getAppContext().startActivity(intents);
                        AppUtils.relaunchApp();
                    }
                    else
                    {
                        //登录失败
                        ToastUtils.showShort("注册失败");
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
