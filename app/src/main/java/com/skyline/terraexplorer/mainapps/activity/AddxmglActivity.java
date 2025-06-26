package com.skyline.terraexplorer.mainapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityAddxmglBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;

public class AddxmglActivity extends BaseActivity {
    private ActionBar actionBar;
    private ActivityAddxmglBinding binding;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private int choose2 = 0;
    private List<Object> imglist;
    public List<Uri> uriChooseList;
    private final int CHOOSE_PICTURE = 0;
    private String access_token;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private Bitmap imgBitmap;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private String img_Path = "";
    private static final String TAG = "AddxmglActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addxmgl);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addxmgl);
        initView();
    }

    private void initView() {
        access_token = new DbConfig(this).getUser().getToken();
        imglist=new ArrayList<>();
        uriChooseList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加项目监管");
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
        RxViewAction.clickNoDouble(binding.quxianEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog(binding.quxianEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.xmjbEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showJibieChangeDailog();
                    }
                });
        RxViewAction.clickNoDouble(binding.oneImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 1){  //只有一张图
                            uriChooseList.remove(0);
                            //  Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                            binding.photoLayout.setVisibility(View.GONE);
                        }else {     //如果有两张图
                            uriChooseList.remove(0);
                            Glide.with(getApplicationContext()).load(uriChooseList.get(0)).into(binding.oneImage);
                            //   Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                            binding.oneImageDelete.setVisibility(View.VISIBLE);
                            binding.twoImageDelete.setVisibility(View.GONE);
                            binding.twoImagLayout.setVisibility(View.GONE);
                        }
                    }
                });

        RxViewAction.clickNoDouble(binding.twoImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        uriChooseList.remove(1);
                        // Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                        //twoImageDelete.setVisibility(View.GONE);
                        binding.twoImagLayout.setVisibility(View.GONE);
                    }
                });
        RxViewAction.clickNoDouble(binding.oneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(AddxmglActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage();
                        }
                    }
                });
        RxViewAction.clickNoDouble(binding.tijiaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialogProgress(progressDialog,"正在上传");
                        if (!img_Path.equals("")) {
                            postPictoService();
                        }else {
                                postDataToService();
                        }
                    }
                });
    }
    private void postDataToService() {
        if (binding.xmjeEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写项目金额", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",binding.bjcdwEdit.getText().toString());
            jsonObject.put("region",binding.quxianEdit.getText().toString());
            jsonObject.put("approvalNum",binding.pzwhEdit.getText().toString());
            jsonObject.put("level",binding.xmjbEdit.getText().toString());
            jsonObject.put("tenderingOrg", binding.zbdwEdit.getText().toString());
            jsonObject.put("tenderingOrgHead",binding.zbdwfzrEdit.getText().toString());
            jsonObject.put("tenderingOrgHeadPhone",binding.fzrdhEdit.getText().toString());
            jsonObject.put("supervisingUnit",binding.jldwEdit.getText().toString());
            jsonObject.put("winningUnit",binding.zbdwEdit1.getText().toString());
            jsonObject.put("winningUnitHead",binding.zbdwfzrEdit.getText().toString());
            jsonObject.put("winningUnitHeadPhone",binding.fzrdhEdit1.getText().toString());
            jsonObject.put("amount",Double.parseDouble(binding.xmjeEdit.getText().toString()));
            jsonObject.put("description",binding.kgqmsEdit.getText().toString());
            if (imglist.size()>0){
                jsonObject.put("image",imglist.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("image",imglist.get(1).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/projectPlanningAndManagement" );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        Log.e(TAG, "postDataToServiceFromDb---" + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(AddxmglActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddxmglActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" +ex.toString());
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
    private void showLeibieChangeDailog(EditText editText) {
        //默认选中第一个
        final String[] items = {"青岛市", "市南区","市北区","崂山区","李沧区","城阳区","即墨区","胶州市","西海岸","平度市","莱西市"};

        builder = new AlertDialog.Builder(this).setTitle("选择区市")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choose1 == 0) {
                            editText.setText("青岛市");
                        } else if (choose1 == 1){
                            editText.setText("市南区");
                        }else if (choose1 == 2){
                            editText.setText("市北区");
                        }else if (choose1 == 3){
                            editText.setText("崂山区");
                        }else if (choose1 == 4){
                            editText.setText("李沧区");
                        }else if (choose1 == 5){
                            editText.setText("城阳区");
                        }else if (choose1 == 6){
                            editText.setText("即墨区");
                        }else if (choose1 == 7){
                            editText.setText("胶州市");
                        }else if (choose1 == 8){
                            editText.setText("西海岸");
                        }else if (choose1 == 9){
                            editText.setText("平度市");
                        }else if (choose1 == 10){
                            editText.setText("莱西市");
                        }

                    }
                });
        builder.create().show();
    }
    private void showJibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"市级","区县级"};

        builder = new AlertDialog.Builder(this).setTitle("市级")
                .setSingleChoiceItems(items, choose2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose2 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choose2 == 0) {
                            binding.xmjbEdit.setText("市级");
                        } else if (choose2 == 1){
                            binding.xmjbEdit.setText("区县级");
                        }
                    }
                });
        builder.create().show();
    }
    private void addImage() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        int size = 2 - uriChooseList.size();
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
//        Log.e(TAG, "onActivityResult:data ",data);
            if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            int degree = ImageUtils.getOrientation(getApplicationContext(), uri);
            uriChooseList.add(uri);
            binding.twoImagLayout.setVisibility(View.VISIBLE);
            binding.twoImageDelete.setVisibility(View.GONE);
            binding.oneImageDelete.setVisibility(View.VISIBLE);
            Glide.with(this).load(uriChooseList.get(0)).into(binding.oneImage);
            //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
            binding.twoImagLayout.setVisibility(View.GONE);
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(AddxmglActivity.this, uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            img_Path = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "fhjdpic");
            Log.e(TAG, "fhjdpic: " + img_Path);
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

    }
    private void postPictoService(){

        RequestParams params = new RequestParams(RequestUtils.SAVE_IMAGE);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {

                Uri uri = uriChooseList.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateTwo, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }
                params.addBodyParameter("file", new File(img_Path),null,img_Path);

            } catch (IOException e) {

            }
        }
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.setConnectTimeout(1000000);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray imgStrArray = data.getJSONArray("img");
                        Log.i(TAG, "imgStrArray: "+imgStrArray.length());
                        for (int i = 0; i<imgStrArray.length(); i++){
                            imglist.add(imgStrArray.get(i));
                        }
                            postDataToService();
                    }else {
                        Toast.makeText(AddxmglActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
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