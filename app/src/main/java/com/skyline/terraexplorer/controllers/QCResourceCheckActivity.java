package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruyiruyi.rylibrary.utils.GifSizeFilter;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.CheckField;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.PicModel;
import com.skyline.terraexplorer.models.PostResourceCheck;
import com.skyline.terraexplorer.models.ResourceCheck;
import com.skyline.terraexplorer.multitype.ChooseImage;
import com.skyline.terraexplorer.utils.ImageUtils;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.LinePathView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

import static com.skyline.terraexplorer.controllers.ResourceCheckActivity.drawTextToBitmap;
import static com.skyline.terraexplorer.controllers.ResourceCheckActivity.rotaingImageView;

public class QCResourceCheckActivity extends HBaseActivity implements DatePicker.OnDateChangedListener{
    ImageView back_button;
    Button btn_submit;
    TextView tv_title;
    TextView tv_type;
    TextView tv_grid;
    FrameLayout fl_end_time;
    TextView tv_end_time;
    EditText et_remark;
    FrameLayout fl_worker;
    TextView tv_worker;
    FrameLayout fl_sign;
    FrameLayout fl_tab_left;
    FrameLayout fl_tab_right;
    TextView tv_tab_left;
    TextView tv_tab_right;
    View view_indicator_left;
    View view_indicator_right;
    LinearLayout ll_items_left;
    LinearLayout ll_items_right;
    View view_tab_bg;
    TextView tv_checker;
    PopupWindow popupWindow;

    private int screenWidth;
    private int screenHeight;
    DisplayMetrics dm = new DisplayMetrics();
    private List<CheckField> leftCheckList;
    private List<CheckField> rightCheckList;
    private List<CheckField> checkFieldList;
    private Intent intent;
    private String code;
    private String id;
    private String name;
    private String resourceName;
    private String type;
    private String apiurl;
    private String gridId;
    private String groupId;
    private String gridName;
    private String qu;
    private String jiedao;
    private String gridNo;
    private String leftString = "isFireEquipment isInstitutionWall isGridWall isWarmSafety isPropagandaSlogan isPropagandaSign isStaffOnduty isUseFireCode isPeopleRegister isHadFireRatingSign isCollectFire isSmoke isCombustiblesClear isClearHead";
    private String rightString = "isHadSacrificeFire isHadFireBelt isDeployHolidayWork isCheckHolidayWork workersKnowHolidayWork isBuildSacrificeControlMeasures isCarryOutFireEducation isNofireNotice isCarryOutPatrol isEquipmentUsable";
    private String picUrl = "";//"http://121.36.6.140:80/";

