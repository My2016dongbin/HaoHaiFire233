package com.hht.hsatellitemobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.db.model.Area;
import com.hht.hsatellitemobile.db.model.Setting;
import com.hht.hsatellitemobile.db.model.User;
import com.hht.hsatellitemobile.ui.activity.FeedBackActivity;
import com.hht.hsatellitemobile.ui.activity.LoginActivity;
import com.hht.hsatellitemobile.ui.activity.PicActivity;
import com.hht.hsatellitemobile.ui.activity.SettingActivity;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;
import com.hht.hsatellitemobile.ui.multitype.FireInfo;
import com.hht.hsatellitemobile.ui.multitype.FireInfoList;
import com.hht.hsatellitemobile.ui.multitype.FireInfoViewBinder;
import com.hht.hsatellitemobile.ui.multitype.GroundFire;
import com.hht.hsatellitemobile.ui.multitype.GroundFireViewBinder;
import com.hht.hsatellitemobile.ui.service.AlarmPointService;
import com.hht.hsatellitemobile.ui.service.TrackService;
import com.hht.hsatellitemobile.utils.JsApi;
import com.hht.hsatellitemobile.utils.NotificationsUtils;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.downcell.CommonProgressDialog;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.NumberUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import me.drakeet.multitype.MultiTypeAdapter;
import pl.droidsonroids.gif.GifImageView;
import rx.functions.Action1;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

