package com.haohai.platform.firelibrary.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.model.LatLng;
import com.haohai.platform.firelibrary.ui.model.OneBodyFire;
import com.haohai.platform.firelibrary.ui.multitype.Empty;
import com.haohai.platform.firelibrary.ui.multitype.EmptyViewBinder;
import com.haohai.platform.firelibrary.ui.multitype.FireMission;
import com.haohai.platform.firelibrary.ui.multitype.FireStatistics;
import com.haohai.platform.firelibrary.ui.multitype.FireStatisticsViewBinder;
import com.haohai.platform.firelibrary.ui.multitype.SenceFooter;
import com.haohai.platform.firelibrary.ui.multitype.SenceFooterViewBinder;
import com.haohai.platform.firelibrary.utils.LatLngChange;
import com.haohai.platform.firelibrary.utils.MyLinearLayoutManager;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class FireMissionInfoActivity extends HhBaseActivity implements INaviInfoCallback, SenceFooterViewBinder.OnSenceFooterItemClick {
    private static final String TAG = FireMissionInfoActivity.class.getSimpleName();
    public static final int SCENE_UPLOAD = 678;
    private ActionBar actionBar;
    private String id;

    private ProgressDialog progressDialog;
    private FireMission fireMission;
    private TextView kaishishijianView;
    private TextView jieshushijianView;
    private TextView zhixingrenView;
    private TextView faburenView;
    private TextView renwuneirongView;
    private TextView jingduView;
    private TextView weiduView;
    private ImageView imageOneView;
    private ImageView imageTwoView;
    private ImageView imageThreeView;
    private TextView orderStateView;
    private TextView kaishirenwuView;
    private TextView daozheliView;
    private boolean isChange = false;
    private TextView shangbaoButton;
    private TextView baojingView;

    private Dialog oneBodyFireDialog;
    private View oneBodyFireInflater;
    private TextView mingchengView;
    private TextView dizhiView;
    private TextView shijianView;
    private TextView jingweiduView;
    private ImageView yitijiOneView;
    private ImageView yitijiTwoView;
    private boolean isGaojiFind = false;
    private TextView resourcenameview;
    private TextView resoucedizhiview;
    private TextView resourcejingweiduview;
    private Button kejianguangbutton;
    private Button rechengxiangbutton;
    private Dialog resourceListDialog;
    private View resourceListInflater;
    private RecyclerView resourceListView;
    private LinearLayout zhenshiLayout;
    private TextView zhenshiButton;
    private TextView wubaoButton;
    private TextView zhenshiTextView;
    private TextView dizhiDialogView;
    private LinearLayout daohangLayout;
    private TextView oneBodyKJGButton;
    private TextView oneBodyRCXButton;
    private LinearLayout oneBodyShipinLayout;
    private LinearLayout ll_taskinfo;
    private OneBodyFire fireInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_mission_info);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        isChange = false;
        initView();
        bindView();

        getDataFromService();
    }

    private void bindView() {
        /**
         * 一体机火点图片1点击
         */
        RxViewAction.clickNoDouble(yitijiOneView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (fireInfo != null){
                            Intent intent = new Intent(getApplicationContext(), Pic1Activity.class);
                            intent.putExtra("pic",fireInfo.getPicPath1());
                            startActivity(intent);
                        }

                    }
                });
        /**
         * 一体机火点图片2 点击
         */
        RxViewAction.clickNoDouble(yitijiTwoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (fireInfo != null){
                            Intent intent = new Intent(getApplicationContext(), Pic1Activity.class);
                            intent.putExtra("pic",fireInfo.getPicPath2());
                            startActivity(intent);
                        }

                    }
                });
        /**
         * 查看报警详情
         */
        RxViewAction.clickNoDouble(baojingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        oneBodyFireDialog.show();
                    }
                });
        /**
         * 到这里
         */
        RxViewAction.clickNoDouble(daozheliView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                      /*  //构建导航组件配置类，没有传入起点，所以起点默认为 “我的位置”
                        AmapNaviParams params = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER, AmapPageType.ROUTE);
//启动导航组件
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);*/
                        String jingweiStr = getLocation();
                        String starweidu = "";
                        String starjingdu = "";
                        if (!jingweiStr.isEmpty()) {
                            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                            starweidu = jingweiList.get(1);
                            starjingdu = jingweiList.get(0);
                        }

                        LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new LatLng(fireMission.getPosition().getLat(), fireMission.getPosition().getLng()));

                        Poi start = new Poi("", new com.amap.api.maps.model.LatLng(Double.parseDouble(starweidu), Double.parseDouble(starjingdu)), "");
                        Poi end = new Poi(fireMission.getReserve(), new com.amap.api.maps.model.LatLng(latLng.latitude, latLng.longitude), "");
                        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                        params.setUseInnerVoice(true);
                        Log.e(TAG, "call: start" + starweidu );
                        Log.e(TAG, "call: end" + latLng.latitude );
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, FireMissionInfoActivity.this);
                    }
                });
        /**
         * 开始任务
         */
        RxViewAction.clickNoDouble(kaishirenwuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (fireMission.getStatus() != 2){
                            changeStateToService();
                        }else {
                            Toast.makeText(FireMissionInfoActivity.this, "任务已完成", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        /**
         * 现场上报
         */
        RxViewAction.clickNoDouble(shangbaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), FireSceneActivity.class);
                        intent.putExtra("ID",fireMission.getId());
//                        startActivity(intent);
                        startActivityForResult(intent,SCENE_UPLOAD);
                    }
                });
    }


    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        ll_taskinfo = ((LinearLayout) findViewById(R.id.ll_taskinfo));
        actionBar.setTitle("任务详情");
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

        /**
         *  一体机火点详细信息
         */
        oneBodyFireDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        oneBodyFireInflater = LayoutInflater.from(this).inflate(R.layout.dialog_fire_info, null);
        oneBodyFireInflater.setMinimumWidth(10000);
        mingchengView = ((TextView) oneBodyFireInflater.findViewById(R.id.mingcheng_view));
        dizhiDialogView = ((TextView) oneBodyFireInflater.findViewById(R.id.dizhi_dialog_view));
        shijianView = ((TextView) oneBodyFireInflater.findViewById(R.id.shijian_view));
        jingweiduView = ((TextView) oneBodyFireInflater.findViewById(R.id.jingweidu_view));
        yitijiOneView = ((ImageView) oneBodyFireInflater.findViewById(R.id.yitiji_one_image));
        yitijiTwoView = ((ImageView) oneBodyFireInflater.findViewById(R.id.yitiji_two_image));
        zhenshiLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.zhenshi_layout));
        zhenshiButton = ((TextView) oneBodyFireInflater.findViewById(R.id.zhenshi_button));
        wubaoButton = ((TextView) oneBodyFireInflater.findViewById(R.id.wubao_button));
        zhenshiTextView = ((TextView) oneBodyFireInflater.findViewById(R.id.zhenshi_text_view));
        daohangLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.daohang_layout));
        oneBodyKJGButton = ((TextView) oneBodyFireInflater.findViewById(R.id.kejianguang_button));
        oneBodyRCXButton = ((TextView) oneBodyFireInflater.findViewById(R.id.rechengxiang_button));
        oneBodyShipinLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.onebody_shipin_layout));

        oneBodyFireDialog.setContentView(oneBodyFireInflater);
        Window oneBodyfireDialogWindow = oneBodyFireDialog.getWindow();
        oneBodyfireDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams oneBodyLpFire = oneBodyfireDialogWindow.getAttributes();
        oneBodyfireDialogWindow.setAttributes(oneBodyLpFire);
        oneBodyFireDialog.setCanceledOnTouchOutside(true);



        baojingView = (TextView) findViewById(R.id.baojing_view);
        shangbaoButton = (TextView) findViewById(R.id.shangbao_button);
        kaishirenwuView = (TextView) findViewById(R.id.kaishirenwu_view);
        daozheliView = (TextView) findViewById(R.id.daozheli_view);

        kaishishijianView = (TextView) findViewById(R.id.kaishishijian_view);
        jieshushijianView = (TextView) findViewById(R.id.jieshushijian_view);
        zhixingrenView = (TextView) findViewById(R.id.zhixingren_view);
        faburenView = (TextView) findViewById(R.id.faburen_view);
        renwuneirongView = (TextView) findViewById(R.id.renwuneirong_view);
        jingduView = (TextView) findViewById(R.id.jingdu_view);
        weiduView = (TextView) findViewById(R.id.weidu_view);
        imageOneView = (ImageView) findViewById(R.id.image_one_view);
        imageTwoView = (ImageView) findViewById(R.id.image_two_view);
        imageThreeView = (ImageView) findViewById(R.id.image_three_view);
        dizhiView = (TextView) findViewById(R.id.dizhi_view);
    }

    /**
     * 更改任务单状态
     */
    private void changeStateToService() {
        showDialogProgress(progressDialog,"提交中...");
        JSONObject jsonObject = new JSONObject();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDate = sdf.format(date);

        try {
            //0未开始，1执行中，2已结束
            if (fireMission.getStatus() == 0){
                jsonObject.put("status", 1);
            }else if(fireMission.getStatus() == 1){
                jsonObject.put("status", 2);
                jsonObject.put("taskEndTime", endDate);
            }
            jsonObject.put("id",fireMission.getId());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskManagement");
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "changeStateToService: " + params);
        Log.e(TAG, "changeStateToService: " + jsonObject.toString());
        x.http().request(HttpMethod.PUT,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideDialogProgress(progressDialog);
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        isChange = true;
                       getDataFromService();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError1: " + ex.toString());
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


    /**
     * 结束任务单状态
     */
    private void endStateToService() {
        JSONObject jsonObject = new JSONObject();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDate = sdf.format(date);

        try {
            //0未开始，1执行中，2已结束
            if (fireMission.getStatus() == 0){
                jsonObject.put("status", 2);
                jsonObject.put("taskEndTime", endDate);
            }else if(fireMission.getStatus() == 1){
                jsonObject.put("status", 2);
                jsonObject.put("taskEndTime", endDate);
            }else{
                getDataFromService();
                return;
            }
            jsonObject.put("id",fireMission.getId());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskManagement");
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "changeStateToService: " + params);
        Log.e(TAG, "changeStateToService: " + jsonObject.toString());
        x.http().request(HttpMethod.PUT,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        isChange = true;
                        getDataFromService();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError1: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void getDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskManagement");
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("id",id);
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideDialogProgress(progressDialog);
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Gson gson = new Gson();
                        fireMission = gson.fromJson(data.toString(), FireMission.class);
                        initData();
                        getFireInfoFromService();
                        getUploadInfoFromService(); //TODO 上报信息展示
                    }else {
                        Toast.makeText(FireMissionInfoActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }



                  /*  for (int i = 0; i < data.length(); i++) {
                        JSONObject dto = data.getJSONObject(i).getJSONObject("dto");
                        FlowApprove flowApprove = gson.fromJson(dto.toString(), FlowApprove.class);
                        flowApproveList.add(flowApprove);
                    }*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError2: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCENE_UPLOAD && resultCode == RESULT_OK && data != null) {
            //现场上报提交完毕
            if(Objects.equals(data.getStringExtra("status"), "ok")){
                endStateToService();
            }
        }
    }

    /**
     * 获取火点详情
     */
    private void getFireInfoFromService() {
        RequestParams params = null;
        if (fireMission.getTaskType().equals("2")) {
            params =  new RequestParams(RequestUtils.REQUEST_URL + "fire/api/monitorFirealarm");
        }else if (fireMission.getTaskType().equals("5")){
            params =  new RequestParams(RequestUtils.REQUEST_URL + "fire/api/BuildingFirealarm");
        }else {

        }

        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("id",fireMission.getFireId());
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccessfireinfo: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                        Gson gson = new Gson();
                        fireInfo = gson.fromJson(data.toString(), OneBodyFire.class);


                        initFireData();
                    }else {
                     //   Toast.makeText(FireMissionInfoActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }



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

    private List<SenceFooter> footerList = new ArrayList<>();
    private List<Object> items = new ArrayList<>();
    /**
     * 获取上报详情
     */
    private void getUploadInfoFromService() {
        footerList.clear();
        items.clear();

        RequestParams params = null;
        params =  new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskDetail/list");

        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("taskId",fireMission.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "postData Upload:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccessfireinfo Upload: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject model = (JSONObject) data.get(i);
                            SenceFooter senceFooter = new SenceFooter();
                            senceFooter.setTime(model.getString("createTime").substring(0,19).replace("T"," "));
                            senceFooter.setDescs(model.getString("siteConditions"));
                            senceFooter.setOther(model.getString("otherConditions"));
                            senceFooter.setImages(model.getString("imgUrl"));
                            senceFooter.setVideos(model.getString("videoUrl"));
                            try{
                                senceFooter.setLatitude(model.getDouble("latitude")+"");
                                senceFooter.setLongitude(model.getDouble("longitude")+"");
                            }catch(Exception e){

                            }

                            footerList.add(senceFooter);
                        }
                        Log.e(TAG, "onSuccess: footerList = " + footerList.toString() );


                        try{
                            ll_taskinfo.removeViewAt(10);

                            View uploadView = LayoutInflater.from(FireMissionInfoActivity.this).inflate(R.layout.footer_sence, null);
                            RecyclerView rl_footer = uploadView.findViewById(R.id.rl_footer);
                            MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(FireMissionInfoActivity.this, LinearLayoutManager.VERTICAL, false);
                            linearLayoutManager.setScrollEnabled(true);
                            rl_footer.setLayoutManager(linearLayoutManager);
                            MultiTypeAdapter adapter = new MultiTypeAdapter(items);

                            SenceFooterViewBinder senceFooterViewBinder = new SenceFooterViewBinder(FireMissionInfoActivity.this);
                            senceFooterViewBinder.setListener(FireMissionInfoActivity.this);
                            adapter.register(SenceFooter.class, senceFooterViewBinder);
                            adapter.register(Empty.class,new EmptyViewBinder());

                            rl_footer.setAdapter(adapter);
                            assertHasTheSameAdapter(rl_footer, adapter);

                            for (int i = 0; i < footerList.size(); i++) {
                                items.add(footerList.get(i));
                            }
                            assertAllRegistered(adapter,items);
                            adapter.notifyDataSetChanged();

                            ll_taskinfo.addView(uploadView);
                        }catch(Exception e){
                            View uploadView = LayoutInflater.from(FireMissionInfoActivity.this).inflate(R.layout.footer_sence, null);
                            RecyclerView rl_footer = uploadView.findViewById(R.id.rl_footer);
                            MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(FireMissionInfoActivity.this, LinearLayoutManager.VERTICAL, false);
                            linearLayoutManager.setScrollEnabled(true);
                            rl_footer.setLayoutManager(linearLayoutManager);
                            MultiTypeAdapter adapter = new MultiTypeAdapter(items);

                            SenceFooterViewBinder senceFooterViewBinder = new SenceFooterViewBinder(FireMissionInfoActivity.this);
                            senceFooterViewBinder.setListener(FireMissionInfoActivity.this);
                            adapter.register(SenceFooter.class, senceFooterViewBinder);
                            adapter.register(Empty.class,new EmptyViewBinder());

                            rl_footer.setAdapter(adapter);
                            assertHasTheSameAdapter(rl_footer, adapter);

                            for (int i = 0; i < footerList.size(); i++) {
                                items.add(footerList.get(i));
                            }
                            assertAllRegistered(adapter,items);
                            adapter.notifyDataSetChanged();

                            ll_taskinfo.addView(uploadView);
                        }

                    }else {
                     //   Toast.makeText(FireMissionInfoActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }



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
            }
        });
    }

    private void initFireData() {
        mingchengView.setText(fireInfo.getName());
        dizhiView.setText(fireInfo.getAddress());
        shijianView.setText(fireInfo.getAlarmDatetime().replace("T", " ").substring(0, fireInfo.getAlarmDatetime().indexOf(".")));
        jingweiduView.setText(fireInfo.getAlarmLongitude() + "、" + fireInfo.getAlarmLatitude());
        Log.e(TAG, "initOneBodyFireModelData:getPicPath1== " + fireInfo.getPicPath1());
        Log.e(TAG, "initOneBodyFireModelData:getPicPath2== " + fireInfo.getPicPath2());
        Picasso.with(this).load(fireInfo.getPicPath1()).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(yitijiOneView);
        if (fireInfo.getPicPath2() == null) {
            yitijiTwoView.setVisibility(View.INVISIBLE);
        } else {
            yitijiTwoView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(fireInfo.getPicPath2()).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(yitijiTwoView);

        }

        if ((fireInfo.getVideoPath1() == null && fireInfo.getVideoPath2() == null)) {
            oneBodyShipinLayout.setVisibility(View.GONE);
            Log.e(TAG, "initOneBodyFireModelData:1 " );
        } else {
            Log.e(TAG, "initOneBodyFireModelData:2" );
            oneBodyShipinLayout.setVisibility(View.VISIBLE);
        }

        if (fireInfo.getVideoPath1() == null) {
            Log.e(TAG, "initOneBodyFireModelData:3 " );
            oneBodyKJGButton.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "initOneBodyFireModelData:4 " );
            oneBodyKJGButton.setVisibility(View.VISIBLE);
        }
        if (fireInfo.getVideoPath2() == null) {
            Log.e(TAG, "initOneBodyFireModelData:5 " );
            oneBodyRCXButton.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "initOneBodyFireModelData:6 " );
            oneBodyRCXButton.setVisibility(View.VISIBLE);
        }

        if (fireInfo.getIsReal() == null) {
            zhenshiLayout.setVisibility(View.VISIBLE);
            zhenshiTextView.setVisibility(View.GONE);
        } else {
            zhenshiLayout.setVisibility(View.GONE);
            zhenshiTextView.setVisibility(View.VISIBLE);
            if (fireInfo.getIsReal() == 0) {
                zhenshiTextView.setText("疑似火情");
            } else {
                zhenshiTextView.setText("真实火情");
            }
        }
    }

    private void initData() {
        kaishishijianView.setText(fireMission.getTaskStartTime());
        jieshushijianView.setText(fireMission.getTaskEndTime()==null?"":fireMission.getTaskEndTime());
        zhixingrenView.setText(fireMission.getOperatorName());
        renwuneirongView.setText(fireMission.getTaskContent());
        jingduView.setText(fireMission.getPosition().getLng() +" ");
        weiduView.setText(fireMission.getPosition().getLat() + " ");
        dizhiView.setText(fireMission.getReserve());

        //0未开始，1执行中，2已结束
        if (fireMission.getStatus() == 0) {
            kaishirenwuView.setText("未开始");
        }else if (fireMission.getStatus() == 1){
            kaishirenwuView.setText("执行中");
        }else if (fireMission.getStatus() == 2){
            kaishirenwuView.setText("已结束");
        }

        String[] imgArray = fireMission.getTaskImg().split(",");
        if (imgArray.length==1){
            imageOneView.setVisibility(View.VISIBLE);
            imageTwoView.setVisibility(View.GONE);
            imageThreeView.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(imgArray[0]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageOneView);
        }else if (imgArray.length==2){
            imageOneView.setVisibility(View.VISIBLE);
            imageTwoView.setVisibility(View.VISIBLE);
            imageThreeView.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(imgArray[0]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageOneView);
            Glide.with(getApplicationContext()).load(imgArray[1]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageTwoView);
        }else {
            imageOneView.setVisibility(View.VISIBLE);
            imageTwoView.setVisibility(View.VISIBLE);
            imageThreeView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(imgArray[0]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageOneView);
            Glide.with(getApplicationContext()).load(imgArray[1]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageTwoView);
            Glide.with(getApplicationContext()).load(imgArray[2]).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(imageThreeView);
        }
    }
    @Override
    public void onBackPressed() {
        if (isChange){
            setResult(FireMissionListActivity.ORDER_CHANGE);
            finish();
        }else {

            finish();
        }

    }

    /**
     * 获取当前位置经纬度
     *
     * @return
     */
    @JavascriptInterface
    public String getLocation() {
        //获得位置服务
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        String provider = judgeProvider(locationManager);
        //有位置提供器的情况
        List<String> providerList = locationManager.getProviders(true);
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();

        }
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            try {
                return location.getLongitude() + "," + location.getLatitude();
            } catch (Exception e) {
                return "0.00,0.00";
            }

        }
        return null;
    }

    /**
     * 定位器provider
     *
     * @param locationManager
     * @return
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;//网络定位
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        } else {
            Toast.makeText(this, "未开启本应用地理位置信息，请先开启！", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }

    @Override
    public void videoClick(String video) {
        //视频预览
        Intent intent = new Intent(FireMissionInfoActivity.this, PlayerActivity.class);
        //     intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/af7ae7cb-d637-66cb-c1cc-ab86af9cc9af-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
        intent.putExtra("PLAYER_URL",video);
        intent.putExtra("PLAYER_NAME", "视频详情");
        startActivity(intent);
    }
}
