
package com.skyline.terraexplorer.wisdomgarden;

import android.app.Activity;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.models.point;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.CityInfoActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.LoginActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.OtherBarChartActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.ParkActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.ParkListActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.QuActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.Park;
import com.skyline.terraexplorer.wisdomgarden.ui.utils.clickUtils;
import com.skyline.terraexplorer.wisdomgarden.utils.PrefsManager;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.downcell.CommonProgressDialog;
import com.ruyiruyi.rylibrary.request.HhRequestParams;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;
import rxhttp.wrapper.param.RxHttp;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private double nLenStart = 0;//监听 WebView所用手势
    private Toolbar toolbar;
    private Dialog searchDialog;
    private View searchInflater;
    private FrameLayout resourceDialogLayout;
    private TextView detailButton;
    private Button backbutton;
    private LinearLayout linearLayout;
    private static final String TAG = "MainActivity";
    Gson gson = new Gson();
    private Dialog leftDialog;
    private Dialog rightDialog;
    private View leftInflater;
    private View rightInflater;
    private TextView district_view;
    private TextView jingdu_view;
    private TextView weidu_view;
    private TextView renkou_view;
    private TextView mianji_view;
    private Button citybutton;
    private View parkButton;
    private ImageView imageView3;
    private String imgPath;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private TextView nameText;
    private User user;
    private Park park;
    List<String> parklist = new ArrayList<String>();
    private ImageView visibleView;
    public boolean isShow = true;
    private LinearLayout quDataLayout;
    private FrameLayout visibleLayout;

    private String versionCode;
    private CommonProgressDialog mBar;
    private static final String DOWNLOAD_NAME = "wisdom_garden_";
    public boolean isGengxin = false;
    private Uri tempUri;
    private String versionService;

    private static final int EXIT = 1;
    private static boolean isExit = false;
    private long time = 0;
    private SharedPreferences sharedPreferences;

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case EXIT:
                    isExit = false;
                    break;
            }

        }
    };
    private OutLoginMainBroad outLoginMainBroad;
    private TextView chakanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wisdomgarden);
        initView();

        //动态注册广播
        outLoginMainBroad = new OutLoginMainBroad();
        IntentFilter intentFilter = new IntentFilter("out_login_main");
        registerReceiver(outLoginMainBroad, intentFilter);
        //版本更新
