package com.skyline.terraexplorer.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.models.ImageLabel;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class ResourceErrorActivity extends HBaseActivity {
    private static final String TAG = ResourceEditActivity.class.getSimpleName();
    private ImageLabel resource;
    private EditText nameEdit;
    private EditText addressEdit;
    private EditText lngEdit;
    private EditText latEdit;
    private EditText errorEdit;
    private TextView tijiaoButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_error);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        resource = ((ImageLabel) intent.getSerializableExtra("RESOURCE"));

        initView();
    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.name_edit);
        addressEdit = (EditText) findViewById(R.id.address_edit);
        lngEdit = (EditText) findViewById(R.id.lng_edit);
        latEdit = (EditText) findViewById(R.id.lat_edit);
        errorEdit = (EditText) findViewById(R.id.error_edit);
        tijiaoButton = (TextView) findViewById(R.id.tijiao_button);
        nameEdit.setFocusable(false);
        nameEdit.setEnabled(false);
        addressEdit.setFocusable(false);
        addressEdit.setEnabled(false);
        lngEdit.setFocusable(false);
        lngEdit.setEnabled(false);
        latEdit.setFocusable(false);
        latEdit.setEnabled(false);

        nameEdit.setText(resource.getName());
        addressEdit.setText(resource.getAddress());
        lngEdit.setText(resource.getLongitude()+"");
        latEdit.setText(resource.getLatitude()+"");


        RxViewAction.clickNoDouble(tijiaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postDataToService();
                    }
                });
    }

    private void postDataToService() {

        showDialogProgress(progressDialog,"提交中...");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",resource.getUuId());
            jsonObject.put("resourceName",resource.getName());
            jsonObject.put("resourceType",resource.getResourceType());
            jsonObject.put("description",errorEdit.getText().toString());
            JSONObject position = new JSONObject();
            position.put("lat",resource.getLatitude());
            position.put("lng",resource.getLongitude());
            jsonObject.put("position",position);


        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/dangerCheck");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
}