import static com.hht.hsatellitemobile.ui.activity.LoginActivity.yingjiTags;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MainActivity extends HhBaseActivity implements GroundFireViewBinder.OnGroundFireInfoItemClick,FireInfoViewBinder.OnFireInfoItemClick,JsApi.OnJsClickListener,DatePicker.OnDateChangedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public boolean isJush = false;
    private static final int EXIT = 1;
    private static final int GUGE_YINGXIANG = 2;
    private static final int GUGE_GAOCHENG = 3;
    private static final int TIANDI_SHILIANG = 4;
    private static final int TIANDI_YINGXIANG = 5;
    private static final int MAP_GAOFEN = 13;
    private static final int DIALOG_FIRE_SHOW = 6;
    private static final int MAP_ERROR_SHOW = 8;
    private static final int FIRE_ERROR_SHOW = 9;
    private static final int TWO_D = 10;
    private static final int THREE_D = 11;
    private static final int SHOW_MAP = 7;
    private static final int TIME_CHANGE = 21;
    private static int page = 1;
    private static int rows = 50;
    private static boolean isGaoji = false;
    private static boolean isLoadMore = false;
    private TextView buttonOne;
    private TextView buttonTwo;/*
    private BridgeWebView mWebView;*/
    private DWebView dWebView;
    private String token;
    private static boolean isExit = false;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXIT:
                    Log.e(TAG, "handleMessage: 5" );
                    isExit = false;
                    break;
                case GUGE_GAOCHENG:
                    Log.e(TAG, "handleMessage: 1" );
                    mapChooseDialog.hide();
                    mapChooseButton.setText("[ 谷歌高程 ]");
                    Toast.makeText(MainActivity.this, "已切换为谷歌高程地图", Toast.LENGTH_SHORT).show();
                    break;
                case GUGE_YINGXIANG:
                    Log.e(TAG, "handleMessage: 2" );
                    mapChooseDialog.hide();
                    mapChooseButton.setText("[ 谷歌影像 ]");
                    Toast.makeText(MainActivity.this, "已切换为谷歌影像地图", Toast.LENGTH_SHORT).show();
                    break;
                case TIANDI_SHILIANG:
                    Log.e(TAG, "handleMessage: 3" );
                    mapChooseDialog.hide();
                    mapChooseButton.setText("[ 天地图矢量 ]");
                    Toast.makeText(MainActivity.this, "已切换为天地图矢量地图", Toast.LENGTH_SHORT).show();
                    break;
                case TIANDI_YINGXIANG:
                    Log.e(TAG, "handleMessage: 4" );
                    mapChooseDialog.hide();
                    mapChooseButton.setText("[ 天地图影像 ]");
                    Toast.makeText(MainActivity.this, "已切换为天地图影像地图", Toast.LENGTH_SHORT).show();
                    break;
                case MAP_GAOFEN:
                    mapChooseDialog.hide();
                    mapChooseButton.setText("[ 高分地图 ]");
                    Toast.makeText(MainActivity.this, "已切换为高分地图", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_MAP:
                    dWebView.setVisibility(View.VISIBLE);
                    loadingImage.setVisibility(View.GONE);
                    User user = new DbConfig(getApplicationContext()).getUser();
                    Setting setting = new DbConfig(getApplicationContext()).getSetting();
                    if (startType.equals("JPUSH")){
                       /* dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                                "ALL","ALL","ALL","ALL","1000", setting.getJingwai(),setting.getHuanchong().equals("0")?false:true}, new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "jpush: ---------------------------------------------1" );
                                isJush = true;
                                getFireFromService(currentFireFindTime);
                               // dWebView.setVisibility(View.VISIBLE);
                            }
                        });*/
                    }else {
                        dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                                setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),
                                setting.getJingwai(),setting.getHuanchong()},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue: ---------------------------------------------2" );
                                isJush = false;
                                //  dWebView.setVisibility(View.VISIBLE);
                                getFireFromService(3);
                                //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    break;

                case TWO_D:
                    mapChooseDialog.hide();
                    break;
                case THREE_D:
                    mapChooseDialog.hide();
                    break;
                case TIME_CHANGE:
                    //循环获取地面火警数据
                    getGroundFireDataFromService();
                    break;

            }

        }
    };

    public int currentFireFindTime = 3;

    //火点的handle
    private Handler fireHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int what = data.getInt("what");
            switch (what){
                case DIALOG_FIRE_SHOW:
                    String id = data.getString("id");
                    FireInfo fireInfo = new FireInfo();
                    for (int i = 0; i < fireInfoList.size(); i++) {
                        if (fireInfoList.get(i).getId().equals(id)) {
                            fireInfo = fireInfoList.get(i);
                        }
                    }
                    currentFire = fireInfo;
                    currentFireId = fireInfo.getId();
                    currentFireLa = fireInfo.getLatitude();
                    currentFireLo = fireInfo.getLongitude();
                    Log.e(TAG, "handleMessage: ----" + fireInfo.toString());
                  //  fireAddressText.setText(fireInfo.getFormattedAddress());
                    Log.e(TAG, "initJpushFireData:边境热源=" + fireInfo.getFormattedAddress()+"--");
                    if (currentFire.getFormattedAddress()==null || currentFire.getFormattedAddress().isEmpty()){

                        fireAddressText.setText("边境热源");
                    }else {
                        fireAddressText.setText(fireInfo.getFormattedAddress());
                    }
                    try {
                        fireTimeText.setText(fireInfo.getObservationDateTime().replace("T","  "));
                        jingWeiText.setText(NumberUtils.saveOneBitTwo(Double.parseDouble(fireInfo.getLongitude()))  + "  " +  NumberUtils.saveOneBitTwo(Double.parseDouble(fireInfo.getLatitude())));
                    }catch (Exception e){
                        fireTimeText.setText(" ");
                        jingWeiText.setText(" ");

                    }

                    kexinText.setText(fireInfo.getCredibility() +"");
                    mianjiText.setText(fireInfo.getArea() +"");
                    cishuText.setText(fireInfo.getObservationFrequency() +"");
                    //leixingText.setText("林地(" + fireInfo.getWoodland() * 100 +"%)草地(" + fireInfo.getGrassland() * 100 + "%)农田(" + fireInfo.getFarmland() * 100 + "%)其他(" + fireInfo.getOtherland() + "%)"  );
                    leixingText.setText("林地(" + getTwoDouble(fireInfo.getWoodland() * 100) +"%)草地(" + getTwoDouble(fireInfo.getGrassland() * 100) + "%)农田(" + getTwoDouble(fireInfo.getFarmland() * 100) + "%)其他(" + getTwoDouble(fireInfo.getOtherland() *100 )+ "%)"  );

                    shujuyuanText.setText(fireInfo.getSatellite());
                    huodianCodeText.setText(fireInfo.getFireNo());
                    xiangyuanmianjiView.setText(fireInfo.getPixelArea()+"");
                    xiangyuanshuView.setText(fireInfo.getPixelNumber()+"");

                    Log.e(TAG, "handleMessage:Noaa " + fireInfo.getVisibleLightImageAddress());
                    Log.e(TAG, "handleMessage:Noaa " + fireInfo.getiRImageAddress());

                    try {
                        Log.e(TAG, "WTF: " + fireInfo.getVisibleLightImageAddress() );
                        if (fireInfo.getVisibleLightImageAddress().equals("null") || fireInfo.getVisibleLightImageAddress().equals("") || fireInfo.getVisibleLightImageAddress().length()==0){
                            huodianOneImage.setVisibility(View.GONE);
                        }else {
                            huodianOneImage.setVisibility(View.VISIBLE);
                            if (fireInfo.getVisibleLightImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                                Glide.with(getApplicationContext()).load(fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                                final FireInfo finalFireInfo = fireInfo;
                                RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", finalFireInfo.getVisibleLightImageAddress());
                                        startActivity(intent);
                                    }
                                });
                            }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                                if (fireInfo.getSatellite().indexOf("NOAA") != -1){
                                    Glide.with(getApplicationContext()).load("http://219.239.221.19" + fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                                    final FireInfo finalFireInfo1 = fireInfo;
                                    RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                            intent.putExtra("pic", "http://219.239.221.19" + finalFireInfo1.getVisibleLightImageAddress());
                                            startActivity(intent);
                                        }
                                    });
                                }else {
                                    Glide.with(getApplicationContext()).load("http://web.ehaohai.com:2018" + fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                                    final FireInfo finalFireInfo2 = fireInfo;
                                    RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                            intent.putExtra("pic", "http://web.ehaohai.com:2018" + finalFireInfo2.getVisibleLightImageAddress());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }

                        if (fireInfo.getiRImageAddress().equals("null") || fireInfo.getiRImageAddress().equals("")){
                            huodianTwoImage.setVisibility(View.GONE);
                        }else {
                            huodianTwoImage.setVisibility(View.VISIBLE);
                            if (fireInfo.getiRImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                                Glide.with(getApplicationContext()).load(fireInfo.getiRImageAddress())
                                        .placeholder(R.drawable.ic_jaizai)
                                        .into(huodianTwoImage);
                                final FireInfo finalFireInfo5 = fireInfo;
                                RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", finalFireInfo5.getiRImageAddress());
                                        startActivity(intent);
                                    }
                                });
                            }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                                if (fireInfo.getSatellite().indexOf("NOAA") != -1){
                                    Glide.with(getApplicationContext()).load("http://219.239.221.19" + fireInfo.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                                    final FireInfo finalFireInfo6 = fireInfo;
                                    RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                            intent.putExtra("pic", "http://219.239.221.19" + finalFireInfo6.getiRImageAddress());
                                            startActivity(intent);
                                        }
                                    });
                                }else {
                                    Glide.with(getApplicationContext()
                                    ).load("http://web.ehaohai.com:2018" + fireInfo.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                                    final FireInfo finalFireInfo3 = fireInfo;
                                    RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Log.e(TAG, "call: bingo 图片"  );
                                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                            intent.putExtra("pic", "http://web.ehaohai.com:2018" + finalFireInfo3.getiRImageAddress());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    }catch (Exception e){

                    }




                    fireDialog.show();
                    break;
                case MAP_ERROR_SHOW:
                  //  chaoshiButton.setVisibility(View.VISIBLE);
                    User user = new DbConfig(getApplicationContext()).getUser();
                    Setting setting = new DbConfig(getApplicationContext()).getSetting();
                    if (startType.equals("JPUSH")){
                        loadingImage.setVisibility(View.GONE);
                       /* isJush = true;
                        dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                                "ALL","ALL","ALL","ALL","500", setting.getJingwai(),setting.getHuanchong()}, new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "jpush:error1" );
                                //  dWebView.setVisibility(View.VISIBLE);
                                getFireFromService(currentFireFindTime);
                                Log.e(TAG, "onValue: ---------------------------------------------" );
                            }
                        });*/
                    }else {
                        isJush = false;
                        dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                                setting.getWeixing(),setting.getTiankong(),setting.getDimao(),setting.getDimao(),setting.getNumber(),
                                setting.getJingwai(),setting.getHuanchong()}, new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "jpush:error2" );
                                //  dWebView.setVisibility(View.VISIBLE);


                                getFireFromService(currentFireFindTime);
                                Log.e(TAG, "jpush: ---------------------------------------------3" );
                                //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                  /*  dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),3}, new OnReturnValue<String>() {
                        @Override
                        public void onValue(String retValue) {
                            // dWebView.setVisibility(View.VISIBLE);
                            getFireFromService(3);
                            Log.e(TAG, "onValue: ---------------------------------------------" );
                            //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    break;
                case FIRE_ERROR_SHOW:
                //    showUserTokenDialog("您的身份已失效,请重新登录");
                  /*  Log.e(TAG, "handleMessage: ++++++++++++++++++");
                    Setting setting1 = new DbConfig(getApplicationContext()).getSetting();
                    dWebView.callHandler("huodian", new Object[]{currentFireFindTime,setting1.getWeixing(),setting1.getTiankong(),setting1.getDimian(),setting1.getDimao(),setting1.getNumber(),setting1.getJingwai(),setting1.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                        @Override
                        public void onValue(String retValue) {
                            searchDialog.hide();
                            isopenFireDialog = true;
                            //   getFireFromService(currentFireFindTime);
                        }
                    });*/
                    break;
            }

        }
    };


    private Dialog mapChooseDialog;
    private View mapChooseInflater;
    private LinearLayout gugeGaochengMap;
    private LinearLayout gugeYingxiangMap;
    private LinearLayout tiandiShiliangMap;
    private LinearLayout tiandiYingxiangMap;
    private TextView mapChooseButton;
    private TextView mapDimensionChoos;
    public boolean isTwoD = true;
    private ImageView huojingButton;
    private ImageView searchButton;
    private Dialog searchDialog;
    private View searchInflater;
    private LinearLayout oneHoursLayout;
    private LinearLayout currntTimeLayout;
    private LinearLayout threeHoursLayout;
    private LinearLayout oneDayLayout;
    private LinearLayout threeDayLayout;
    private LinearLayout fiveDayLayout;
    private LinearLayout gaojiSearchLayout;
    private Dialog fireInfoListDialog;
    private View fireInfoListInflater;
    private TextView fireCountText;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView listView;
    public List<FireInfoList> fireInfoListList;
    public List<FireInfo> fireInfoList;
    public boolean isopenFireDialog = false;
    private String total;
    public String country;
    private Dialog fireDialog;
    private View fireInflater;
    private TextView fireAddressText;
    private TextView fireTimeText;
    private TextView jingWeiText;
    private TextView kexinText;
    private TextView mianjiText;
    private TextView cishuText;
    private TextView leixingText;
    private TextView shujuyuanText;
    private TextView huodianCodeText;
    private TextView fankuiText;
    private ImageView huodianOneImage;
    private ImageView huodianTwoImage;

    public String fireId = "noId";
    private ImageView outLoginButton;
    private Dialog gaojiDialog;
    private View gaojiInflater;
    private TextView huanchongText;
    private LinearLayout weixingAllLayout;
    private ImageView weixingAllImage;
    private LinearLayout weixingFY3Layout;
    private ImageView weixingFY3Image;
    private LinearLayout weixingFY4Layout;
    private ImageView weixingFY4Image;
    private LinearLayout weixingNPPLayout;
    private ImageView weixingNPPImage;
    private LinearLayout weixingHima8Layout;
    private ImageView weixingHima8Image;
    private LinearLayout weixingNOAA19Layout;
    private LinearLayout weixingNOAA20Layout;
    private LinearLayout weixingGK2aLayout;
    private ImageView weixingNOAA19Image;
    public boolean weixingAllChoose = true;
    public boolean weixingNPPChoose = true;
    public boolean weixingFY4Choose = true;
    public boolean weixingFY3Choose = true;
    public boolean weixingHIMA8Choose = true;
    public boolean weixingNOAA19Choose = true;
    public boolean weixingNOAA20Choose = true;
    public boolean weixingGK2aChoose = true;
    private LinearLayout tiankongAllLayout;
    private ImageView tiankongAllImage;
    private LinearLayout tiankongWurenjiLayout;
    private ImageView tiankongWurenjiImage;
    private LinearLayout tiankongXuanfuqiLayout;
    private ImageView tiankongXuanfuqiImage;
    public boolean tiankongAllChoose = false;
    public boolean tiankongWurenjiChoose = false;
    public boolean tiankongXuancifuChoose = false;
    private LinearLayout dimianAllLayout;
    private ImageView dimianAllImage;
    private LinearLayout dimianSheyingLayout;
    private ImageView dimianSheyingImage;
    private LinearLayout dimianHulinLayout;
    private ImageView dimianHulinImage;
    private LinearLayout dimianLiaowangLayout;
    private ImageView dimianLiaowangImage;
    private LinearLayout dimianQunzhongLayout;
    private ImageView dimianQunzhongImage;
    public boolean dimianAllChoose = false;
    public boolean dimianSheyingChoose = false;
    public boolean dimianHulinChoose = false;
    public boolean dimianLiaowangChoose = false;
    public boolean dimianQunzhongChoose = false;
    private LinearLayout dimaoAllLayout;
    private ImageView dimaoAllImage;
    private LinearLayout dimaoLindiLayout;
    private ImageView dimaoLindiImage;
    private LinearLayout dimaoCaodiLayout;
    private ImageView dimaoCaodiImage;
    private LinearLayout dimaoNongtianLayout;
    private ImageView dimaoNongtianImage;
    private LinearLayout dimaoQitaLayout;
    private ImageView dimaoQitaImage;
    public boolean dimaoAllChoose = true;
    public boolean dimaoLindiChoose = true;
    public boolean dimaoCaodiChoose = true;
    public boolean dimaoNongtianChoose = true;
    public boolean dimaoQitaChoose = true;
    private LinearLayout gaojiStarTimeLayout;
    private LinearLayout gaojiEndTimeLayout;
    private TextView gaojiStartimeText;
    private TextView gaojiEndTimeText;
    private StringBuffer date;
    private StringBuffer endDate;
    private int year;


    private int month;
    private int day;
    public int chooseHour;
    public int chooseMinute;
    public boolean isChooseStarTime ;
    private LinearLayout shiLayout;
    private LinearLayout shengLayout;
    private LinearLayout quLayout;
    private TextView shengText;
    private TextView shiText;
    private TextView quText;
    public List<Area> shengList;
    public List<String> shengStrList;
    public List<Area> shiList;
    public List<Area> quList;
    public List<String> shiStrList;
    public List<String> quStrList;
    private WheelView areaWy;
    public String companyName = "";
    public String provinceNo = "";
    public String provinceName = "";
    public String cityNo = "";
    public String cityName = "";
    public String countyNo = "";
    public String countyName = "";
    public int currentQuanxian = 0;   //0是全国权限  1是省级权限  2市级权限
    public int currentChooseArea = 0;  //当前在选择省还是市   0选择省  1选择市
    public String currentChooseSheng = "";
    public String currentChooseShi = "";
    public String currentChooseQu = "";
    public int shengSelectIndex = 0;
    public int shiSelectIndex = 0;
    public int quSelectIndex = 0;
    public boolean isChooseSheng = false;
    private LinearLayout jingwanLayout;
    private ImageView jingwaiImage;
    private LinearLayout huanChongLayout;
    private ImageView huanChongImage;
    public boolean isChooseJingwai = false;
    public boolean isChooseHuanchong = false;
    private TextView chongzhiButton;
    public FireInfo currentFire;
    public String currentFireId = "";
    public String currentFireLo = "";
    public String currentFireLa = "";
    public String currentFireNo = "";
    private TextView findButton;
    private ProgressDialog gaojiFindDialog;
    private TextView weixingAllText;
    private TextView weixingFY3Text;
    private TextView weixingFY4Text;
    private TextView weixingNppText;
    private TextView weixingHima8Text;
    private TextView weixingNOAA19Te;
    private TextView weixingNOAA20Te;
    private TextView weixingGK2aTe;
    private TextView tiankongAllText;
    private TextView tiankongWurenjiText;
    private TextView tiankongXuanfuqiText;
    private TextView dimianAllText;
    private TextView dimianSheyingText;
    private TextView dimianHulinText;
    private TextView dimianLiaowangText;
    private TextView dimianQunzhongText;
    private TextView dimaoAllText;
    private TextView dimaoLindiText;
    private TextView dimaoCaodiText;
    private TextView dimaoNongtianText;
    private TextView dimaoQitaText;
    private String versionCode;
    private String versionService;
    public boolean isGengxin = false;
    private static final String DOWNLOAD_NAME = "haohai_";
    private CommonProgressDialog mBar;
    private Uri tempUri;
    private GifImageView loadingImage;
    private String startType;  //JPUSH推送打开
    private ImageView warnImage;
    private DynamicReceiver dynamicReceiver;
    private TextView jingwaiText;
    private Button chaoshiButton;
    private LinearLayout quyuLayout;
    private ImageView gugeYingxiangImage;
    private TextView gugeYingxiangText;
    private ImageView gugeGaochengImage;
    private TextView gugeGaochengText;
    private ImageView tiandiShiliangImage;
    private TextView tiandiShiliangText;
    private ImageView tiandiYingxiangImage;
    private TextView tiandiYingxiangText;
    private ImageView tucengButton;
    private boolean hasActivity = false;
    private LinearLayout twoDLayout;
    private LinearLayout threeDLayout;
    private ImageView twoDImage;
    private ImageView threeDImage;
    private TextView twoDText;
    private TextView threeDText;
    public String currentDime = "2D";
    private PullRefreshLayout pullView;
    private ImageView refreshButton;
    public int reloginState = 0;  //0是执行高级查询火点  1是执行普通火点
    private Bundle bundle;
    private OutLoginReceiver outLoginReceiver;
    private FireClickReceiver fireClickReceiver;
    private ImageView openApp;
    private ImageView openShipinApp;
    private View groundFireListInflater;
    private RecyclerView fireListView;
    private Dialog groundFireListDialog;
    private MultiTypeAdapter groundAdapter;
    private List<Object> groundItems = new ArrayList<>();
    private List<GroundFire> groundFireList;
    private List<GroundFire> groundNewFireList;
    private ImageView groundFireView;
    private Dialog groundFireInfoDialog;
    private View groundFireInfoInflater;
    private TextView groundAddressView;
    private TextView groundLngView;
    private TextView groundLatView;
    private TextView groundDataView;
    private ImageView groundOneImageView;
    private ImageView groundTwoImageView;
    private TextView groundQuerenButton;
    private TextView groundHulueButton;
    private String currentGroundFireID;
    private Dialog phoneChooseDialog;
    private View phoneChooseInflate;
    private RadioGroup phoneChooseRg;
    private RadioButton phoneRb1;
    private RadioButton phoneRb2;
    private RadioButton phoneRb3;
    private RadioButton phoneRb4;
    private String currentPhone;
    private TextView phoneChooseButton;
    private Timer timer;
    private LinearLayout gaofenMapLayout;
    private ImageView gaofenImage;
    private TextView gaofenText;
    private TextView xiangyuanmianjiView;
    private TextView xiangyuanshuView;
    private int currentFireListType = 1;  //1是时间排序  2是编号分类
    private LinearLayout fenleiLayout;
    private TextView fenleiView;
    private AlertDialog.Builder builder;
    private  int choose1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 开始用户轨迹更新服务
         */
        startService(new Intent(getApplicationContext(), TrackService.class));

        Intent intent = getIntent();
        bundle = intent.getExtras();
        // token = bundle.getString("token");
        token = new DbConfig(this).getUser().getToken();
        startType = bundle.getString("START_TYPE");

        Log.e(TAG, "jpush" + startType );
        User user = new DbConfig(this).getUser();
        if (user!=null){
            String username = user.getUsername();
            Log.e(TAG, "onCreate: username=======" + username);
        }
        if (user.getIsLogin().equals("0")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        gaojiFindDialog = new ProgressDialog(this);
        date = new StringBuffer();
        endDate = new StringBuffer();
        fireInfoListList = new ArrayList<>();
        fireInfoList = new ArrayList<>();
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        quList = new ArrayList<>();
        shengStrList = new ArrayList<>();
        shiStrList = new ArrayList<>();
        quStrList = new ArrayList<>();
        currentFire = new FireInfo();
        groundFireList = new ArrayList<>();
        groundNewFireList = new ArrayList<>();

        //版本更新
        getVersion();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    requestNotofication();
                }catch (Exception e){
                    Log.e(TAG, "run: " + e.getMessage() );
                }
            }
        },5000);

        List<String> aList = new ArrayList<>();
        List<List<String>> bList = new ArrayList<>();
        aList.add("aa");
        aList.add("bb");
        bList.add(aList);

        aList = new ArrayList<>();
        aList.add("cc");
        aList.add("dd");
        bList.add(aList);

        for (int i = 0; i < bList.size(); i++) {
            List<String> clist = bList.get(i);
            for (int j = 0; j < clist.size(); j++) {
                Log.e(TAG, "onCreate: "+ clist.get(j));
            }

        }


        Intent intent1 = new Intent(this, AlarmPointService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            this.startForegroundService(intent1);
        } else {
            this.startService(intent1);
        }

        country = "中国";
        timeTest();     //时间测试

        initView();
        initDateTime();
        isopenFireDialog = false;

        DbConfig dbConfig = new DbConfig(this);


        if (startType.equals("JPUSH")){     //极光推送打开app   根据火警id查出火警详情 判断火警发生时间距现在时间的小时  查询出时间内的所有火警展示  缩放到当前火警
            hasActivity = false;
            user.setJpush(true);
            DbManager db = dbConfig.getDbManager();
            try {
                db.saveOrUpdate(user);
            } catch (DbException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "jpush1" );
            String fireStr = bundle.getString("fire_id");
            Log.e(TAG, "jpush1: "+fireStr);
          //  JpsuhFireInfo(fireStr);
        }else {
            Log.e(TAG, "onCreate: app----1" );
            user.setJpush(false);
            DbManager db = dbConfig.getDbManager();
            try {
                db.saveOrUpdate(user);
            } catch (DbException e) {
                e.printStackTrace();
            }
            Setting setting = new DbConfig(this).getSetting();
            dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                    setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),
                    setting.getJingwai(),setting.getHuanchong()},new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    Log.e(TAG, "onValue: ---------------------------------------------4" );
                    isJush = false;
                 //   dWebView.setVisibility(View.VISIBLE);
                    getFireFromService(3);
                  //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                }
            });
        }
        //getUserMsgFromService();


        /*//开启循环获取地面火警数据 todo 230403 轮询去除
        if (timer ==null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // (1) 使用handler发送消息
                    Log.e(TAG, "service: 过了10秒" );
                    Message message=new Message();
                    message.what=TIME_CHANGE;
                    mHandler.sendMessage(message);
                }
            },0, 10000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

        }*/
    }

    /**
     * 获取地面火警数据
     */
    private void getGroundFireDataFromService() {
        RequestParams params = new RequestParams("http://49.232.128.132:10171/api/FireAlarm/GetFireAlarmList");
        params.setConnectTimeout(10000);
        Log.e(TAG, "getSyncPlotListData: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    groundNewFireList.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String alarmID = object.getString("AlarmID");
                        String alarmLongitude = object.getString("AlarmLongitude");
                        String alarmLatitude = object.getString("AlarmLatitude");
                        String picPath1 = object.getString("PicPath1");
                        String picPath2 = object.getString("PicPath2");
                        String alarmDateTime = object.getString("AlarmDateTime");
                        String IsDispose = object.getString("IsDispose");
                        GroundFire groundFire = new GroundFire(alarmID, alarmLongitude, alarmLatitude, alarmDateTime, picPath1, picPath2, IsDispose);
                        groundNewFireList.add(groundFire);
                       /* boolean isNewData = true;
                        for (int j = 0; j < groundFireList.size(); j++) {
                            if (groundFireList.get(j).getAlarmID().equals(alarmID)) {  //如果相等则为老数据 否则为新数据加到loatNewList中
                                isNewData = false;
                            }
                        }
                        if (isNewData){
                            GroundFire groundFire = new GroundFire(alarmID, alarmLongitude, alarmLatitude, alarmDateTime, picPath1, picPath2, isLocation);
                            groundNewFireList.add(groundFire);
                        }*/
                    }
                    if (groundNewFireList.size()>groundFireList.size()){
                        groundFireView.setImageResource(R.drawable.ic_button_huojing_red);
                    }
                    initGroundFireData();

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

    private void initGroundFireData() {

        groundItems.clear();
        Log.e(TAG, "initFireData: 火点4");
        for (int i = 0; i < groundNewFireList.size(); i++) {
            groundItems.add(groundNewFireList.get(i));
        }
        groundFireList.clear();
        groundFireList.addAll(groundNewFireList);
        assertAllRegistered(groundAdapter,groundItems);
        groundAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.e(TAG, "onRestart: ---");
        hasActivity = true;
        User user = new DbConfig(this).getUser();
        if (user.getIsLogin().equals("0")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        boolean isJpush = user.isJpush();
        if (isJpush){     //极光推送打开app   根据火警id查出火警详情 判断火警发生时间距现在时间的小时  查询出时间内的所有火警展示  缩放到当前火警
          /*  Log.e(TAG, "jpush1" );
            String fireStr = user.getJpushStr();
            Log.e(TAG, "jpush1: "+fireStr);、


            JpsuhFireInfo(fireStr);*/
        }else {
           //  currentFireFindTime = 3;
          //  showDialogProgress(gaojiFindDialog,"查询中...");
           /* Toast.makeText(this, "已显示最新火点信息", Toast.LENGTH_SHORT).show();
            Setting setting = new DbConfig(getApplicationContext()).getSetting();
            dWebView.callHandler("huodian", new Object[]{currentFireFindTime,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    searchDialog.hide();
                    isJush = false;
                  //  Toast.makeText(MainActivity.this, "已显示3小时之内的火点信息", Toast.LENGTH_SHORT).show();
                    isopenFireDialog = true;
                    getFireFromService(currentFireFindTime);
                }
            });*/
        }
    }



    private void  JpsuhFireInfo(String fireStr) {
        Log.e(TAG, "jpush2" );
        JSONObject fireObj = null;
        try {
            fireObj = new JSONObject(fireStr);
            String observationDateTime = fireObj.getString("ObservationDatetime");
            String fireNo = fireObj.getString("fireNo");
            String fire_id = fireObj.getString("fire_id");
            String location = fireObj.getString("location");
            String[] strArray = location.split(",");
            String longitude = strArray[0];
            String latitude = strArray[1];
            Log.e(TAG, "jpush: longitude"+ longitude);
            Log.e(TAG, "jpush: latitude "+ latitude);

      /*
            String id = fireObj.getString("Id");
            String longitude = fireObj.getString("Longitude");
            String latitude = fireObj.getString("Latitude");
            int observationFrequency = fireObj.getInt("ObservationFrequency");
            String observationDateTime = fireObj.getString("ObservationDateTime");
            int strength = fireObj.getInt("Strength");
            int strengthLevel = fireObj.getInt("StrengthLevel");
            double woodland = fireObj.getDouble("Woodland");
            double grassland = fireObj.getDouble("Grassland");
            double farmland = fireObj.getDouble("Farmland");
            double otherland = fireObj.getDouble("Otherland");
            double area = fireObj.getDouble("Area");
            double credibility = fireObj.getDouble("Credibility");
            double pixelArea = fireObj.getDouble("PixelArea");
            int pixelNumber = fireObj.getInt("PixelNumber");
            String country = fireObj.getString("Country");
            String countryCode = fireObj.getString("CountryCode");
            String province = fireObj.getString("Province");
            String provinceCode = fireObj.getString("ProvinceCode");
            String city = fireObj.getString("City");
            String cityCode = fireObj.getString("CityCode");
            String county = fireObj.getString("County");
            String countyCode = fireObj.getString("CountyCode");
            String formattedAddress = fireObj.getString("FormattedAddress");
            String visibleLightImageAddress = fireObj.getString("VisibleLightImageAddress");
            String irImageAddress = fireObj.getString("IRImageAddress");
            String satellite = fireObj.getString("Satellite");
            String putStorageTime = fireObj.getString("PutStorageTime");
            String dataSourceFile = fireObj.getString("DataSourceFile");
            String fireNo = fireObj.getString("FireNo");
            FireInfo fireInfo = new FireInfo(id,longitude,latitude,observationFrequency,observationDateTime,strength,strengthLevel,woodland,grassland,farmland,otherland,area,credibility,pixelArea,
                    pixelNumber,country,countryCode,province,provinceCode,city,cityCode,county,countyCode,formattedAddress,visibleLightImageAddress,irImageAddress,satellite,
                    putStorageTime,dataSourceFile,fireNo);
            currentFire = fireInfo;
            currentFireId = fireInfo.getId();
            currentFireLa = fireInfo.getLatitude();
            currentFireLo = fireInfo.getLongitude();
*/
            currentFireId = fire_id;
            currentFireLa = longitude;
            currentFireLo = latitude;
            currentFireNo = fireNo;
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date1 = df.parse(df1.format(new Date()));
            Date date2 = df.parse(observationDateTime);
            long diff = date1.getTime() - date2.getTime();
            long hour = diff / 1000/(60*60);
            Log.e(TAG, "onCreate: hour--" + hour);//根据时间去查询之前的火点跟地图上的火点

            currentFireFindTime = (int) hour + 1;
            Log.e(TAG, "jpush:currentFireFindTime= " + currentFireFindTime);
            showDialogProgress(gaojiFindDialog,"查询中...");
            Setting setting = new DbConfig(getApplicationContext()).getSetting();
          /*  dWebView.callHandler("huodian", new Object[]{currentFireFindTime,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    isJush = true;
                    searchDialog.hide();
                    Toast.makeText(MainActivity.this, "已显示1天之内的火点信息", Toast.LENGTH_SHORT).show();
                    isopenFireDialog = true;
                    getFireFromService(currentFireFindTime);
                }
            });*/
            Log.e(TAG, "jpush3" + hour );
            User user = new DbConfig(this).getUser();

            if (hasActivity){       //直接查询
                dWebView.callHandler("huodian", new Object[]{currentFireFindTime, "ALL","ALL","ALL","ALL","1000",setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        isJush = true;
                       // Toast.makeText(MainActivity.this, "已显示3小时之内的火点信息", Toast.LENGTH_SHORT).show();
                        getFireFromService(currentFireFindTime);
                    }
                });
            }else {
                dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                        "ALL","ALL","ALL","ALL","1000", setting.getJingwai(),setting.getHuanchong().equals("0")?false:true}, new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        Log.e(TAG, "jpush4" );
                        isJush = true;
                        getFireFromService(currentFireFindTime);
                        dWebView.setVisibility(View.VISIBLE);
                        Log.e(TAG, "jpush: ---------------------------------------------5" );
                        //   Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                    }
                });
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


        String time2 = "2019-08-22 12:22:20";


    }

    private void timeTest() {

        String data = "{\"employees\":[{\"firstName\":\"Bill\",\"lastName\":\"Gates\"},{\"firstName\":\"George\",\"lastName\":\"Bush\"},{\"firstName\":\"Thomas\",\"lastName\":\"Carter\"}]}";
        try {
            JSONObject jsonObject = new JSONObject(data);
            Log.e(TAG, "timeTest: " +jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

/*        Object systemService = this.getSystemService(Context.AUDIO_SERVICE);
        systemService.
        AudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;*/

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max1 = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);// 1
        int current1 = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.e("service", "系统音量值：" + max1 + "-" + current1);

        int max2 = am.getStreamMaxVolume(AudioManager.STREAM_RING);// 2
        int current2 = am.getStreamVolume(AudioManager.STREAM_RING);
        Log.e("service", "系统铃声值：" + max2 + "-" + current2);

        int max3 = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);// 5
        int current3 = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        Log.e("service", "提示声音音量值：" + max3 + "-" + current3 );


        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = "2019-08-22 12:22:20";
        try {
            Date date1 = df.parse(df1.format(new Date()));
            Date date2 = df.parse(time2);
            long diff = date1.getTime() - date2.getTime();
            long hour = diff / 1000/(60*60);
           // Log.e(TAG, "onCreate: hour--" + hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //实例化IntentFilter对象
        IntentFilter filterFireClick = new IntentFilter();
        filterFireClick.addAction("fire_click");
        fireClickReceiver = new FireClickReceiver();
        //注册广播接收
        registerReceiver(fireClickReceiver,filterFireClick);
    }

    @Override
    protected void onResume() {
        super.onResume();


        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("jpush_fire");
        dynamicReceiver = new DynamicReceiver();
        //注册广播接收
        registerReceiver(dynamicReceiver,filter);

        //实例化IntentFilter对象
        IntentFilter filterOutLogin = new IntentFilter();
        filterOutLogin.addAction("out_login");
        outLoginReceiver = new OutLoginReceiver();
        //注册广播接收
        registerReceiver(outLoginReceiver,filterOutLogin);


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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_VERSION);
        Log.e(TAG, "version: " + params);
     //   params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:version-- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    int versionCodeService = jsonObject1.getInt("versionCode");
                    String versionDesc = jsonObject1.getString("versionDesc");
                    versionService = jsonObject1.getString("version");
                    String downloadUrl = jsonObject1.getString("downloadUrl");
                    boolean isMustUpgrade = jsonObject1.getBoolean("isMustUpgrade");
                    if (Integer.parseInt(versionCode) < versionCodeService){
                       ShowDialog(versionService, downloadUrl, versionDesc, isMustUpgrade);

                    }
                  //  ShowDialog(versionService, "http://49.232.128.132:10172/app/qd_wgh1.1.2.apk", versionDesc, true);
                 //   ShowDialog(versionService, "http://27.223.18.10:2020/app/HYSatellite.apk", versionDesc, true);
               //    ShowDialog(versionService, "http://web.ehaohai.com:2020/app/HYSatellite.apk", versionDesc, true);

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
     * @param version
     * @param downloadUrl
     * @param versionDesc
     * @param isMustUpgrade
     */
    private void ShowDialog(String version, final String downloadUrl, String versionDesc, boolean isMustUpgrade) {
        if (!isMustUpgrade) {
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
     * 获取用户信息
     */
    private void getUserMsgFromService() {
        Log.e(TAG, "getUserMsgFromService:11111 ");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetUserMsg");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
        //params.setConnectTimeout(20000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: userMeg===========" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String userId = jsonObject.getString("UserId");
                    String userName = jsonObject.getString("UserName");
                    companyName = jsonObject.getString("CompanyName");
                    provinceNo = jsonObject.getString("ProvinceNo");
                    provinceName = jsonObject.getString("ProvinceName");
                    cityNo = jsonObject.getString("CityNo");
                    cityName = jsonObject.getString("CityName");
                    countyNo = jsonObject.getString("CountyNo");
                    countyName = jsonObject.getString("CountyName");

                    Log.e(TAG, "onSuccess: provinceNo == " + provinceNo);

                    if (provinceNo.equals("null")){  //全国权限  0
                        Log.e(TAG, "initArea: -0" );
                        currentQuanxian = 0;
                        quLayout.setVisibility(View.GONE);
                        quyuLayout.setVisibility(View.VISIBLE);
                     //   getAllAre();
                    } else {
                        if (!cityNo.equals("null")){     //市权限  2
                            currentQuanxian = 2;
                            Log.e(TAG, "initArea: -2" );
                            quLayout.setVisibility(View.GONE);
                            shengLayout.setVisibility(View.GONE);
                            quyuLayout.setVisibility(View.GONE);
                        }else {         //省权限1
                            currentQuanxian = 1;
                            Log.e(TAG, "initArea: -1" );
                            quLayout.setVisibility(View.GONE);
                            shengLayout.setVisibility(View.GONE);
                            quyuLayout.setVisibility(View.VISIBLE);
                        }

                     //   getShengAre(provinceNo);
                    }

                   /* if (!provinceNo.isEmpty()){ //省级权限 隐藏省    provinceNo是空的为全国权限  不为空为省级权限
                        Log.e(TAG, "initArea: -2" );
                        quLayout.setVisibility(View.GONE);
                        shengLayout.setVisibility(View.GONE);
                        getShengAre(provinceNo);
                    }else {//全国权限
                        Log.e(TAG, "initArea: -3" );
                        quLayout.setVisibility(View.GONE);
                        getAllAre();
                    }*/

                    User user = new DbConfig(getApplicationContext()).getUser();
                    user.setUserId(userId);
                    user.setCompanyName(companyName);
                    user.setProvinceName(provinceName);
                    user.setProvinceNo(provinceNo);
                    user.setCityName(cityName);
                    user.setCityNo(cityNo);
                    user.setCountyName(countyName);
                    user.setCountyNo(countyNo);

                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }



                   // initArea();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        String token = jsonObject.getString("message");

                        getUserInfo(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
           //     Toast.makeText(MainActivity.this, "网络异常，请检查网络链接14", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + ex.toString());
               // showUserTokenDialog("您的身份已失效,请重新登录");
               /* Toast.makeText(MainActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
                String message = ex.toString();
                if(message.indexOf("401")!=-1){ //在其他手机登录  跳到登录界面
                    showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                }else{
                    //  Toast.makeText(MainActivity.this, "网络异常，请检查网络链接11", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void initArea() {
        User user = new DbConfig(this).getUser();
        Log.e(TAG, "initArea: -1" );
        Log.e(TAG, "initArea: -1" + user.getProvinceNo());
        Log.e(TAG, "initArea: -1" + user.getCityNo());
        Log.e(TAG, "initArea: -1" + user.getCountyNo());


        if (user.getCityNo() != null){  //市权限 隐藏区
            quLayout.setVisibility(View.GONE);
        }
        if (user.getProvinceNo() !=null ){ //省级权限 隐藏省
            Log.e(TAG, "initArea: -2" );
            quLayout.setVisibility(View.GONE);
            shengLayout.setVisibility(View.GONE);
            getShengAre(new DbConfig(this).getUser().getProvinceNo());
        }else {//全国权限
            Log.e(TAG, "initArea: -3" );
            quLayout.setVisibility(View.GONE);
            getAllAre();
        }

    }

    private void initView() {
        Log.e(TAG, "initView: -----");
        openApp = (ImageView) findViewById(R.id.open_app);
        openShipinApp = (ImageView) findViewById(R.id.open_shipin_app);
        groundFireView = (ImageView) findViewById(R.id.dimian_fire_list);
        RxViewAction.clickNoDouble(groundFireView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        groundFireView.setImageResource(R.drawable.ic_button_huojing);
                        groundFireListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(openShipinApp)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        /**知道要跳转应用的包命与目标Activity*/
                        ComponentName componentName = new ComponentName("com.hyphenate.chatuidemo", "com.hyphenate.chatuidemo.ui.MainActivity");
                        intent.setComponent(componentName);
                        //     intent.putExtra("", "");//这里Intent传值
                        startActivity(intent);
                    }
                });
        /**
         * 打开同步标会app
         */
        RxViewAction.clickNoDouble(openApp)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        /**知道要跳转应用的包命与目标Activity*/
                        ComponentName componentName = new ComponentName("com.skyline.terraexplorer", "com.skyline.terraexplorer.controllers.TEMainActivity");
                        intent.setComponent(componentName);
                   //     intent.putExtra("", "");//这里Intent传值
                        startActivity(intent);
                    }
                });

        chaoshiButton = (Button) findViewById(R.id.chaoshi_button);
        warnImage = (ImageView) findViewById(R.id.warn_image);
        if (startType.equals("JPUSH")){
            warnImage.setVisibility(View.VISIBLE);
        }else {
            warnImage.setVisibility(View.GONE);
        }
        loadingImage = (GifImageView) findViewById(R.id.loadging_image);
        outLoginButton = (ImageView) findViewById(R.id.out_login_button);
        huojingButton = (ImageView) findViewById(R.id.huojing_button);
        searchButton = (ImageView) findViewById(R.id.search_button);
        mapDimensionChoos = (TextView) findViewById(R.id.map_2d_3d_choose);
        mapChooseButton = (TextView) findViewById(R.id.map_choose_button);
        tucengButton = (ImageView) findViewById(R.id.tuceng_button);
        refreshButton = (ImageView) findViewById(R.id.refresh_button);
       /* pullView = (PullRefreshLayout) findViewById(R.id.prl_view);
        pullView.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //修改数据的代码，最后记得填上此行代码
                pullView.setRefreshing(false);
            }
        });*/
       RxViewAction.clickNoDouble(refreshButton)
               .subscribe(new Action1<Void>() {
                   @Override
                   public void call(Void aVoid) {
                       currentFireFindTime = 3;
                       showDialogProgress(gaojiFindDialog,"查询中...");
                       Setting setting = new DbConfig(getApplicationContext()).getSetting();
                       dWebView.callHandler("huodian", new Object[]{1,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                           @Override
                           public void onValue(String retValue) {
                               searchDialog.hide();
                               isJush = false;
                               Toast.makeText(MainActivity.this, "已显示最新火点信息", Toast.LENGTH_SHORT).show();
                               isopenFireDialog = false;
                               getFireFromService(1);

                           }
                       });
                   }
               });


        dWebView = (DWebView) findViewById(R.id.dwebView);
        DWebView.setWebContentsDebuggingEnabled(true);

        dWebView.clearCache(true);
        dWebView.getSettings().setAllowFileAccess(true);

        dWebView.getSettings().setJavaScriptEnabled(true);

        String url = "file:///android_asset/index.html";
        //url：网页地址；name/pwd:cookie信息
       // String isUser = SPUtils.getInstance().getString("userContent");
        dWebView.loadUrl(url);
        dWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mHandler.sendEmptyMessageDelayed(SHOW_MAP,1000);
             //   dWebView.setVisibility(View.VISIBLE);
            }
        });
        User user = new DbConfig(this).getUser();

        dWebView.addJavascriptInterface(this, "jk");
        JsApi jsApi = new JsApi(this);
        jsApi.setListener(this);
        dWebView.addJavascriptObject(jsApi,null);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            dWebView.setWebContentsDebuggingEnabled(true);
        }
        //添加跨域支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            dWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            dWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        }else{
            try {
                Class<?> clazz = dWebView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(dWebView.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
     //   dWebView.disableJavascriptDialogBlock(true);

        //自动登录，改成自动进入主页


      /*  dWebView.callHandler("androidRequestMap", new Object[]{user.getUsername(),user.getPassword(),user.getToken(),3},new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue: ---------------------------------------------" );

                //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
            }
        });*/

        Log.e(TAG, "initView: token ===" + token );
 /*       dWebView.callHandler("nativeToJs", new Object[]{token}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Toast.makeText(MainActivity.this, "denglu", Toast.LENGTH_SHORT).show();
            }
        });*/



     /*  dWebView.callHandler("autoLogin", new Object[]{userName, pwd,token}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Toast.makeText(MainActivity.this, "denglu", Toast.LENGTH_SHORT).show();
            }
        });*/



       // dWebView.addJavascriptObject(new JsApi(this),"gaocheng");


        /**
         * 地图切换 dialog
         */
        mapChooseDialog = new Dialog(this, R.style.ActionSheetDialogStyleRight);
        mapChooseInflater = LayoutInflater.from(this).inflate(R.layout.dialog_map_choose_new,null);
        mapChooseInflater.setMinimumWidth(10000);
        gaofenMapLayout = ((LinearLayout) mapChooseInflater.findViewById(R.id.gaofen_map));
        gaofenImage = ((ImageView) mapChooseInflater.findViewById(R.id.gaofen_map_image));
        gaofenText = ((TextView) mapChooseInflater.findViewById(R.id.gaofen_map_texy));
        gugeGaochengMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.guge_gaocheng));
        gugeYingxiangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.guge_yingxiang));
        tiandiShiliangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.tiandi_shiliang));
        tiandiYingxiangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.tiandi_yingxiang));
        gugeYingxiangImage = ((ImageView) mapChooseInflater.findViewById(R.id.guge_yingxiang_image));
        gugeYingxiangText = ((TextView) mapChooseInflater.findViewById(R.id.guge_yingxiang_text));
        gugeGaochengImage = ((ImageView) mapChooseInflater.findViewById(R.id.guge_gaocheng_image));
        gugeGaochengText = ((TextView) mapChooseInflater.findViewById(R.id.gugegaocheng_text));
        tiandiShiliangImage = ((ImageView) mapChooseInflater.findViewById(R.id.tiandi_shiliang_image));
        tiandiShiliangText = ((TextView) mapChooseInflater.findViewById(R.id.tiandi_shiliang_text));
        tiandiYingxiangImage = ((ImageView) mapChooseInflater.findViewById(R.id.tiandi_yingxiang_image));
        tiandiYingxiangText = ((TextView) mapChooseInflater.findViewById(R.id.tiandi_yingxiang_text));
        twoDLayout = ((LinearLayout) mapChooseInflater.findViewById(R.id.twod_layout));
        threeDLayout = ((LinearLayout) mapChooseInflater.findViewById(R.id.threed_layout));
        twoDImage = ((ImageView) mapChooseInflater.findViewById(R.id.twod_image));
        threeDImage = ((ImageView) mapChooseInflater.findViewById(R.id.threed_image));
        twoDText = ((TextView) mapChooseInflater.findViewById(R.id.twod_text));
        threeDText =  ((TextView) mapChooseInflater.findViewById(R.id.threed_tezt));
        mapChooseDialog.setContentView(mapChooseInflater);
        Window dialogWindow = mapChooseDialog.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wmMap = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wmMap.getDefaultDisplay().getWidth();
        int height1 = wmMap.getDefaultDisplay().getHeight();
        lp.width = (int) (width*0.65);
        lp.height = (int) height1;
        dialogWindow.setAttributes(lp);
        mapChooseDialog.setCanceledOnTouchOutside(true);
        /**
         * 火警信息 dialog
         */
        fireInfoListDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        fireInfoListInflater = LayoutInflater.from(this).inflate(R.layout.dialog_fireinfo_list,null);
        fireInfoListInflater.setMinimumWidth(10000);
        refreshLayout = ((SmartRefreshLayout) fireInfoListInflater.findViewById(R.id.refreshLayout));
        fireCountText = ((TextView) fireInfoListInflater.findViewById(R.id.fire_count_textview));
        listView = ((RecyclerView) fireInfoListInflater.findViewById(R.id.fire_info_listview));
        fenleiLayout = ((LinearLayout) fireInfoListInflater.findViewById(R.id.fenlei_layout));
        fenleiView = ((TextView) fireInfoListInflater.findViewById(R.id.fenlei_View));
        fireInfoListDialog.setContentView(fireInfoListInflater);
        Window fireListWindow = fireInfoListDialog.getWindow();
        fireListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams fireListLp = fireListWindow.getAttributes();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        fireListLp.height = (int) (height * 0.8);
        fireListWindow.setAttributes(fireListLp);
        fireInfoListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                //bingo did
                Setting setting = new DbConfig(getApplicationContext()).getSetting();
                dWebView.callHandler("huodian", new Object[]{currentFireFindTime,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        searchDialog.hide();
                        isJush = false;
                        isopenFireDialog = true;
                        getFireFromService(currentFireFindTime);

                    }
                });
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000);
                page++;
                isLoadMore = true;
                if(isGaoji){
                    findFirePost();
                }else{
                    Setting setting = new DbConfig(getApplicationContext()).getSetting();
                    dWebView.callHandler("huodian_more", new Object[]{currentFireFindTime,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),page,setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                        @Override
                        public void onValue(String retValue) {
                            searchDialog.hide();
                            isJush = false;
                            isopenFireDialog = true;
                            getFireFromService(currentFireFindTime);

                        }
                    });
                }
            }
        });
        RxViewAction.clickNoDouble(fenleiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog();
                    }
                });

        /**
         * 查询信息 dialog
         */
        searchDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        searchInflater = LayoutInflater.from(this).inflate(R.layout.dialog_search,null);
        searchInflater.setMinimumWidth(10000);
        oneHoursLayout = ((LinearLayout) searchInflater.findViewById(R.id.one_hours));
        currntTimeLayout = ((LinearLayout) searchInflater.findViewById(R.id.current_time));
        threeHoursLayout = ((LinearLayout) searchInflater.findViewById(R.id.three_hours));
        oneDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.one_day));
        threeDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.three_day));
        fiveDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.five_day));
        gaojiSearchLayout = ((LinearLayout) searchInflater.findViewById(R.id.gaoji_search));
        searchDialog.setContentView(searchInflater);
        Window searchDialogWindow = searchDialog.getWindow();
        searchDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpSearch = searchDialogWindow.getAttributes();
        searchDialogWindow.setAttributes(lpSearch);
        searchDialog.setCanceledOnTouchOutside(true);

        /**
         * 高级查询
         */
        gaojiDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        gaojiInflater = LayoutInflater.from(this).inflate(R.layout.dialog_gaoji,null);
        gaojiInflater.setMinimumWidth(10000);
        gaojiDialog.setContentView(gaojiInflater);
