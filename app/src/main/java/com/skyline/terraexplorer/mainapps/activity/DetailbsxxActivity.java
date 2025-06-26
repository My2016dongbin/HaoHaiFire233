package com.skyline.terraexplorer.mainapps.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityBsxxAddBinding;
import com.skyline.terraexplorer.databinding.ActivityBsxxDetailBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.Bsxxjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
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

public class DetailbsxxActivity extends BaseActivity implements DatePicker.OnDateChangedListener{
    private ActionBar actionBar;
    private ActivityBsxxDetailBinding binding;
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
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    public boolean isEdit = false;
    private String BX_ID="";
    public Bsxxjd bsxxjd;
    private static final String TAG = Activity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bsxx_detail);
        Intent intent = getIntent();
        BX_ID = intent.getStringExtra("BX_ID");
        Log.e(TAG, "onCreate: "+BX_ID );
        getDataFromService();
        initView();
        initDateTime();
    }

    private void initView() {
        date = new StringBuffer();
        access_token = new DbConfig(this).getUser().getToken();
        imglist=new ArrayList<>();
        uriChooseList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("病树信息");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if (isEdit){
                            actionBar.setRightView("修改");
                            isEdit = false;
                        }else {
                            actionBar.setRightView("取消");
                            isEdit = true;
                        }
                        setEditState();
                        break;
                }
            }
        });
        actionBar.setRightView("修改");
        binding.dcrEdit.setText(new DbConfig(this).getUser().getFullName());
