package com.skyline.test.myview;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.ProjectsTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.test.utils.Constance;

import java.util.EnumSet;

public class ServerAddressActivity extends MatchParentActivity {
    private static final String TAG = "ServerAddressActivity";
    EditText serverIp=null;
    EditText serverPort=null;
    Button button_save=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serveraddress);
        EnumSet<UI.HeaderOptions> options = EnumSet.noneOf(UI.HeaderOptions.class);
        if (getIntent().getBooleanExtra(ProjectsTool.DISABLE_BACK_BUTTON, false))
            options = EnumSet.of(UI.HeaderOptions.NoBackButton);
        UI.addHeader(R.string.titile_serveraddress, R.drawable.settings, this, options);
        initUI();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveContent();
                //ToastUtils.showShort("123456");


            }
        });

    }



    /**
     * 初始化界面
     */
    private  void initUI()
    {

        serverIp=(EditText)findViewById(R.id.editText_serverAddress);
        serverPort=(EditText)findViewById(R.id.editText_serverPort);
        button_save=(Button)findViewById(R.id.button_save);
        String ip=SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_INITIAL_IP);
        int port= SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getInt(Constance.USER_INITIAL_PORT);
        if (ip.isEmpty())
        {
            serverIp.setText("192.168.1.1");
        }
        else
        {
            serverIp.setText(ip);
        }
        if (port==-1)
        {
            serverPort.setText("80");
        }
        else
        {
            serverPort.setText(String.valueOf(port));
        }
    }
    private  void saveContent()
    {
        SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).put(Constance.USER_INITIAL_IP,serverIp.getText().toString());
        SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).put(Constance.USER_INITIAL_PORT,Integer.parseInt(serverPort.getText().toString()));
        finish();
    }
}
