package com.haohai.platform.platformmodel.ui.acticity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.utils.DYLoadingView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class ChangePassActivity extends HhBaseActivity {
    ImageView iv_back;
    EditText et_old;
    EditText et_new;
    TextView tv_submit;
    DYLoadingView dy3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        RxViewAction.clickNoDouble(iv_back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                onBackPressed();
            }
        });

        et_old = findViewById(R.id.et_old);
        et_new = findViewById(R.id.et_new);
        tv_submit = findViewById(R.id.tv_submit);
        dy3 = findViewById(R.id.dy3);

        RxViewAction.clickNoDouble(tv_submit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.e("TAG", "onCreate: bingo");
                if(et_old.getText().toString().length()<6){
                    Toast.makeText(ChangePassActivity.this, "请输入合理的原密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_new.getText().toString().length()<6){
                    Toast.makeText(ChangePassActivity.this, "请输入合理的新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit();

            }
        });
    }


    void showDY3(){
        dy3.setVisibility(View.VISIBLE);
        dy3.start();
    }
    void hideDY3(){
        dy3.setVisibility(View.GONE);
        dy3.stop();
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("newPasswd",et_new.getText().toString());
            jsonObject.put("oldPasswd",et_old.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_QUANXIAN + "api/auth/user/modfiy/passwd");
        params.addBodyParameter("newPasswd",et_new.getText().toString());
        params.addBodyParameter("oldPasswd",et_old.getText().toString());
        params.addParameter("newPasswd",et_new.getText().toString());
        params.addParameter("oldPasswd",et_old.getText().toString());
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "Bearer " + new DbConfig(ChangePassActivity.this).getUser().getToken());
        showDY3();
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "onSuccess: bingo changepwd" + result );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if(jsonObject.getInt("code") == 200){
                        Toast.makeText(ChangePassActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ChangePassActivity.this, jsonObject.getString("message")+"", Toast.LENGTH_SHORT).show();
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideDY3();
                    }
                },500);
            }
        });
    }
}