//        getVersion();
        sharedPreferences = this.getSharedPreferences("sptest",MODE_PRIVATE);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_more) {
            //searchDialog.show();
            rightDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initView() {
        webView = (WebView)findViewById(R.id.webView);
        detailButton=findViewById(R.id.go_button);
        district_view=findViewById(R.id.district_view);
        jingdu_view=findViewById(R.id.jingdu_view);
        weidu_view=findViewById(R.id.weidu_view);
        renkou_view=findViewById(R.id.renkou_view);
        mianji_view=findViewById(R.id.mianji_view);
        visibleView = (ImageView) findViewById(R.id.visible_view);
        quDataLayout = (LinearLayout) findViewById(R.id.qu_data_layout);
        visibleLayout = (FrameLayout) findViewById(R.id.visible_layout);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        //访问网页
        webView.loadUrl("file:///android_asset/map/mars_zhyl.html"); // mars
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        toolbar = findViewById(R.id.tb_demo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.people);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               leftDialog.show();
            }
        });
    //js与android调用
    final DWebView dwebView = (DWebView)findViewById(R.id.webView);

        dwebView.addJavascriptObject(new JsUtils(), "zhyl"); // 供js调用的android方法

    String url = "assets/qds_2005261619/{z}/{x}/{y}.png"; // 瓦片路径
        dwebView.callHandler("onload", new Object[]{url}, new OnReturnValue<String>() {
        @Override
        public void onValue(String retValue) {
            Log.d("jsbridge", "call succeed,return value is " + retValue);
        }
    });

        dwebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int nCnt = event.getPointerCount();
                int n = event.getAction();
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt)//<span style="color:#ff0000;">2表示两个手指</span>
                {
                    for (int i = 0; i < nCnt; i++) {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int) x, (int) y);
                    }
                    int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
                    int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));
                    nLenStart = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);
                } else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP && 2 == nCnt) {
                    for (int i = 0; i < nCnt; i++) {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int) x, (int) y);
                    }
                    int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
                    int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));

                    double nLenEnd = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);

                    if (nLenEnd > nLenStart)//通过两个手指开始距离和结束距离，来判断放大缩小
                    {
                        if (clickUtils.isFastDoubleClick()) {
                            Log.e(TAG, "onClick: 多次点击" );
                        }else {
                            dwebView.callHandler("zoom", new Object[]{"in"});
                        }
                    } else {
                        dwebView.callHandler("zoom", new Object[]{"out"});
                    }
                }
                return false;
            }
        });
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, QuActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 左侧抽屉dialog
         */
        leftDialog = new Dialog(this, R.style.ActionSheetDialogStyleLeft);
        leftInflater = LayoutInflater.from(this).inflate(R.layout.dialog_video_left, null);
        leftInflater.setMinimumWidth(100000);
     //   listView = ((ListView) videoListInflater.findViewById(R.id.listView));
       // videoListLayout = ((LinearLayout) videoListInflater.findViewById(R.id.video_list_layout));
       // listDialogImage = ((ImageView) videoListInflater.findViewById(R.id.list_dialog_button));
        backbutton=leftInflater.findViewById(R.id.backbutton);
        nameText=leftInflater.findViewById(R.id.name_text);
        chakanView = ((TextView) leftInflater.findViewById(R.id.chakan_view));
        leftDialog.setContentView(leftInflater);
        Window leftDialogWindow = leftDialog.getWindow();
        leftDialogWindow.setGravity(Gravity.LEFT);

        WindowManager.LayoutParams lpleftList = leftDialogWindow.getAttributes();
        WindowManager wm = (WindowManager)this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        lpleftList.width = (int) (width * 0.5);
        lpleftList.height = height * 1;
        leftDialogWindow.setAttributes(lpleftList);
        leftDialog.setCanceledOnTouchOutside(true);
        user = new DbConfig(this).getUser();
        Log.i(TAG, String.valueOf(user));
        nameText.setText("用户名："+user.getFullName());
        imageView3=leftInflater.findViewById(R.id.imageView3);
        RxViewAction.clickNoDouble(chakanView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), OtherBarChartActivity.class);
                        intent.putExtra("isHasData",true);
                        intent.putExtra("parkData","城区公园个数(个)");
                        intent.putExtra("quData","青岛市");
                        startActivity(intent);
                    }
                });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbConfig config = new DbConfig(getApplicationContext());
                User user = config.getUser();
                user.setIsLogin(0);
                DbManager db = config.getDbManager();
                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
