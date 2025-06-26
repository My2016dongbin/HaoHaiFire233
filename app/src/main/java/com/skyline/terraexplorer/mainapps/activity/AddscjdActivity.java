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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.bean.Addscjd;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.Checkfhjd;
import com.skyline.terraexplorer.mainapps.multitype.CheckfhjdViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.CheckslyeditViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Checksyledit;
import com.skyline.terraexplorer.mainapps.multitype.LcpInfo;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class AddscjdActivity extends BaseActivity{
    private static final String TAG = AddscjdActivity.class.getSimpleName();;
    private ActionBar actionBar;
    private String code;
    private EditText jingduEdit;
    private EditText weiduEdit;
    private EditText fzrEdit;
    private EditText lxdhEdit;
    private EditText dizhiEdit;
    private EditText quxianEdit;
    private EditText beizhuEdit;
    private EditText jdnameEdit;
    private TextView tijiaoButton;
    private String access_token;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private fhjdReceiver Receiver;
    private int nonum=0;
    private List<Object> imglist;
    private String videostr="";
    private Button addLcp;
    private List<LcpInfo> lcpInfoList=new ArrayList<>();
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scjd_add);
        access_token = new DbConfig(this).getUser().getToken();
        imglist=new ArrayList<>();
        initView();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加生产基地");
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
        db=new DbConfig(this).getDbManager();
        tijiaoButton = findViewById(R.id.xiugai_button);
        quxianEdit = findViewById(R.id.quxian_edit);
        jingduEdit= findViewById(R.id.jingdu_edit);
        weiduEdit=findViewById(R.id.weidu_edit);
        fzrEdit=findViewById(R.id.fcr_edit);
        lxdhEdit=findViewById(R.id.lxdh_edit);
        dizhiEdit=findViewById(R.id.dizhi_edit);
        beizhuEdit=findViewById(R.id.beizhu_edit);
        jdnameEdit=findViewById(R.id.jdname_edit);
        addLcp=findViewById(R.id.add_lcp);
        progressDialog = new ProgressDialog(this);
        Receiver = new fhjdReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("GPSinfo");
        registerReceiver(Receiver, filter);

        RxViewAction.clickNoDouble(tijiaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialogProgress(progressDialog,"正在上传");
                        postDataToService();
                    }
                });
        RxViewAction.clickNoDouble(quxianEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog(quxianEdit);
                    }
                });
        RxViewAction.clickNoDouble(addLcp)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent=new Intent(AddscjdActivity.this, AddlcpActivity.class);
                        startActivity(intent);
                    }
                });
    }
    private void postDataToService() {
        try {
            lcpInfoList=db.selector(LcpInfo.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        Addscjd addscjd =new Addscjd();
        addscjd.setHead(fzrEdit.getText().toString());
        addscjd.setForestProductInfoDTOList(lcpInfoList);
        if (!weiduEdit.getText().toString().equals("")){
            addscjd.setLat(Double.parseDouble(weiduEdit.getText().toString()));
        }
        if (!jingduEdit.getText().toString().equals("")){
            addscjd.setLng(Double.parseDouble(jingduEdit.getText().toString()));
        }

        addscjd.setName(jdnameEdit.getText().toString());
        addscjd.setPhone(lxdhEdit.getText().toString());
        addscjd.setRemark(beizhuEdit.getText().toString());
        addscjd.setHead(fzrEdit.getText().toString());
        addscjd.setAddress(dizhiEdit.getText().toString());
        addscjd.setRegion(quxianEdit.getText().toString());
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(addscjd);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "forestry/api/forestProductProductionBaseInfo" );
        params.setAsJsonContent(true);
        params.setBodyContent(json1.toString());
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        Log.e(TAG, "postDataToServiceFromDb---" + json1.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(AddscjdActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddscjdActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
    class fhjdReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("message");
            //Toast.makeText(context, "广播已经接收", Toast.LENGTH_SHORT).show();
            Log.e("fhjdReceive: ", msg);
            try {
                JSONObject result = new JSONObject(msg);
                JSONObject position = (JSONObject) result.get("position");
                jingduEdit.setText(position.get("lng").toString());
                weiduEdit.setText(position.get("lat").toString());
                dizhiEdit.setText(position.get("address").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}