/*        Window gaojiDialogWindow = gaojiDialog.getWindow();
        gaojiDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpGaoji = gaojiDialogWindow.getAttributes();
        gaojiDialogWindow.setAttributes(lpGaoji);
        gaojiDialog.setCanceledOnTouchOutside(true);*/
        Window gaojiDialogWindow = gaojiDialog.getWindow();
        gaojiDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams gaojiLp = gaojiDialogWindow.getAttributes();

        WindowManager wmGaoji = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int heightGaoji = wmGaoji.getDefaultDisplay().getHeight();
        gaojiLp.height = (int) (heightGaoji * 0.8);
        gaojiDialogWindow.setAttributes(gaojiLp);
        gaojiDialog.setCanceledOnTouchOutside(true);

        initGaojiView();

    /*    String str = "包含缓冲区（含权限外10公里范围数据，会延长查询时间）";
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);
        //设置字体大小
        RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan((float) 0.7);
        spannableBuilder.setSpan(sizeSpan1, 5, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 单独设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.c5));
        spannableBuilder.setSpan(colorSpan, 5, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        huanchongText.setText(spannableBuilder);
*/

        /**
         * 火点信息dialog
         */
        fireDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        fireInflater = LayoutInflater.from(this).inflate(R.layout.dialog_fire,null);
        fireInflater.setMinimumWidth(10000);
        fireAddressText = ((TextView) fireInflater.findViewById(R.id.address_text));
        fireTimeText = ((TextView) fireInflater.findViewById(R.id.time_text));
        jingWeiText = ((TextView) fireInflater.findViewById(R.id.jing_wei_text));
        kexinText = ((TextView) fireInflater.findViewById(R.id.kexin_text));
        mianjiText = ((TextView) fireInflater.findViewById(R.id.mianji_text));
        cishuText = ((TextView) fireInflater.findViewById(R.id.cishu_text));
        leixingText = ((TextView) fireInflater.findViewById(R.id.leixing_text));
        shujuyuanText = ((TextView) fireInflater.findViewById(R.id.shujuyuan_text));
        huodianCodeText = ((TextView) fireInflater.findViewById(R.id.huodian_code_text));
        fankuiText = ((TextView) fireInflater.findViewById(R.id.fankui_text));
        huodianOneImage = ((ImageView) fireInflater.findViewById(R.id.huodian_image_one));
        huodianTwoImage = ((ImageView) fireInflater.findViewById(R.id.huodian_image_two));
        RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.e(TAG, "call: bingo " );
            }
        });
        xiangyuanmianjiView = ((TextView) fireInflater.findViewById(R.id.xiangyuanmianji_view));
        xiangyuanshuView = ((TextView) fireInflater.findViewById(R.id.xiangyuanshu_view));
        fireDialog.setContentView(fireInflater);
        Window fireDialogWindow = fireDialog.getWindow();
        fireDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpFire = fireDialogWindow.getAttributes();
        fireDialogWindow.setAttributes(lpFire);
        fireDialog.setCanceledOnTouchOutside(true);


        /**
         * 地面火警报警
         */
        groundFireListDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        groundFireListInflater = LayoutInflater.from(this).inflate(R.layout.dialog_ground_fire_list,null);
        groundFireListInflater.setMinimumWidth(10000);
        fireListView = ((RecyclerView) groundFireListInflater.findViewById(R.id.ground_fire_listview));
        groundFireListDialog.setContentView(groundFireListInflater);
        Window groundFireListWindow = groundFireListDialog.getWindow();
        groundFireListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams groundFireListLp = groundFireListWindow.getAttributes();

        groundFireListLp.height = (int) (height * 0.8);
        groundFireListWindow.setAttributes(groundFireListLp);
        groundFireListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        fireListView.setLayoutManager(linearLayoutManager1);
        groundAdapter = new MultiTypeAdapter(groundItems);
        GroundFireViewBinder groundFireViewBinder = new GroundFireViewBinder();
        groundFireViewBinder.setListener(this);
        groundFireViewBinder.setContext(this);
        groundAdapter.register(GroundFire.class, groundFireViewBinder);
        fireListView.setAdapter(groundAdapter);
        assertHasTheSameAdapter(fireListView, groundAdapter);

        /**
         * 地面火警详细信息
         */
        groundFireInfoDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        groundFireInfoInflater = LayoutInflater.from(this).inflate(R.layout.dialog_ground_fire_info,null);
        groundFireInfoInflater.setMinimumWidth(10000);
        groundAddressView = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_address_text));
        groundLngView = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_lng_view));
        groundLatView = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_lat_view));
        groundDataView = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_data_view));
        groundOneImageView = ((ImageView) groundFireInfoInflater.findViewById(R.id.ground_one_image));
        groundTwoImageView = ((ImageView) groundFireInfoInflater.findViewById(R.id.ground_two_image));
        groundQuerenButton = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_queren_button));
        groundHulueButton = ((TextView) groundFireInfoInflater.findViewById(R.id.ground_hulue_button));
        groundFireInfoDialog.setContentView(groundFireInfoInflater);
        Window groundFireInfoDialogWindow = groundFireInfoDialog.getWindow();
        groundFireInfoDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpGroundFire = groundFireInfoDialogWindow.getAttributes();
        groundFireInfoDialogWindow.setAttributes(lpGroundFire);
        groundFireInfoDialog.setCanceledOnTouchOutside(true);
        /**
         * 地面火警电话选择
         */
        phoneChooseDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        phoneChooseInflate = LayoutInflater.from(this).inflate(R.layout.dialog_phone_choose,null);
        phoneChooseInflate.setMinimumWidth(10000);
        phoneChooseRg = ((RadioGroup) phoneChooseInflate.findViewById(R.id.rg_phone));
        phoneRb1 = ((RadioButton) phoneChooseInflate.findViewById(R.id.rb_phone1));
        phoneRb2 = ((RadioButton) phoneChooseInflate.findViewById(R.id.rb_phone2));
        phoneRb3 = ((RadioButton) phoneChooseInflate.findViewById(R.id.rb_phone3));
        phoneRb4 = ((RadioButton) phoneChooseInflate.findViewById(R.id.rb_phone4));
        phoneChooseButton = ((TextView) phoneChooseInflate.findViewById(R.id.phone_choose_button));
        phoneChooseDialog.setContentView(phoneChooseInflate);
        Window phoneChooseDialogWindow = phoneChooseDialog.getWindow();
        phoneChooseDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpPhoneChoose = phoneChooseDialogWindow.getAttributes();
        phoneChooseDialogWindow.setAttributes(lpPhoneChoose);
        phoneChooseDialog.setCanceledOnTouchOutside(true);
        phoneChooseRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_phone1:
                        currentPhone = "15650161960";
                        break;
                    case R.id.rb_phone2:
                        currentPhone = "15192698918";
                        break;
                    case R.id.rb_phone3:
                        currentPhone = "13869866569";
                        break;
                    case R.id.rb_phone4:
                        currentPhone = "19853208820";
                        break;
                }

            }
        });

        RxViewAction.clickNoDouble(phoneChooseButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postGroundFireIntoService(true);
                    }
                });

        /**
         * 地面火警确认
         */
        RxViewAction.clickNoDouble(groundQuerenButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        groundFireInfoDialog.dismiss();
                        phoneChooseDialog.show();
                    }
                });
        /**
         * 地面火警忽略
         */
        RxViewAction.clickNoDouble(groundHulueButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postGroundFireIntoService(false);
                    }
                });

        /**
         * 请求超时的点击
         */
        chaoshiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chaoshiButton.setVisibility(View.GONE);
                User user = new DbConfig(getApplicationContext()).getUser();
                Setting setting = new DbConfig(getApplicationContext()).getSetting();

                dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                        setting.getWeixing(),setting.getTiankong(),setting.getDimao(),setting.getDimao(),setting.getNumber(),
                        setting.getJingwai(),setting.getHuanchong()}, new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                       // dWebView.setVisibility(View.VISIBLE);
                     //   dWebView.setVisibility(View.VISIBLE);
                        isJush = false;
                        getFireFromService(3);
                        Log.e(TAG, "jpush: ---------------------------------------------6" );
                        Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                    }
                });

              /*  dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),3}, new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {

                        dWebView.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onValue: ---------------------------------------------" );
                        Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        //23D切换
        RxViewAction.clickNoDouble(twoDLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentDime.equals("3D")){
                            currentDime = "2D";
                            twoDText.setTextColor(getResources().getColor(R.color.map));
                            threeDText.setTextColor(getResources().getColor(R.color.c7));
                            twoDImage.setImageResource(R.drawable.ic_2d_select);
                            threeDImage.setImageResource(R.drawable.ic_3d);
                            mapChooseDialog.hide();
                            dWebView.callHandler("dimensional_change", new OnReturnValue<String>() {
                                @Override
                                public void onValue(String retValue) {
                                    Log.e(TAG, "onValue:  fanhiu1" + retValue);
                                    //  mHandler.sendEmptyMessage(GUGE_GAOCHENG);
                                }
                            });
                        }
                    }
               });
        RxViewAction.clickNoDouble(threeDLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentDime.equals("2D")){
                            currentDime = "3D";
                            twoDText.setTextColor(getResources().getColor(R.color.c7));
                            threeDText.setTextColor(getResources().getColor(R.color.map));
                            twoDImage.setImageResource(R.drawable.ic_2d);
                            threeDImage.setImageResource(R.drawable.ic_3d_select);
                            mapChooseDialog.hide();
                            dWebView.callHandler("dimensional_change", new OnReturnValue<String>() {
                                @Override
                                public void onValue(String retValue) {
                                    Log.e(TAG, "onValue:  fanhiu1" + retValue);
                                }
                            });
                        }
                    }
                });

        //新火警的点击
        RxViewAction.clickNoDouble(warnImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        fireInfoListDialog.show();
                        warnImage.setVisibility(View.GONE);
                    }
                });

        //反馈按钮的点击
        RxViewAction.clickNoDouble(fankuiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), FeedBackActivity.class);
                        Log.e(TAG, "call: fireinfo---" + currentFire.getId() );
                        Log.e(TAG, "call: fireinfo---" + currentFire.getLongitude() );
                        Log.e(TAG, "call: fireinfo---" + currentFire.getLatitude() );
                        intent.putExtra("ID",currentFireId);
                        intent.putExtra("LO",currentFireLo);
                        intent.putExtra("LA",currentFireLa);
                        startActivity(intent);
                    }
                });

        //当前时间点击  1
        RxViewAction.clickNoDouble(currntTimeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 1;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{1,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示当前时间的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(1);

                            }
                        });
                       /* dWebView.callHandler("huodian_jpush", new Object[]{1},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示当前时间的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(1);
                            }
                        });*/
                    }
                });

        //一小时内点击 1
        RxViewAction.clickNoDouble(oneHoursLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 1;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{1,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {

                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示1小时之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(1);


                            }
                        });
                        /*dWebView.callHandler("huodian_jpush", new Object[]{1},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示1小时之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(1);
                            }
                        });*/
                    }
                });

        //三小时内点击  3
        RxViewAction.clickNoDouble(threeHoursLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 3;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{3,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示3小时之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(3);
                            }
                        });
                       /* dWebView.callHandler("huodian_jpush", new Object[]{3},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示3小时之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(3);
                            }
                        });*/
                    }
                });

        //一天内点击  24
        RxViewAction.clickNoDouble(oneDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 24;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{24,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示1天之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(24);
                            }
                        });
                       /* dWebView.callHandler("huodian_jpush", new Object[]{24},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示1天之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(24);
                            }
                        });*/
                    }
                });

        //三天内点击  72
        RxViewAction.clickNoDouble(threeDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 72;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{72,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示3天之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(72);
                            }
                        });
                     /*   dWebView.callHandler("huodian_jpush", new Object[]{72},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示3天之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(72);
                            }
                        });*/
                    }
                });

        //五天内点击 120
        RxViewAction.clickNoDouble(fiveDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireFindTime = 120;
                        showDialogProgress(gaojiFindDialog,"查询中...");
                        Setting setting = new DbConfig(getApplicationContext()).getSetting();
                        dWebView.callHandler("huodian", new Object[]{120,setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),setting.getJingwai(),setting.getHuanchong().equals("0")?false:true},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                searchDialog.hide();
                                isJush = false;
                                Toast.makeText(MainActivity.this, "已显示5天之内的火点信息", Toast.LENGTH_SHORT).show();
                                getFireFromService(120);
                                fireInfoListDialog.show();
                            }
                        });
                       /* dWebView.callHandler("huodian_jpush", new Object[]{120},new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {x
                                searchDialog.hide();
                                Toast.makeText(MainActivity.this, "已显示5天之内的火点信息", Toast.LENGTH_SHORT).show();
                                isopenFireDialog = true;
                                getFireFromService(120);
                            }
                        });*/
                    }
                });

        //高级点击
        RxViewAction.clickNoDouble(gaojiSearchLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        searchDialog.hide();
                        gaojiDialog.show();

                    }
                });

        //弹出查询dialog
        RxViewAction.clickNoDouble(searchButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        searchDialog.show();
                    }
                });

        //切换谷歌高程地图
        RxViewAction.clickNoDouble(gugeGaochengMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc_select);
                        gugeGaochengText.setTextColor(getResources().getColor(R.color.map));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        gaofenImage.setImageResource(R.drawable.ic_gfmap);
                        gaofenText.setTextColor(getResources().getColor(R.color.c7));

                        dWebView.callHandler("google_gaocheng", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu1" + retValue);
                                mHandler.sendEmptyMessage(GUGE_GAOCHENG);
                            }
                        });
                    }
                });
        //谷歌影像地图
        RxViewAction.clickNoDouble(gugeYingxiangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
                        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl_select);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.map));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        gaofenImage.setImageResource(R.drawable.ic_gfmap);
                        gaofenText.setTextColor(getResources().getColor(R.color.c7));
                        gugeYingxiang();

                    }
                });
        //天地图矢量地图
        RxViewAction.clickNoDouble(tiandiShiliangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dWebView.callHandler("tianditu_shiliang", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
                                gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                                gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                                gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                                tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl_select);
                                tiandiShiliangText.setTextColor(getResources().getColor(R.color.map));

                                tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                                tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                                gaofenImage.setImageResource(R.drawable.ic_gfmap);
                                gaofenText.setTextColor(getResources().getColor(R.color.c7));
                                Log.e(TAG, "onValue:  fanhiu3" + retValue);
                                mHandler.sendEmptyMessage(TIANDI_SHILIANG);
                            }
                        });
                    }
                });
        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx_select);
        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.map));

        //天地图影像地图
        RxViewAction.clickNoDouble(tiandiYingxiangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
                        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx_select);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.map));

                        gaofenImage.setImageResource(R.drawable.ic_gfmap);
                        gaofenText.setTextColor(getResources().getColor(R.color.c7));
                        dWebView.callHandler("tianditu_yingxiang", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu4" + retValue);
                                mHandler.sendEmptyMessage(TIANDI_YINGXIANG);
                            }
                        });
                    }
                });

        //高分地图
        RxViewAction.clickNoDouble(gaofenMapLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
                        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        gaofenImage.setImageResource(R.drawable.ic_gfmap_selected);
                        gaofenText.setTextColor(getResources().getColor(R.color.map));


                        dWebView.callHandler("map_gaofen", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu4" + retValue);

                                mHandler.sendEmptyMessage(MAP_GAOFEN);
                            }
                        });
                    }
                });

        //地图选择按钮弹出dialog
        RxViewAction.clickNoDouble(mapChooseButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mapChooseDialog.show();
                    }
                });

        //地图选择按钮弹出dialog
        RxViewAction.clickNoDouble(tucengButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mapChooseDialog.show();
                    }
                });



        //地图2d3d切换
        RxViewAction.clickNoDouble(mapDimensionChoos)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                 /*       MediaPlayer player = new MediaPlayer();

                        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.fire);

                        try {
                            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                            file.close();
                            if (!player.isPlaying()){

                                player.prepare();

                                player.start();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                       */




                       // try {
                            MediaPlayer mMediaPlayer;
                            mMediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.find_fire);
