package com.haohai.platform.platformmodel.ui.acticity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class AttendanceRecordInfoActivity extends HhBaseActivity {
    private static final String TAG = AttendanceRecordInfoActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView xingqiView;
    private TextView riqiView;
    private TextView qiandaoShijianView;
    private TextView qiantuishijianvView;
    private TextView qiandaoButton;
    private TextView qiantuiButton;
    private ProgressDialog progressDialog;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record_info);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        time = intent.getStringExtra("TIME");

        initView();

        getDataFromService();
    }

    private void getDataFromService() {


        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/attendance/myAttRecordByTime");

        params.addParameter("time",time);

        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        Log.e(TAG, "getDataFromService: " + params );
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);




            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("工作汇报");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        break;
                }
            }
        });
        actionBar.setTitle("考勤统计");

        xingqiView = (TextView) findViewById(R.id.xingqi_view);
        riqiView = (TextView) findViewById(R.id.riqi_view);
        qiandaoShijianView = (TextView) findViewById(R.id.qiandao_shijian_view);
        qiantuishijianvView = (TextView) findViewById(R.id.qiantui_shijian_view);
    }
}
