package com.hht.hsatellitemobile.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.db.model.Setting;
import com.hht.hsatellitemobile.db.model.User;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;
import com.hht.hsatellitemobile.db.model.Area;
import com.hht.hsatellitemobile.ui.multitype.FireInfo;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rx.functions.Action1;

import static com.hht.hsatellitemobile.ui.activity.LoginActivity.yingjiTags;

public class SettingActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener{

    private static final String TAG = SettingActivity.class.getSimpleName();
    private TextView outLoginButton;
    private FrameLayout ziqidongLayout;
    private ActionBar actionBar;

    private AlertDialog.Builder builder;
    private ComponentName componentName = null;
    private Switch yuyinSwitch;
    private FrameLayout baojingSettingLayout;
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
    private LinearLayout weixingNOAA18Layout;
    private ImageView weixingNOAA18Image;
    private LinearLayout weixingNOAA19Layout;
    private ImageView weixingNOAA19Image;
    private LinearLayout weixingNOAA15Layout;
    private LinearLayout weixingNOAA20Layout;
    private LinearLayout weixingMODISLayout;
    private LinearLayout weixingGK2aLayout;
    public boolean weixingAllChoose = true;
    public boolean weixingNPPChoose = true;
    public boolean weixingFY4Choose = true;
    public boolean weixingFY3Choose = true;
    public boolean weixingHIMA8Choose = true;
    public boolean weixingNOAA18Choose = true;
    public boolean weixingNOAA19Choose = true;
    public boolean weixingNOAA15Choose = true;
    public boolean weixingNOAA20Choose = true;
    public boolean weixingMODISChoose = true;
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
    public List<String> shiStrList;
    private WheelView areaWy;
    public String companyName = "";
    public String provinceNo = "";
    public String provinceName = "";
    public String cityNo = "";
    public String cityName = "";
    public String countyNo = "";
    public String countyName = "";
    public int currentQuanxian = 0;   //0是全国权限  1是省级权限
    public int currentChooseArea = 0;  //当前在选择省还是市   0选择省  1选择市
    public String currentChooseSheng = "";
    public String currentChooseShi = "";
    public int shengSelectIndex = 0;
    public int shiSelectIndex = 0;
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
    private TextView findButton;
    private ProgressDialog gaojiFindDialog;
    private TextView weixingAllText;
    private TextView weixingFY3Text;
    private TextView weixingFY4Text;
    private TextView weixingNppText;
    private TextView weixingHima8Text;
    private TextView weixingNoaa18Text;
    private TextView weixingNOAA19Te;
    private TextView weixingNOAA15Te;
    private TextView weixingNOAA20Te;
    private TextView weixingMODISTe;
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
    private TextView jingwaiText;
    private Button chaoshiButton;
    private TextView yibaiText;
    private TextView wubaiText;
    private TextView yiqianText;
    private TextView liangqianText;
    private TextView wuqianText;
    public String currentNum = "100";
    private FrameLayout addFireLayout;
    private TextView ziqidongView;

    //获取手机类型
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("设置");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        shengStrList = new ArrayList<>();
        shiList = new ArrayList<>();