//                            mMediaPlayer.prepare();
                            mMediaPlayer.start();
                      /*  } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                       /* dWebView.callHandler("ditu_dimension", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  2D3D  " + retValue);
                               // mHandler.sendEmptyMessage(TIANDI_YINGXIANG);
                                if (isTwoD){
                                    Toast.makeText(MainActivity.this, "地图已切换为3D模式", Toast.LENGTH_SHORT).show();
                                    isTwoD = false;
                                }else {
                                    Toast.makeText(MainActivity.this, "地图已切换为2D模式", Toast.LENGTH_SHORT).show();
                                    isTwoD = true;
                                }

                            }
                        });*/
                    }
                });

        //火警列表dialog弹出
        RxViewAction.clickNoDouble(huojingButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        fireInfoListDialog.show();
                    }
                });

        //退出登录
        RxViewAction.clickNoDouble(outLoginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    }
                });


    }

    private void showLeibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"按时间分类", "按编号分类"};

       if (currentFireListType == 1){
           choose1 = 0;
       }else {
           choose1 = 1;
       }
        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("单选列表")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i );
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (choose1 == 0){ //按时间分类
                            currentFireListType = 1;
                            fenleiView.setText("按时间分类");
                        }else {  //按编号分类
                            currentFireListType = 2;
                            fenleiView.setText("按编号分类");
                        }
                        initFireData();
                    }
                });
        builder.create().show();
    }


    private void requestNotofication() {
        if(!NotificationsUtils.isNotificationEnabled(this)){
            NotificationsUtils.requestNotify(this);
        }
    }

    /**
     * 地面火警状态提交
     */
    private void postGroundFireIntoService(final boolean isOk) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AlarmID",currentGroundFireID);
            jsonObject.put("IsDispose",1);

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams("http://49.232.128.132:10171/api/FireAlarm/ConfirmFireAlarmInfo");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        if (isOk){
            params.addParameter("mobiles" , currentPhone);
        }

        Log.e(TAG, "grounddata:-- " + params);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "grounddataonSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String state = jsonObject1.getString("result");
                    if (state.equals("1")){
                        if (isOk){
                            Toast.makeText(MainActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                            phoneChooseDialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, "火警已忽略", Toast.LENGTH_SHORT).show();
                            groundFireInfoDialog.dismiss();
                        }
                       
                    }else {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
                getGroundFireDataFromService();
            }
        });

    }

    /**
     * 日期选择控件
     */
    private void showDataDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                if (endDate.length() > 0) { //清除上次记录的日期
                    endDate.delete(0, endDate.length());
                }
                if (isChooseStarTime){
                    gaojiStartimeText.setText(date.append(String.valueOf(year)).append("/").append(String.valueOf(month + 1)).append("/").append(day));
                }else {
                    gaojiEndTimeText.setText(date.append(String.valueOf(year)).append("/").append(String.valueOf(month + 1)).append("/").append(day));
                }
               dialog.dismiss();
                showTimeDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final android.support.v7.app.AlertDialog dialog = builder.create();
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
     * 日期选择控件
     */
    private void showTimeDialog() {
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);
        builder1.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isChooseStarTime){
                    gaojiStartimeText.append("  " + parseNumber(chooseHour) + ":" + parseNumber(chooseMinute));
                }else {
                    gaojiEndTimeText.append("  " + parseNumber(chooseHour) + ":" + parseNumber(chooseMinute));
                }
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final android.support.v7.app.AlertDialog timeDialog = builder1.create();
        View dialogView = View.inflate(this, R.layout.dialog_time, null);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timepicker);
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
      /*  String endData = year1 - 10 + "-" + month1 + "-" + day1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endData);
        } catch (ParseException e) {

        }
        long starTimre = date2.getTime();


        long endTimre = System.currentTimeMillis();*/

        timePicker.setIs24HourView(true);   //设置时间显示为24小时

        timePicker.setHour(hour);  //设置当前小时
        timePicker.setMinute(minute); //设置当前分（0-59）

        timeDialog.setTitle("设置时间");
        timeDialog.setView(dialogView);
        timeDialog.show();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                chooseHour = hourOfDay;
                chooseMinute = minute;
            }
        });
        //初始化日期监听事件
     //   timePicker.init(year, month, day, this);
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        chooseHour = calendar.get(Calendar.HOUR_OF_DAY);
        chooseMinute = calendar.get(Calendar.MINUTE);

    }

    private String parseNumber(int number){
        if(number > 9){
            return ""+number;
        }else{
            return "0"+number;
        }
    }


    /**
     * 高级设置的初始化
     */
    private void initGaojiView() {
        //缓冲跟境外
        jingwaiText = ((TextView) gaojiInflater.findViewById(R.id.jingwai_text));
        huanchongText = ((TextView) gaojiInflater.findViewById(R.id.huanchong_text));

        //重置  查询
        chongzhiButton = ((TextView) gaojiInflater.findViewById(R.id.chongzhi_button));
        findButton = ((TextView) gaojiInflater.findViewById(R.id.find_button));

        //省市区
        shiLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.gao_shi_layout));
        shengLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.sheng_layout));
        quLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.qu_layout));
        shengText = ((TextView) gaojiInflater.findViewById(R.id.sheng_text));
        shiText = ((TextView) gaojiInflater.findViewById(R.id.shi_text));
        quText = ((TextView) gaojiInflater.findViewById(R.id.qu_text));
        quyuLayout = (LinearLayout) gaojiInflater.findViewById(R.id.quyu_layout);

        //时间
        gaojiStarTimeLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.gaoji_startime_layout));
        gaojiEndTimeLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.gaoji_endtime_layout));
        gaojiStartimeText = ((TextView) gaojiInflater.findViewById(R.id.gaoji_startime_text));
        gaojiEndTimeText = ((TextView) gaojiInflater.findViewById(R.id.gaoji_endtime_text));
        //卫星监测
        weixingAllLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_all_layout));
       // weixingAllImage = ((ImageView) gaojiInflater.findViewById(R.id.weixing_all_image));
        weixingAllText = (TextView) gaojiInflater.findViewById(R.id.weixing_all_text);
        weixingFY3Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_fy3_layout));
    //    weixingFY3Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_fy3_image));
        weixingFY3Text = ((TextView) gaojiDialog.findViewById(R.id.weixing_fy3_text));
        weixingFY4Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_fy4_layout));
    //    weixingFY4Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_fy4_image));
        weixingFY4Text = ((TextView) gaojiInflater.findViewById(R.id.weixing_fy4_text));
        weixingNPPLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_npp_layout));
     //   weixingNPPImage = ((ImageView) gaojiInflater.findViewById(R.id.weixing_npp_image));
        weixingNppText = ((TextView) gaojiInflater.findViewById(R.id.weixing_npp_text));
        weixingHima8Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_himawar8_layout));
     //   weixingHima8Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_himawar8_image));
        weixingHima8Text = ((TextView) gaojiInflater.findViewById(R.id.weixing_himawar8_text));
        weixingNOAA19Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa19_layout));
        weixingNOAA20Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa20_layout));
        weixingGK2aLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_gk2a_layout));
     //   weixingNOAA19Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_noaa19_image));
        weixingNOAA19Te = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa19_text));
        weixingNOAA20Te = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa20_text));
        weixingGK2aTe = ((TextView) gaojiInflater.findViewById(R.id.weixing_gk2a_text));

        //天空监测
        tiankongAllLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.tiankong_all_layout));
      //  tiankongAllImage = ((ImageView) gaojiInflater.findViewById(R.id.tiankong_all_image));
        tiankongAllText = ((TextView) gaojiInflater.findViewById(R.id.tiankong_all_text));
        tiankongWurenjiLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.tiankong_wurenji_layout));
     //   tiankongWurenjiImage = ((ImageView) gaojiInflater.findViewById(R.id.tiankong_wurenji_image));
        tiankongWurenjiText = ((TextView) gaojiInflater.findViewById(R.id.tiankong_wurenji_text));
        tiankongXuanfuqiLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.tiankong_xuanfuqi_layout));
        tiankongXuanfuqiText = ((TextView) gaojiInflater.findViewById(R.id.tiankong_xuanfuqi_text));
        // tiankongXuanfuqiImage = ((ImageView) gaojiInflater.findViewById(R.id.tiankong_xuanfuqi_image));

        //地面监测
        dimianAllLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimn_all_layout));
        dimianAllText = ((TextView) gaojiInflater.findViewById(R.id.dimian_all_text));
        //   dimianAllImage = ((ImageView) gaojiInflater.findViewById(R.id.dimian_all_image));
        dimianSheyingLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimian_sheyingji_layout));
        dimianSheyingText = ((TextView) gaojiInflater.findViewById(R.id.dimian_sheyingji_text));
        //   dimianSheyingImage = ((ImageView) gaojiInflater.findViewById(R.id.dimian_sheyingji_image));
        dimianHulinLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimian_hulin_layout));
        dimianHulinText = ((TextView) gaojiInflater.findViewById(R.id.dimian_hulin_text));
        //  dimianHulinImage = ((ImageView) gaojiInflater.findViewById(R.id.dimian_hulin_image));
        dimianLiaowangLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimian_liaowang_layout));
        dimianLiaowangText = ((TextView) gaojiInflater.findViewById(R.id.dimian_liaowang_text));
        //  dimianLiaowangImage = ((ImageView) gaojiInflater.findViewById(R.id.dimian_liaowang_image));
        dimianQunzhongLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimian_qunzhong_layout));
        dimianQunzhongText = ((TextView) gaojiInflater.findViewById(R.id.dimian_qunzhong_text));
        //  dimianQunzhongImage = ((ImageView) gaojiInflater.findViewById(R.id.dimian_qunzhong_image));

        //地貌监测
        dimaoAllLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimao_all_layout));
        dimaoAllText = ((TextView) gaojiInflater.findViewById(R.id.dimao_all_text));
        //  dimaoAllImage = ((ImageView) gaojiInflater.findViewById(R.id.dimao_all_image));
        dimaoLindiLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimao_lindi_layout));
        dimaoLindiText = ((TextView) gaojiInflater.findViewById(R.id.dimao_lindi_Text));
        //   dimaoLindiImage = ((ImageView) gaojiInflater.findViewById(R.id.dimao_lindi_image));
        dimaoCaodiLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimao_caodi_layout));
        dimaoCaodiText = ((TextView) gaojiInflater.findViewById(R.id.dimao_caodi_text));
        //   dimaoCaodiImage = ((ImageView) gaojiInflater.findViewById(R.id.dimao_caodi_image));
        dimaoNongtianLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimao_nongtian_layout));
        dimaoNongtianText = ((TextView) gaojiInflater.findViewById(R.id.dimao_nongtian_text));
        //  dimaoNongtianImage = ((ImageView) gaojiInflater.findViewById(R.id.dimao_nongtian_image));
        dimaoQitaLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.dimao_qita_layout));
        dimaoQitaText = ((TextView) gaojiInflater.findViewById(R.id.dimao_qita_text));
        //   dimaoQitaImage = ((ImageView) gaojiInflater.findViewById(R.id.dimao_qita_image));
        /**
         * 境外 缓冲区的点击
         */
        RxViewAction.clickNoDouble(jingwaiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isChooseJingwai){
                            isChooseJingwai = false;
                            jingwaiText.setBackgroundResource(R.drawable.bg_text_hui);
                            jingwaiText.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            isChooseJingwai = true;
                            jingwaiText.setBackgroundResource(R.drawable.bg_text_lan);
                            jingwaiText.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });
        RxViewAction.clickNoDouble(huanchongText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isChooseHuanchong){
                            isChooseHuanchong = false;
                            huanchongText.setBackgroundResource(R.drawable.bg_text_hui);
                            huanchongText.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            isChooseHuanchong = true;
                            huanchongText.setBackgroundResource(R.drawable.bg_text_lan);
                            huanchongText.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });



        /**
         * 按钮点击
         */
        RxViewAction.clickNoDouble(findButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        findFirePost();
                    }
                });
        RxViewAction.clickNoDouble(chongzhiButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //卫星初始化
/*                        weixingAllImage.setImageResource(R.drawable.choose);
                        weixingNPPImage.setImageResource(R.drawable.choose);
                        weixingFY4Image.setImageResource(R.drawable.choose);
                        weixingFY3Image.setImageResource(R.drawable.choose);
                        weixingHima8Image.setImageResource(R.drawable.choose);
                        weixingNOAA19Image.setImageResource(R.drawable.choose);*/
                        weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                        weixingNppText.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNppText.setTextColor(getResources().getColor(R.color.c12));
                        weixingFY4Text.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingFY4Text.setTextColor(getResources().getColor(R.color.c12));
                        weixingFY3Text.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingFY3Text.setTextColor(getResources().getColor(R.color.c12));
                        weixingHima8Text.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingHima8Text.setTextColor(getResources().getColor(R.color.c12));
                        weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                        weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                        weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));

                        weixingAllChoose = true;
                        weixingNPPChoose = true;
                        weixingFY3Choose = true;
                        weixingFY4Choose = true;
                        weixingHIMA8Choose = true;
                        weixingNOAA19Choose = true;
                        weixingNOAA20Choose = true;
                        weixingGK2aChoose = true;

                        //天空初始化
                       // tiankongAllImage.setImageResource(R.drawable.choose_no);
                      //  tiankongWurenjiImage.setImageResource(R.drawable.choose_no);
                       // tiankongXuanfuqiImage.setImageResource(R.drawable.choose_no);
                        tiankongAllText.setBackgroundResource(R.drawable.bg_text_hui);
                        tiankongAllText.setTextColor(getResources().getColor(R.color.c6));
                        tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_hui);
                        tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c6));
                        tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_hui);
                        tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c6));
                        tiankongAllChoose = false;
                        tiankongXuancifuChoose = false;
                        tiankongWurenjiChoose = false;
                        //地面初始化
                       // dimianAllImage.setImageResource(R.drawable.choose_no);
                     //   dimianSheyingImage.setImageResource(R.drawable.choose_no);
                      //  dimianHulinImage.setImageResource(R.drawable.choose_no);
                      //  dimianLiaowangImage.setImageResource(R.drawable.choose_no);
                     //   dimianQunzhongImage.setImageResource(R.drawable.choose_no);

                        dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                        dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                        dimianSheyingText.setBackgroundResource(R.drawable.bg_text_hui);
                        dimianSheyingText.setTextColor(getResources().getColor(R.color.c6));
                        dimianHulinText.setBackgroundResource(R.drawable.bg_text_hui);
                        dimianHulinText.setTextColor(getResources().getColor(R.color.c6));
                        dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_hui);
                        dimianLiaowangText.setTextColor(getResources().getColor(R.color.c6));
                        dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_hui);
                        dimianQunzhongText.setTextColor(getResources().getColor(R.color.c6));

                        dimianAllChoose = false;
                        dimianSheyingChoose = false;
                        dimianHulinChoose = false;
                        dimianLiaowangChoose = false;
                        dimianQunzhongChoose = false;

                        //时间初始化
                        gaojiStartimeText.setText("请输入开始时间");
                        gaojiEndTimeText.setText("请输入结束时间");

                        //地貌初始化
                     /*   dimaoAllImage.setImageResource(R.drawable.choose);
                        dimaoLindiImage.setImageResource(R.drawable.choose);
                        dimaoCaodiImage.setImageResource(R.drawable.choose);
                        dimaoNongtianImage.setImageResource(R.drawable.choose);
                        dimaoQitaImage.setImageResource(R.drawable.choose);
                        */
                        dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                        dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                        dimaoLindiText.setBackgroundResource(R.drawable.bg_text_lan);
                        dimaoLindiText.setTextColor(getResources().getColor(R.color.c12));
                        dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_lan);
                        dimaoCaodiText.setTextColor(getResources().getColor(R.color.c12));
                        dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_lan);
                        dimaoNongtianText.setTextColor(getResources().getColor(R.color.c12));
                        dimaoQitaText.setBackgroundResource(R.drawable.bg_text_lan);
                        dimaoQitaText.setTextColor(getResources().getColor(R.color.c12));

                        dimaoAllChoose = true;
                        dimaoCaodiChoose = true;
                        dimaoLindiChoose = true;
                        dimaoNongtianChoose = true;
                        dimaoQitaChoose = true;

                        //区域重置
                        currentChooseSheng = "";
                        currentChooseShi = "";
                        currentChooseQu = "";
                        shengSelectIndex = 0;
                        shiSelectIndex = 0;
                        quSelectIndex = 0;
                        isChooseSheng = false;
                        shengText.setText("请选择省");
                        shiText.setText("请选择市");
                        //初始话境外
                        isChooseJingwai = false;
                        isChooseHuanchong = false;

                        jingwaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        jingwaiText.setTextColor(getResources().getColor(R.color.c6));
                        huanchongText.setBackgroundResource(R.drawable.bg_text_hui);
                        huanchongText.setTextColor(getResources().getColor(R.color.c6));

                        //   jingwaiImage.setImageResource(R.drawable.choose_no);
                      //  huanChongImage.setImageResource(R.drawable.choose_no);
                    }
                });



        provinceNo = new DbConfig(getApplicationContext()).getUser().getProvinceNo();
        cityNo = new DbConfig(getApplicationContext()).getUser().getCityNo();
        if (provinceNo.equals("null")){  //全国权限  0
            currentQuanxian = 0;
            //   getAllAre();
        } else {
            if (!cityNo.equals("null")){     //市权限  2
                currentQuanxian = 2;
            }else {         //省权限1
                currentQuanxian = 1;
            }
        }

        //全国省市账号权限默认值匹配
        if(currentQuanxian == 0){
            //全国账号
        }
        if(currentQuanxian == 1){
            //省账号
            shengText.setText(new DbConfig(getApplicationContext()).getUser().getProvinceName());
            shengText.setTextColor(getResources().getColor(R.color.c3));
        }
        if(currentQuanxian == 2){
            //市账号
            shengText.setText(new DbConfig(getApplicationContext()).getUser().getProvinceName());
            shiText.setText(new DbConfig(getApplicationContext()).getUser().getCityName());
            shiText.setTextColor(getResources().getColor(R.color.c3));
        }


        /**
         * 省市的点击
         */
        RxViewAction.clickNoDouble(shengText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(currentQuanxian ==0){
                            currentChooseArea = 0;
                            getAllAre();
                        }
                    }
                });
        RxViewAction.clickNoDouble(shiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(currentQuanxian ==0 || currentQuanxian == 1){
                        Log.e(TAG, "call: shi");
                        currentChooseArea = 1;
                        String id = "";
                        if (currentQuanxian == 0){
                            if (shengText.getText().toString().equals("请选择省")){
                                Toast.makeText(MainActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
                            }else {
                                for (int i = 0; i < shengList.size(); i++) {
                                    if (shengList.get(i).getName().equals(currentChooseSheng)) {
                                        id = shengList.get(i).getId();
                                    }

                                }
                                Log.e(TAG, "call: +  id ==" +id);
                                getShengAre(id);
                            }
                        }else {
                            getShengAre(new DbConfig(getApplicationContext()).getUser().getProvinceNo());
                        }
                        }

                    }
                });
        RxViewAction.clickNoDouble(quText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: qu");
                        currentChooseArea = 2;
                        String id = "";
                        if (currentQuanxian == 0 || currentQuanxian == 1){
                            Log.e(TAG, "call: +  id ==" +shiText.getText().toString());
                            if (shiText.getText().toString().equals("请选择市")){
                                Toast.makeText(MainActivity.this, "请先选择市", Toast.LENGTH_SHORT).show();
                            }else {
                                for (int i = 0; i < shiList.size(); i++) {
                                    if (shiList.get(i).getName().equals(currentChooseShi)) {
                                        id = shiList.get(i).getId();
                                    }

                                }
                                Log.e(TAG, "call: +  id ==" +id);
                                getShiAre(id);
                            }
                        }else {
                            getShiAre(new DbConfig(getApplicationContext()).getUser().getCityNo());
                        }

                    }
                });


        /**
         * 时间点击
         */
        RxViewAction.clickNoDouble(gaojiStartimeText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isChooseStarTime = true;
                        showDataDialog();
                    }
                });

        RxViewAction.clickNoDouble(gaojiEndTimeText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isChooseStarTime = false;
                        showDataDialog();
                    }
                });

        /**
         * 地貌监测点击
         */
        RxViewAction.clickNoDouble(dimaoAllLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimaoAllChoose){
                            dimaoAllChoose = false;
                            dimaoLindiChoose = false;
                            dimaoCaodiChoose = false;
                            dimaoNongtianChoose = false;
                            dimaoQitaChoose = false;
                        /*    dimaoAllImage.setImageResource(R.drawable.choose_no);
                            dimaoLindiImage.setImageResource(R.drawable.choose_no);
                            dimaoCaodiImage.setImageResource(R.drawable.choose_no);
                            dimaoNongtianImage.setImageResource(R.drawable.choose_no);
                            dimaoQitaImage.setImageResource(R.drawable.choose_no);*/
                            dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
                            dimaoLindiText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoLindiText.setTextColor(getResources().getColor(R.color.c6));
                            dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoCaodiText.setTextColor(getResources().getColor(R.color.c6));
                            dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoNongtianText.setTextColor(getResources().getColor(R.color.c6));
                            dimaoQitaText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoQitaText.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            dimaoAllChoose = true;
                            dimaoLindiChoose = true;
                            dimaoCaodiChoose = true;
                            dimaoNongtianChoose = true;
                            dimaoQitaChoose = true;
                           /* dimaoAllImage.setImageResource(R.drawable.choose);
                            dimaoLindiImage.setImageResource(R.drawable.choose);
                            dimaoCaodiImage.setImageResource(R.drawable.choose);
                            dimaoNongtianImage.setImageResource(R.drawable.choose);
                            dimaoQitaImage.setImageResource(R.drawable.choose);*/
                            dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                            dimaoLindiText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoLindiText.setTextColor(getResources().getColor(R.color.c12));
                            dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoCaodiText.setTextColor(getResources().getColor(R.color.c12));
                            dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoNongtianText.setTextColor(getResources().getColor(R.color.c12));
                            dimaoQitaText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoQitaText.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });

        RxViewAction.clickNoDouble(dimaoLindiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimaoLindiChoose){  //从已选中变为未选中
                            dimaoLindiChoose = false;
                          //  dimaoLindiImage.setImageResource(R.drawable.choose_no);
                            dimaoLindiText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoLindiText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimaoLindiChoose || !dimaoCaodiChoose || !dimaoQitaChoose || !dimaoNongtianChoose){   //判断全部未选中
                                dimaoAllChoose = false;
                              //  dimaoAllImage.setImageResource(R.drawable.choose_no);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimaoLindiChoose = true;
                      //      dimaoLindiImage.setImageResource(R.drawable.choose);
                            dimaoLindiText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoLindiText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimaoLindiChoose && dimaoCaodiChoose && dimaoQitaChoose && dimaoNongtianChoose ){
                                dimaoAllChoose = true;
                            //    dimaoAllImage.setImageResource(R.drawable.choose);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


        RxViewAction.clickNoDouble(dimaoCaodiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimaoCaodiChoose){  //从已选中变为未选中
                            dimaoCaodiChoose = false;
                         //   dimaoCaodiImage.setImageResource(R.drawable.choose_no);
                            dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoCaodiText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimaoLindiChoose || !dimaoCaodiChoose || !dimaoQitaChoose || !dimaoNongtianChoose){   //判断全部未选中
                                dimaoAllChoose = false;
                             //   dimaoAllImage.setImageResource(R.drawable.choose_no);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimaoCaodiChoose = true;
                       //     dimaoCaodiImage.setImageResource(R.drawable.choose);
                            dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoCaodiText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimaoLindiChoose && dimaoCaodiChoose && dimaoQitaChoose && dimaoNongtianChoose ){
                                dimaoAllChoose = true;
                      //          dimaoAllImage.setImageResource(R.drawable.choose);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


        RxViewAction.clickNoDouble(dimaoNongtianLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimaoNongtianChoose){  //从已选中变为未选中
                            dimaoNongtianChoose = false;
                          //  dimaoNongtianImage.setImageResource(R.drawable.choose_no);
                            dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoNongtianText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimaoLindiChoose || !dimaoCaodiChoose || !dimaoQitaChoose || !dimaoNongtianChoose){   //判断全部未选中
                                dimaoAllChoose = false;
                              //  dimaoAllImage.setImageResource(R.drawable.choose_no);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimaoNongtianChoose = true;
                            //dimaoNongtianImage.setImageResource(R.drawable.choose);
                            dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoNongtianText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimaoLindiChoose && dimaoCaodiChoose && dimaoQitaChoose && dimaoNongtianChoose ){
                                dimaoAllChoose = true;
                            //    dimaoAllImage.setImageResource(R.drawable.choose);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


        RxViewAction.clickNoDouble(dimaoQitaLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimaoQitaChoose){  //从已选中变为未选中
                            dimaoQitaChoose = false;
                          //  dimaoQitaImage.setImageResource(R.drawable.choose_no);
                            dimaoQitaText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimaoQitaText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimaoLindiChoose || !dimaoCaodiChoose || !dimaoQitaChoose || !dimaoNongtianChoose){   //判断全部未选中
                                dimaoAllChoose = false;
                               // dimaoAllImage.setImageResource(R.drawable.choose_no);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimaoQitaChoose = true;
                    //        dimaoQitaImage.setImageResource(R.drawable.choose);
                            dimaoQitaText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimaoQitaText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimaoLindiChoose && dimaoCaodiChoose && dimaoQitaChoose && dimaoNongtianChoose ){
                                dimaoAllChoose = true;
                              //  dimaoAllImage.setImageResource(R.drawable.choose);
                                dimaoAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimaoAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


        /**
         * 地面监测点击
         */
        RxViewAction.clickNoDouble(dimianAllLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimianAllChoose){
                            dimianAllChoose = false;
                            dimianSheyingChoose = false;
                            dimianHulinChoose = false;
                            dimianLiaowangChoose = false;
                            dimianQunzhongChoose = false;
                          /*  dimianAllImage.setImageResource(R.drawable.choose_no);
                            dimianSheyingImage.setImageResource(R.drawable.choose_no);
                            dimianHulinImage.setImageResource(R.drawable.choose_no);
                            dimianLiaowangImage.setImageResource(R.drawable.choose_no);
                            dimianQunzhongImage.setImageResource(R.drawable.choose_no);*/

                            dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                            dimianSheyingText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianSheyingText.setTextColor(getResources().getColor(R.color.c6));
                            dimianHulinText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianHulinText.setTextColor(getResources().getColor(R.color.c6));
                            dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianLiaowangText.setTextColor(getResources().getColor(R.color.c6));
                            dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianQunzhongText.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            dimianAllChoose = true;
                            dimianSheyingChoose = true;
                            dimianHulinChoose = true;
                            dimianLiaowangChoose = true;
                            dimianQunzhongChoose = true;
                          /*  dimianAllImage.setImageResource(R.drawable.choose);
                            dimianSheyingImage.setImageResource(R.drawable.choose);
                            dimianHulinImage.setImageResource(R.drawable.choose);
                            dimianLiaowangImage.setImageResource(R.drawable.choose);
                            dimianQunzhongImage.setImageResource(R.drawable.choose);*/
                            dimianAllText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianAllText.setTextColor(getResources().getColor(R.color.c12));
                            dimianSheyingText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianSheyingText.setTextColor(getResources().getColor(R.color.c12));
                            dimianHulinText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianHulinText.setTextColor(getResources().getColor(R.color.c12));
                            dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianLiaowangText.setTextColor(getResources().getColor(R.color.c12));
                            dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianQunzhongText.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });

        RxViewAction.clickNoDouble(dimianSheyingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimianSheyingChoose){  //从已选中变为未选中
                            dimianSheyingChoose = false;
                     //       dimianSheyingImage.setImageResource(R.drawable.choose_no);
                            dimianSheyingText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianSheyingText.setTextColor(getResources().getColor(R.color.c6));
                            if ( !dimianLiaowangChoose || !dimianHulinChoose || !dimianSheyingChoose || dimianQunzhongChoose ){   //判断全部未选中
                                dimianAllChoose = false;
                               // dimianAllImage.setImageResource(R.drawable.choose_no);
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimianSheyingChoose = true;
                       //.     dimianSheyingImage.setImageResource(R.drawable.choose);
                            dimianSheyingText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianSheyingText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimianLiaowangChoose && dimianHulinChoose && dimianSheyingChoose && dimianQunzhongChoose ){
                                dimianAllChoose = true;
                          //      dimianAllImage.setImageResource(R.drawable.choose);
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(dimianHulinLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimianHulinChoose){  //从已选中变为未选中
                            dimianHulinChoose = false;
                          // dimianHulinImage.setImageResource(R.drawable.choose_no);
                            dimianHulinText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianHulinText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimianLiaowangChoose || !dimianHulinChoose || !dimianSheyingChoose || dimianQunzhongChoose ){   //判断全部未选中
                                dimianAllChoose = false;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimianHulinChoose = true;
                         //   dimianHulinImage.setImageResource(R.drawable.choose);
                            dimianHulinText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianHulinText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimianLiaowangChoose && dimianHulinChoose && dimianSheyingChoose && dimianQunzhongChoose ){
                                dimianAllChoose = true;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(dimianLiaowangLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimianLiaowangChoose){  //从已选中变为未选中
                            dimianLiaowangChoose = false;
                          //  dimianLiaowangImage.setImageResource(R.drawable.choose_no);
                            dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianLiaowangText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimianLiaowangChoose || !dimianHulinChoose || !dimianSheyingChoose || dimianQunzhongChoose ){   //判断全部未选中
                                dimianAllChoose = false;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimianLiaowangChoose = true;
                        //    dimianLiaowangImage.setImageResource(R.drawable.choose);
                            dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianLiaowangText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimianLiaowangChoose && dimianHulinChoose && dimianSheyingChoose && dimianQunzhongChoose ){
                                dimianAllChoose = true;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(dimianQunzhongLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (dimianQunzhongChoose){  //从已选中变为未选中
                            dimianQunzhongChoose = false;
                        //    dimianQunzhongImage.setImageResource(R.drawable.choose_no);
                            dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_hui);
                            dimianQunzhongText.setTextColor(getResources().getColor(R.color.c6));
                            if (!dimianLiaowangChoose || !dimianHulinChoose || !dimianSheyingChoose || dimianQunzhongChoose ){   //判断全部未选中
                                dimianAllChoose = false;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            dimianQunzhongChoose = true;
                     //       dimianQunzhongImage.setImageResource(R.drawable.choose);
                            dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_lan);
                            dimianQunzhongText.setTextColor(getResources().getColor(R.color.c12));
                            if (dimianLiaowangChoose && dimianHulinChoose && dimianSheyingChoose && dimianQunzhongChoose ){
                                dimianAllChoose = true;
                                dimianAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                dimianAllText.setTextColor(getResources().getColor(R.color.c12));

                            }
                        }
                    }
                });

        /**
         * 天空监测的点击
         */
        RxViewAction.clickNoDouble(tiankongAllLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tiankongAllChoose){
                            tiankongAllChoose = false;
                            tiankongWurenjiChoose = false;
                            tiankongXuancifuChoose = false;
                          //  tiankongAllImage.setImageResource(R.drawable.choose_no);
                         //   tiankongXuanfuqiImage.setImageResource(R.drawable.choose_no);
                          //  tiankongWurenjiImage.setImageResource(R.drawable.choose_no);

                            tiankongAllText.setBackgroundResource(R.drawable.bg_text_hui);
                            tiankongAllText.setTextColor(getResources().getColor(R.color.c6));
                            tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_hui);
                            tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c6));
                            tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_hui);
                            tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            tiankongAllChoose = true;
                            tiankongWurenjiChoose = true;
                            tiankongXuancifuChoose = true;
                        //    tiankongAllImage.setImageResource(R.drawable.choose);
                          //  tiankongXuanfuqiImage.setImageResource(R.drawable.choose);
                          //  tiankongWurenjiImage.setImageResource(R.drawable.choose);

                            tiankongAllText.setBackgroundResource(R.drawable.bg_text_lan);
                            tiankongAllText.setTextColor(getResources().getColor(R.color.c12));
                            tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_lan);
                            tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c12));
                            tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_lan);
                            tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });

        RxViewAction.clickNoDouble(tiankongWurenjiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tiankongWurenjiChoose){  //从已选中变为未选中
                            tiankongWurenjiChoose = false;
                         //   tiankongWurenjiImage.setImageResource(R.drawable.choose_no);
                            tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_hui);
                            tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c6));
                            if (!tiankongWurenjiChoose || !tiankongXuancifuChoose ){   //判断全部未选中
                                tiankongAllChoose = false;
                                //tiankongAllImage.setImageResource(R.drawable.choose_no);
                                tiankongAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                tiankongAllText.setTextColor(getResources().getColor(R.color.c6));

                            }
                        }else {
                            tiankongWurenjiChoose = true;
                         //   tiankongWurenjiImage.setImageResource(R.drawable.choose);
                            tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_lan);
                            tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c12));
                            if (tiankongWurenjiChoose && tiankongXuancifuChoose){
                                tiankongAllChoose = true;
                         //       tiankongAllImage.setImageResource(R.drawable.choose);
                                tiankongAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                tiankongAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(tiankongXuanfuqiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tiankongXuancifuChoose){  //从已选中变为未选中  gaidaozhe
                            tiankongXuancifuChoose = false;
                        //    tiankongXuanfuqiImage.setImageResource(R.drawable.choose_no);
                            tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_hui);
                            tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c6));
                            if (!tiankongWurenjiChoose || !tiankongXuancifuChoose ){   //判断全部未选中
                                tiankongAllChoose = false;
                              //  tiankongAllImage.setImageResource(R.drawable.choose_no);
                                tiankongAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                tiankongAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            tiankongXuancifuChoose = true;
                           // tiankongXuanfuqiImage.setImageResource(R.drawable.choose);
                            tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_lan);
                            tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c12));
                            if (tiankongWurenjiChoose && tiankongXuancifuChoose){
                                tiankongAllChoose = true;
                              //  tiankongAllImage.setImageResource(R.drawable.choose);
                                tiankongAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                tiankongAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        /**
         * 卫星检测的点击
         */
        RxViewAction.clickNoDouble(weixingAllLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingAllChoose){
                            weixingAllChoose = false;
                            weixingNPPChoose = false;
                            weixingFY3Choose = false;
                            weixingFY4Choose = false;
                            weixingHIMA8Choose = false;
                            weixingNOAA19Choose = false;
                            weixingNOAA20Choose = false;
                            weixingGK2aChoose = false;
/*                            weixingAllImage.setImageResource(R.drawable.choose_no);
                            weixingNPPImage.setImageResource(R.drawable.choose_no);
                            weixingFY3Image.setImageResource(R.drawable.choose_no);
                            weixingFY4Image.setImageResource(R.drawable.choose_no);
                            weixingHima8Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA19Image.setImageResource(R.drawable.choose_no);*/
                            weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            weixingNppText.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNppText.setTextColor(getResources().getColor(R.color.c6));
                            weixingFY4Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingFY4Text.setTextColor(getResources().getColor(R.color.c6));
                            weixingFY3Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingFY3Text.setTextColor(getResources().getColor(R.color.c6));
                            weixingHima8Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingHima8Text.setTextColor(getResources().getColor(R.color.c6));
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c6));
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c6));
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c6));

                        }else {
                            weixingAllChoose = true;
                            weixingNPPChoose = true;
                            weixingFY3Choose = true;
                            weixingFY4Choose = true;
                            weixingHIMA8Choose = true;
                            weixingNOAA19Choose = true;
                            weixingNOAA20Choose = true;
                            weixingGK2aChoose = true;
                         /*   weixingAllImage.setImageResource(R.drawable.choose);
                            weixingNPPImage.setImageResource(R.drawable.choose);
                            weixingFY3Image.setImageResource(R.drawable.choose);
                            weixingFY4Image.setImageResource(R.drawable.choose);
                            weixingHima8Image.setImageResource(R.drawable.choose);
                            weixingNOAA19Image.setImageResource(R.drawable.choose);*/
                            weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            weixingNppText.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNppText.setTextColor(getResources().getColor(R.color.c12));
                            weixingFY4Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingFY4Text.setTextColor(getResources().getColor(R.color.c12));
                            weixingFY3Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingFY3Text.setTextColor(getResources().getColor(R.color.c12));
                            weixingHima8Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingHima8Text.setTextColor(getResources().getColor(R.color.c12));
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));
                        }
                    }
                });

        RxViewAction.clickNoDouble(weixingNPPLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {  //&&qie
                        if (weixingNPPChoose){  //从已选中变为未选中
                            weixingNPPChoose = false;
                    //     weixingNPPImage.setImageResource(R.drawable.choose_no);
                            weixingNppText.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNppText.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose || !weixingNOAA20Choose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                              //  weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            weixingNPPChoose = true;
                      //      weixingNPPImage.setImageResource(R.drawable.choose);
                            weixingNppText.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNppText.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                           //     weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(weixingFY3Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingFY3Choose){  //从已选中变为未选中
                            weixingFY3Choose = false;
                     //       weixingFY3Image.setImageResource(R.drawable.choose_no);
                            weixingFY3Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingFY3Text.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose|| !weixingNOAA20Choose|| !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                             //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            weixingFY3Choose = true;
                           // weixingFY3Image.setImageResource(R.drawable.choose);
                            weixingFY3Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingFY3Text.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                        //        weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(weixingFY4Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingFY4Choose){  //从已选中变为未选中
                            weixingFY4Choose = false;
                       //     weixingFY4Image.setImageResource(R.drawable.choose_no);
                            weixingFY4Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingFY4Text.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose|| !weixingNOAA20Choose|| !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                               // weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            weixingFY4Choose = true;
                        //    weixingFY4Image.setImageResource(R.drawable.choose);
                            weixingFY4Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingFY4Text.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                               // weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(weixingHima8Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingHIMA8Choose){  //从已选中变为未选中
                            weixingHIMA8Choose = false;
                         //   weixingHima8Image.setImageResource(R.drawable.choose_no);
                            weixingHima8Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingHima8Text.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose || !weixingNOAA20Choose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                             //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            weixingHIMA8Choose = true;
                      //      weixingHima8Image.setImageResource(R.drawable.choose);
                            weixingHima8Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingHima8Text.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                             //   weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


        RxViewAction.clickNoDouble(weixingNOAA19Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingNOAA19Choose){  //从已选中变为未选中
                            weixingNOAA19Choose = false;
                      //      weixingNOAA19Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose || !weixingNOAA20Choose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                             //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingNOAA19Choose = true;
                        //    weixingNOAA19Image.setImageResource(R.drawable.choose);
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                     //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(weixingNOAA20Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingNOAA20Choose){  //从已选中变为未选中
                            weixingNOAA20Choose = false;
                      //      weixingNOAA19Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose || !weixingNOAA20Choose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                             //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingNOAA20Choose = true;
                        //    weixingNOAA19Image.setImageResource(R.drawable.choose);
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                     //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(weixingGK2aLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingGK2aChoose){  //从已选中变为未选中
                            weixingGK2aChoose = false;
                      //      weixingNOAA19Image.setImageResource(R.drawable.choose_no);
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA19Choose || !weixingNOAA20Choose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                             //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingGK2aChoose = true;
                        //    weixingNOAA19Image.setImageResource(R.drawable.choose);
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA19Choose && weixingNOAA20Choose && weixingGK2aChoose){
                                weixingAllChoose = true;
                     //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


    }

    /**
     * 高级查询火点信息
     */
    private void findFirePost() {
        isGaoji = true;
        isopenFireDialog = true;
        String isCountry = "中国";
        if (isChooseJingwai){
            isCountry = "";
        }
        //卫星
        String satellite = "";
        if (weixingAllChoose){
            satellite = "ALL";
        }else {
          //  StringBuilder str = new StringBuilder();
            if (weixingNPPChoose){
                satellite = satellite + ",NPP";
            }
            if (weixingFY4Choose){
                satellite = satellite + ",FY-4";
            }
            if (weixingFY3Choose){
                satellite = satellite + ",FY-3";
            }
            if (weixingHIMA8Choose){
                satellite = satellite + ",Himawari-9";
            }
            if (weixingNOAA19Choose){
                satellite = satellite + ",NOAA-19";
            }
            if (weixingNOAA20Choose){
                satellite = satellite + ",NOAA-20";
            }
            if (weixingGK2aChoose){
                satellite = satellite + ",GK2a";
            }
            if (weixingNPPChoose || weixingFY3Choose || weixingFY4Choose || weixingHIMA8Choose || weixingNOAA19Choose || weixingNOAA20Choose || weixingGK2aChoose){
                satellite = satellite.substring(1,satellite.length());
            }

        }
        //天空
        String tiankongStr = "";
        if (tiankongAllChoose){
            tiankongStr = "ALL";
        }else {
            if (tiankongWurenjiChoose){
                tiankongStr = tiankongStr + ",无人机";
            }
            if (tiankongXuancifuChoose){
                tiankongStr = tiankongStr + ",悬浮器";
            }
            if (tiankongWurenjiChoose || tiankongXuancifuChoose){
                tiankongStr = tiankongStr.substring(1,tiankongStr.length());
            }

        }
        //地面
        String dimianStr = "";
        if (dimianAllChoose){
            dimianStr = "ALL";
        }else {
            if (dimianSheyingChoose){
                dimianStr = dimianStr + ",摄影机";
            }
            if (dimianHulinChoose){
                dimianStr = dimianStr + ",护林员";
            }
            if (dimianLiaowangChoose){
                dimianStr = dimianStr + ",瞭望员";
            }
            if (dimianQunzhongChoose){
                dimianStr = dimianStr + ",群众";
            }
            if (dimianSheyingChoose ||  dimianHulinChoose || dimianLiaowangChoose || dimianQunzhongChoose){
                dimianStr = dimianStr.substring(1,dimianStr.length());
            }

        }

        //地貌
        String dimaoStr = "";
        if (dimaoAllChoose){
            dimaoStr = "ALL";
        }else {
            if (dimaoLindiChoose){
                if(dimaoStr.isEmpty()){
                    dimaoStr = dimaoStr + "Woodland";
                }else{
                    dimaoStr = dimaoStr + ",Woodland";
                }
            }
            if (dimaoCaodiChoose){
                if(dimaoStr.isEmpty()){
                    dimaoStr = dimaoStr + "Grassland";
                }else{
                    dimaoStr = dimaoStr + ",Grassland";
                }
            }
            if (dimaoNongtianChoose){
                if(dimaoStr.isEmpty()){
                    dimaoStr = dimaoStr + "Farmland";
                }else{
                    dimaoStr = dimaoStr + ",Farmland";
                }
            }
            if (dimaoQitaChoose){
                if(dimaoStr.isEmpty()){
                    dimaoStr = dimaoStr + "Otherland";
                }else{
                    dimaoStr = dimaoStr + ",Otherland";
                }
            }
//            if (dimaoLindiChoose || dimaoCaodiChoose || dimaoNongtianChoose || dimaoQitaChoose){
//                dimaoStr =  dimaoStr.substring(1,dimaoStr.length());
//            }

        }

        //时间
        String startTimeStr = "";
        if (!gaojiStartimeText.getText().toString().equals("请输入开始时间")) {
            startTimeStr = gaojiStartimeText.getText().toString();
        }
        String endTimeStr = "";
        if (!gaojiEndTimeText.getText().toString().equals("请输入结束时间")){
            endTimeStr = gaojiEndTimeText.getText().toString();
        }

        //区域
        String shengId = "";
        String shiId = "";
        String quId = "";
        if (isChooseSheng){

            for (int i = 0; i < shengList.size(); i++) {
                if (shengList.get(i).getName().equals( shengText.getText().toString())) {
                    shengId = shengList.get(i).getId();
                }
            }
        }
        if (!shiText.getText().equals("请选择市")){
            for (int i = 0; i < shiList.size(); i++) {
                if (shiList.get(i).getName().equals(shiText.getText().toString())) {
                    shiId = shiList.get(i).getId();
                }
            }
        }
        if (!quText.getText().equals("请选择区")){
            for (int i = 0; i < quList.size(); i++) {
                if (quList.get(i).getName().equals(quText.getText().toString())) {
                    quId = quList.get(i).getId();
                }
            }
        }


        Setting setting = new DbConfig(this).getSetting();
        showDialogProgress(gaojiFindDialog,"正在查询中..");
        if(isLoadMore){
            dWebView.callHandler("huodian_gaoji_more", new Object[]{startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,page,setting.getNumber(),isChooseHuanchong,isCountry,shengId,shiId,quId},new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    //searchDialog.hide();
                    // Toast.makeText(MainActivity.this, "已显示查询的火点信息", Toast.LENGTH_SHORT).show();
                    // getFireFromService(120);
                    //     fireInfoListDialog.show();
                }
            });
        }else{
            dWebView.callHandler("huodian_gaoji", new Object[]{startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,setting.getNumber(),isChooseHuanchong,isCountry,shengId,shiId,quId},new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    //searchDialog.hide();
                    // Toast.makeText(MainActivity.this, "已显示查询的火点信息", Toast.LENGTH_SHORT).show();
                    // getFireFromService(120);
                    //     fireInfoListDialog.show();
                }
            });
        }

        reloginState = 0;
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Satellite/GetListByPutTime");
        // params.addBodyParameter("reqJson", jsonObject.toString());

        params.addParameter("Token",new DbConfig(this).getUser().getToken());
        params.addParameter("page",page);//bingo did
        params.addParameter("rows",setting.getNumber());
        params.addParameter("sort","PutStorageTime");
        params.addParameter("order","desc");
        params.addParameter("hour",0);
        params.addParameter("startTime",startTimeStr);
        params.addParameter("endTime",endTimeStr);
        params.addParameter("satellite",satellite);
        params.addParameter("sky",tiankongStr);
        params.addParameter("ground",dimianStr);
        params.addParameter("landtype",dimaoStr);
        params.addParameter("isbuffer",isChooseHuanchong);
        params.addParameter("Country",isCountry);
        params.addParameter("Province",shengId);
        params.addParameter("City",shiId);
        params.addParameter("county",quId);
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginByPassword1: param---gaoji" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: 火点:" + result);
                if(!isLoadMore){
                    fireInfoList.clear();
                }
                isLoadMore = false;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    total = jsonObject.getString("total");
                    if (!total.equals("0")){
                        JSONArray fireInfoJsonArray = jsonObject.getJSONArray("rows");
                        Log.e(TAG, "onSuccess: 火点1" );
                        for (int i = 0; i < fireInfoJsonArray.length(); i++) {
                            try {
                                JSONObject fireObj = fireInfoJsonArray.getJSONObject(i);
                                String id = fireObj.getString("Id");
                                String longitude = fireObj.getString("Longitude");
                                String latitude = fireObj.getString("Latitude");
                                int observationFrequency = fireObj.getInt("ObservationFrequency");
                                String observationDateTime = fireObj.getString("ObservationDateTime");
//                                int strength = fireObj.getInt("Strength");
//                                int strengthLevel = fireObj.getInt("StrengthLevel");
                                double woodland = fireObj.getDouble("Woodland");
                                double grassland = fireObj.getDouble("Grassland");
                                double farmland = fireObj.getDouble("Farmland");
                                double otherland = fireObj.getDouble("Otherland");
                                double area = fireObj.getDouble("Area");
                                double credibility = fireObj.getDouble("Credibility");
                                double pixelArea = fireObj.getDouble("PixelArea");
                                int pixelNumber = fireObj.getInt("PixelNumber");
                                String country = fireObj.getString("Country");
                                String countryCode = fireObj.getString("CountryCode");
                                String province = fireObj.getString("Province");
                                String provinceCode = fireObj.getString("ProvinceCode");
                                String city = fireObj.getString("City");
                                String cityCode = fireObj.getString("CityCode");
                                String county = fireObj.getString("County");
                                String countyCode = fireObj.getString("CountyCode");
                                String formattedAddress = fireObj.getString("FormattedAddress");
                                String visibleLightImageAddress = fireObj.getString("VisibleLightImageAddress");
                                String irImageAddress = fireObj.getString("IRImageAddress");
                                String satellite = fireObj.getString("Satellite");
                                String putStorageTime = fireObj.getString("PutStorageTime");
                                String dataSourceFile = fireObj.getString("DataSourceFile");
                                String fireNo = fireObj.getString("FireNo");
                                String districtNum = fireObj.getString("DistrictNum");
                                FireInfo fireInfo = new FireInfo(id,longitude,latitude,observationFrequency,observationDateTime,0,0,woodland,grassland,farmland,otherland,area,credibility,pixelArea,
                                        pixelNumber,country,countryCode,province,provinceCode,city,cityCode,county,countyCode,formattedAddress,visibleLightImageAddress,irImageAddress,satellite,
                                        putStorageTime,dataSourceFile,fireNo,districtNum);
                                fireInfoList.add(fireInfo);
                            }catch (Exception e){
                                continue;
                            }


                        }
                        Log.e(TAG, "onSuccess: 火点2 fireInfoList.size() " + fireInfoList.size() );
                        gaojiDialog.hide();
                        initFireData();
                    }else {
                        Toast.makeText(MainActivity.this, "当前未有火点", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //Toast.makeText(MainActivity.this, "网络异常，请检查网络链接13", Toast.LENGTH_SHORT).show();
               // relogin();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(gaojiFindDialog);
            }
        });

    }

    /**
     * 显示地区选择的dialog
     */
    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, shengSelectIndex);//init selected position is 0 初始选中位置为0
        }else if (currentChooseArea == 1){
            areaWy.setItems(strList, shiSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(strList, quSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0){   //选择省
                    isChooseSheng = true;
                    currentChooseSheng = areaWy.getSelectedItem();
                    shengSelectIndex = areaWy.getSelectedPosition();
                    shengText.setText(currentChooseSheng);
                }else if (currentChooseArea == 1){
                    //选择市
                    currentChooseShi = areaWy.getSelectedItem();
                    shiSelectIndex = areaWy.getSelectedPosition();
                    shiText.setText(currentChooseShi);
                }else {
                    //选择区
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    quText.setText(currentChooseQu);
                }
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setView(areaView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();


                    }
                })
                .show();
    }

    private void  getAllAre() {
        Log.e(TAG, "initArea: -4" );
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",token);
        params.addParameter("parentId",0);
        params.setConnectTimeout(100000);
        Log.e(TAG, "initArea: -5" );
        Log.e(TAG, "quanguo: quanguo---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.e(TAG, "onSuccess: " + jsonArray.toString());
                    shengList.clear();
                    shengStrList.clear();
                    shengStrList.add("请选择省");
                    Log.e(TAG, "onSuccess: area0");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject area = jsonArray.getJSONObject(i);
                        String id = area.getString("Id");
                        String name = area.getString("Name");
                        String parentId = area.getString("ParentId");
                        shengList.add(new Area(id,name,parentId));
                        shengStrList.add(name);
                    }


                    showAreaDialog(shengStrList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
             //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接12", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getShengAre(String id) {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",token);
        params.addParameter("parentId",id);
        params.setConnectTimeout(100000);
        Log.e(TAG, "sheng: sheng---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:sheng----- " + result);
                try {
                    shiList.clear();
                    shiStrList.clear();
                    Log.e(TAG, "onSuccess: 1" );
                    JSONArray jsonArray = new JSONArray(result);
                    Log.e(TAG, "onSuccess: 2" );
                    shiStrList.add("请选择市");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject area = jsonArray.getJSONObject(i);
                        String id = area.getString("Id");
                        String name = area.getString("Name");
                        String parentId = area.getString("ParentId");
                        shiList.add(new Area(id,name,parentId));
                        shiStrList.add(name);
                    }
                    Log.e(TAG, "onSuccess: 3" );
                    showAreaDialog(shiStrList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        String token = jsonObject.getString("message");

                        getUserInfo(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
             //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接14", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getShiAre(String id) {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",token);
        params.addParameter("parentId",id);
        params.setConnectTimeout(100000);
        Log.e(TAG, "shi: shi---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:shi----- " + result);
                try {
                    quList.clear();
                    quStrList.clear();
                    Log.e(TAG, "onSuccess: 1" );
                    JSONArray jsonArray = new JSONArray(result);
                    Log.e(TAG, "onSuccess: 2" );
                    quStrList.add("请选择区");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject area = jsonArray.getJSONObject(i);
                        String id = area.getString("Id");
                        String name = area.getString("Name");
                        String parentId = area.getString("ParentId");
                        quList.add(new Area(id,name,parentId));
                        quStrList.add(name);
                    }
                    Log.e(TAG, "onSuccess: 3" );
                    showAreaDialog(quStrList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        String token = jsonObject.getString("message");

                        getUserInfo(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
             //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接14", Toast.LENGTH_SHORT).show();
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
     * 注册火警信息item
     */
    private void register() {
        FireInfoViewBinder fireInfoViewBinder = new FireInfoViewBinder();
        fireInfoViewBinder.setListener(this);
        adapter.register(FireInfo.class, fireInfoViewBinder);




    }

    /**
     * 获取火点信息
     * @param hours
     */
    private void getFireFromService(int hours) {
        if(!isLoadMore){
            page = 1;
        }
        isGaoji = false;
        Log.e(TAG, "jpush5" );
        reloginState = 1;
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Satellite/GetListByPutTime");
        // params.addBodyParameter("reqJson", jsonObject.toString());

        Setting setting = new DbConfig(this).getSetting();
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
        params.addParameter("page",page);//bingo did
        params.addParameter("rows",setting.getNumber());
       // params.addParameter("rows",setting.getNumber());

        params.addParameter("sort","PutStorageTime");
        params.addParameter("order","desc");
        params.addParameter("hour",hours);
        params.addParameter("startTime","");
        params.addParameter("endTime","");
        params.setConnectTimeout(60000);


        if (!isJush){
            //params.addParameter("rows",setting.getNumber());
            params.addParameter("satellite",setting.getWeixing());
            params.addParameter("sky",setting.getTiankong());
            params.addParameter("ground",setting.getDimian());
            params.addParameter("landtype",setting.getDimao());
            params.addParameter("isbuffer",setting.getHuanchong().equals("0")? false : true);
            params.addParameter("Country",setting.getJingwai());
        }else {
            Log.e(TAG, "jpush6" );
            params.addParameter("isbuffer",true);
            params.addParameter("Country","中国");
        }


        params.setConnectTimeout(100000);
        Log.e(TAG, "loginByPassword1: param---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: 火点:" + result);
                if(!isLoadMore){
                    fireInfoList.clear();
                }
                isLoadMore = false;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    total = jsonObject.getString("total");
                    if (!total.equals("0")){
                        JSONArray fireInfoJsonArray = jsonObject.getJSONArray("rows");
//                        Log.e(TAG, "loginByPassword1: param---Android" + fireInfoJsonArray.length());

//                        Log.e(TAG, "loginByPassword1: param---Android " + "00" + " " + params.toString());
                        for (int i = 0; i < fireInfoJsonArray.length(); i++) {
                            try {

                                JSONObject fireObj = fireInfoJsonArray.getJSONObject(i);
//                                Log.e(TAG, "loginByPassword1: param---Android " + i + " " + fireObj);
                                String id = fireObj.getString("Id");
                                String longitude = fireObj.getString("Longitude");
                                String latitude = fireObj.getString("Latitude");
                                int observationFrequency = fireObj.getInt("ObservationFrequency");
                                String observationDateTime = fireObj.getString("ObservationDateTime");
//                                int strength = fireObj.getInt("Strength");
//                                int strengthLevel = fireObj.getInt("StrengthLevel");
                                int strength = 0;
                                int strengthLevel = 0;
                                double woodland = fireObj.getDouble("Woodland");
                                double grassland = fireObj.getDouble("Grassland");
                                double farmland = fireObj.getDouble("Farmland");
                                double otherland = fireObj.getDouble("Otherland");
                                double area = fireObj.getDouble("Area");
                                double credibility = fireObj.getDouble("Credibility");
                                double pixelArea = fireObj.getDouble("PixelArea");
                                int pixelNumber = fireObj.getInt("PixelNumber");
                                String country = fireObj.getString("Country");
                                String countryCode = fireObj.getString("CountryCode");

                                String province = fireObj.getString("Province");
                                String provinceCode = fireObj.getString("ProvinceCode");
                                String city = fireObj.getString("City");
                                String cityCode = fireObj.getString("CityCode");
                                String county = fireObj.getString("County");
                                String countyCode = fireObj.getString("CountyCode");
                                String formattedAddress = fireObj.getString("FormattedAddress");
                                String visibleLightImageAddress = fireObj.getString("VisibleLightImageAddress");
                                String irImageAddress = fireObj.getString("IRImageAddress");
                                String satellite = fireObj.getString("Satellite");
                                String putStorageTime = fireObj.getString("PutStorageTime");
                                String dataSourceFile = fireObj.getString("DataSourceFile");
                                String fireNo = fireObj.getString("FireNo");
                                String districtNum = fireObj.getString("DistrictNum");

                                FireInfo fireInfo = new FireInfo(id,longitude,latitude,observationFrequency,observationDateTime,strength,strengthLevel,woodland,grassland,farmland,otherland,area,credibility,pixelArea,
                                        pixelNumber,country,countryCode,province,provinceCode,city,cityCode,county,countyCode,formattedAddress,visibleLightImageAddress,irImageAddress,satellite,
                                        putStorageTime,dataSourceFile,fireNo,districtNum);
                                fireInfoList.add(fireInfo);
                            }catch (Exception e){
                                continue;
                            }


                        }
                        Log.e(TAG, "jpush7" );
                        initFireData();

                      /*  if (isJush){
                            initJpushFireData();
                        }*/
                    }else {
                        //Toast.makeText(MainActivity.this, "当前未有火点", Toast.LENGTH_SHORT).show();
                        initFireData();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
             //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接11", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + ex.toString());
                String message = ex.toString();
               // showUserTokenDialog("账号异常,请重新登录");
                if(message.indexOf("401")!=-1){ //在其他手机登录  跳到登录界面
                    relogin();
                    // showUserTokenDialog("您的账号在其它设备登录,请重新登录");
               //     Toast.makeText(MainActivity.this, "token错了", Toast.LENGTH_SHORT).show();
                  //  getFireFromService(currentFireFindTime);
                }else if (message.equals("Your Items/List is empty.")){
                  //  Toast.makeText(MainActivity.this, "网络异常，请检查网络链接11", Toast.LENGTH_SHORT).show();
                }else if (message.indexOf("500")!=-1){
                    showUserTokenDialog("账号异常,请重新登录");
                    //  Toast.makeText(MainActivity.this, "网络异常，请检查网络链接11", Toast.LENGTH_SHORT).show();
                }else {
                  //  showUserTokenDialog("您的身份已失效,请重新登录");

                 //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接11", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(gaojiFindDialog);
            }
        });

    }

    /**
     * 重新登录
     */
    private void relogin() {
        final User user = new DbConfig(this).getUser();
        String username = user.getUsername();
        String password = user.getPassword();

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/Login");
        params.addParameter("userName",username);
        params.addParameter("password",password);
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginByPassword2: param---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        token = jsonObject.getString("message");

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();

                        user.setToken(token);
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        if (startType.equals("JPUSH")){     //极光推送打开app   根据火警id查出火警详情 判断火警发生时间距现在时间的小时  查询出时间内的所有火警展示  缩放到当前火警
                            user.setJpush(true);
                            try {
                                db.saveOrUpdate(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "jpush1" );
                            hasActivity = false;
                            String fireStr = bundle.getString("fire_id");
                            Log.e(TAG, "jpush1: "+fireStr);
                            JpsuhFireInfo(fireStr);
                        }else {
                            user.setJpush(false);
                            try {
                                db.saveOrUpdate(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            Setting setting = new DbConfig(getApplicationContext()).getSetting();
                            dWebView.callHandler("abc", new Object[]{user.getUsername(), user.getPassword(),user.getToken(),currentFireFindTime,
                                    setting.getWeixing(),setting.getTiankong(),setting.getDimian(),setting.getDimao(),setting.getNumber(),
                                    setting.getJingwai(),setting.getHuanchong()},new OnReturnValue<String>() {
                                @Override
                                public void onValue(String retValue) {
                                    Log.e(TAG, "jpush: ---------------------------------------------7" );
                                    isJush = false;
                                    //   dWebView.setVisibility(View.VISIBLE);
                                    getFireFromService(3);
                                    //  Toast.makeText(MainActivity.this, retValue, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        if (reloginState == 1){
                            getFireFromService(currentFireFindTime);
                        }else {
                            findFirePost();
                        }



                    }else {
                        String token = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(MainActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
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
     * 显示极光的data
     */
    private void initJpushFireData() {

        Log.e(TAG, "jpush10" );

        Log.e(TAG, "jpush:currentFireNo---- " + currentFireNo);
        Log.e(TAG, "jpush:currentFireLa---- " + currentFireLa);
        Log.e(TAG, "jpush:currentFireLo---- " + currentFireLo);
        Log.e(TAG, "jpush:fireInfoList.size---- " +fireInfoList.size());

        //应该根据id 查找出cireInfoList中的一条火警数据展示 现在默认展示第一条  后期再改
        if (fireInfoList.size()>0){
           // currentFire = fireInfoList.get(0);
            for (int i = 0; i < fireInfoList.size(); i++) {
                FireInfo fireInfo = fireInfoList.get(i);
                Log.e(TAG, "jpush:fireInfo.getFireNo()---- " + fireInfo.getFireNo());
                Log.e(TAG, "jpush:fireInfo.getLongitude()---- " + fireInfo.getLongitude());
                Log.e(TAG, "jpush:fireInfo.getLatitude()---- " + fireInfo.getLatitude());
                if (fireInfo.getId().equals(currentFireId)){
                    currentFire = fireInfo;
                }
           }
        }else {
            Toast.makeText(this, "火点数据是空的", Toast.LENGTH_SHORT).show();
        }

       /* for (int i = 0; i < fireInfoList.size(); i++) {
            FireInfo fireInfo = fireInfoList.get(i);
            Log.e(TAG, "jpush:fireInfo.getFireNo()---- " + fireInfo.getFireNo());
            Log.e(TAG, "jpush:fireInfo.getLongitude()---- " + fireInfo.getLongitude());
            Log.e(TAG, "jpush:fireInfo.getLatitude()---- " + fireInfo.getLatitude());

            if (fireInfo.getId().equals(currentFireId)){
                currentFire = fireInfo;
            }
           *//* if (fireInfo.getFireNo().equals(currentFireNo) && fireInfo.getLatitude().equals(currentFireLa) && fireInfo.getLongitude().equals(currentFireLo)) {
                currentFire = fireInfo;
                Log.e(TAG, "jpush-fireinfor" );
                return;
            }*//*
        }*/
        Log.e(TAG, "jpush11" );
        Log.e(TAG, "jpush111" + currentFire.getFireNo());


        Log.e(TAG, "initJpushFireData:边境热源=" + currentFire.getFormattedAddress()+"--");
        if (currentFire.getFormattedAddress().isEmpty()){
            fireAddressText.setText("边境热源");
        }else {
            fireAddressText.setText(currentFire.getFormattedAddress());
        }

        try {
            fireTimeText.setText(currentFire.getObservationDateTime().replace("T","  "));
            jingWeiText.setText(NumberUtils.saveOneBitTwo(Double.parseDouble(currentFire.getLongitude()))  + "  " +  NumberUtils.saveOneBitTwo(Double.parseDouble(currentFire.getLatitude())));

        }catch (Exception e){
            fireTimeText.setText("");
            jingWeiText.setText("");

        }
        kexinText.setText(currentFire.getCredibility() +"");
        mianjiText.setText(currentFire.getArea() +"");
        cishuText.setText(currentFire.getObservationFrequency() +"");
        leixingText.setText("林地(" + getTwoDouble(currentFire.getWoodland() * 100) +"%)草地(" + getTwoDouble(currentFire.getGrassland() * 100 ) + "%)农田(" + getTwoDouble(currentFire.getFarmland() * 100) + "%)其他(" + getTwoDouble(currentFire.getOtherland() *100 )+ "%)"  );
        shujuyuanText.setText(currentFire.getSatellite());
        huodianCodeText.setText(currentFire.getFireNo());
        xiangyuanmianjiView.setText(currentFire.getPixelArea()+"");
        xiangyuanshuView.setText(currentFire.getPixelNumber()+"");


        try {
            if (currentFire.getVisibleLightImageAddress().equals("null")){
                huodianOneImage.setVisibility(View.GONE);
            }else {
                huodianOneImage.setVisibility(View.VISIBLE);
                Log.e(TAG, "initJpushFireData: "+currentFire.getVisibleLightImageAddress() );
                if (currentFire.getVisibleLightImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                    Glide.with(this).load(currentFire.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);

                    RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            Log.e(TAG, "call: bingo 图片"  );
                            Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                            intent.putExtra("pic", currentFire.getVisibleLightImageAddress());
                            startActivity(intent);
                        }
                    });
                }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                    if (currentFire.getSatellite().indexOf("NOAA") != -1){
                        Log.e(TAG, "initJpushFireData: "+"11111111111111" );
                        Glide.with(this).load("http://219.239.221.19" + currentFire.getVisibleLightImageAddress())
                                .error(R.drawable.no_pic)
                                .placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                        RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                intent.putExtra("pic", "http://219.239.221.19" + currentFire.getVisibleLightImageAddress());
                                startActivity(intent);
                            }
                        });
                    }else {
                        Log.e(TAG, "initJpushFireData: "+"http://web.ehaohai.com:2018" + currentFire.getVisibleLightImageAddress());
                        Glide.with(this).load("http://web.ehaohai.com:2018" + currentFire.getVisibleLightImageAddress())
                                .error(R.drawable.no_pic)
                                .placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                        RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                intent.putExtra("pic", "http://web.ehaohai.com:2018" + currentFire.getVisibleLightImageAddress());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            if (currentFire.getiRImageAddress().equals("null")){
                huodianTwoImage.setVisibility(View.GONE);
            }else {
                huodianTwoImage.setVisibility(View.VISIBLE);
                try {
                    Log.e(TAG, "WTF: " + currentFire.getVisibleLightImageAddress() );
                    if (currentFire.getVisibleLightImageAddress().equals("null") || currentFire.getVisibleLightImageAddress().equals("") || currentFire.getVisibleLightImageAddress().length()==0){
                        huodianOneImage.setVisibility(View.GONE);
                    }else {
                        huodianOneImage.setVisibility(View.VISIBLE);
                        if (currentFire.getVisibleLightImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                            Glide.with(getApplicationContext()).load(currentFire.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                            RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", currentFire.getVisibleLightImageAddress());
                                    startActivity(intent);
                                }
                            });
                        }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                            if (currentFire.getSatellite().indexOf("NOAA") != -1){
                                Glide.with(getApplicationContext()).load("http://219.239.221.19" + currentFire.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                                RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", "http://219.239.221.19" + currentFire.getVisibleLightImageAddress());
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                Glide.with(getApplicationContext()).load("http://web.ehaohai.com:2018" + currentFire.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                                RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", "http://web.ehaohai.com:2018" + currentFire.getVisibleLightImageAddress());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }

                    if (currentFire.getiRImageAddress().equals("null") || currentFire.getiRImageAddress().equals("")){
                        huodianTwoImage.setVisibility(View.GONE);
                    }else {
                        huodianTwoImage.setVisibility(View.VISIBLE);
                        if (currentFire.getiRImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                            RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                                    Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", currentFire.getiRImageAddress());
                                    startActivity(intent);
                                }
                            });
                            Glide.with(getApplicationContext()).load(currentFire.getiRImageAddress())
                                    .placeholder(R.drawable.ic_jaizai)
                                    .into(huodianTwoImage);
                        }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                            if (currentFire.getSatellite().indexOf("NOAA") != -1){
                                RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", "http://219.239.221.19" + currentFire.getiRImageAddress());
                                        startActivity(intent);
                                    }
                                });
                                Glide.with(getApplicationContext()).load("http://219.239.221.19" + currentFire.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                            }else {
                                RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.e(TAG, "call: bingo 图片"  );
                                        Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                        intent.putExtra("pic", "http://web.ehaohai.com:2018" + currentFire.getiRImageAddress());
                                        startActivity(intent);
                                    }
                                });
                                Glide.with(getApplicationContext()
                                ).load("http://web.ehaohai.com:2018" + currentFire.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                            }
                        }
                    }
                }catch (Exception e){

                }
            }
        }catch (Exception e){

        }

        Log.e(TAG, "bingo: currentFire.getVisibleLightImageAddress() push = " + currentFire.getVisibleLightImageAddress() );


        //   Glide.with(this).load("http://27.223.18.10:2018" + fireInfo.getiRImageAddress()).into(huodianTwoImage);
        Log.e(TAG, "jpush12" );

        fireInfoListDialog.hide();
        fireDialog.show();
        dWebView.callHandler("android_fly_to", new Object[]{Double.parseDouble(currentFire.getLongitude()), Double.parseDouble(currentFire.getLatitude())}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue: ---------------------------------------------" );
            }
        });
    }

    public Double getTwoDouble(double f){
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     *
     */
    private void initFireData() {
        fireCountText.setText(total);
        Log.e(TAG, "jpush8" );
        if (isJush){
            Log.e(TAG, "jpush9" );
            initJpushFireData();
        }else {
            if (isopenFireDialog && !hasActivity){
                fireInfoListDialog.show();
            }
        }

        Log.e(TAG, "initFireData: 火点3");
        items.clear();

        if (currentFireListType == 1){      //按时间分类
            for (int i = 0; i < fireInfoList.size(); i++) {
                if (i > 0){
                    if (fireInfoList.get(i).getObservationDateTime().equals(fireInfoList.get(i - 1).getObservationDateTime())) {    //如果这一条如上一条时间相同
                        fireInfoList.get(i).setShowTime(false);
                        fireInfoList.get(i).setShowLine(false);
                    }else {
                        fireInfoList.get(i).setShowTime(true);
                        fireInfoList.get(i).setShowLine(true);
                    }
                }else {
                    fireInfoList.get(i).setShowTime(true);
                    fireInfoList.get(i).setShowLine(false);
                }
                fireInfoList.get(i).setFireListType(1);
                items.add(fireInfoList.get(i));
            }
        }else {         //按编号分类
            for (int i = 0; i < fireInfoList.size(); i++) {
                if (i > 0){
                    if (fireInfoList.get(i).getFireNo().equals(fireInfoList.get(i - 1).getFireNo())) {    //如果这一条如上一条时间相同
                        fireInfoList.get(i).setShowTime(false);
                        fireInfoList.get(i).setShowLine(false);
                    }else {
                        fireInfoList.get(i).setShowTime(true);
                        fireInfoList.get(i).setShowLine(true);
                    }
                }else {
                    fireInfoList.get(i).setShowTime(true);
                    fireInfoList.get(i).setShowLine(false);
                }
                fireInfoList.get(i).setFireListType(2);
                items.add(fireInfoList.get(i));
            }
        }

        Log.e(TAG, "initFireData: 火点4");
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void gugeYingxiang() {
        dWebView.callHandler("google_yingxiang", new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue:  fanhiu2" + retValue);
                mHandler.sendEmptyMessage(GUGE_YINGXIANG);
                // mapChooseDialog.hide();
                //Toast.makeText(MainActivity.this, "已切换为谷歌影像地图", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        exit();

    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次回到主页",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
            moveTaskToBack(true);       //返回首页
            Log.e(TAG, "exit: -----");
        }
    }

    /**
     * 火点列表的点击回调
     * @param fireInfo
     */
    @Override
    public void onFireInfoClick(final FireInfo fireInfo) {
        Log.e(TAG, "bingo: fireInfo = " + fireInfo.toString() );

        dWebView.callHandler("android_fly_to", new Object[]{Double.parseDouble(fireInfo.getLongitude()), Double.parseDouble(fireInfo.getLatitude())}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue: ---------------------------------------------" );
            }
        });

        currentFire = fireInfo;
        currentFireId = fireInfo.getId();
        currentFireLa = fireInfo.getLatitude();
        currentFireLo = fireInfo.getLongitude();

        Log.e(TAG, "initJpushFireData:边境热源=" + fireInfo.getFormattedAddress()+"--");
        if (currentFire.getFormattedAddress().isEmpty()){

            fireAddressText.setText("边境热源");
        }else {
            fireAddressText.setText(fireInfo.getFormattedAddress());
        }
      //  fireAddressText.setText(fireInfo.getFormattedAddress());
        fireTimeText.setText(fireInfo.getObservationDateTime().replace("T","  "));
        jingWeiText.setText(NumberUtils.saveOneBitTwo(Double.parseDouble(fireInfo.getLongitude()))  + "  " +  NumberUtils.saveOneBitTwo(Double.parseDouble(fireInfo.getLatitude())));
        kexinText.setText(fireInfo.getCredibility() +"");
        mianjiText.setText(fireInfo.getArea() +"");
        cishuText.setText(fireInfo.getObservationFrequency() +"");
        leixingText.setText("林地(" + getTwoDouble(fireInfo.getWoodland() * 100) +"%)草地(" + getTwoDouble(fireInfo.getGrassland() * 100) + "%)农田(" + getTwoDouble(fireInfo.getFarmland() * 100) + "%)其他(" + getTwoDouble(fireInfo.getOtherland() *100 )+ "%)"  );

        shujuyuanText.setText(fireInfo.getSatellite());
        huodianCodeText.setText(fireInfo.getFireNo());
        xiangyuanmianjiView.setText(fireInfo.getPixelArea()+"");
        xiangyuanshuView.setText(fireInfo.getPixelNumber()+"");


        if (fireInfo.getVisibleLightImageAddress().equals("null")||fireInfo.getVisibleLightImageAddress().length()==0){
            huodianOneImage.setVisibility(View.GONE);
        }else {
            huodianOneImage.setVisibility(View.VISIBLE);

            try {
                Log.e(TAG, "WTF: " + fireInfo.getVisibleLightImageAddress() );
                if (fireInfo.getVisibleLightImageAddress().equals("null") || fireInfo.getVisibleLightImageAddress().equals("") || fireInfo.getVisibleLightImageAddress().length()==0){
                    huodianOneImage.setVisibility(View.GONE);
                }else {
                    huodianOneImage.setVisibility(View.VISIBLE);
                    if (fireInfo.getVisibleLightImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                        Glide.with(getApplicationContext()).load(fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                        RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                intent.putExtra("pic", fireInfo.getVisibleLightImageAddress());
                                startActivity(intent);
                            }
                        });
                    }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                        if (fireInfo.getSatellite().indexOf("NOAA") != -1){
                            Glide.with(getApplicationContext()).load("http://219.239.221.19" + fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                            RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", "http://219.239.221.19" + fireInfo.getVisibleLightImageAddress());
                                    startActivity(intent);
                                }
                            });
                        }else {
                            Glide.with(getApplicationContext()).load("http://web.ehaohai.com:2018" + fireInfo.getVisibleLightImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
                            RxViewAction.clickNoDouble(huodianOneImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                            Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", "http://web.ehaohai.com:2018" + fireInfo.getVisibleLightImageAddress());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }

                if (fireInfo.getiRImageAddress().equals("null") || fireInfo.getiRImageAddress().equals("")){
                    huodianTwoImage.setVisibility(View.GONE);
                }else {
                    huodianTwoImage.setVisibility(View.VISIBLE);
                    if (fireInfo.getiRImageAddress().indexOf("http") != -1){       //包含http地址 直接加载
                        RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Log.e(TAG, "call: bingo 图片"  );
                                Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                intent.putExtra("pic", fireInfo.getiRImageAddress());
                                startActivity(intent);
                            }
                        });
                        Glide.with(getApplicationContext()).load(fireInfo.getiRImageAddress())
                                .placeholder(R.drawable.ic_jaizai)
                                .into(huodianTwoImage);
                    }else {             //NOAA  用的地址 http://219.239.221.19    其他卫星用的地址：http://27.223.18.10:2018
                        if (fireInfo.getSatellite().indexOf("NOAA") != -1){
                            RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                                    Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", "http://219.239.221.19" + fireInfo.getiRImageAddress());
                                    startActivity(intent);
                                }
                            });
                            Glide.with(getApplicationContext()).load("http://219.239.221.19" + fireInfo.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                        }else {
                            RxViewAction.clickNoDouble(huodianTwoImage).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    Log.e(TAG, "call: bingo 图片"  );
                                    Intent intent = new Intent(MainActivity.this, PicActivity.class);
                                    intent.putExtra("pic", "http://web.ehaohai.com:2018" + fireInfo.getiRImageAddress());
                                    startActivity(intent);
                                }
                            });
                            Glide.with(getApplicationContext()
                            ).load("http://web.ehaohai.com:2018" + fireInfo.getiRImageAddress()).placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
                        }
                    }
                }
            }catch (Exception e){

            }
        }




     //   Glide.with(this).load("http://27.223.18.10:2018" + fireInfo.getiRImageAddress()).into(huodianTwoImage);

        fireInfoListDialog.hide();
        fireDialog.show();
    }

    @Override
    public void onJsFireInfoClickListener(String id) {

        Message message = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putInt("what",DIALOG_FIRE_SHOW);
        message.setData(b);
        fireHandler.sendMessage(message);

        //mHandler.sendEmptyMessage(DIALOG_FIRE_SHOW);
    }

    /**
     * map加载请求错误的回调
     */
    @Override
    public void onMapRequestErrorListener() {
       // Log.e(TAG, "onMapRequestErrorListener: shou8daosdhasoudhasodho");
        Message message = fireHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("what",MAP_ERROR_SHOW);
        message.setData(b);
        fireHandler.sendMessage(message);
    }

    /**
     * 火点加载请求错误的回调
     */
    @Override
    public void onQuearFireErrorListener() {
        Message message = fireHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("what",FIRE_ERROR_SHOW);
        message.setData(b);
        fireHandler.sendMessage(message);
    }

    /**
     * 地面火警点击事件
     * @param groundFire
     */
    @Override
    public void onGroundFireInfoClick(GroundFire groundFire) {
        showDialogProgress(gaojiFindDialog,"加载中...");
        currentGroundFireID = groundFire.getAlarmID();
        getAddressData(groundFire);
    }

    private void getAddressData(final GroundFire groundFire) {
        String url = "http://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" +groundFire.getAlarmLongitude()+ "," +groundFire.getAlarmLatitude() +"&key=9432225bac2c179002b3531e31a85410&radius=0&extensions=base";
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(10000);
        Log.e(TAG, "getSyncPlotListData: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    String formatted_address = regeocode.getString("formatted_address");

                    groundAddressView.setText(formatted_address);
                    groundLngView.setText(groundFire.getAlarmLongitude());
                    groundLatView.setText(groundFire.getAlarmLatitude());
                    groundDataView.setText(groundFire.getAlarmDateTime());
                    Glide.with(getApplicationContext()).load(groundFire.getPicPath1()).into(groundOneImageView);
                    Glide.with(getApplicationContext()).load(groundFire.getPicPath2()).into(groundTwoImageView);
                    Log.e(TAG, "地面huojing : " + groundFire.getIsDispose() );
                    if (groundFire.getIsDispose().equals("1")){
                        groundQuerenButton.setVisibility(View.GONE);
                        groundHulueButton.setVisibility(View.GONE);
                    }else {
                        groundQuerenButton.setVisibility(View.VISIBLE);
                        groundHulueButton.setVisibility(View.VISIBLE);
                    }
                    groundFireListDialog.dismiss();
                    groundFireInfoDialog.show();
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
                gaojiFindDialog.hide();
            }
        });
    }


    /**
     * js上点击地图上火警点回调
     * @param id
     *//*
    @Override
    public void onJsFireInfoClickListener(String id) {


    }*/

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
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.hht.hsatellitemobile.fileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(tempUri, "application/vnd.android.package-archive");
            cleanTags();
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cleanTags();
            startActivity(install);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        DbConfig dbConfig = new DbConfig(this);
        User user = dbConfig.getUser();
        user.setJpush(false);
        user.setJpushStr("");
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
        unregisterReceiver(dynamicReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: zoule");
        /*timer.cancel();*/ //todo 230403 轮询去除
//        unregisterReceiver(fireClickReceiver);
       // unregisterReceiver(outLoginReceiver);
    }

    //通过继承 BroadcastReceiver建立动态广播接收器
    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
         //   Log.e(TAG, "onReceive: 收到广播");
            warnImage.setVisibility(View.VISIBLE);
            String fireInfoStr = intent.getStringExtra("jpush_fire");
            Log.e(TAG, "jpush2" );
            JSONObject fireObj = null;
            try {
                fireObj = new JSONObject(fireInfoStr);
                String observationDateTime = fireObj.getString("ObservationDatetime");


                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date1 = df.parse(df1.format(new Date()));
                Date date2 = df.parse(observationDateTime);
                long diff = date1.getTime() - date2.getTime();
                long hour = diff / 1000/(60*60);
                Log.e(TAG, "onCreate: hour--" + hour);//根据时间去查询之前的火点跟地图上的火点
                currentFireFindTime = (int) hour + 1;
                dWebView.callHandler("huodian", new Object[]{currentFireFindTime},new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        getFireFromService(currentFireFindTime);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }


    //通过继承 BroadcastReceiver建立动态广播接收器
    class OutLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: MainActivity销毁了");
            MainActivity.this.onDestroy();
        }
    }
    //通过继承 BroadcastReceiver建立动态广播接收器
    class FireClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "jpush 从通知栏打开火警信息");
         //   Log.e(TAG, "onReceive: MainActivity销毁了");
          //  MainActivity.this.onDestroy();
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            User user = dbConfig.getUser();
            user.setJpush(true);
            DbManager db = dbConfig.getDbManager();
            try {
                db.saveOrUpdate(user);
            } catch (DbException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "jpush1" );
            String fireStr = intent.getStringExtra("fire_id");
            Log.e(TAG, "jpush1: "+fireStr);
            JpsuhFireInfo(fireStr);
        }
    }
}