    private ProgressDialog progressDialog;

    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private String CurrentTime;
    private Paint paint;
    private Dialog qianmingDialog;
    private View qianmingInflater;
    private LinePathView mPathView;
    private TextView clearQianmingView;
    private TextView qianmingButton;
    private String signUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcresource_check);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth= dm.widthPixels;
        screenHeight= dm.heightPixels;
        intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        type = intent.getStringExtra("TYPE");
        code = intent.getStringExtra("CODE");
        apiurl = intent.getStringExtra("APIURL");
        gridId = intent.getStringExtra("GRID_ID");
        groupId = intent.getStringExtra("GROUP_ID");
        gridName = intent.getStringExtra("GRID_NAME");
        qu = intent.getStringExtra("QU");
        jiedao = intent.getStringExtra("JIEDAO");
        resourceName = intent.getStringExtra("RESOURCE_NAME");
        gridNo = intent.getStringExtra("GRID_NO");

        initView();
        bindView();
        initData();

    }

    private void initData() {
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        checkFieldList.clear();
        leftCheckList.clear();
        rightCheckList.clear();
        try {
            checkFieldList = db.selector(CheckField.class)
                    .where("resourcetype", "=", code)
                    .findAll();
            if(checkFieldList == null){
                checkFieldList = new ArrayList<>();
            }
            Log.e("TAG", "initCheckFieldToDb:-size-- " + checkFieldList.size() );
            Log.e("TAG", "initCheckFieldToDb:-list-- " + checkFieldList.toString() );

            for (int i = 0; i < checkFieldList.size(); i++) {
                CheckField checkField = checkFieldList.get(i);
                if(leftString.contains(checkField.getCode())){
                    leftCheckList.add(checkField);
                }
                if(rightString.contains(checkField.getCode())){
                    rightCheckList.add(checkField);
                }
            }

            initCheckItems();

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private int currentResourceIndex = 0;
    private boolean currentResourceIsLeft = true;
    private final int CHOOSE_PICTURE = 123;
    private final int TAKE_PICTURE = 1234;
    //渲染检查项
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initCheckItems() {
        ll_items_left.removeAllViews();
        ll_items_right.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        ///日常检查
        for (int i = 0; i < leftCheckList.size(); i++) {
            int finalI = i;
            CheckField resourceCheck = leftCheckList.get(i);
            View leftItem = inflater.inflate(R.layout.resource_check_items,null);
            TextView tv_title = leftItem.findViewById(R.id.tv_title);
            TextView tv_yes = leftItem.findViewById(R.id.tv_yes);
            TextView tv_no = leftItem.findViewById(R.id.tv_no);
            ImageView iv_img = leftItem.findViewById(R.id.iv_img);
            ImageView iv_img_delete = leftItem.findViewById(R.id.iv_img_delete);
            View view_line = leftItem.findViewById(R.id.view_line);
            tv_title.setText(resourceCheck.getName());
            if(resourceCheck.getPicModel()!=null){
                Glide.with(QCResourceCheckActivity.this).load(picUrl+resourceCheck.getPicModel().getFullPath()).into(iv_img);
                iv_img_delete.setVisibility(View.VISIBLE);
            }else{
                iv_img_delete.setVisibility(View.GONE);
            }
            RxViewAction.clickNoDouble(iv_img_delete).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    leftCheckList.get(finalI).setPicModel(null);
                    iv_img.setImageDrawable(getDrawable(R.drawable.no_pic));
                    iv_img_delete.setVisibility(View.GONE);
                }
            });
            if(resourceCheck.getState()==1){
                tv_yes.setBackground(getDrawable(R.drawable.check_left_yes));
                tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                tv_yes.setTextColor(getColor(R.color.white));
                tv_no.setTextColor(getColor(R.color.c6));
            }else if(resourceCheck.getState()==2){
                tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                tv_no.setBackground(getDrawable(R.drawable.check_right_yes));
                tv_yes.setTextColor(getColor(R.color.c6));
                tv_no.setTextColor(getColor(R.color.white));
            }else{
                tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                tv_yes.setTextColor(getColor(R.color.c6));
                tv_no.setTextColor(getColor(R.color.c6));
            }
            RxViewAction.clickNoDouble(tv_yes).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getState()==1){
                        //已选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.c6));
                        leftCheckList.get(finalI).setState(0);
                    }else{
                        //未选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_yes));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.white));
                        tv_no.setTextColor(getColor(R.color.c6));
                        leftCheckList.get(finalI).setState(1);
                    }
                }
            });
            RxViewAction.clickNoDouble(tv_no).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getState()==2){
                        //已选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.c6));
                        leftCheckList.get(finalI).setState(0);
                    }else{
                        //未选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_yes));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.white));
                        leftCheckList.get(finalI).setState(2);
                    }
                }
            });
            int finalI1 = i;
            RxViewAction.clickNoDouble(iv_img).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getPicModel()!=null){
                        //查看图片
                        showPictureInfo(picUrl + resourceCheck.getPicModel().getFullPath());
                    }else{
                        //添加图片
                        currentResourceIndex = finalI1;
                        currentResourceIsLeft = true;
                        picDialog();
                    }
                }
            });
            if(i == leftCheckList.size()-1){
                view_line.setVisibility(View.GONE);
            }

            ll_items_left.addView(leftItem);
        }
        ///督导检查
        for (int i = 0; i < rightCheckList.size(); i++) {
            int finalI = i;
            CheckField resourceCheck = rightCheckList.get(i);
            View rightItem = inflater.inflate(R.layout.resource_check_items,null);
            TextView tv_title = rightItem.findViewById(R.id.tv_title);
            TextView tv_yes = rightItem.findViewById(R.id.tv_yes);
            TextView tv_no = rightItem.findViewById(R.id.tv_no);
            ImageView iv_img = rightItem.findViewById(R.id.iv_img);
            ImageView iv_img_delete = rightItem.findViewById(R.id.iv_img_delete);
            View view_line = rightItem.findViewById(R.id.view_line);
            tv_title.setText(resourceCheck.getName());
            if(resourceCheck.getPicModel()!=null){
                Glide.with(QCResourceCheckActivity.this).load(picUrl+resourceCheck.getPicModel().getFullPath()).into(iv_img);
                iv_img_delete.setVisibility(View.VISIBLE);
            }else{
                iv_img_delete.setVisibility(View.GONE);
            }
            RxViewAction.clickNoDouble(iv_img_delete).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    rightCheckList.get(finalI).setPicModel(null);
                    iv_img.setImageDrawable(getDrawable(R.drawable.no_pic));
                    iv_img_delete.setVisibility(View.GONE);
                }
            });
            if(resourceCheck.getState()==1){
                tv_yes.setBackground(getDrawable(R.drawable.check_left_yes));
                tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                tv_yes.setTextColor(getColor(R.color.white));
                tv_no.setTextColor(getColor(R.color.c6));
            }else if(resourceCheck.getState()==2){
                tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                tv_no.setBackground(getDrawable(R.drawable.check_right_yes));
                tv_yes.setTextColor(getColor(R.color.c6));
                tv_no.setTextColor(getColor(R.color.white));
            }else{
                tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                tv_yes.setTextColor(getColor(R.color.c6));
                tv_no.setTextColor(getColor(R.color.c6));
            }
            RxViewAction.clickNoDouble(tv_yes).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getState()==1){
                        //已选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.c6));
                        rightCheckList.get(finalI).setState(0);
                    }else{
                        //未选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_yes));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.white));
                        tv_no.setTextColor(getColor(R.color.c6));
                        rightCheckList.get(finalI).setState(1);
                    }
                }
            });
            RxViewAction.clickNoDouble(tv_no).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getState()==2){
                        //已选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_no));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.c6));
                        rightCheckList.get(finalI).setState(0);
                    }else{
                        //未选时
                        tv_yes.setBackground(getDrawable(R.drawable.check_left_no));
                        tv_no.setBackground(getDrawable(R.drawable.check_right_yes));
                        tv_yes.setTextColor(getColor(R.color.c6));
                        tv_no.setTextColor(getColor(R.color.white));
                        rightCheckList.get(finalI).setState(2);
                    }
                }
            });
            int finalI1 = i;
            RxViewAction.clickNoDouble(iv_img).subscribe(new Action1<Void>() {
                @Override
                public void call(Void unused) {
                    if(resourceCheck.getPicModel()!=null){
                        //查看图片
                        showPictureInfo(picUrl + resourceCheck.getPicModel().getFullPath());
                    }else{
                        //添加图片
                        currentResourceIndex = finalI1;
                        currentResourceIsLeft = false;
                        picDialog();
                    }
                }
            });
            if(i == rightCheckList.size()-1){
                view_line.setVisibility(View.GONE);
            }

            ll_items_right.addView(rightItem);
        }
    }

    private void showPictureInfo(String picUrl) {
        Intent intent = new Intent(this,Pic1Activity.class);
        intent.putExtra("pic",picUrl);
        startActivity(intent);
    }

    private void picDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_picture, null);
        Button takePhotoBtn = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        Button pickPictureBtn = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.picture_selector_cancel_btn);
        RxViewAction.clickNoDouble(takePhotoBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                takePicture();
                dismissPicDialog();
            }
        });
        RxViewAction.clickNoDouble(pickPictureBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                choosePicture();
                dismissPicDialog();
            }
        });
        RxViewAction.clickNoDouble(cancelBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                dismissPicDialog();
            }
        });

        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }

    public void dismissPicDialog() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private void choosePicture() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
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
    private void takePicture() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Intent openBendiPicIntent = new Intent();
                        openBendiPicIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(openBendiPicIntent, TAKE_PICTURE);
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
        if (requestCode == CHOOSE_PICTURE && resultCode == RESULT_OK) {
            postImageToService(data.getData(),null);
        }else if(requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Bitmap bitmap = data.getParcelableExtra("data");
            postImageToService(null,bitmap);
        }
    }

    private void postImageToService(Uri uri,Bitmap bitmap){
        Bitmap evaluate = null;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = "";
        try {
            dateString = dateFormat.format(date);
            if(uri!=null){
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                Bitmap shuiYinPhoto = drawTextToBitmap(this, photo, dateString + "  " + name, "", "", "", "", "", paint, 10, 40);
                evaluate = rotaingImageView(degree, shuiYinPhoto);
            }else{
                Bitmap photo = ImageUtils.compressImage(bitmap);
                evaluate = drawTextToBitmap(this, photo, dateString + "  " + name, "", "", "", "", "", paint, 10, 40);
            }

        } catch (IOException e) {

        }
        String pic = ImageUtils.savePhoto(evaluate, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),date.getTime() + "pic");

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(pic),null,pic);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e("TAG", "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "postimage: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject obj = (JSONObject) data.get(0);
                        PicModel picModel = new Gson().fromJson(obj.toString(),PicModel.class);
                        //更新源数据
                        if(currentResourceIsLeft){
                            leftCheckList.get(currentResourceIndex).setPicModel(picModel);
                        }else{
                            rightCheckList.get(currentResourceIndex).setPicModel(picModel);
                        }
                        initCheckItems();

                    }else {
                        Toast.makeText(QCResourceCheckActivity.this, "上传失败,请重新选择图片", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError: 请求失败" );
                Toast.makeText(QCResourceCheckActivity.this, "上传失败,请重新选择图片", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void postSignToService(Bitmap bitmap){
        Bitmap evaluate = null;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = "";

        dateString = dateFormat.format(date);

        Bitmap photo = ImageUtils.compressImage(bitmap);
        evaluate = drawTextToBitmap(this, photo, dateString + "  " + name, "", "", "", "", "", paint, 10, 40);

        String pic = ImageUtils.savePhoto(evaluate, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),date.getTime() + "pic");

        Log.e("TAG", "postSignToService: bitmap "  + bitmap );
        Log.e("TAG", "postSignToService: pic "  + pic );
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        File value = new File(pic);
        Log.e("TAG", "postSignToService: value "  + value.getAbsolutePath() );
        params.addBodyParameter("file", value,null,pic);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e("TAG", "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "postimage: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject obj = (JSONObject) data.get(0);
                        PicModel picModel = new Gson().fromJson(obj.toString(),PicModel.class);
                        signUrl = picUrl + picModel.getFullPath();

                    }else {
                        Toast.makeText(QCResourceCheckActivity.this, "上传失败,请重新签名", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError: 请求失败" );
                Toast.makeText(QCResourceCheckActivity.this, "上传失败,请重新签名", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private String signTime = "";
    private void bindView() {
        //提交
        RxViewAction.clickNoDouble(btn_submit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                postDataToService();
            }
        });
        //签名-确认
        RxViewAction.clickNoDouble(qianmingButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                if (mPathView.getTouched()) {
                    try {
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        signTime = dateFormat.format(date);

                        mPathView.save(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + signTime +"qm.png", true, 10);

                        qianmingDialog.dismiss();

                        String qianmingUrlStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + signTime +"qm.png";
                        Bitmap qmBitmap = BitmapFactory.decodeFile(qianmingUrlStr);
                        postSignToService(qmBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", "call: error " + e.toString() );
                    }
                } else {
                    Toast.makeText(QCResourceCheckActivity.this, "您没有签名~请签名后提交", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //签名-清除
        RxViewAction.clickNoDouble(clearQianmingView).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                mPathView.clear();
                mPathView.setBackColor(Color.WHITE);
                mPathView.setPaintWidth(20);
                mPathView.setPenColor(Color.BLACK);
                signUrl = "";
            }
        });
        //签名
        RxViewAction.clickNoDouble(fl_sign).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                qianmingDialog.show();
            }
        });
        //任务截止时间
        RxViewAction.clickNoDouble(fl_end_time).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                initDateTime();
                showDateDialog(tv_end_time);
            }
        });

        RxViewAction.clickNoDouble(fl_tab_left).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                tabSelected(true);
            }
        });
        RxViewAction.clickNoDouble(fl_tab_right).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                tabSelected(false);
            }
        });
        RxViewAction.clickNoDouble(back_button).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                finish();
            }
        });
    }

    private void postDataToService2() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("resourceId",id);
            jsonObject.put("name",name);
            List<CheckField> list = new ArrayList<>();
            for (int i = 0; i < leftCheckList.size(); i++) {
                if(leftCheckList.get(i).state!=0){
                    jsonObject.put(leftCheckList.get(i).getCode(), leftCheckList.get(i).state-1+"");
                    list.add(leftCheckList.get(i));
                }
            }
            for (int i = 0; i < rightCheckList.size(); i++) {
                if(rightCheckList.get(i).state!=0){
                    jsonObject.put(rightCheckList.get(i).getCode(), rightCheckList.get(i).state-1+"");
                    list.add(rightCheckList.get(i));
                }
            }

            // jsonObject.put("type",type);
            jsonObject.put("signaturePic",signUrl);
            for (int i = 0; i < list.size(); i++) {
                Log.e("TAG", "postDataToServiceFromDb: 已上传" + i+1);
                int num = i + 1;
                jsonObject.put("pic" + num ,picUrl + list.get(i).getPicModel().getFullPath());
            }

            jsonObject.put("checkTime",signTime);
            jsonObject.put("description",et_remark.getText().toString());
            jsonObject.put("gridId",gridId);
            jsonObject.put("gridName",gridName);
            jsonObject.put("gridNo",gridNo);

        } catch (JSONException e) {
        }
        Log.e("TAG", "postDataToService: ---" + jsonObject.toString() );
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + apiurl + "Check" );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(QCResourceCheckActivity.this).getUser().getToken());
        params.setConnectTimeout(10000);
        Log.e("TAG", "postDataToServiceFromDb---" + params);
        showDialogProgress(progressDialog,"正在提交..");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(QCResourceCheckActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        Toast.makeText(QCResourceCheckActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError: 请求失败" +ex.toString());
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
    private void postDataToService() {
        if(tv_end_time.getText().toString().contains("点击")){
            Toast.makeText(this, "请先选择检查时间", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if(et_remark.getText().toString().equals("")){
            Toast.makeText(this, "请先输入备注", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(signUrl==null||signUrl.equals("")){
            Toast.makeText(this, "请先进行签名", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean left = false;
        boolean right = false;
        PostResourceCheck postResourceCheck = new PostResourceCheck();
        List<PostResourceCheck.PostField> postFieldList = new ArrayList<>();
        for (int i = 0; i < leftCheckList.size(); i++) {
            CheckField checkField = leftCheckList.get(i);
            if(checkField.getState()!=0){
                /*if(checkField.getPicModel()==null){
                    Toast.makeText(this, "请先上传已选检查项图片", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                left = true;
                PostResourceCheck.PostField postField = new PostResourceCheck.PostField();
                postField.setFieldCode(checkField.getCode());
                postField.setFieldName(checkField.getName());
                postField.setGroupId(checkField.getGroupId());
                postField.setId(checkField.getId());
                try{
                    postField.setImgUrl(checkField.getPicModel().getFullPath());
                }catch (Exception e){
                    Log.e("Exception", "postDataToService: exception getPicModel");
                }
                postField.setQualified(checkField.getState()==1);
                postFieldList.add(postField);
            }
        }
        for (int i = 0; i < rightCheckList.size(); i++) {
            CheckField checkField = rightCheckList.get(i);
            if(checkField.getState()!=0){
                /*if(checkField.getPicModel()==null){
                    Toast.makeText(this, "请先上传已选检查项图片", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                right = true;
                PostResourceCheck.PostField postField = new PostResourceCheck.PostField();
                postField.setFieldCode(checkField.getCode());
                postField.setFieldName(checkField.getName());
                postField.setGroupId(checkField.getGroupId());
                postField.setId(checkField.getId());
                try{
                    postField.setImgUrl(checkField.getPicModel().getFullPath());
                }catch (Exception e){
                    Log.e("Exception", "postDataToService: exception getPicModel");
                }
                postField.setQualified(checkField.getState()==1);
                postFieldList.add(postField);
            }
        }
        postResourceCheck.setList(postFieldList);
        //检查类型：1日常检查；2督导检查；3自选检查
        if(left){
            if(right){
                postResourceCheck.setCheckType(3);
            }else{
                postResourceCheck.setCheckType(1);
            }
        }else{
            if(right){
                postResourceCheck.setCheckType(2);
            }else{
                Toast.makeText(this, "您还没有编辑检查项", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = dateFormat.format(date);
        postResourceCheck.setCheckTime(tv_end_time.getText().toString()+time.substring(10,19));
        postResourceCheck.setCheckUser(tv_checker.getText().toString());
        postResourceCheck.setSignaturePic(signUrl);
        postResourceCheck.setDescription(et_remark.getText().toString());
        postResourceCheck.setGridId(gridId);
        postResourceCheck.setGridName(gridName);
        postResourceCheck.setGridNo(gridNo);
        postResourceCheck.setGroupId(groupId);
        postResourceCheck.setResourceId(id);
        postResourceCheck.setName(name);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_BASE_ + apiurl + "CheckNew" );
        params.setAsJsonContent(true);
        String content = new Gson().toJson(postResourceCheck);
        params.setBodyContent(content);
        params.addHeader("Authorization","bearer " + new DbConfig(QCResourceCheckActivity.this).getUser().getToken());
        params.setConnectTimeout(10000);
        Log.e("TAG", "postDataToServiceFromDb---" + params);
        Log.e("TAG", "postDataToServiceFromDb---content" + content);
        showDialogProgress(progressDialog,"正在提交..");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(QCResourceCheckActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        Toast.makeText(QCResourceCheckActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError: 请求失败" +ex.toString());
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

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }


    private void showDateDialog(final TextView textView) {
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
                textView.setText(date);
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

        //datePicker.setMaxDate(endTimre);
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

        int monthnow=month+1;
        CurrentTime = year + "-" + monthnow + "-" + day;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    private void initView() {
        back_button = findViewById(R.id.back_button);

        btn_submit = findViewById(R.id.btn_submit);

        tv_title = findViewById(R.id.tv_title);
        tv_type = findViewById(R.id.tv_type);
        tv_grid = findViewById(R.id.tv_grid);
        tv_title.setText(name.replace("null",""));
        tv_type.setText(resourceName.replace("null",""));
        tv_grid.setText(qu+"/"+jiedao);

        fl_end_time = findViewById(R.id.fl_end_time);
        tv_end_time = findViewById(R.id.tv_end_time);

        et_remark = findViewById(R.id.et_remark);

        fl_worker = findViewById(R.id.fl_worker);
        tv_worker = findViewById(R.id.tv_worker);

        fl_sign = findViewById(R.id.fl_sign);

        fl_tab_left = findViewById(R.id.fl_tab_left);
        fl_tab_right = findViewById(R.id.fl_tab_right);
        tv_tab_left = findViewById(R.id.tv_tab_left);
        tv_tab_right = findViewById(R.id.tv_tab_right);
        view_indicator_left = findViewById(R.id.view_indicator_left);
        view_indicator_right = findViewById(R.id.view_indicator_right);
        ll_items_left = findViewById(R.id.ll_items_left);
        ll_items_right = findViewById(R.id.ll_items_right);
        view_tab_bg = findViewById(R.id.view_tab_bg);

        tabSelected(true);

        tv_checker = findViewById(R.id.tv_checker);
        tv_checker.setText(new DbConfig(this).getUser().getFullName());

        leftCheckList = new ArrayList<>();
        rightCheckList = new ArrayList<>();
        checkFieldList = new ArrayList<>();
        date=new StringBuffer();
        progressDialog = new ProgressDialog(this);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);


        qianmingDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        qianmingInflater = LayoutInflater.from(this).inflate(R.layout.dialog_qianming,null);
        qianmingInflater.setMinimumWidth(10000);
        mPathView = ((LinePathView) qianmingInflater.findViewById(R.id.path_view));
        clearQianmingView = ((TextView) qianmingInflater.findViewById(R.id.clear_qianming_view));
        qianmingButton = ((TextView) qianmingInflater.findViewById(R.id.qianming_button));

        qianmingDialog.setContentView(qianmingInflater);
        Window qianmingDialogWindow = qianmingDialog.getWindow();
        qianmingDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpSearch = qianmingDialogWindow.getAttributes();
        qianmingDialogWindow.setAttributes(lpSearch);
        qianmingDialog.setCanceledOnTouchOutside(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void tabSelected(boolean isLeft){
        LinearLayout.LayoutParams layoutParamsLeft = (LinearLayout.LayoutParams) fl_tab_left.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsRight = (LinearLayout.LayoutParams) fl_tab_right.getLayoutParams();
        ViewGroup.LayoutParams layoutParamsBg = view_tab_bg.getLayoutParams();
        layoutParamsBg.height = screenHeight/16;
        view_tab_bg.setLayoutParams(layoutParamsBg);
        if(isLeft){
            layoutParamsLeft.height = screenHeight/14;
           fl_tab_left.setLayoutParams(layoutParamsLeft);
           layoutParamsRight.height = screenHeight/16;
           fl_tab_right.setLayoutParams(layoutParamsRight);
           fl_tab_left.setBackground(getDrawable(R.drawable.tab_left));
           fl_tab_right.setBackgroundColor(getColor(R.color.transparent));
           tv_tab_left.setTextColor(getColor(R.color.black));
           tv_tab_right.setTextColor(getColor(R.color.c6));
           view_indicator_left.setVisibility(View.VISIBLE);
           view_indicator_right.setVisibility(View.GONE);
            ll_items_left.setVisibility(View.VISIBLE);
            ll_items_right.setVisibility(View.GONE);
       }else{
            layoutParamsLeft.height = screenHeight/16;
           fl_tab_left.setLayoutParams(layoutParamsLeft);
           layoutParamsRight.height = screenHeight/14;
           fl_tab_right.setLayoutParams(layoutParamsRight);
           fl_tab_left.setBackgroundColor(getColor(R.color.transparent));
           fl_tab_right.setBackground(getDrawable(R.drawable.tab_right));
           tv_tab_left.setTextColor(getColor(R.color.c6));
           tv_tab_right.setTextColor(getColor(R.color.black));
           view_indicator_left.setVisibility(View.GONE);
           view_indicator_right.setVisibility(View.VISIBLE);
            ll_items_left.setVisibility(View.GONE);
            ll_items_right.setVisibility(View.VISIBLE);
       }
    }
}