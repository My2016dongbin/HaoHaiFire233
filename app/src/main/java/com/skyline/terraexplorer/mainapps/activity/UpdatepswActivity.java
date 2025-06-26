package com.skyline.terraexplorer.mainapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityUpdatepswBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class UpdatepswActivity extends BaseActivity {
    private ActionBar actionBar;
    private ActivityUpdatepswBinding binding;
    private String access_token;
    private static final String TAG = UpdatepswActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepsw);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_updatepsw);
        initView();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("修改密码");
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
        access_token= new DbConfig(this).getUser().getToken();
        RxViewAction.clickNoDouble(binding.updateButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (binding.pswEdit.getText().toString().equals(binding.pswEdit1.getText().toString())){
                            postDataToService();
                        }else {
                            Toast.makeText(UpdatepswActivity.this, "俩次输入信息不一致！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void postDataToService() {
        JSONObject modifyPasswdDTO = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            modifyPasswdDTO.put("newPasswd", binding.pswEdit1.getText().toString());
            modifyPasswdDTO.put("oldPasswd", new DbConfig(this).getUser().getUserPasswd());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_HXY_LOGIN + "api/auth/user/modfiy/passwd");
        params.setAsJsonContent(true);
        params.setBodyContent(modifyPasswdDTO.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- params--" + params);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")) {
                        Toast.makeText(UpdatepswActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        //setResult( RequestCode.LIST_CHANGE);
                        finish();
                    } else {
                        //    Toast.makeText(LeaveFlowAddActivity.this, "流程申请失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
                Toast.makeText(UpdatepswActivity.this, "数据提交失败", Toast.LENGTH_SHORT).show();
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