/*        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image*//*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
            }
        });*/
        RxViewAction.clickNoDouble(imageView3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                          Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                          startActivityForResult(i, 66);

                    }
                });
        //设置图片
        if (user.getHeadUrl().isEmpty()) {
            Log.e(TAG, "initView: "+user.getHeadUrl() );
            Glide.with(this).load(user.getHeadUrl())/*.error(R.drawable.ic_park)*/.into(imageView3);
        }else {
            Log.e(TAG, "initView1: "+user.getHeadUrl() );
            Glide.with(this).load(user.getHeadUrl()).into(imageView3);
        }
        //Glide.with(this).load(user.getHeadUrl()).error(R.drawable.ic_park).into(imageView3);
        //右侧抽屉
        rightDialog = new Dialog(this, R.style.ActionSheetDialogStyleRight);
        rightInflater = LayoutInflater.from(this).inflate(R.layout.dialog_right, null);
        rightInflater.setMinimumWidth(100000);
        //   listView = ((ListView) videoListInflater.findViewById(R.id.listView));
        // videoListLayout = ((LinearLayout) videoListInflater.findViewById(R.id.video_list_layout));
        // listDialogImage = ((ImageView) videoListInflater.findViewById(R.id.list_dialog_button));
        citybutton=rightInflater.findViewById(R.id.citybutton);
        parkButton = rightInflater.findViewById(R.id.parkbutton);
        rightDialog.setContentView(rightInflater);
        Window rightDialogWindow = rightDialog.getWindow();
        rightDialogWindow.setGravity(Gravity.RIGHT);


        WindowManager.LayoutParams lprightList = rightDialogWindow.getAttributes();
        WindowManager wmr = (WindowManager)this
                .getSystemService(Context.WINDOW_SERVICE);
        int rightheight = wmr.getDefaultDisplay().getHeight();
        int rightwidth = wmr.getDefaultDisplay().getWidth();
        lprightList.width = (int) (rightwidth * 0.5);
        lprightList.height = rightheight * 1;
        rightDialogWindow.setAttributes(lprightList);
        rightDialog.setCanceledOnTouchOutside(true);
        citybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightDialog.dismiss();
                Intent intent=new Intent(MainActivity.this, CityInfoActivity.class);
                startActivity(intent);
            }
        });
        //获取公园信息
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();
            jsonObject.put("dto", dto);
            jsonObject.put("limit", 150);
            jsonObject.put("page", 1);


        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/garden/page");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());

        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        Log.e(TAG, "park: --"  + new DbConfig(this).getUser().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
//                 try {
//                    JSONObject json0bject = new JSONObject(result);
//                    Log.e("result",json0bject.toString());
//                    JSONArray data = json0bject.getJSONArray("data");
//                    Log.e("data1",data.toString());
//                    JSONArray dataLists = null;
//                   for(int i=0; i<data.length(); i++){
//                        JSONObject dataListsObj = data.getJSONObject(i);
//                         dataLists = dataListsObj.getJSONArray("dataList");
//                        Log.e("dataLists1",dataLists.toString());
//                        for(int j=0; j<dataLists.length(); j++){
//                            JSONObject dataList = dataLists.getJSONObject(j);
//                            String name = dataList.getString("name");
//                            String positionStr = dataList.getString("position");
//                            Log.e("position1", positionStr);
//                            JSONObject positionObj = new JSONObject(positionStr);
//                            JSONArray coordinates = positionObj.getJSONArray("coordinates").getJSONArray(0);
//                            List<Double> jwdlist = new ArrayList<>();
//                            Double[] arrlist = new Double[coordinates.length()*2];
//                            String str = "";
//                            for (int k = 0; k < coordinates.length(); k++) {
//                                String lng = coordinates.getJSONArray(k).getString(0);
//                                String lat = coordinates.getJSONArray(k).getString(1);
//                                str = str + lng +","+ lat+",";
//                            }
//                            String str1= str.substring(0,str.length()-1);
//                            Log.i(TAG, "arrlist: "+arrlist.toString());
//                        }
//                    }
//
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
                dwebView.callHandler("parkinfo", new Object[]{new Gson().toJson(result),});
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
        RxViewAction.clickNoDouble(parkButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        rightDialog.dismiss();
                        Intent intent=new Intent(MainActivity.this, ParkListActivity.class);
                        startActivity(intent);
                    }
                });

        RxViewAction.clickNoDouble(visibleLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        if (isShow) {   //隐藏
                            isShow = false;

                            ObjectAnimator animator = ObjectAnimator.ofFloat(quDataLayout, View.TRANSLATION_X, -width * 0.88f);
                            animator.setDuration(1000);
                            animator.start();

                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(visibleView, View.ROTATION_Y, 180);
                            animator1.setDuration(1000);
                            animator1.start();

                        }else {//显示
                            isShow =true;
                            ObjectAnimator animator = ObjectAnimator.ofFloat(quDataLayout, View.TRANSLATION_X,width * 0f);
                            animator.setDuration(1000);
                            animator.start();

                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(visibleView, View.ROTATION_Y, 360);
                            animator1.setDuration(1000);
                            animator1.start();

                        }
                    }
                });
    }

    public class JsUtils {
        //获取点位坐标
    @JavascriptInterface
    public void givejavapoint(Object msg, CompletionHandler<String> handler) {
        handler.complete(msg+" [ asyn call]");
        point point=gson.fromJson(String.valueOf(msg), point.class);
        if (point.district.equals("市南区")||point.district.equals("城阳区")||point.district.equals("市北区")||point.district.equals("李沧区")||
                point.district.equals("胶州市")||point.district.equals("西海岸新区")||point.district.equals("崂山区")||point.district.equals("平度市")||
                point.district.equals("莱西市")||point.district.equals("即墨区")) {
            PrefsManager.setpoint(point);
            Log.e("地点",point.district);
            district_view.setText(point.district);
            jingdu_view.setText(point.latitude);
            weidu_view.setText(point.longitude);
        }
        Log.e(TAG, "givejavapoint: "+point.district );
        //区市详情

        RequestParams quparams = new RequestParams( RequestUtils.REQUEST_URL+"/api/statistics/getStatiscs");
        quparams.setAsJsonContent(true);
        quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
        Log.i(TAG, "quparams: "+quparams);
        x.http().get(quparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject qujson0bject = null;
                try {
                    qujson0bject = new JSONObject(result);
                    JSONArray qudata = qujson0bject.getJSONArray("data");
                    JSONArray dataLists = null;
                    for(int i=0; i<qudata.length(); i++){
                        String countyPopulation="";
                        String builtArea="";
                        JSONObject jsonObject = qudata.getJSONObject(i);
                        Log.i(TAG, "jsonObject: "+jsonObject.getString("builtArea"));
                        if (jsonObject.getString("countyName").equals(point.district)){
                        if (jsonObject.getString("builtArea")!="null"){
                            builtArea=jsonObject.getString("builtArea");
                        }else {
                            builtArea="";
                        }
                        if (jsonObject.getString("countyPopulation")!="null"){
                            countyPopulation=jsonObject.getString("countyPopulation");
                        }else {
                            countyPopulation="";
                        }
                            renkou_view.setText(jsonObject.getString("countyPopulation"));
                            mianji_view.setText(jsonObject.getString("builtArea"));
                        return;
                        }else {
                            renkou_view.setText("");
                            mianji_view.setText("");
                        }
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
        if (point.ifpark) {
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //searchDialog.dismiss();
                   // linearLayout.setVisibility(View.GONE);
                }
            });
        }else {
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
//                    searchDialog.show();
                 //   linearLayout.setVisibility(View.VISIBLE);
                    Log.i(TAG, "run: "+point.district);
                    if (point.district.equals("市南区")||point.district.equals("城阳区")||point.district.equals("市北区")||point.district.equals("李沧区")||
                            point.district.equals("胶州市")||point.district.equals("西海岸新区")||point.district.equals("崂山区")||point.district.equals("平度市")||
                            point.district.equals("莱西市")||point.district.equals("即墨区")){
                        quDataLayout.setVisibility(View.VISIBLE);
                      //  linearLayout.setVisibility(View.VISIBLE);
                    }else {
                     //   linearLayout.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
    @JavascriptInterface
    public void giveidgetinfo(Object msg, CompletionHandler<String> handler){
        Log.i(TAG, "giveidgetinfo: "+msg);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/garden?id=" + msg);
        params.setAsJsonContent(true);

        params.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());

        Log.e(TAG, "park: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONObject parkObj = jsonObject1.getJSONArray("data").getJSONObject(0);

                        handler.complete(result);
                    }else {
                        Toast.makeText(MainActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
                //progressDialog.dismiss();
            }
        });
    }
    @JavascriptInterface
    public void parkintent(Object msg, CompletionHandler<String> handler){
        Intent intent = new Intent(MainActivity.this, ParkActivity.class);
        intent.putExtra("PARK_ID",msg.toString());
        startActivity(intent);
    }
    }

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
        Uri selectedVideo = data.getData();
        String[] filePathColumn = {MediaStore.Video.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedVideo,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imgPath = cursor.getString(columnIndex);
        cursor.close();
        Log.e(TAG, "onActivityResult: " + imgPath);
        postVideoToServiceRx();
    }
    if (resultCode != Activity.RESULT_OK) {
        return;
    }
}
    private void postVideoToServiceRx() {


        RxHttp.postForm("http://api.ehaohai.com:10100/oa/api/workReport/fileUploadAnByNotToken")
                // .addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken())
                .add("file",new File(imgPath))
                .asUpload(progress -> {
                    //上传进度回调,0-100，仅在进度有更新时才会回调,最多回调101次，最后一次回调Http执行结果
                    int currentProgress = progress.getProgress(); //当前进度 0-100
                    long currentSize = progress.getCurrentSize(); //当前已上传的字节大小
                    long totalSize = progress.getTotalSize();     //要上传的总字节大小



                    Log.e(TAG, "postVideoToServiceRx:当前进度= " +currentProgress );
                    Log.e(TAG, "postVideoToServiceRx:当前已上传的字节大小= " +currentSize );
                    Log.e(TAG, "postVideoToServiceRx:要上传的总字节大小= " +totalSize );
                })
                .subscribe(s -> {
                    Log.e(TAG, "postVideoToServiceRx: 上传成功" +s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String headUrl = data.getString("all");
                    Log.e(TAG, "postVideoToServiceRx: "+headUrl);
                    JSONObject userjson = new JSONObject();
                    try {
                        userjson.put("headUrl", headUrl);
                        userjson.put("id", user.getId());
                        userjson.put("userPasswd", user.getUserPasswd());
                    } catch (JSONException e) {
                    }
                    HhRequestParams quparams = new HhRequestParams(RequestUtils.REQUEST_URL + "api/user");
                    quparams.setAsJsonContent(true);
                    quparams.setBodyContent(userjson.toString());
                    quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
                    x.http().request(HttpMethod.PUT,quparams, new Callback.CommonCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "onSuccess: " + result);
                            user.setHeadUrl(headUrl);
                            Glide.with(getApplicationContext()).load(user.getHeadUrl()).into(imageView3);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e(TAG, "onError: " + ex);
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {
                        }
                    });
                },throwable -> {
                    Log.e(TAG, "postVideoToServiceRx: 上传失败"+throwable.toString());
                    Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 版本更新
     */
    private void getVersion() {
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode + "";
            Log.e(TAG, "getVersion: versionCode -- " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +"api/androidUpgrade/getCurrent");
        Log.e(TAG, "version: " + params);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        //   params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:version-- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")) {
                        JSONObject object = jsonObject1.getJSONArray("data").getJSONObject(0);
                        versionService = object.getString("version");
                        String versionDescription = object.getString("versionDescription");
                        String apkUrl = object.getString("apkUrl");
                        String isForce = object.getString("isForce");
                        Log.e(TAG, "onSuccess:version--1 ");
                        Log.e(TAG, "onSuccess:version--versionCode " + versionCode);
                        Log.e(TAG, "onSuccess:version--versionService " + versionService);
                        // ShowDialog(versionService, apkUrl, versionDescription, isForce);
                        if (Integer.parseInt(versionCode) < Integer.parseInt(versionService)) {
                            Log.e(TAG, "onSuccess:version--2 ");
                            ShowDialog(versionService, apkUrl, versionDescription, isForce);
                        }

                    }

                } catch (JSONException e) {

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
     * 用户更新dialog
     *
     * @param version
     * @param downloadUrl
     * @param versionDesc
     * @param isMustUpgrade
     */
    private void ShowDialog(String version, final String downloadUrl, String versionDesc, String isMustUpgrade) {
        if (isMustUpgrade.equals("0")) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(versionDesc)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(MainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    MainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    MainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }).show();
        } else {
            Log.e(TAG, "onSuccess:version--4 ");
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(versionDesc)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(MainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    MainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    MainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(MainActivity.this.getObbDir().getAbsolutePath(),
                            DOWNLOAD_NAME + versionService + ".apk");

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mBar.setIndeterminate(false);
            mBar.setMax(100);
            mBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mBar.dismiss();
            if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
                // 申请多个权限。
               /* AndPermission.with(MainActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener
                        )
                        .send();*/


                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                isGengxin = true;
                if (Build.VERSION.SDK_INT >= 26) {
                    update();
                   /* boolean b = getPackageManager().canRequestPackageInstalls();
                    if (b) {
                        update();
                    } else {
                        //请求安装未知应用来源的权限
                        //  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                        //   Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.fromParts("package:"+ getPackageName()));
                        //  startActivityForResult(intent, 10086);

                        ActivityCompat.requestPermissions(TEMainActivity.this, new String[]{android.Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                    }*/
                } else {
                    update();
                }

            }

        }
    }

    private void update() {
        isGengxin = false;
        //安装应用
        //  Intent intent = new Intent(Intent.ACTION_VIEW);


        String fileName = MainActivity.this.getObbDir().getAbsolutePath() + "/" + DOWNLOAD_NAME + versionService + ".apk";
      /*  File file = null;
        file = new File(fileName);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.hht.hsatellitemobile.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(this.getObbDir().getAbsolutePath(), DOWNLOAD_NAME + version + ".apk"));
        }

        Log.e(TAG, "update: ----" + tempUri);
        intent.setDataAndType(tempUri,
                "application/vnd.android.package-archive");
        startActivity(intent);*/
        if (Build.VERSION.SDK_INT >= 24) {
            File file = new File(fileName);
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.haohai.garden.wisdomgarden", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(tempUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }
    }

    @Override
    public void onBackPressed() {

        exit();

    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getString(R.string.exit_string),
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {

            Intent intent = new Intent("haohai.haohai.baseActivity");       //关闭程序
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播
            String name = sharedPreferences.getString("userGuid", "");
            Log.d(TAG, "exitreturned:" + name);
            if (name.equals("")){

            }else {
                PackageManager packageManager = getPackageManager();
                Intent rsintent = packageManager.getLaunchIntentForPackage("com.otitan.qdly");
                rsintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rsintent.putExtra("data", "你好");
                startActivity(rsintent);
            }
            this.finish();
            //  System.exit(0);

        }
    }
    //定义一个广播
    public class OutLoginMainBroad extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {

            finish();

        }
    }
}