//        RxViewAction.clickNoDouble(binding.oneImageDelete)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (uriChooseList.size() == 1){  //只有一张图
//                            uriChooseList.remove(0);
//                            //  Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
//                            binding.photoLayout.setVisibility(View.GONE);
//                        }else {     //如果有两张图
//                            uriChooseList.remove(0);
//                            Glide.with(getApplicationContext()).load(uriChooseList.get(0)).into(binding.oneImage);
//                            //   Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
//                            binding.oneImageDelete.setVisibility(View.VISIBLE);
//                            binding.twoImageDelete.setVisibility(View.GONE);
//                            binding.twoImagLayout.setVisibility(View.GONE);
//                        }
//                    }
//                });
//        RxViewAction.clickNoDouble(binding.twoImageDelete)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        uriChooseList.remove(1);
//                        // Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
//                        //twoImageDelete.setVisibility(View.GONE);
//                        binding.twoImagLayout.setVisibility(View.GONE);
//                    }
//                });
//        RxViewAction.clickNoDouble(binding.oneImage)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (uriChooseList.size() == 2){
//                            Toast.makeText(DetailbsxxActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
//                        }else {
//                            addImage();
//                        }
//                    }
//                });
        RxViewAction.clickNoDouble(binding.xiugaiButton)
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
                        if (isEdit) {
                            showDataDialog(binding.jianchashijianView);
                        }
                    }
                });
        RxViewAction.clickNoDouble(binding.quxianEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isEdit) {
                            showLeibieChangeDailog(binding.quxianEdit);
                        }
                    }
                });
        RxViewAction.clickNoDouble(binding.fjBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BsfjListActivity.class);
                        intent.putExtra("BX_ID",BX_ID);
                        startActivity(intent);
                    }
                });
        RxViewAction.clickNoDouble(binding.dwBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BsysListActivity.class);
                        intent.putExtra("BS_ID",BX_ID);
                        startActivity(intent);
                    }
                });
        RxViewAction.clickNoDouble(binding.szBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BsszListActivity.class);
                        intent.putExtra("BS_ID",BX_ID);
                        startActivity(intent);
                    }
                });

        RxViewAction.clickNoDouble(binding.xhBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BsxhListActivity.class);
                        intent.putExtra("BS_ID",BX_ID);
                        startActivity(intent);
                    }
                });
    }
    private void setEditState() {

        if (isEdit){
            binding.xiugaiButton.setVisibility(View.VISIBLE);
            binding.smbhEdit.setFocusable(true);
            binding.smbhEdit.setFocusableInTouchMode(true);
            binding.yqxbEdit.setFocusable(true);
            binding.yqxbEdit.setFocusableInTouchMode(true);
            binding.xzjdEdit.setFocusable(true);
            binding.xzjdEdit.setFocusableInTouchMode(true);
            binding.cjwhEdit.setFocusable(true);
            binding.cjwhEdit.setFocusableInTouchMode(true);
            binding.remarkEdit.setFocusable(true);
            binding.remarkEdit.setFocusableInTouchMode(true);
            binding.dizhiEdit.setFocusable(true);
            binding.dizhiEdit.setFocusableInTouchMode(true);
        }else {
            binding.xiugaiButton.setVisibility(View.GONE);
            binding.smbhEdit.setFocusable(false);
            binding.smbhEdit.setFocusableInTouchMode(false);
            binding.yqxbEdit.setFocusable(false);
            binding.yqxbEdit.setFocusableInTouchMode(false);
            binding.xzjdEdit.setFocusable(false);
            binding.xzjdEdit.setFocusableInTouchMode(false);
            binding.cjwhEdit.setFocusable(false);
            binding.cjwhEdit.setFocusableInTouchMode(false);
            binding.remarkEdit.setFocusable(false);
            binding.remarkEdit.setFocusableInTouchMode(false);
            binding.dizhiEdit.setFocusable(false);
            binding.dizhiEdit.setFocusableInTouchMode(false);
        }

    }
    private void postDataToService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("inquirer",binding.dcrEdit.getText().toString());
            jsonObject.put("treeNo",binding.smbhEdit.getText().toString());
            jsonObject.put("surveyTime",binding.jianchashijianView.getText().toString()+" 00:00:00");
            jsonObject.put("county",binding.quxianEdit.getText().toString());
            jsonObject.put("smallPlaces",binding.dizhiEdit.getText().toString());
            jsonObject.put("smallClassNo",binding.yqxbEdit.getText().toString());
            jsonObject.put("town",binding.xzjdEdit.getText().toString());
            jsonObject.put("village",binding.cjwhEdit.getText().toString());
            jsonObject.put("lat",Double.parseDouble(binding.jingduEdit.getText().toString()));
            jsonObject.put("lng",Double.parseDouble(binding.weiduEdit.getText().toString()));
            jsonObject.put("remark",binding.remarkEdit.getText().toString());
            jsonObject.put("treeImage",bsxxjd.getTreeImage());
            jsonObject.put("id",bsxxjd.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/sickTreeInfo");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        Log.e(TAG, "postDataToServiceFromDb---" + jsonObject.toString());
        x.http().request(HttpMethod.PUT,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(DetailbsxxActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(DetailbsxxActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
                photo = ImageUtils.getBitmapFormUri(DetailbsxxActivity.this, uri);
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
                        Toast.makeText(DetailbsxxActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
        binding.jianchashijianView.setText(year+"-"+month+"-"+day);
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
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
    private void getDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/sickTreeInfo");
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("id",BX_ID);
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        Log.e(TAG, "onSuccess: "+data.get(0).toString() );
                        Gson gson = new Gson();
                        bsxxjd = gson.fromJson(data.get(0).toString(), Bsxxjd.class);
                        initData();
                    }else {
                        Toast.makeText(DetailbsxxActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: "+e);
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
    private void initData() {
        Log.e(TAG, "initData: "+bsxxjd.getRemark() );
        binding.jianchashijianView.setText(bsxxjd.getSurveyTime());
        binding.smbhEdit.setText(bsxxjd.getTreeNo());
        binding.xzjdEdit.setText(bsxxjd.getVillage());
        binding.quxianEdit.setText(bsxxjd.getCounty());
        binding.yqxbEdit.setText(bsxxjd.getSmallClassNo());
        binding.cjwhEdit.setText(bsxxjd.getTown());
        binding.jingduEdit.setText( String.valueOf(bsxxjd.getLng()));
        binding.weiduEdit.setText(String.valueOf(bsxxjd.getLat()));
        binding.dizhiEdit.setText(bsxxjd.getSmallPlaces());
        binding.remarkEdit.setText(bsxxjd.getRemark());
        if (bsxxjd.getTreeImage()==null){
            binding.oneImage.setVisibility(View.GONE);
        }else {
            Glide.with(getApplicationContext()).load(bsxxjd.getTreeImage()).into(binding.oneImage);
        }
    }
}