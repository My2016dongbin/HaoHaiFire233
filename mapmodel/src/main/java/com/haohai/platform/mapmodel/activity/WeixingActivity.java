package com.haohai.platform.mapmodel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.mapmodel.R;
import com.ruyiruyi.rylibrary.base.FullBaseActivity;
import com.ruyiruyi.rylibrary.db.Setting;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.Area;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class WeixingActivity extends FullBaseActivity {
    private static final String TAG = WeixingActivity.class.getSimpleName();
    private TextView outLoginButton;
    private FrameLayout ziqidongLayout;
    private ActionBar actionBar;


    private AlertDialog.Builder builder;
    private ComponentName componentName = null;
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
    private TextView chixubaojing_view;
    public boolean weixingAllChoose = true;
    public boolean weixingNPPChoose = true;
    public boolean weixingFY4Choose = true;
    public boolean weixingFY3Choose = true;
    public boolean weixingHIMA8Choose = true;
    public boolean weixingNOAA18Choose = true;
    public boolean weixingNOAA19Choose = true;

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
    public boolean ischixu = false;
    public boolean isChooseJingwai = false;
    public boolean isChooseHuanchong = false;
    private TextView chongzhiButton;
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
    private Switch yuyinSwitch;

    //获取手机类型 ceshi
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixing);


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
    }

    private void initView() {
        yuyinSwitch = (Switch) findViewById(R.id.yuyin_switch);
        User user = new DbConfig(this).getUser();
        int isyunyin = user.getIsyunyin();
        if (isyunyin == 1){
            yuyinSwitch.setChecked(true);
        }else {
            yuyinSwitch.setChecked(false);
        }

        baojingSettingLayout = (FrameLayout) findViewById(R.id.baojing_setting_layout);
        gaojiDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        gaojiInflater = LayoutInflater.from(this).inflate(R.layout.dialog_setting_weixing,null);
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

        //报警设置点击
        RxViewAction.clickNoDouble(baojingSettingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        gaojiDialog.show();
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


        chixubaojing_view = ((TextView) gaojiInflater.findViewById(R.id.chixubaojing_view));

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
        RxViewAction.clickNoDouble(chixubaojing_view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (ischixu){
                            ischixu = false;
                            chixubaojing_view.setBackgroundResource(R.drawable.bg_text_hui);
                            chixubaojing_view.setTextColor(getResources().getColor(R.color.c6));
                        }else {
                            ischixu = true;
                            chixubaojing_view.setBackgroundResource(R.drawable.bg_text_lan);
                            chixubaojing_view.setTextColor(getResources().getColor(R.color.c12));
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
                        Toast.makeText(WeixingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
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

                        weixingAllChoose = true;
                        weixingNPPChoose = true;
                        weixingFY3Choose = true;
                        weixingFY4Choose = true;
                        weixingHIMA8Choose = true;
                        weixingNOAA19Choose = true;
                        weixingNOAA18Choose = true;


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

                        //区域重置
                        currentChooseSheng = "";
                        currentChooseShi = "";
                        shengSelectIndex = 0;
                        shiSelectIndex = 0;
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
                        ischixu = false;
                        chixubaojing_view.setBackgroundResource(R.drawable.bg_text_hui);
                        chixubaojing_view.setTextColor(getResources().getColor(R.color.c6));
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
                                Toast.makeText(WeixingActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
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
                            //  getShengAre(new DbConfig(getApplicationContext()).getUser().getProvinceNo());
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

                        }else {
                            weixingAllChoose = true;
                            weixingNPPChoose = true;
                            weixingFY3Choose = true;
                            weixingFY4Choose = true;
                            weixingHIMA8Choose = true;
                            weixingNOAA19Choose = true;
                            weixingNOAA18Choose = true;
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
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
                                    || !weixingNOAA18Choose || !weixingNOAA19Choose){   //判断全部未选中
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
                            if (weixingNPPChoose && weixingFY3Choose && weixingFY4Choose && weixingHIMA8Choose && weixingNOAA18Choose && weixingNOAA19Choose){
                                weixingAllChoose = true;
                                //           weixingAllImage.setImageResource(R.drawable.choose);
                                weixingAllText.setBackgroundResource(R.drawable.bg_text_lan);
                                weixingAllText.setTextColor(getResources().getColor(R.color.c12));
                            }
                        }
                    }
                });


    }

    private void showDataDialog() {

    }

    private void getShengAre(String id) {

    }

    private void getAllAre() {

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
            if (weixingNPPChoose || weixingFY3Choose || weixingFY4Choose || weixingHIMA8Choose || weixingNOAA18Choose || weixingNOAA19Choose){
                satellite.substring(1,satellite.length());
            }

        }


        //地貌
        String dimaoStr = "";
        if (dimaoAllChoose){
            dimaoStr = "ALL";
        }else {
            if (dimaoLindiChoose){
                dimaoStr = dimaoStr + ",林地";
            }
            if (dimaoCaodiChoose){
                dimaoStr = dimaoStr + ",草地";
            }
            if (dimaoNongtianChoose){
                dimaoStr = dimaoStr + ",农田";
            }
            if (dimaoQitaChoose){
                dimaoStr = dimaoStr + ",其他";
            }
            if (dimaoLindiChoose || dimaoCaodiChoose || dimaoNongtianChoose || dimaoQitaChoose){
                dimaoStr.substring(1,dimaoStr.length());
            }

        }

        Setting setting;
        if (new DbConfig(this).getSetting()==null){
             setting=new Setting();
        }else {
             setting = new DbConfig(this).getSetting();
        }
        setting.setWeixing(satellite);
        setting.setTiankong("");
        setting.setDimian("");
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
}