        initView();
        initSettingView();
    }

    private void initSettingView() {
        Setting setting = new DbConfig(this).getSetting();
        String weixing = setting.getWeixing();
        String tiankong = setting.getTiankong();
        String dimian = setting.getDimian();
        String dimao = setting.getDimao();
        String number = setting.getNumber();
        String jingwai = setting.getJingwai();
        String huanchong = setting.getHuanchong();
        Log.e(TAG, "initSettingView: weixing" +weixing );
        Log.e(TAG, "initSettingView: tiankong" +tiankong);
        Log.e(TAG, "initSettingView: dimian" +dimian);
        Log.e(TAG, "initSettingView: dimao" +dimao);
        Log.e(TAG, "initSettingView: number " +number);
        Log.e(TAG, "initSettingView: jingwai " +jingwai);
        Log.e(TAG, "initSettingView: nuhuanchongmber " + huanchong);
        if (weixing.equals("ALL")){
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
            weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_lan);
            weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c12));
            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
            weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_lan);
            weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c12));
            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
            weixingMODISTe.setBackgroundResource(R.drawable.bg_text_lan);
            weixingMODISTe.setTextColor(getResources().getColor(R.color.c12));
            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));

            weixingAllChoose = true;
            weixingNPPChoose = true;
            weixingFY4Choose = true;
            weixingFY3Choose = true;
            weixingHIMA8Choose = true;
            weixingNOAA19Choose = true;
            weixingNOAA18Choose = true;
            weixingNOAA15Choose = true;
            weixingNOAA20Choose = true;
            weixingMODISChoose = true;
            weixingGK2aChoose = true;
        }else {
            weixingAllChoose = false;
            weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
            weixingAllText.setTextColor(getResources().getColor(R.color.c6));
            if (weixing.indexOf("NPP") != -1){
                weixingNppText.setBackgroundResource(R.drawable.bg_text_lan);
                weixingNppText.setTextColor(getResources().getColor(R.color.c12));
                weixingNPPChoose = true;
            }else {
                weixingNppText.setBackgroundResource(R.drawable.bg_text_hui);
                weixingNppText.setTextColor(getResources().getColor(R.color.c6));
                weixingNPPChoose = false;
            }

            if (weixing.indexOf("FY-4") != -1){
                weixingFY4Text.setBackgroundResource(R.drawable.bg_text_lan);
                weixingFY4Text.setTextColor(getResources().getColor(R.color.c12));
                weixingFY4Choose = true;
            }else {
                weixingFY4Text.setBackgroundResource(R.drawable.bg_text_hui);
                weixingFY4Text.setTextColor(getResources().getColor(R.color.c6));
                weixingFY4Choose = false;
            }

            if (weixing.indexOf("FY-3") != -1){
                weixingFY3Text.setBackgroundResource(R.drawable.bg_text_lan);
                weixingFY3Text.setTextColor(getResources().getColor(R.color.c12));
                weixingFY3Choose = true;
            }else {
                weixingFY3Text.setBackgroundResource(R.drawable.bg_text_hui);
                weixingFY3Text.setTextColor(getResources().getColor(R.color.c6));
                weixingFY3Choose = false;
            }

            if (weixing.indexOf("Himawari-8") != -1){
                weixingHima8Text.setBackgroundResource(R.drawable.bg_text_lan);
                weixingHima8Text.setTextColor(getResources().getColor(R.color.c12));
                weixingHIMA8Choose = true;
            }else {
                weixingHima8Text.setBackgroundResource(R.drawable.bg_text_hui);
                weixingHima8Text.setTextColor(getResources().getColor(R.color.c6));
                weixingHIMA8Choose = false;
            }

            if (weixing.indexOf("NOAA-18") != -1){
                weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_lan);
                weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c12));
                weixingNOAA18Choose = true;
            }else {
                weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_hui);
                weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c6));
                weixingNOAA18Choose = false;
            }

            if (weixing.indexOf("NOAA-19") != -1){
                weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                weixingNOAA19Choose = true;
            }else {
                weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_hui);
                weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c6));
                weixingNOAA19Choose = false;
            }
            if (weixing.indexOf("NOAA-15") != -1){
                weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_lan);
                weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c12));
                weixingNOAA15Choose = true;
            }else {
                weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_hui);
                weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c6));
                weixingNOAA15Choose = false;
            }
            if (weixing.indexOf("NOAA-20") != -1){
                weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                weixingNOAA20Choose = true;
            }else {
                weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_hui);
                weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c6));
                weixingNOAA20Choose = false;
            }
            if (weixing.indexOf("MODIS") != -1){
                weixingMODISTe.setBackgroundResource(R.drawable.bg_text_lan);
                weixingMODISTe.setTextColor(getResources().getColor(R.color.c12));
                weixingMODISChoose = true;
            }else {
                weixingMODISTe.setBackgroundResource(R.drawable.bg_text_hui);
                weixingMODISTe.setTextColor(getResources().getColor(R.color.c6));
                weixingMODISChoose = false;
            }
            if (weixing.indexOf("GK2a") != -1){
                weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));
                weixingGK2aChoose = true;
            }else {
                weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_hui);
                weixingGK2aTe.setTextColor(getResources().getColor(R.color.c6));
                weixingGK2aChoose = false;
            }
        }

        if (tiankong.equals("ALL")){
            tiankongAllText.setBackgroundResource(R.drawable.bg_text_lan);
            tiankongAllText.setTextColor(getResources().getColor(R.color.c12));
            tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_lan);
            tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c12));
            tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_lan);
            tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c12));
            tiankongAllChoose = true;
            tiankongXuancifuChoose = true;
            tiankongWurenjiChoose = true;
        }else {
            tiankongAllChoose = false;
            tiankongAllText.setBackgroundResource(R.drawable.bg_text_hui);
            tiankongAllText.setTextColor(getResources().getColor(R.color.c6));
            if (tiankong.indexOf("无人机") != -1){
                tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_lan);
                tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c12));
                tiankongWurenjiChoose = true;
            }else {
                tiankongWurenjiText.setBackgroundResource(R.drawable.bg_text_hui);
                tiankongWurenjiText.setTextColor(getResources().getColor(R.color.c6));
                tiankongWurenjiChoose = false;
            }

            if (tiankong.indexOf("悬浮器") != -1){
                tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_lan);
                tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c12));
                tiankongXuancifuChoose = true;
            }else {
                tiankongXuanfuqiText.setBackgroundResource(R.drawable.bg_text_hui);
                tiankongXuanfuqiText.setTextColor(getResources().getColor(R.color.c6));
                tiankongXuancifuChoose = false;
            }
        }
        if (dimian.equals("ALL")){
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

            dimianAllChoose = true;
            dimianSheyingChoose = true;
            dimianHulinChoose = true;
            dimianLiaowangChoose = true;
            dimianQunzhongChoose = true;
        }else {
            dimianAllChoose = false;
            dimianAllText.setBackgroundResource(R.drawable.bg_text_hui);
            dimianAllText.setTextColor(getResources().getColor(R.color.c6));
            if (dimian.indexOf("摄像机") != -1){
                dimianSheyingText.setBackgroundResource(R.drawable.bg_text_lan);
                dimianSheyingText.setTextColor(getResources().getColor(R.color.c12));
                dimianSheyingChoose = true;
            }else {
                dimianSheyingText.setBackgroundResource(R.drawable.bg_text_hui);
                dimianSheyingText.setTextColor(getResources().getColor(R.color.c6));
                dimianSheyingChoose = false;
            }

            if (dimian.indexOf("护林员") != -1){
                dimianHulinText.setBackgroundResource(R.drawable.bg_text_lan);
                dimianHulinText.setTextColor(getResources().getColor(R.color.c12));
                dimianHulinChoose = true;
            }else {
                dimianHulinText.setBackgroundResource(R.drawable.bg_text_hui);
                dimianHulinText.setTextColor(getResources().getColor(R.color.c6));
                dimianHulinChoose = false;
            }

            if (dimian.indexOf("瞭望员") != -1){
                dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_lan);
                dimianLiaowangText.setTextColor(getResources().getColor(R.color.c12));
                dimianLiaowangChoose = true;
            }else {
                dimianLiaowangText.setBackgroundResource(R.drawable.bg_text_hui);
                dimianLiaowangText.setTextColor(getResources().getColor(R.color.c6));
                dimianLiaowangChoose = false;
            }

            if (dimian.indexOf("群众") != -1){
                dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_lan);
                dimianQunzhongText.setTextColor(getResources().getColor(R.color.c12));
                dimianQunzhongChoose = true;
            }else {
                dimianQunzhongText.setBackgroundResource(R.drawable.bg_text_hui);
                dimianQunzhongText.setTextColor(getResources().getColor(R.color.c6));
                dimianQunzhongChoose = false;
            }
        }

        if (dimao.equals("ALL")){
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
        }else {
            dimaoAllChoose = false;
            dimaoAllText.setBackgroundResource(R.drawable.bg_text_hui);
            dimaoAllText.setTextColor(getResources().getColor(R.color.c6));
            if (dimao.indexOf("林地") != -1){
                dimaoLindiText.setBackgroundResource(R.drawable.bg_text_lan);
                dimaoLindiText.setTextColor(getResources().getColor(R.color.c12));
                dimaoLindiChoose = true;
            }else {
                dimaoLindiText.setBackgroundResource(R.drawable.bg_text_hui);
                dimaoLindiText.setTextColor(getResources().getColor(R.color.c6));
                dimaoLindiChoose = false;
            }

            if (dimao.indexOf("草地") != -1){
                dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_lan);
                dimaoCaodiText.setTextColor(getResources().getColor(R.color.c12));
                dimaoCaodiChoose = true;
            }else {
                dimaoCaodiText.setBackgroundResource(R.drawable.bg_text_hui);
                dimaoCaodiText.setTextColor(getResources().getColor(R.color.c6));
                dimaoCaodiChoose = false;
            }

            if (dimao.indexOf("农田") != -1){
                dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_lan);
                dimaoNongtianText.setTextColor(getResources().getColor(R.color.c12));
                dimaoNongtianChoose = true;
            }else {
                dimaoNongtianText.setBackgroundResource(R.drawable.bg_text_hui);
                dimaoNongtianText.setTextColor(getResources().getColor(R.color.c6));
                dimaoNongtianChoose = false;
            }

            if (dimao.indexOf("其他") != -1){
                dimaoQitaText.setBackgroundResource(R.drawable.bg_text_lan);
                dimaoQitaText.setTextColor(getResources().getColor(R.color.c12));
                dimaoQitaChoose = true;
            }else {
                dimaoQitaText.setBackgroundResource(R.drawable.bg_text_hui);
                dimaoQitaText.setTextColor(getResources().getColor(R.color.c6));
                dimaoQitaChoose = false;
            }
        }

        if (number.equals("100")){
            currentNum = "100";
            yibaiText.setBackgroundResource(R.drawable.bg_text_lan);
            yibaiText.setTextColor(getResources().getColor(R.color.c12));
            wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
            wubaiText.setTextColor(getResources().getColor(R.color.c6));
            yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
            yiqianText.setTextColor(getResources().getColor(R.color.c6));
            liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
            liangqianText.setTextColor(getResources().getColor(R.color.c6));
            wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
            wuqianText.setTextColor(getResources().getColor(R.color.c6));
        }else if (number.equals("500")){
            currentNum = "500";
            yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
            yibaiText.setTextColor(getResources().getColor(R.color.c6));
            wubaiText.setBackgroundResource(R.drawable.bg_text_lan);
            wubaiText.setTextColor(getResources().getColor(R.color.c12));
            yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
            yiqianText.setTextColor(getResources().getColor(R.color.c6));
            liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
            liangqianText.setTextColor(getResources().getColor(R.color.c6));
            wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
            wuqianText.setTextColor(getResources().getColor(R.color.c6));
        }else if (number.equals("1000")){
            currentNum = "1000";
            yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
            yibaiText.setTextColor(getResources().getColor(R.color.c6));
            wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
            wubaiText.setTextColor(getResources().getColor(R.color.c6));
            yiqianText.setBackgroundResource(R.drawable.bg_text_lan);
            yiqianText.setTextColor(getResources().getColor(R.color.c12));
            liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
            liangqianText.setTextColor(getResources().getColor(R.color.c6));
            wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
            wuqianText.setTextColor(getResources().getColor(R.color.c6));
        }else if (number.equals("2000")){
            currentNum = "2000";
            yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
            yibaiText.setTextColor(getResources().getColor(R.color.c6));
            wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
            wubaiText.setTextColor(getResources().getColor(R.color.c6));
            yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
            yiqianText.setTextColor(getResources().getColor(R.color.c6));
            liangqianText.setBackgroundResource(R.drawable.bg_text_lan);
            liangqianText.setTextColor(getResources().getColor(R.color.c12));
            wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
            wuqianText.setTextColor(getResources().getColor(R.color.c6));
        }else if (number.equals("5000")){
            currentNum = "5000";
            yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
            yibaiText.setTextColor(getResources().getColor(R.color.c6));
            wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
            wubaiText.setTextColor(getResources().getColor(R.color.c6));
            yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
            yiqianText.setTextColor(getResources().getColor(R.color.c6));
            liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
            liangqianText.setTextColor(getResources().getColor(R.color.c6));
            wuqianText.setBackgroundResource(R.drawable.bg_text_lan);
            wuqianText.setTextColor(getResources().getColor(R.color.c12));
        }

        if (jingwai.equals("中国")){
            jingwaiText.setBackgroundResource(R.drawable.bg_text_hui);
            jingwaiText.setTextColor(getResources().getColor(R.color.c6));
        }else {
            jingwaiText.setBackgroundResource(R.drawable.bg_text_lan);
            jingwaiText.setTextColor(getResources().getColor(R.color.c12));
        }

        if (huanchong.equals("0")){
            huanchongText.setBackgroundResource(R.drawable.bg_text_hui);
            huanchongText.setTextColor(getResources().getColor(R.color.c6));
        }else {
            huanchongText.setBackgroundResource(R.drawable.bg_text_lan);
            huanchongText.setTextColor(getResources().getColor(R.color.c12));
        }
    }

    private void initView() {
        ziqidongView = (TextView) findViewById(R.id.ziqidong_view);

        SpannableString spannableString = new SpannableString("开启自启动权限(更及时得接到推送消息)");
        //设置颜色
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c5)), 7,19 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小，true表示前面的字体大小20单位为dip
        spannableString.setSpan(new AbsoluteSizeSpan(11, true), 7, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ziqidongView.setText(spannableString);


        addFireLayout = (FrameLayout) findViewById(R.id.add_fire_layout);
        outLoginButton = (TextView) findViewById(R.id.out_login_but);
        ziqidongLayout = (FrameLayout) findViewById(R.id.ziqidong_layout);
        yuyinSwitch = (Switch) findViewById(R.id.yuyin_switch);
        baojingSettingLayout = (FrameLayout) findViewById(R.id.baojing_setting_layout);
        User user = new DbConfig(this).getUser();
        int isyunyin = user.getIsyunyin();
        if (isyunyin == 1){
            yuyinSwitch.setChecked(true);
        }else {
            yuyinSwitch.setChecked(false);
        }

        gaojiDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        gaojiInflater = LayoutInflater.from(this).inflate(R.layout.dialog_setting,null);
        gaojiInflater.setMinimumWidth(10000);
        gaojiDialog.setContentView(gaojiInflater);
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


        RxViewAction.clickNoDouble(addFireLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),AddFireActivity.class));
                    }
                });

        //报警设置点击
        RxViewAction.clickNoDouble(baojingSettingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });

        yuyinSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User user = new DbConfig(getApplicationContext()).getUser();
                Log.e(TAG, "onCheckedChanged: "+user.getIsyunyin());
                if (isChecked){
                    Log.e(TAG, "onCheckedChanged: setting1");
                    user.setIsyunyin(1);
                }else {
                    Log.e(TAG, "onCheckedChanged: setting0");
                    user.setIsyunyin(0);
                }
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                DbManager db = dbConfig.getDbManager();
                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

        RxViewAction.clickNoDouble(baojingSettingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: 报警设置");
                        gaojiDialog.show();
                    }
                });


        RxViewAction.clickNoDouble(ziqidongLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: 其自动");
                        startZiqidong();
                    }
                });

        RxViewAction.clickNoDouble(outLoginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        outLoginDialog("您确定要退出么？");
                    }
                });
    }

    private void initGaojiView() {
        //数量
        yibaiText = ((TextView) gaojiInflater.findViewById(R.id.yibai_text));
        wubaiText = ((TextView) gaojiInflater.findViewById(R.id.wubai_text));
        yiqianText = ((TextView) gaojiInflater.findViewById(R.id.yiqian_text));
        liangqianText = ((TextView) gaojiInflater.findViewById(R.id.liangqian_text));
        wuqianText = ((TextView) gaojiInflater.findViewById(R.id.wuqian_text));
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
        weixingNOAA18Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa18_layout));
        //  weixingNOAA18Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_noaa18_image));
        weixingNoaa18Text = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa18_text));
        weixingNOAA19Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa19_layout));
        //   weixingNOAA19Image = ((ImageView) gaojiInflater.findViewById(R.id.weixing_noaa19_image));
        weixingNOAA19Te = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa19_text));
        weixingNOAA15Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa15_layout));
        weixingNOAA15Te = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa15_text));
        weixingNOAA20Layout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_noaa20_layout));
        weixingNOAA20Te = ((TextView) gaojiInflater.findViewById(R.id.weixing_noaa20_text));
        weixingMODISLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_modis_layout));
        weixingMODISTe = ((TextView) gaojiInflater.findViewById(R.id.weixing_modis_text));
        weixingGK2aLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.weixing_gk2a_layout));
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
         * 数量的点击
         */
        RxViewAction.clickNoDouble(yibaiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentNum = "100";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_lan);
                        yibaiText.setTextColor(getResources().getColor(R.color.c12));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaiText.setTextColor(getResources().getColor(R.color.c6));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        yiqianText.setTextColor(getResources().getColor(R.color.c6));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        liangqianText.setTextColor(getResources().getColor(R.color.c6));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        wuqianText.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(wubaiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentNum = "500";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        yibaiText.setTextColor(getResources().getColor(R.color.c6));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_lan);
                        wubaiText.setTextColor(getResources().getColor(R.color.c12));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        yiqianText.setTextColor(getResources().getColor(R.color.c6));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        liangqianText.setTextColor(getResources().getColor(R.color.c6));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        wuqianText.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(yiqianText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentNum = "1000";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        yibaiText.setTextColor(getResources().getColor(R.color.c6));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaiText.setTextColor(getResources().getColor(R.color.c6));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_lan);
                        yiqianText.setTextColor(getResources().getColor(R.color.c12));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        liangqianText.setTextColor(getResources().getColor(R.color.c6));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        wuqianText.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(liangqianText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentNum = "2000";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        yibaiText.setTextColor(getResources().getColor(R.color.c6));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaiText.setTextColor(getResources().getColor(R.color.c6));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        yiqianText.setTextColor(getResources().getColor(R.color.c6));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_lan);
                        liangqianText.setTextColor(getResources().getColor(R.color.c12));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        wuqianText.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(wuqianText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentNum = "5000";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        yibaiText.setTextColor(getResources().getColor(R.color.c6));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaiText.setTextColor(getResources().getColor(R.color.c6));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        yiqianText.setTextColor(getResources().getColor(R.color.c6));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        liangqianText.setTextColor(getResources().getColor(R.color.c6));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_lan);
                        wuqianText.setTextColor(getResources().getColor(R.color.c12));
                    }
                });

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
                     //   findFirePost();
                        gaojiDialog.hide();
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        updateSetting();
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
                        weixingNOAA18Image.setImageResource(R.drawable.choose);
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
                        weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c12));
                        weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                        weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c12));
                        weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                        weixingMODISTe.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingMODISTe.setTextColor(getResources().getColor(R.color.c12));
                        weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                        weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));

                        weixingAllChoose = true;
                        weixingNPPChoose = true;
                        weixingFY3Choose = true;
                        weixingFY4Choose = true;
                        weixingHIMA8Choose = true;
                        weixingNOAA19Choose = true;
                        weixingNOAA18Choose = true;
                        weixingNOAA15Choose = true;
                        weixingNOAA20Choose = true;
                        weixingMODISChoose = true;
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
                        shengSelectIndex = 0;
                        shiSelectIndex = 0;
                        isChooseSheng = false;
                        shengText.setText("请选择省");
                        shiText.setText("请选择市");

                        currentNum = "100";
                        yibaiText.setBackgroundResource(R.drawable.bg_text_lan);
                        yibaiText.setTextColor(getResources().getColor(R.color.c12));
                        wubaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaiText.setTextColor(getResources().getColor(R.color.c6));
                        yiqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        yiqianText.setTextColor(getResources().getColor(R.color.c6));
                        liangqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        liangqianText.setTextColor(getResources().getColor(R.color.c6));
                        wuqianText.setBackgroundResource(R.drawable.bg_text_hui);
                        wuqianText.setTextColor(getResources().getColor(R.color.c6));

                        //初始话境外
                        isChooseJingwai = false;
                        isChooseHuanchong = false;
                        huanchongText.setBackgroundResource(R.drawable.bg_text_hui);
                        huanchongText.setTextColor(getResources().getColor(R.color.c6));
                        jingwaiText.setBackgroundResource(R.drawable.bg_text_hui);
                        jingwaiText.setTextColor(getResources().getColor(R.color.c6));
                        /*jingwaiImage.setImageResource(R.drawable.choose_no);
                        huanChongImage.setImageResource(R.drawable.choose_no);*/
                    }
                });



        /**
         * 省市的点击
         */
        RxViewAction.clickNoDouble(shengText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                        getAllAre();
                    }
                });
        RxViewAction.clickNoDouble(shiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: shi");
                        currentChooseArea = 1;
                        String id = "";
                        if (currentQuanxian == 0){
                            if (shengText.getText().toString().equals("请选择省")){
                                Toast.makeText(SettingActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
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
                            weixingNOAA18Choose = false;
                            weixingNOAA15Choose = false;
                            weixingNOAA20Choose = false;
                            weixingMODISChoose = false;
                            weixingGK2aChoose = false;
/*                            weixingAllImage.setImageResource(R.drawable.choose_no);
                            weixingNPPImage.setImageResource(R.drawable.choose_no);
                            weixingFY3Image.setImageResource(R.drawable.choose_no);
                            weixingFY4Image.setImageResource(R.drawable.choose_no);
                            weixingHima8Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA18Image.setImageResource(R.drawable.choose_no);
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
                            weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c6));
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c6));
                            weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c6));
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c6));
                            weixingMODISTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingMODISTe.setTextColor(getResources().getColor(R.color.c6));
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c6));

                        }else {
                            weixingAllChoose = true;
                            weixingNPPChoose = true;
                            weixingFY3Choose = true;
                            weixingFY4Choose = true;
                            weixingHIMA8Choose = true;
                            weixingNOAA19Choose = true;
                            weixingNOAA18Choose = true;
                            weixingNOAA15Choose = true;
                            weixingNOAA20Choose = true;
                            weixingMODISChoose = true;
                            weixingGK2aChoose = true;
                         /*   weixingAllImage.setImageResource(R.drawable.choose);
                            weixingNPPImage.setImageResource(R.drawable.choose);
                            weixingFY3Image.setImageResource(R.drawable.choose);
                            weixingFY4Image.setImageResource(R.drawable.choose);
                            weixingHima8Image.setImageResource(R.drawable.choose);
                            weixingNOAA18Image.setImageResource(R.drawable.choose);
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
                            weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c12));
                            weixingNOAA19Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA19Te.setTextColor(getResources().getColor(R.color.c12));
                            weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c12));
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                            weixingMODISTe.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingMODISTe.setTextColor(getResources().getColor(R.color.c12));
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
                                weixingAllChoose = true;
                                //   weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });

        RxViewAction.clickNoDouble(weixingNOAA18Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingNOAA18Choose){  //从已选中变为未选中
                            weixingNOAA18Choose = false;
                            //     weixingNOAA18Image.setImageResource(R.drawable.choose_no);
                            weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                                //       weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {
                            weixingNOAA18Choose = true;
                            //   weixingNOAA18Image.setImageResource(R.drawable.choose);
                            weixingNoaa18Text.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNoaa18Text.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
                                weixingAllChoose = true;
                                //      weixingAllImage.setImageResource(R.drawable.choose);
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
                                weixingAllChoose = true;
                                //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(weixingNOAA15Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingNOAA15Choose){  //从已选中变为未选中
                            weixingNOAA15Choose = false;
                            //      weixingNOAA15Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                                //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingNOAA15Choose = true;
                            //    weixingNOAA15Image.setImageResource(R.drawable.choose);
                            weixingNOAA15Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA15Te.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
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
                            //      weixingNOAA20Image.setImageResource(R.drawable.choose_no);
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                                //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingNOAA20Choose = true;
                            //    weixingNOAA20Image.setImageResource(R.drawable.choose);
                            weixingNOAA20Te.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingNOAA20Te.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
                                weixingAllChoose = true;
                                //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(weixingMODISLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (weixingMODISChoose){  //从已选中变为未选中
                            weixingMODISChoose = false;
                            //      weixingMODISImage.setImageResource(R.drawable.choose_no);
                            weixingMODISTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingMODISTe.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                                //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingMODISChoose = true;
                            //    weixingMODISImage.setImageResource(R.drawable.choose);
                            weixingMODISTe.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingMODISTe.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
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
                            //      weixingGK2aImage.setImageResource(R.drawable.choose_no);
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_hui);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c6));
                            if (!weixingNPPChoose || !weixingFY3Choose || !weixingFY4Choose || !weixingHIMA8Choose
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose || !weixingNOAA15Choose || !weixingNOAA20Choose || !weixingMODISChoose || !weixingGK2aChoose){   //判断全部未选中
                                weixingAllChoose = false;
                                //   weixingAllImage.setImageResource(R.drawable.choose_no);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_hui);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c6));
                            }
                        }else {     //从未选中变为已选中
                            weixingGK2aChoose = true;
                            //    weixingGK2aImage.setImageResource(R.drawable.choose);
                            weixingGK2aTe.setBackgroundResource(R.drawable.bg_text_lan);
                            weixingGK2aTe.setTextColor(getResources().getColor(R.color.c12));
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose && weixingNOAA15Choose && weixingNOAA20Choose && weixingMODISChoose && weixingGK2aChoose){
                                weixingAllChoose = true;
                                //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


    }

    private void updateSetting() {
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
                satellite = satellite + ",Himawari-8";
            }
            if (weixingNOAA18Choose){
                satellite = satellite + ",NOAA-18";
            }
            if (weixingNOAA19Choose){
                satellite = satellite + ",NOAA-19";
            }
            if (weixingNOAA15Choose){
                satellite = satellite + ",NOAA-15";
            }
            if (weixingNOAA20Choose){
                satellite = satellite + ",NOAA-20";
            }
            if (weixingMODISChoose){
                satellite = satellite + ",MODIS";
            }
            if (weixingGK2aChoose){
                satellite = satellite + ",GK2a";
            }
            if (weixingNPPChoose || weixingFY3Choose || weixingFY4Choose || weixingHIMA8Choose || weixingNOAA18Choose || weixingNOAA19Choose || weixingNOAA15Choose || weixingNOAA20Choose || weixingMODISChoose || weixingGK2aChoose){
                satellite.substring(1,satellite.length());
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
                tiankongStr.substring(1,tiankongStr.length());
            }

        }
        //地面
        String dimianStr = "";
        if (dimianAllChoose){
            dimianStr = "ALL";
        }else {
            if (dimianSheyingChoose){
                dimianStr = dimianStr + ",摄像机";
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
                dimianStr.substring(1,dimianStr.length());
            }

        }

        //地貌
        String dimaoStr = "";
        User user = new DbConfig(this).getUser();
        String tagSetStr = user.getTagSet().replace("[","").replace("]","");
        String[] strings = tagSetStr.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (int i = 0; i < strings.length; i++) {
            tagSet.add(strings[i]);
        }
        if (dimaoAllChoose){
            dimaoStr = "ALL";
            tagSet.add("Farmland");
            tagSet.add("Woodland");
            tagSet.add("Grassland");
            tagSet.add("Otherland");
            XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
        }else {
            if (dimaoLindiChoose){
                dimaoStr = dimaoStr + ",林地";
                tagSet.add("Woodland");
            }else{
                XGPushManager.cleanTags(getApplicationContext(),"Woodland");
            }
            if (dimaoCaodiChoose){
                dimaoStr = dimaoStr + ",草地";
                tagSet.add("Grassland");
            }else{
                XGPushManager.cleanTags(getApplicationContext(),"Grassland");
            }
            if (dimaoNongtianChoose){
                dimaoStr = dimaoStr + ",农田";
                tagSet.add("Farmland");
            }else{
                XGPushManager.cleanTags(getApplicationContext(),"Farmland");
            }
            if (dimaoQitaChoose){
                dimaoStr = dimaoStr + ",其他";
                tagSet.add("Otherland");
            }else{
                XGPushManager.cleanTags(getApplicationContext(),"Otherland");
            }
            if (dimaoLindiChoose || dimaoCaodiChoose || dimaoNongtianChoose || dimaoQitaChoose){
                dimaoStr.substring(1,dimaoStr.length());
            }

        }
        XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
        Log.e(TAG, "updateSetting: tagSet " + tagSet );


        Setting setting = new DbConfig(this).getSetting();
        setting.setWeixing(satellite);
        setting.setTiankong(tiankongStr);
        setting.setDimian(dimianStr);
        setting.setDimao(dimaoStr);
        setting.setNumber(currentNum);
        setting.setJingwai(isCountry);
        if (isChooseHuanchong){
            setting.setHuanchong("1");
        }else {
            setting.setHuanchong("0");
        }

        DbConfig dbConfig = new DbConfig(getApplicationContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(setting);
        } catch (DbException e) {
            e.printStackTrace();
        }
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
                    gaojiStartimeText.append("  " + chooseHour + ":" + chooseMinute);
                }else {
                    gaojiEndTimeText.append("  " + chooseHour + ":" + chooseMinute);
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
        int hour = date.get(Calendar.HOUR);
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
        chooseHour = calendar.get(Calendar.HOUR);
        chooseMinute = calendar.get(Calendar.MINUTE);

    }


    /**
     * 跳转到开启自启动的方法
     */
    private void startZiqidong() {
        jumpStartInterface(this);
    }


    //跳转至授权页面
    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }



    public void outLoginDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(msg);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent1= new Intent();
                intent1.setAction("out_login");
                sendBroadcast(intent1);

                cleanTags();
                //JPushInterface.cleanTags(getApplicationContext(),1001);
                //登录失效 更新本地User信息
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                User user = dbConfig.getUser();
                user.setIsLogin("0");
                DbManager db = dbConfig.getDbManager();

                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {

                }
                //即将跳转登录界面
                //  finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }

    private void cleanTags() {
        try{
            String username = new DbConfig(this).getUser().getUsername();
            if(username!=null && username.equals("山东省应急管理厅")){
                String[] tags = yingjiTags.split(",");
                for (int i = 0; i < tags.length; i++) {
                    XGPushManager.cleanTags(this,tags[i]);
                }
            }else{
                XGPushManager.cleanTags(this,new DbConfig(this).getUser().getPushTag());
            }
        }catch (Exception e){
            Log.e("Exception", "cleanTags");
        }
    }

    private void getAllAre() {
        Log.e(TAG, "initArea: -4" );
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
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
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
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

    /**
     * 显示地区选择的dialog
     */
    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, shengSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(strList, shiSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0){   //选择省
                    isChooseSheng = true;
                    currentChooseSheng = areaWy.getSelectedItem();
                    shengSelectIndex = areaWy.getSelectedPosition();
                    shengText.setText(currentChooseSheng);
                }else {                          //选择市
                    currentChooseShi = areaWy.getSelectedItem();
                    shiSelectIndex = areaWy.getSelectedPosition();
                    shiText.setText(currentChooseShi);
                }
                //  currentCity = city.getSelectedPosition();
               /* currentShengPosition = shengWv.getSelectedPosition();
                currentSheng = shengWv.getSelectedItem();
                getShi();
                ;
                shiWv.setItems(shiList, currentShiPosition);
                currentShi = shiWv.getSelectedItem();
                getXian();
                xianWv.setItems(xianList, currentXianPosition);*/
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


    //小米手机显示弹窗
    private void showtip() {
        try {
            // dialog_per=new SettingDialogPermision(context, R.style.CustomDialog4);
            //dialog_per.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);//注意这里改成吐司类型
            //dialog_per.show();
            Log.e("HLQ_Struggle","显示弹窗");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HLQ_Struggle", "没有显示弹窗"+e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
