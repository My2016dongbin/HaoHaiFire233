package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HhBaseActivity;
import com.skyline.terraexplorer.db.CemeteryDTO;
import com.skyline.terraexplorer.db.CheckField;
import com.skyline.terraexplorer.db.CheckStationDTO;
import com.skyline.terraexplorer.db.DangerSourceDTO;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.FireCommandDTO;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.db.HelicopterPointDTO;
import com.skyline.terraexplorer.db.JobOrder;
import com.skyline.terraexplorer.db.MaterialRepositoryDTO;
import com.skyline.terraexplorer.db.MonitorDTO;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.db.TeamDTO;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.db.WatchTowerDTO;
import com.skyline.terraexplorer.db.WaterSourceDTO;
import com.skyline.terraexplorer.multitype.Plan;
import com.skyline.terraexplorer.multitype.PlanOrder;
import com.skyline.terraexplorer.utils.HhRequestParams;
import com.skyline.terraexplorer.utils.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LaunchActivity extends HhBaseActivity {

    public boolean isHasPermission = true;
    private static final int GO_NEXT = 99;
    private static final int GO_MAIN = 100;
    private static final int GO_GUIDE = 101;
    private static final int GO_END = 102;
    private static final int GO_DOWN = 103;
    private static final int GO_NEXT_TIME = 1000;
    private static final String TAG = LaunchActivity.class.getSimpleName();
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_NEXT:
                    //initDingwei();
                    //判断是否为第一次登陆
                    JudgeToMain();
                    break;
                case GO_MAIN:
                    //开启服务下载
                    //   StartDownlodeService();
                    handler.sendEmptyMessageDelayed(GO_END, 0);

                    break;

                case GO_END:
                    User user = new DbConfig(getApplicationContext()).getUser();
                    //   Log.e(TAG, "handleMessage: " + user.getIsLogin());
                    if (user == null){      //用户不存在
                        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("FROM","launch");
                        startActivity(intent);*/
                        getAccount();
                       // finish();
                    }else {
                        if (isDataChange){
                            user.setDataIsChange(isDataChange);
                            DbConfig dbConfig = new DbConfig(getApplicationContext());
                            DbManager db = dbConfig.getDbManager();
                            try {
                                db.saveOrUpdate(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                        if (user.getIsLogin() == 0) {       //用户未登录
                            /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("FROM","launch");
                            startActivity(intent);*/
                            getAccount();
                           // finish();
                        }else {
                            String userName = user.getUserName();
                            String userPasswd = user.getUserPasswd();
                            //每次打开app重新获取token  防止token过期  请求完不管成不成功都前往主页
                            loginGetUserToken(userName,userPasswd);

                        }
                    }
                    break;
            }

        }
    };
    private OutLoginReceiver outLoginReceiver;
    private String plan_id;

    private void loginGetUserToken(String userName, String userPasswd) {
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE_LOGIN + "auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username",userName);
        params.addParameter("password",userPasswd);
        params.addParameter("grant_type","password");
        params.addParameter("client_id","client_password");
        params.addParameter("client_secret","123456");
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginGetToken: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String token = jsonObject.getString("access_token");

                    User user = new DbConfig(getApplicationContext()).getUser();
                    user.setToken(token);

                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
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
                Intent intent = new Intent(getApplicationContext(), TEMainActivity.class);
                intent.putExtra("plan_id",plan_id);
                startActivity(intent);
               // finish();
            }
        });
    }

    private Handler downHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int what = data.getInt("what");
            switch (what) {
                case GO_DOWN:
                    int pro = data.getInt("pro");
                    progress = progress + pro;
                    tv_num.setText(progress + "%");
                    //数据下载完成 前往登录
                    if (progress >= 100){
                        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putBoolean("downloadSuccess",true);
                        editor.commit();
                        handler.sendEmptyMessage(GO_MAIN);
                    }

                    break;

            }

        }
    };
    private LinearLayout loadingLayout;
    private String access_token = "";
    private List<Resource> resourceList ;
    private List<TeamDTO> teamDTOList ;
    private List<CheckStationDTO> checkStationDTOList ;
    private List<MonitorDTO> monitorDTOList ;
    private List<WaterSourceDTO> waterSourceDTOList ;
    private List<WatchTowerDTO> watchTowerDTOList ;
    private List<FireCommandDTO> fireCommandDTOList ;
    private List<DangerSourceDTO> dangerSourceDTOList ;
    private List<CemeteryDTO> cemeteryDTOList ;
    private List<MaterialRepositoryDTO> materialRepositoryDTOList ;
    private List<HelicopterPointDTO> helicopterPointDTOList ;
    private List<Grid> gridList ;
    private List<CheckField> checkFieldList ;
    private DbManager db;
    private int progress = 0;
    private TextView tv_num;
    private List<JobOrder> jobOrderList;
    public List<Plan> planList;

    private PlanOrder planOrder;
    public boolean isDataChange = false;
    private String token="";
    private String userPasswd;
    private String userCode;
    private ProgressDialog loginDialog;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        plan_id = getIntent().getStringExtra("plan_id");
        resourceList = new ArrayList<>();
        planList = new ArrayList<>();
        jobOrderList = new ArrayList<>();
        teamDTOList = new ArrayList<>();
        checkStationDTOList = new ArrayList<>();
        monitorDTOList = new ArrayList<>();
        waterSourceDTOList = new ArrayList<>();
        watchTowerDTOList = new ArrayList<>();
        fireCommandDTOList = new ArrayList<>();
        dangerSourceDTOList = new ArrayList<>();
        cemeteryDTOList = new ArrayList<>();
        materialRepositoryDTOList = new ArrayList<>();
        helicopterPointDTOList = new ArrayList<>();
        gridList = new ArrayList<>();
        checkFieldList = new ArrayList<>();
        DbConfig dbConfig = new DbConfig(this);
        db = dbConfig.getDbManager();
        loginDialog = new ProgressDialog(this);

        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num.setText(progress + "%");
        intent =getIntent();
        if (intent.getStringExtra("token")!=null) {
            token = intent.getStringExtra("token");

            access_token = token;
            getUserInfo();
        }
        Log.e(TAG, "token: "+token );
        //权限获取
        requestPower();
        loginGetToken();


        //实例化IntentFilter对象
        IntentFilter filterOutLogin = new IntentFilter();
        filterOutLogin.addAction("out_login_loginactivity");
        outLoginReceiver = new OutLoginReceiver();
        //注册广播接收
        registerReceiver(outLoginReceiver,filterOutLogin);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(outLoginReceiver);
    }

    private void getAccount() {
        ///本地存储账号密码
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        userCode = sharedPreferences.getString("username","");
        userPasswd = sharedPreferences.getString("password","");

        if((!userCode.isEmpty()) && (!userPasswd.isEmpty())){
            login();
        }else{
            Intent intent = new Intent(getApplicationContext(), com.skyline.terraexplorer.mainapps.activity.LoginActivity.class);
            startActivity(intent);
        }
    }
    //获取token换取用户信息
    private void posttoken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("access_token", token);
        } catch (JSONException e) {
        }
        HhRequestParams params = new HhRequestParams( "http://120.221.95.109:8083/api/user/loginNew1");       //政务云接口地址
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "posttoken: "+jsonObject.toString() );
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "posttoken: "+result);
                try {
                    JSONObject  jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        JSONObject dataObj = data.getJSONObject(0);
                        userPasswd=dataObj.getString("userPasswd");
                        userCode=dataObj.getString("userCode");
                        Log.e(TAG, "userPasswd: "+userPasswd+","+userCode );
                        login();
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
    private void login() {
        showDialogProgress(loginDialog,"加载中...              ");

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE_LOGIN + "auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username",userCode);
        params.addParameter("password",userPasswd);
        params.addParameter("grant_type","password");
        params.addParameter("client_id","client_password");
        params.addParameter("client_secret","123456");
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginGetToken: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    access_token = jsonObject.getString("access_token");

                    getUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LaunchActivity.this, "用户名密码错误!", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE_LOGIN + "auth/api/auth/user/get/userinfo");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "getUserInfo:-- " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:--2- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        Log.e(TAG, "onSuccess: --3-" );
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject userJsonObj = data.getJSONObject(0);
                        String createUser = userJsonObj.getString("createUser");
                        String updateUser = userJsonObj.getString("updateUser");
                        String createTime = userJsonObj.getString("createTime");
                        String updateTime = userJsonObj.getString("updateTime");
                        String id = userJsonObj.getString("id");
                        String userCode = userJsonObj.getString("userCode");
                        String userPasswd = userJsonObj.getString("userPasswd");
                        String fullName = userJsonObj.getString("fullName");
                        String email = userJsonObj.getString("email");
                        String phone = userJsonObj.getString("phone");
                        String sex = userJsonObj.getString("sex");
                        String entryTime = userJsonObj.getString("entryTime");
                        String birthday = userJsonObj.getString("birthday");
                        String type = userJsonObj.getString("type");
                        String isSuperAdmin = userJsonObj.getString("isSuperAdmin");
                        String comment = userJsonObj.getString("comment");
                        String groupId = userJsonObj.getString("groupId");

                        String gridNo = userJsonObj.getString("gridNo");
                        String bkchar2 = userJsonObj.getString("bkchar2");
                        String money = userJsonObj.getString("money");
                        String lockMoney = userJsonObj.getString("lockMoney");
                        String groupName = userJsonObj.getString("groupName");
                        String state = userJsonObj.getString("state");
                        Log.e(TAG, "onSuccess: --4-" );
              /*          User user = new User(id, userCode,userCode, userPasswd, fullName, email, phone, sex, entryTime, birthday, type, isSuperAdmin, comment, groupId,
                                gridNo, bkchar2, money, lockMoney, groupName, state, 1, access_token);
                        user.setDataIsChange(false);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        getQuanxianDataFromService();*/
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
                //   loginDialog.dismiss();
            }
        });

    }
    /**
     * 获取用户权限数据
     */
    private void getQuanxianDataFromService() {

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE_LOGIN + "auth/api/auth/auth/list/element/from/menu");
        params.setConnectTimeout(10000);
        params.addParameter("menuCode","ResourceListManager");
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "getUserInfo:-- " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:--2- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        JSONArray data = jsonObject.getJSONArray("data");
                        User user = new DbConfig(getApplicationContext()).getUser();
                        user.setDelete(false);
                        user.setAdd(false);
                        user.setEdit(false);
                        for (int i = 0; i < data.length(); i++) {
                            String elementCode = data.getJSONObject(i).getString("elementCode");
                            Log.e(TAG, "onSuccess: " + elementCode);
                            if (elementCode.equals("ResourceListManager:btn_edit")){
                                Log.e(TAG, "onSuccess: 1" );
                                user.setEdit(true);
                            }else if (elementCode.equals("ResourceListManager:btn_del")){
                                Log.e(TAG, "onSuccess: 2" );
                                user.setDelete(true);
                            }else if (elementCode.equals("ResourceListManager:btn_add")){
                                Log.e(TAG, "onSuccess: 3" );
                                user.setAdd(true);
                            }
                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add("0");
                        JPushInterface.setTags(getApplicationContext(),tagSet,mTagsCallback);

                        Intent intent = new Intent(getApplicationContext(), TEMainActivity.class);
                        intent.putExtra("STATE",1);
                        intent.putExtra("plan_id",plan_id);
                        startActivity(intent);
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
                loginDialog.dismiss();
            }
        });

    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "推送注册成功";
                    Log.e(TAG, "gotResult:推送注册成功 ");
                    break;
                case 6002:
                    logs = "推送注册失败";
                    Log.e(TAG, "gotResult:推送注册失败 "  + alias);
                    Log.e(TAG, "gotResult:推送注册失败 "  + tags);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
            //Log.e("标签设置", logs);
            //System.out.println("推送状态:"+logs);
        }
    };
    private void loginGetToken() {

        /*SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否完成下载
        boolean downloadSuccess = sf.getBoolean("downloadSuccess", false);
        if(downloadSuccess){
            loadingLayout.setVisibility(View.INVISIBLE);
            handler.sendEmptyMessage(GO_MAIN);

            return;
        }*/
        //初始化资源列表
        initResourceIntoDb();
        //初始化专业队伍数据
        initTeamDTOIntoDb();
        //初始化护林员检查站数据
        initCheckStationIntoDb();
        //初始化视频监控点数据
        initMonitorIntoDb();
        //初始化水源地数据
        initWaterSourceIntoDb();
        //初始化瞭望塔数据
        initWatchTowerIntoDb();
        //初始化指挥部数据
        initFireCommandIntoDb();
        //重大资源数据
        initDangerSourceIntoDb();
        //初始化墓地数据
        initCemeteryIntoDb();
        //初始化物资库
        initMaterialRepositoryIntoDb();
        //初始话直升机机降点
        initHelicopterPointIntoDb();
        //初始化检查内容
        initCheckFieldIntoDb();
        //初始化区域信息
        initGridIntoDb();//
        //获取检查计划列表
        initJobOrderIntoDb();//


      /*  Log.e(TAG, "loginGetToken: --" );
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "auth/api/auth/user/login/default");
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        access_token = jsonObject.getJSONArray("data").getJSONObject(0).getString("access_token");

                        //初始化资源列表
                        initResourceIntoDb();
                        //初始化专业队伍数据
                        initTeamDTOIntoDb();
                        //初始化护林员检查站数据
                        initCheckStationIntoDb();
                        //初始化视频监控点数据
                        initMonitorIntoDb();
                        //初始化水源地数据
                        initWaterSourceIntoDb();
                        //初始化瞭望塔数据
                        initWatchTowerIntoDb();
                        //初始化指挥部数据
                        initFireCommandIntoDb();
                        //重大资源数据
                        initDangerSourceIntoDb();
                        //初始化墓地数据
                        initCemeteryIntoDb();
                        //初始化物资库
                        initMaterialRepositoryIntoDb();
                        //初始话直升机机降点
                        initHelicopterPointIntoDb();
                        //初始化检查内容
                        initCheckFieldIntoDb();
                        //初始化区域信息
                        initGridIntoDb();
                        //获取检查计划列表
                        initJobOrderIntoDb();




                    }else {
                        Toast.makeText(LaunchActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(GO_MAIN);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });*/
    }

    /**
     * 检查计划
     */
    private void initJobOrderIntoDb() {
        Log.e(TAG, "checkPlan: " );
        List<JobOrder> jobOrderbList = null;
        try {
            jobOrderbList = db.selector(JobOrder.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (jobOrderbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = jobOrderbList.get(jobOrderbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);

            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getUseCheckPlanList");

        params.setBodyContent(new JSONObject().toString());
        params.setConnectTimeout(20000);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "checkPlan: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess14------ " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        deleteJobOrderInfo();
                        JSONArray data = jsonObject.getJSONArray("data");
                        jobOrderList.clear();
                        Gson gson = new Gson();
                        jobOrderList = gson.fromJson(String.valueOf(data), new TypeToken<List<JobOrder>>(){}.getType());

                        for (int i = 0; i < jobOrderList.size(); i++) {

                            Log.e(TAG, "onSuccess: planinfo" + jobOrderList.get(i).getId() );
                            initJobOrderInfoIntoDb(jobOrderList.get(i).getId());
                        }
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();

                        try {
                            Log.e(TAG, "onSuccess: team11" );
                            db.saveOrUpdate(jobOrderList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 2);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else {
                       // Toast.makeText(LaunchActivity.this, "暂无检查计划！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                if (ex.toString().contains("timeout")){
                    initJobOrderIntoDb();
                }else {
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        Log.e(TAG, "onSuccess: team11" );
                        db.saveOrUpdate(jobOrderList);

                        Message message = downHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putInt("pro", 2);
                        b.putInt("what",GO_DOWN);
                        message.setData(b);
                        downHandler.sendMessage(message);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void deleteJobOrderInfo() {
        try {
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();
            List<JobOrder> jobOrderDeleteList = db.selector(JobOrder.class)
                    .findAll();
            for (int i = 0; i < jobOrderDeleteList.size(); i++) {
                db.delete(jobOrderDeleteList.get(i).getId());
            }
            List<Plan> planDeleteList = db.selector(Plan.class)
                    .findAll();
            for (int i = 0; i < planDeleteList.size(); i++) {
                db.delete(planDeleteList.get(i).getId());
            }

            List<PlanOrder> planOrderDeleteList = db.selector(PlanOrder.class)
                    .findAll();
            for (int i = 0; i < planOrderDeleteList.size(); i++) {
                db.delete(planOrderDeleteList.get(i).getId());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initJobOrderInfoIntoDb(String planId) {
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getCheckPlanDetail");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("id",planId);
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(20000);
        Log.e(TAG, "planinfo: " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: planinfo"+ result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    JSONArray resourceJsonArray = data.getJSONArray("resourceList");
                    JSONObject checkPlanObject = data.getJSONObject("checkPlan");
                    Gson gson = new Gson();

                    planOrder = gson.fromJson(String.valueOf(checkPlanObject), new TypeToken<PlanOrder>(){}.getType());
                    planList.clear();
                    planList = gson.fromJson(String.valueOf(resourceJsonArray), new TypeToken<List<Plan>>(){}.getType());
                    for (int i = 0; i < planList.size(); i++) {
                        double lat = planList.get(i).getResourcePosition().getLat();
                        double lng = planList.get(i).getResourcePosition().getLng();
                        planList.get(i).setLat(lat);
                        planList.get(i).setLng(lng);
                    }
                    Log.e(TAG, "onSuccess:planinfo.size= " + planList.size() );
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    DbManager db1 = dbConfig.getDbManager();
                    try {
                        Log.e(TAG, "onSuccess: team11" );
                        db.saveOrUpdate(planList);
                        db1.saveOrUpdate(planOrder);
/*
                        Message message = downHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putInt("pro", 2);
                        b.putInt("what",GO_DOWN);
                        message.setData(b);
                        downHandler.sendMessage(message);*/
                    } catch (DbException e) {
                        e.printStackTrace();
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

    /**
     * 网格信息
     */
    private void initGridIntoDb() {
        List<Grid> gridbList = null;
        try {
            gridbList = db.selector(Grid.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (gridbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = gridbList.get(gridbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/grid/list");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess13------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        Log.e(TAG, "onSuccess13:1 "   );
                        JSONArray data = jsonObject1.getJSONArray("data");
                        for(int i = 0; i < data.length(); i++){
                            Log.e(TAG, "onSuccess: 获取省市区" + data.get(i) );
                        }
                        gridList.clear();
                        Log.e(TAG, "onSuccess13:2 "   );
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        Log.e(TAG, "onSuccess13:3"   );
                        Gson gson = new Gson();
                        Log.e(TAG, "onSuccess13-:4 "   );
                        /*for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = (JSONObject) data.get(i);
                            Grid grid = gson.fromJson(String.valueOf(obj),Grid.class);
                            gridList.add(grid);
                        }*/
                        gridList = gson.fromJson(String.valueOf(data), new TypeToken<List<Grid>>(){}.getType());
                        Log.e(TAG, "onSuccess13-: " +gridList.size() );

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {

                            db.saveOrUpdate(gridList);

                            Log.e(TAG, "onSuccess:gridList "+gridList.size());
                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro",2);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LaunchActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex.toString().contains("timeout")){
                    initGridIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro",2);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 直升机机降点
     */
    private void initHelicopterPointIntoDb() {
        List<HelicopterPointDTO> helicopterPointDTObList = null;
        try {
            helicopterPointDTObList = db.selector(HelicopterPointDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (helicopterPointDTObList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = helicopterPointDTObList.get(helicopterPointDTObList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/helicopterPoint/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess11----- " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        helicopterPointDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String address = object.optString("address");
                                String createTime = object.optString("createTime");
                                String createUser = object.optString("createUser");
                                String description = object.optString("description");
                                String districtName = object.optString("districtName");
                                String districtNo = object.optString("districtNo");
                                String gridId = object.optString("gridId");
                                String gridName = object.optString("gridName");
                                String gridNo = object.optString("gridNo");
                                String groupId = object.optString("groupId");
                                String iconFile = object.optString("iconFile");
                                String id = object.optString("id");
                                String leaderId = object.optString("leaderId");
                                String leaderName = object.optString("leaderName");

                                String leaderPhone = object.optString("leaderPhone");
                                String name = object.optString("name");
                                String picture = object.optString("picture");
                                String streetName = object.optString("streetName");
                                String streetNo = object.optString("streetNo");
                                String textColor = object.optString("textColor");
                                String updateTime = object.optString("updateTime");
                                String updateUser = object.optString("updateUser");
                                String waterSource = object.optString("waterSource");
                                String resourceType = object.optString("resourceType");
                                String state = object.optString("state");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");

                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                HelicopterPointDTO helicopterPointDTO = new HelicopterPointDTO(id, address, createTime, createUser, description, districtName, districtNo, gridId, gridName, gridNo, groupId, iconFile, leaderId, leaderName, leaderPhone,
                                        name, picture, lat, lng, streetName, streetNo, textColor, updateTime, updateUser, waterSource,resourceType,state);
                                helicopterPointDTO.setOtherPic(otherPic);
                                helicopterPointDTO.setCheckState(checkState);
                                helicopterPointDTOList.add(helicopterPointDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team1" + helicopterPointDTOList.size());
                            db.saveOrUpdate(helicopterPointDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro",3);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: materialRepository请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initHelicopterPointIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro",3);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 检查内容
     */
    private void initCheckFieldIntoDb() {
        List<CheckField> checkFieldDbList = null;
        /*try {
            checkFieldDbList = db.selector(CheckField.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (checkFieldDbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = checkFieldDbList.get(checkFieldDbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }*/
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkField/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess12------ api/checkField/listAll access_token" + access_token);
                Log.e(TAG, "onSuccess12------ api/checkField/listAll" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        checkFieldList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String id = object.optString("id");
                                String resourceType = object.optString("resourceType");
                                String code1 = object.optString("code");
                                String name = object.optString("name");
                                String fieldType = object.optString("fieldType");
                                String description = object.optString("description");
                                String groupId = object.optString("groupId");

                                CheckField checkField = new CheckField(id, code1, createTime, createUser, description, fieldType, groupId, name, resourceType, updateTime, updateUser,0);
                                checkFieldList.add(checkField);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team11" );
                            db.saveOrUpdate(checkFieldList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro",3);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 检查内容请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initCheckFieldIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 3);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 物资库
     */
    private void initMaterialRepositoryIntoDb() {
        List<MaterialRepositoryDTO> materialRepositoryDTODbList = null;
        try {
            materialRepositoryDTODbList = db.selector(MaterialRepositoryDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (materialRepositoryDTODbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = materialRepositoryDTODbList.get(materialRepositoryDTODbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/materialRepository/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess10------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        materialRepositoryDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String type = object.optString("type");
                                String name = object.optString("name");
                                Log.e(TAG, "onSuccess: wuzi"+name );
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                int fireEquipmentCount = object.optInt("fireEquipmentCount");
                                int safeEquipmentCount = object.optInt("safeEquipmentCount");
                                int outdoorEquipmentCount = object.optInt("outdoorEquipmentCount");
                                int communicateEquipmentCount = object.optInt("communicateEquipmentCount");
                                int carCount = object.optInt("carCount");
                                int machineCount = object.optInt("machineCount");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String districtName = object.optString("districtName");
                                String districtNo = object.optString("districtNo");
                                String streetName = object.optString("streetName");
                                String streetNo = object.optString("streetNo");
                                String state = object.optString("state");

                                String windFireCount = object.optString("windFireCount");
                                String sprayFireCount = object.optString("sprayFireCount");
                                String waterPumpCount = object.optString("waterPumpCount");
                                String twoToolCount = object.optString("twoToolCount");
                                String waterPistolCount = object.optString("waterPistolCount");
                                String chainSawCount = object.optString("chainSawCount");
                                String bushCutterCount = object.optString("bushCutterCount");
                                String fireCutterCount = object.optString("fireCutterCount");
                                String fireproofClothesCount = object.optString("fireproofClothesCount");
                                String glovesCount = object.optString("glovesCount");
                                String helmetCount = object.optString("helmetCount");
                                String shoesCount = object.optString("shoesCount");
                                String waterBagCount = object.optString("waterBagCount");
                                String waterSacCount = object.optString("waterSacCount");
                                String oilDrumCount = object.optString("oilDrumCount");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                    MaterialRepositoryDTO materialRepositoryDTO = new MaterialRepositoryDTO(id,groupId,name,type,no,lng,lat,address,gridId,gridNo,gridName,fireEquipmentCount,safeEquipmentCount,outdoorEquipmentCount,communicateEquipmentCount,carCount,
                                            machineCount,leaderName,leaderPhone,picture,textColor,iconFile,description,state,createUser,updateUser,createTime,updateTime,windFireCount,sprayFireCount,waterPumpCount,twoToolCount,
                                            waterPistolCount,chainSawCount,bushCutterCount,fireCutterCount,fireproofClothesCount,glovesCount,helmetCount,shoesCount,waterBagCount,waterSacCount,oilDrumCount,resourceType,districtName,districtNo,streetName,streetNo);
                                materialRepositoryDTO.setOtherPic(otherPic);
                                materialRepositoryDTO.setCheckState(checkState);
                                materialRepositoryDTOList.add(materialRepositoryDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team1" + materialRepositoryDTOList.size());
                            db.saveOrUpdate(materialRepositoryDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro",5);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: materialRepository请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initMaterialRepositoryIntoDb();
                }else {
                    Log.e(TAG, "onError: material" +
                            " ");
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro",5);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 墓地数据
     */
    private void initCemeteryIntoDb() {
        List<CemeteryDTO> cemeteryDTODbList = null;
        try {
            cemeteryDTODbList = db.selector(CemeteryDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (cemeteryDTODbList == null) {
                jsonObject.put("updateTime", "2019-08-07T03:12:42.404Z");
            } else {
                String time = cemeteryDTODbList.get(cemeteryDTODbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/cemetery/listAll");

        params.setConnectTimeout(200000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess9--- " + result);
                Message message1 = downHandler.obtainMessage();
                Bundle b1 = new Bundle();
                b1.putInt("pro", 5);
                b1.putInt("what",GO_DOWN);
                message1.setData(b1);
                downHandler.sendMessage(message1);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        cemeteryDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                Log.e(TAG, "onSuccess: 1111" );
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String districtNo = object.optString("districtNo");
                                String districtName = object.optString("districtName");
                                String streetNo = object.optString("streetNo");
                                String streetName = object.optString("streetName");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");

                                String graveCount = object.optString("graveCount");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                CemeteryDTO cemeteryDTO = new CemeteryDTO(id, groupId, name, no, lng, lat, address, districtNo, districtName, streetNo, streetName, gridId, gridNo, gridName,leaderName,
                                        leaderPhone, picture, textColor, iconFile, description, state, createUser, updateUser, createTime, updateTime,resourceType,graveCount);
                                cemeteryDTO.setOtherPic(otherPic);
                                cemeteryDTO.setCheckState(checkState);
                                cemeteryDTOList.add(cemeteryDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team2" );
                            db.saveOrUpdate(cemeteryDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 5);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: cemetery请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initCemeteryIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 5);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 重大危险源数据
     */
    private void initDangerSourceIntoDb() {
        List<DangerSourceDTO> dangerSourceDTODbList = null;
        try {
            dangerSourceDTODbList = db.selector(DangerSourceDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (dangerSourceDTODbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = dangerSourceDTODbList.get(dangerSourceDTODbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/dangerSource/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess8------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        dangerSourceDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String type = object.optString("type");
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");
                                String districtName = object.optString("districtName");
                                String districtNo = object.optString("districtNo");
                                String streetName = object.optString("streetName");
                                String streetNo = object.optString("streetNo");

                                String isMajorHazard = object.optString("isMajorHazard");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                DangerSourceDTO dangerSourceDTO = new DangerSourceDTO(id, groupId, name, type, no, lng, lat, address, gridId, gridNo, gridName,  leaderName, leaderPhone, picture, textColor,
                                        iconFile, description, state, createUser, updateUser, createTime, updateTime,resourceType,districtName,districtNo,streetName,streetNo,isMajorHazard);
                                dangerSourceDTO.setOtherPic(otherPic);
                                dangerSourceDTO.setCheckState(checkState);
                                dangerSourceDTOList.add(dangerSourceDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team3" );
                            db.saveOrUpdate(dangerSourceDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError:dangerSource 请求失败" + ex.toString());

                if (ex.toString().contains("timeout")){
                    initDangerSourceIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 指挥部数据
     */
    private void initFireCommandIntoDb() {
        List<FireCommandDTO> fireCommandDTODbList = null;
        try {
            fireCommandDTODbList = db.selector(FireCommandDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (fireCommandDTODbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = fireCommandDTODbList.get(fireCommandDTODbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/fireCommand/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess7------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fireCommandDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String type = object.optString("type");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");
                                String districtName = object.optString("districtName");
                                String districtNo = object.optString("districtNo");
                                String streetName = object.optString("streetName");
                                String streetNo = object.optString("streetNo");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");

                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");


                                FireCommandDTO fireCommandDTO = new FireCommandDTO(id, groupId, name, type, lng, lat, address, gridId, gridNo, gridName, picture, textColor, iconFile, description, state, createUser,
                                        updateUser, createTime, updateTime,leaderName,leaderPhone,resourceType,districtName,districtNo,streetName,streetNo);
                                fireCommandDTO.setOtherPic(otherPic);
                                fireCommandDTO.setCheckState(checkState);
                                fireCommandDTOList.add(fireCommandDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team4" );
                            db.saveOrUpdate(fireCommandDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError:fireCommand 请求失败" + ex.toString());

                if (ex.toString().contains("timeout")){
                    initFireCommandIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 瞭望塔数据
     */
    private void initWatchTowerIntoDb() {
        List<WatchTowerDTO> watchTowerDTODbListList = null;
        try {
            watchTowerDTODbListList = db.selector(WatchTowerDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (watchTowerDTODbListList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = watchTowerDTODbListList.get(watchTowerDTODbListList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/watchTower/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess6------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        watchTowerDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String districtNo = object.optString("districtNo");
                                String districtName = object.optString("districtName");
                                String streetNo = object.optString("streetNo");
                                String streetName = object.optString("streetName");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");

                                String watchRange = object.optString("watchRange");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");

                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }


                                WatchTowerDTO watchTowerDTO = new WatchTowerDTO(id, groupId, name, no, lng, lat, address, gridId, gridNo, gridName, districtNo, districtName, streetNo, streetName,leaderName,
                                        leaderPhone, picture, textColor, iconFile, description, state, createUser, updateUser, createTime, updateTime,resourceType,watchRange);
                                watchTowerDTO.setOtherPic(otherPic);
                                watchTowerDTO.setCheckState(checkState);

                                watchTowerDTOList.add(watchTowerDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team5" );
                            db.saveOrUpdate(watchTowerDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: watchTower请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initWatchTowerIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 水源地数据
     */
    private void initWaterSourceIntoDb() {
        List<WaterSourceDTO> waterSourceDbDTOListList = null;
        try {
            waterSourceDbDTOListList = db.selector(WaterSourceDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (waterSourceDbDTOListList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = waterSourceDbDTOListList.get(waterSourceDbDTOListList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/waterSource/listAll");
        params.setConnectTimeout(200000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess5------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        waterSourceDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String type = object.optString("type");
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String waterCapacity = object.optString("waterCapacity");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String districtNo = object.optString("districtNo");
                                String districtName = object.optString("districtName");
                                String streetNo = object.optString("streetNo");
                                String streetName = object.optString("streetName");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");
                                String isHelicopterWater = object.optString("isHelicopterWater");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");

                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                WaterSourceDTO waterSourceDTO = new WaterSourceDTO(id, groupId, name, type, no, lng, lat, address, waterCapacity, leaderName, leaderPhone, gridId, gridNo, gridName, districtNo,
                                        districtName, streetNo, streetName, picture, textColor, iconFile, description, state, createUser, updateUser, createTime, updateTime,resourceType);
                                waterSourceDTO.setIsHelicopterWater(isHelicopterWater);
                                waterSourceDTO.setOtherPic(otherPic);
                                waterSourceDTO.setCheckState(checkState);
                                waterSourceDTOList.add(waterSourceDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team6" );
                            db.saveOrUpdate(waterSourceDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: waterSource请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initWaterSourceIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 视频监控点数据
     */
    private void initMonitorIntoDb() {
        List<MonitorDTO> monitorDTODbListList = null;
        try {
            monitorDTODbListList = db.selector(MonitorDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (monitorDTODbListList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = monitorDTODbListList.get(monitorDTODbListList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/monitor/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess video------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        monitorDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String monitorNo = object.optString("monitorNo");
                                String isOnline = object.optString("isOnline");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String districtNo = object.optString("districtNo");
                                String districtName = object.optString("districtName");
                                String streetNo = object.optString("streetNo");
                                String streetName = object.optString("streetName");
                                String monitorArea = object.optString("monitorArea");
                                String placeName = object.optString("placeName");
                                String sweepArea = object.optString("sweepArea");
                                String altitude = object.optString("altitude");
                                String towerHeight = object.optString("towerHeight");
                                String northCorrection = object.optString("northCorrection");
                                String horizontalCorrection = object.optString("horizontalCorrection");
                                String visualRange = object.optString("visualRange");
                                String visualTime = object.optString("visualTime");
                                String horizontalAngle = object.optString("horizontalAngle");
                                String verticalAngle = object.optString("verticalAngle");
                                String remark = object.optString("remark");
                                String state = object.optString("state");

                                String monitorRange = object.optString("monitorRange");
                                String isNetworking = object.optString("isNetworking");
                                String isIntelligentEntry = object.optString("isIntelligentEntry");
                                String description = object.optString("description");
                                String picture = object.optString("description");


                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");
                               // String otherPic = " ";
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");

                                MonitorDTO monitorDTO = new MonitorDTO(id, groupId, name, monitorNo, isOnline, lng, lat, address, gridId, gridNo, gridName, districtNo, districtName, streetNo, streetName,
                                        monitorArea, placeName, sweepArea, altitude, towerHeight, northCorrection, horizontalCorrection, visualRange, visualTime, horizontalAngle, verticalAngle, remark,
                                        state, createUser, updateUser, createTime, updateTime,leaderName,leaderPhone,resourceType,monitorRange,isNetworking,isIntelligentEntry,description,picture);
                                monitorDTO.setOtherPic(otherPic);
                                monitorDTO.setCheckState(checkState);
                                monitorDTOList.add(monitorDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team7" );
                            db.saveOrUpdate(monitorDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: monitor请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initMonitorIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 获取护林员检查站
     */
    private void initCheckStationIntoDb() {
        List<CheckStationDTO> checkStationDTODbListList = null;
        try {
            checkStationDTODbListList = db.selector(CheckStationDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (checkStationDTODbListList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = checkStationDTODbListList.get(checkStationDTODbListList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkStation/listAll");
        params.setConnectTimeout(200000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess3------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        checkStationDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String type = object.optString("type");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String districtNo = object.optString("districtNo");
                                String districtName = object.optString("districtName");
                                String streetNo = object.optString("streetNo");
                                String streetName = object.optString("streetName");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String description = object.optString("description");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String picture = object.optString("picture");
                                String state = object.optString("state");

                                String peopleCount = object.optString("peopleCount");
                                String extinguisherCount = object.optString("extinguisherCount");
                                String sawCount = object.optString("sawCount");
                                String truckCount = object.optString("truckCount");
                                String dataSnapshot = object.optString("dataSnapshot");
                                String isAllday = object.optString("isAllday");
                                String mountain = object.optString("mountain");
                                String peopleName = object.optString("peopleName");
                                String waterPistolCount = object.optString("waterPistolCount");
                                String twoToolCount = object.optString("twoToolCount");
                                String otherToolCount = object.optString("otherToolCount");
                                String hasMonitor = object.optString("hasMonitor");
/*                                String mountain = "测试";
                                String peopleName = "测试";
                                String waterPistolCount = "测试";
                                String twoToolCount = "测试";
                                String otherToolCount = "测试";
                                String hasMonitor = "0";*/
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }

                                CheckStationDTO checkStationDTO = new CheckStationDTO(id, groupId, name, type, lng, lat, address, gridId, gridNo, gridName, districtNo, districtName, streetNo, streetName,  leaderName,
                                        leaderPhone, description, textColor, iconFile, picture, state, createUser, updateUser, createTime, updateTime,resourceType,peopleCount,extinguisherCount,sawCount,truckCount,dataSnapshot,isAllday,
                                        mountain,peopleName,waterPistolCount,twoToolCount,otherToolCount,hasMonitor);
                                checkStationDTO.setOtherPic(otherPic);
                                checkStationDTO.setCheckState(checkState);
                                checkStationDTOList.add(checkStationDTO);
                            }catch (Exception e){
                                Log.e(TAG, "onSuccess: bingo error = " + e.toString() + "，" + data.getJSONObject(i).toString() );
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team8" );
                            db.saveOrUpdate(checkStationDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError:checkStation 请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initCheckStationIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 下载专业队伍资源点
     */
    private void initTeamDTOIntoDb() {

        List<TeamDTO> teamDTODbListList = null;
        try {
            teamDTODbListList = db.selector(TeamDTO.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (teamDTODbListList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = teamDTODbListList.get(teamDTODbListList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/team/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess2------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        teamDTOList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject object = data.getJSONObject(i);
                                String createUser = object.optString("createUser");
                                String updateUser = object.optString("updateUser");
                                String createTime = object.optString("createTime");
                                String updateTime = object.optString("updateTime");
                                String resourceType = object.optString("resourceType");
                                String groupId = object.optString("groupId");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String no = object.optString("no");
                                String address = object.optString("address");
                                String gridId = object.optString("gridId");
                                String gridNo = object.optString("gridNo");
                                String gridName = object.optString("gridName");
                                String type = object.optString("type");
                                String peopleNumber = object.optString("peopleNumber");
                                String leaderName = object.optString("leaderName");
                                String leaderPhone = object.optString("leaderPhone");
                                String picture = object.optString("picture");
                                String textColor = object.optString("textColor");
                                String iconFile = object.optString("iconFile");
                                String description = object.optString("description");
                                String state = object.optString("state");
                                String lng = "0";
                                String lat = "0";
                                try{
                                    JSONObject position = object.getJSONObject("position");//null
                                    lng = position.optString("lng");
                                    lat = position.optString("lat");
                                }catch (Exception e){

                                }
                                String districtName = object.optString("districtName");
                                String districtNo = object.optString("districtNo");
                                String streetName = object.optString("streetName");
                                String streetNo = object.optString("streetNo");

                                String teamCount = object.optString("teamCount");
                                String phone = object.optString("phone");
                                String truckCount = object.optString("truckCount");
                                String troopCarrierCount = object.optString("troopCarrierCount");
                                String commandCarCount = object.optString("commandCarCount");
                                String waterPumpCount = object.optString("waterPumpCount");
                                String windFireCount = object.optString("windFireCount");
                                String twoToolCount = object.optString("twoToolCount");
                                String waterPistolCount = object.optString("waterPistolCount");
                                String intercomCount = object.optString("intercomCount");
                                String equipmentTruckCount = object.optString("equipmentTruckCount");
                                String barracksMeasure = object.optString("barracksMeasure");
                                String waterCarCount = object.optString("waterCarCount");
                                String otherPic = object.optString("otherPic");
                                String checkState = object.optString("checkState");

                            /*    String teamCount = "测试1";
                                String phone = "测试2";
                                String truckCount = "测试3";
                                String troopCarrierCount = "测试4";
                                String commandCarCount ="测试5";
                                String waterPumpCount ="测试6";
                                String windFireCount ="测试7";
                                String twoToolCount ="测试8";
                                String waterPistolCount = "测试9";
                                String intercomCount ="测试10";
                                String equipmentTruckCount ="测试11";
                                String barracksMeasure ="测试12";
                                String waterCarCount = "测试13";*/

                                TeamDTO teamDTO = new TeamDTO(id, groupId, name, no, lng, lat, address, gridId, gridNo, gridName, type, peopleNumber,leaderName, leaderPhone, picture,
                                        textColor, iconFile, description, state, createUser, updateUser, createTime, updateTime,resourceType,districtName,districtNo,streetName,streetNo,
                                        teamCount,phone,truckCount,troopCarrierCount,commandCarCount,waterPumpCount,windFireCount,twoToolCount,waterPistolCount,intercomCount,equipmentTruckCount,barracksMeasure,waterCarCount);
                                teamDTO.setOtherPic(otherPic);
                                teamDTO.setCheckState(checkState);
                                teamDTOList.add(teamDTO);
                            }catch (Exception e){
                                continue;
                            }

                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team9" );
                            db.saveOrUpdate(teamDTOList);

                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 10);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: team请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initTeamDTOIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 10);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 下载资源列表出数据
     */
    private void initResourceIntoDb() {
        List<Resource> resourceDbList = null;
        try {
            resourceDbList = db.selector(Resource.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
       // Log.e(TAG, "initResourceIntoDb: " + resourceDbList.size() );
        JSONObject jsonObject = new JSONObject();

       // Log.e(TAG, "initResourceIntoDb:time-- " + time);
        try {
            if (resourceDbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = resourceDbList.get(resourceDbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/resourceList/listAll");
        params.setConnectTimeout(20000);
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "initResourceIntoDb: --" +  access_token);
        Log.e(TAG, "initResourceIntoDb: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess1------------- " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        resourceList.clear();
                        if (data.length() > 0){
                            isDataChange = true;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String createUser = object.optString("createUser");
                            String updateUser = object.optString("updateUser");
                            String createTime = object.optString("createTime");
                            String updateTime = object.optString("updateTime");
                            String id = object.optString("id");
                            String name = object.optString("name");
                            String code1 = object.optString("code");
                            String isDisplay = object.optString("isDisplay");
                            int totalCount = object.optInt("totalCount");
                            int useCount = object.optInt("useCount");
                            int unuseCount = object.optInt("unuseCount");
                            String apiUrl = object.optString("apiUrl");
                            String textColor = object.optString("textColor");
                            String iconFile = object.optString("iconFile");
                            String description = object.optString("description");
                            String groupId = object.optString("groupId");
                            String state = object.optString("state");
                            Resource resource = new Resource(id, apiUrl, code1, createTime, createUser, description, groupId, iconFile, isDisplay, name, state, textColor, totalCount, unuseCount,
                                    updateTime, updateUser, useCount);
                            resourceList.add(resource);
                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            Log.e(TAG, "onSuccess: team10" );
                            db.saveOrUpdate(resourceList);
                            Message message = downHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putInt("pro", 5);
                            b.putInt("what",GO_DOWN);
                            message.setData(b);
                            downHandler.sendMessage(message);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initResourceIntoDb();
                }else {
                    Message message = downHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("pro", 5);
                    b.putInt("what",GO_DOWN);
                    message.setData(b);
                    downHandler.sendMessage(message);
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void JudgeToMain() {
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
        boolean isFirstIn = sf.getBoolean("isFirstIn", true);
        SharedPreferences.Editor editor = sf.edit();
        if (isFirstIn) {     //若为true，则是第一次进入
            loadingLayout.setVisibility(View.VISIBLE);

          //  handler.sendEmptyMessage(GO_MAIN); //最后要去掉
            editor.putBoolean("isFirstIn", false);  //修改标志位移动到service下载完毕数据后GuideActivity
           // handler.sendEmptyMessage(GO_GUIDE);//将message设置为跳转到引导页GuideActivity，跳转在goGuide中实现
        } else {
          //  loadingLayout.setVisibility(View.GONE);
         //   handler.sendEmptyMessage(GO_MAIN);//将message设置文跳转到MainActivity，跳转功能在goMain中实现
        }
        editor.commit();

    }

    //权限获取
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
                Toast.makeText(LaunchActivity.this, "1111", Toast.LENGTH_SHORT).show();
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
            }
        } else {
            if (intent.getStringExtra("token")==null||"".equals(intent.getStringExtra("token"))){
                handler.sendEmptyMessageDelayed(GO_NEXT, GO_NEXT_TIME);
            }else {
                token=intent.getStringExtra("token");
                //posttoken();
                getAccount();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       /* Log.e(TAG, "onRequestPermissionsResult:requestCode --" + requestCode);

        Log.e(TAG, "onRequestPermissionsResult: permissions--" + permissions.toString());
        Log.e(TAG, "onRequestPermissionsResult:  permissions.length--" +  permissions.length);
        Log.e(TAG, "onRequestPermissionsResult: grantResults--" + grantResults.toString());
        Log.e(TAG, "onRequestPermissionsResult: grantResults.length--" + grantResults.length);
*/
        for (int i = 0; i < permissions.length; i++) {

            Log.e(TAG, "onRequestPermissionsResult: permissions------" + permissions[i]);
        }


        if (requestCode == 1) {

            boolean isPremission = true;
            for (int i = 0; i < grantResults.length; i++) {
                Log.e(TAG, "onRequestPermissionsResult: permissions++++++" + grantResults[i]);
                if (grantResults[i] == -1) {
                    isPremission = false;
                }
                //  Log.e(TAG, "onRequestPermissionsResult: permissions++++++" +  grantResults[i]);
            }

            if (isPremission) {          //有权限
                handler.sendEmptyMessageDelayed(GO_NEXT, GO_NEXT_TIME);
            } else {
                judgePower();
            }
        }
    }

    private void judgePower() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class OutLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: MainActivity销毁了");
           // LaunchActivity.this.onDestroy();
            finish();
        }
    }


}
