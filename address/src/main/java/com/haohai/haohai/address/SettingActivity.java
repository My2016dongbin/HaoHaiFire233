package com.haohai.haohai.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.haohai.address.db.DbConfig;
import com.haohai.haohai.address.db.model.User;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;

public class SettingActivity extends BaseActivity {

    private EditText userIdView;
    private EditText userNameView;
    private EditText phoneView;
    private EditText timeView;
    private EditText ipView;
    private ActionBar actionBar;
    private TextView saveSettingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("参数设置");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        initView();
    }

    private void initView() {
        userIdView = (EditText) findViewById(R.id.user_id_view);
        userNameView = (EditText) findViewById(R.id.username_view);
        phoneView = (EditText) findViewById(R.id.user_phone_view);
        timeView = (EditText) findViewById(R.id.time_view);
        ipView = (EditText) findViewById(R.id.ip_view);

        final User user = new DbConfig(this).getUser();
        userIdView.setText(user.getUserId() + "");
        userNameView.setText(user.getUsername());
        phoneView.setText(user.getPhone());
        timeView.setText(user.getTime());
        ipView.setText(user.getIp());


        saveSettingView = (TextView) findViewById(R.id.savae_setting_view);

        RxViewAction.clickNoDouble(saveSettingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String userId = userIdView.getText().toString();
                        String userName = userNameView.getText().toString();
                        String phone = phoneView.getText().toString();
                        String time = timeView.getText().toString();
                        String ip = ipView.getText().toString();
                        User user1 = new DbConfig(getApplicationContext()).getUser();
                        user1.setUserId(userId);
                        user1.setPhone(phone);
                        user1.setUsername(userName);
                        user1.setTime(time);
                        user1.setIp(ip);

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user1);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(SettingActivity.this, "参数设置成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });


    }
}
