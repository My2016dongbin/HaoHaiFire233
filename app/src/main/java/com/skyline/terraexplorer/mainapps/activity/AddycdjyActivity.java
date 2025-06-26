package com.skyline.terraexplorer.mainapps.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityAddcdjyBinding;
import com.skyline.terraexplorer.databinding.ActivityAddysglBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.xmjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;

public class AddycdjyActivity extends BaseActivity implements DatePicker.OnDateChangedListener{
    private ActionBar actionBar;
    private ActivityAddcdjyBinding binding;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private int choose2 = 0;
    private List<Object> imglist;
    public List<Uri> uriChooseList;
    private List<Object> imglist1;
    public List<Uri> uriChooseList1;
    private final int CHOOSE_PICTURE = 0;
    private final int CHOOSE_PICTURE1 = 1;
    private String access_token;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private Bitmap imgBitmap;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private String img_Path = "";
    private String img_Path1 = "";
    private Boolean yqrw = true;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    public List<xmjd> xmjdTypeList;
    private List<String> xmnameList;
    private static final String TAG = "AddxmglActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcdjy);
        initView();
        initDateTime();
        getDataFromService();
    }

    private void initView() {
        date = new StringBuffer();
        access_token = new DbConfig(this).getUser().getToken();
        imglist=new ArrayList<>();
        imglist1=new ArrayList<>();
        xmjdTypeList = new ArrayList<>();
        xmnameList =new ArrayList<>();
        uriChooseList = new ArrayList<>();
        uriChooseList1 = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加产地检疫");
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
        RxViewAction.clickNoDouble(binding.oneImageDelete1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList1.size() == 1){  //只有一张图
                            uriChooseList1.remove(0);
                            //  Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                            binding.photoLayout.setVisibility(View.GONE);
                        }else {     //如果有两张图
                            uriChooseList.remove(0);
                            Glide.with(getApplicationContext()).load(uriChooseList1.get(0)).into(binding.oneImage1);
                            //   Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                            binding.oneImageDelete1.setVisibility(View.VISIBLE);
                            binding.twoImageDelete1.setVisibility(View.GONE);
                            binding.twoImagLayout1.setVisibility(View.GONE);
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
        RxViewAction.clickNoDouble(binding.twoImageDelete1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        uriChooseList1.remove(1);
                        // Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                        //twoImageDelete.setVisibility(View.GONE);
                        binding.twoImagLayout1.setVisibility(View.GONE);
                    }
                });
        RxViewAction.clickNoDouble(binding.oneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(AddycdjyActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage();
                        }
                    }
                });
        RxViewAction.clickNoDouble(binding.oneImage1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList1.size() == 2){
                            Toast.makeText(AddycdjyActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage1();
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
        RxViewAction.clickNoDouble(binding.jianchashijianView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.jianchashijianView);
                    }
                });
    }
    private void postDataToService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("enterpriseName",binding.bjcdwEdit.getText().toString());
            jsonObject.put("enterpriseHead",binding.qyfzrEdit.getText().toString());
            jsonObject.put("quarantinePersonnel",binding.jyryEdit.getText().toString());
            jsonObject.put("quarantineTime",binding.jianchashijianView.getText().toString()+" 00:00:00");
            jsonObject.put("remark",binding.kgqmsEdit.getText().toString());
            if (imglist.size()>0){
                jsonObject.put("scenePhoto",imglist.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("scenePhoto",imglist.get(1).toString());
                }
            }
            if (imglist1.size()>0){
                jsonObject.put("certificatePhoto",imglist1.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("certificatePhoto",imglist1.get(1).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/pestOriginQuarantine" );
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
                        Toast.makeText(AddycdjyActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddycdjyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
    private void addImage1() {
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
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE1);
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
                photo = ImageUtils.getBitmapFormUri(AddycdjyActivity.this, uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            img_Path = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "fhjdpic");
            Log.e(TAG, "fhjdpic: " + img_Path);
        }else if (requestCode == 1 && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                int degree = ImageUtils.getOrientation(getApplicationContext(), uri);
                uriChooseList1.add(uri);
                binding.twoImagLayout1.setVisibility(View.VISIBLE);
                binding.twoImageDelete1.setVisibility(View.GONE);
                binding.oneImageDelete1.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList1.get(0)).into(binding.oneImage1);
                //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
                binding.twoImagLayout1.setVisibility(View.GONE);
                Bitmap photo = null;
                try {
                    photo = ImageUtils.getBitmapFormUri(AddycdjyActivity.this, uri);
                } catch (IOException e) {
                }
                imgBitmap = rotaingImageView(degree, photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                img_Path1 = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "fhjdpic1");
                Log.e(TAG, "fhjdpic: " + img_Path1);
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
                        if (!img_Path1.equals("")){
                            postPictoService1();
                        }else {
                            postDataToService();
                        }

                    }else {
                        Toast.makeText(AddycdjyActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
    private void postPictoService1(){

        RequestParams params = new RequestParams(RequestUtils.SAVE_IMAGE);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        for (int i = 0; i < uriChooseList1.size(); i++) {
            try {

                Uri uri = uriChooseList1.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateTwo, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }
                params.addBodyParameter("file", new File(img_Path1),null,img_Path1);

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
                            imglist1.add(imgStrArray.get(i));
                        }
                            postDataToService();
                    }else {
                        Toast.makeText(AddycdjyActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
    private void showDataDialog(EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }

                date.append(String.valueOf(year));
                if (month < 9){
                    date.append("-0").append(String.valueOf(month + 1));
                }else {
                    date.append("-").append(String.valueOf(month + 1));
                }
                if (day <10){
                    date.append("-0").append(String.valueOf(day));
                } else {
                    date.append("-").append(String.valueOf(day));
                }

                editText.setText(date);


                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog  dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        Calendar date = Calendar.getInstance();
        int year1 = date.get(Calendar.YEAR);
        int month1 = date.get(Calendar.MONTH);
        int day1 = date.get(Calendar.DATE);
        String endData = year1 - 10 + "-" + month1 + "-" + day1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endData);
        } catch (ParseException e) {

        }
        long starTimre = date2.getTime();


        long endTimre = System.currentTimeMillis();

        datePicker.setMaxDate(endTimre);
        datePicker.setMinDate(starTimre);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }
    private void getDataFromService() {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/projectPlanningAndManagement/list");
        params.setAsJsonContent(true);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        params.setBodyContent(jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    Log.e(TAG, "getDataFromService: " + result);
                    JSONArray data = jsonObject1.getJSONArray("data");
                    xmnameList.clear();
                    Gson gson = new Gson();
                    xmjdTypeList = gson.fromJson(String.valueOf(data), new TypeToken<List<xmjd>>(){}.getType());
                    Log.e(TAG, "showxxmcChangeDailog: "+xmjdTypeList.size() );
                    for (int i = 0; i < xmjdTypeList.size(); i++) {
                        xmnameList.add(xmjdTypeList.get(i).getName());
                    }
                    Log.e(TAG, "showxxmcChangeDailog: "+xmnameList.size() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}