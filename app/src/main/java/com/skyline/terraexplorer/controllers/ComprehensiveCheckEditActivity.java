package com.skyline.terraexplorer.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.models.CheckDto;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class ComprehensiveCheckEditActivity extends HBaseActivity {

    private static final String TAG = ComprehensiveCheckEditActivity.class.getSimpleName();
    private String id;
    private CheckDto checkDto;
    private EditText resourceNameView;
    private EditText zongtiView;
    private EditText yinhuanView;
    private EditText zhenggaiView;
    private EditText checkMenView;
    private ImageView backButton;
    private LinearLayout quLayout;
    private TextView quText;
    private LinearLayout jiedaoLayout;
    private TextView jiedaoText;
    private ImageView image1View;
    private ImageView image2View;
    private ImageView image3View;
    private ImageView image4View;
    private ImageView image5View;
    private ImageView image6View;
    private ImageView image7View;
    private ImageView image8View;
    private ImageView image9View;
    private TextView updateButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_check_edit);

        progressDialog = new ProgressDialog(this);
        initView();

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        getDataFromService();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resourceNameView = (EditText) findViewById(R.id.check_name_view);
        resourceNameView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        resourceNameView.setGravity(Gravity.TOP);
        resourceNameView.setSingleLine(false);
        resourceNameView.setSelection(resourceNameView.getText().toString().length());
        resourceNameView.setHorizontallyScrolling(false); //水平滚动设置为False

        zongtiView = (EditText) findViewById(R.id.zongti_view);
        zongtiView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        zongtiView.setGravity(Gravity.TOP);
        zongtiView.setSingleLine(false);
        zongtiView.setHorizontallyScrolling(false); //水平滚动设置为False

        yinhuanView = (EditText) findViewById(R.id.yinhuan_view);
        yinhuanView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        yinhuanView.setGravity(Gravity.TOP);
        yinhuanView.setSingleLine(false);
        yinhuanView.setHorizontallyScrolling(false); //水平滚动设置为False

        zhenggaiView = (EditText) findViewById(R.id.zhenggai_view);
        zhenggaiView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        zhenggaiView.setGravity(Gravity.TOP);
        zhenggaiView.setSingleLine(false);
        zhenggaiView.setHorizontallyScrolling(false); //水平滚动设置为False

        checkMenView = (EditText) findViewById(R.id.check_men_view);
        checkMenView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        checkMenView.setGravity(Gravity.TOP);
        checkMenView.setSingleLine(false);
        checkMenView.setHorizontallyScrolling(false); //水平滚动设置为False

        quLayout = (LinearLayout) findViewById(R.id.qu_layout);
        quText = (TextView) findViewById(R.id.qu_text);
        jiedaoLayout = (LinearLayout) findViewById(R.id.jiedao_layout);
        jiedaoText = (TextView) findViewById(R.id.jiedao_text);
        image1View = (ImageView) findViewById(R.id.image1_view);
        image2View = (ImageView) findViewById(R.id.image2_view);
        image3View = (ImageView) findViewById(R.id.image3_view);
        image4View = (ImageView) findViewById(R.id.image4_view);
        image5View = (ImageView) findViewById(R.id.image5_view);
        image6View = (ImageView) findViewById(R.id.image6_view);
        image7View = (ImageView) findViewById(R.id.image7_view);
        image8View = (ImageView) findViewById(R.id.image8_view);
        image9View = (ImageView) findViewById(R.id.image9_view);

        updateButton = (TextView) findViewById(R.id.update_button);

        RxViewAction.clickNoDouble(updateButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        updateDataToService();
                    }
                });

    }

    private void updateDataToService() {
        if (resourceNameView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入检查点名称！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zongtiView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入总体情况！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (yinhuanView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入隐患情况！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkMenView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入检查人！", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialogProgress(progressDialog,"正在上传");
        String name = resourceNameView.getText().toString();
        String zongti  = zongtiView.getText().toString();

        String yinhuan = yinhuanView.getText().toString();
        String jiancharen = checkMenView.getText().toString();
        checkDto.setDescription(zongti);
        checkDto.setDangerDescription(yinhuan);
        checkDto.setName(name);
        checkDto.setCheckUser(jiancharen);

        Gson gson = new Gson();
        final String json = gson.toJson(checkDto);

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE  + "api/generalCheck");
        params.setBodyContent(json);
        Log.e(TAG, "updateDataToService: json--" + json);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postimage: " + params );

        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        Toast.makeText(ComprehensiveCheckEditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        finish();
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
                progressDialog.dismiss();
            }
        });


    }

    private void getDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",id);

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/generalCheck");
        params.setAsJsonContent(true);
        params.addParameter("id",id);
       // params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("code").equals("200")) {
                        JSONObject data = object.getJSONArray("data").getJSONObject(0);
                        Gson gson = new Gson();
                         checkDto = gson.fromJson(String.valueOf(data), CheckDto.class);

                        quText.setText(checkDto.getDistrictName());
                        jiedaoText.setText(checkDto.getStreetName());
                        if (checkDto.getDistrictName().equals("")){
                            quLayout.setVisibility(View.GONE);
                        }else {
                            quLayout .setVisibility(View.VISIBLE);
                        }
                        if (checkDto.getStreetName().equals("")){
                            jiedaoLayout.setVisibility(View.GONE);
                        }else {
                            jiedaoLayout.setVisibility(View.VISIBLE);
                        }
                        resourceNameView.setText(checkDto.getName());
                        zongtiView.setText(checkDto.getDescription());
                        yinhuanView.setText(checkDto.getDangerDescription());
                        checkMenView.setText(checkDto.getCheckUser());
                        if (checkDto.getPic1().equals("null")) {
                            image1View.setVisibility(View.INVISIBLE);
                        }else {
                            image1View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL + checkDto.getPic1()).into(image1View);
                        }
                        Log.e(TAG, "onSuccess:getPic2 " + checkDto.getPic2());
                        if (checkDto.getPic2() == null) {
                            image2View.setVisibility(View.INVISIBLE);
                        }else {
                            image2View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic2()).into(image2View);
                        }
                        if (checkDto.getPic3()== null) {
                            image3View.setVisibility(View.INVISIBLE);
                        }else {
                            image3View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic3()).into(image3View);
                        }

                        if (checkDto.getPic4()== null) {
                            image4View.setVisibility(View.INVISIBLE);
                        }else {
                            image4View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic4()).into(image4View);
                        }
                        if (checkDto.getPic5()== null) {
                            image5View.setVisibility(View.INVISIBLE);
                        }else {
                            image5View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic5()).into(image5View);
                        }
                        if (checkDto.getPic6()== null) {
                            image6View.setVisibility(View.INVISIBLE);
                        }else {
                            image6View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic6()).into(image6View);
                        }
                        if (checkDto.getPic7()== null) {
                            image7View.setVisibility(View.INVISIBLE);
                        }else {
                            image7View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic7()).into(image7View);
                        }
                        if (checkDto.getPic8()== null) {
                            image8View.setVisibility(View.INVISIBLE);
                        }else {
                            image8View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic8()).into(image8View);
                        }
                        if (checkDto.getPic9()== null) {
                            image9View.setVisibility(View.INVISIBLE);
                        }else {
                            image9View.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL +checkDto.getPic9()).into(image9View);
                        }
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
            }
        });
    }
}
