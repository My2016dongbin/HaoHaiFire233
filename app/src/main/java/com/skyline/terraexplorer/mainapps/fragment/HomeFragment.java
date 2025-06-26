package com.skyline.terraexplorer.mainapps.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.geek.banner.Banner;
import com.geek.banner.loader.BannerEntry;
import com.geek.banner.loader.ImageLoader;
import  com.skyline.terraexplorer.mainapps.MainActivity;
import  com.skyline.terraexplorer.R;
import  com.skyline.terraexplorer.mainapps.ServiceApkInstaller;
import com.skyline.terraexplorer.mainapps.activity.FamousTreesActivity;
import  com.skyline.terraexplorer.mainapps.activity.LdzzBarChartActivity;
import  com.skyline.terraexplorer.mainapps.activity.LmzmBarChartActivity;
import  com.skyline.terraexplorer.mainapps.activity.LyxzajBarChartActivity;
import  com.skyline.terraexplorer.mainapps.activity.OtherBarChartActivity;
import com.skyline.terraexplorer.mainapps.activity.PhoneListActivity;
import  com.skyline.terraexplorer.mainapps.activity.SwdyxBarChartActivity;
import  com.skyline.terraexplorer.mainapps.activity.YhswBarChartActivity;
import  com.skyline.terraexplorer.mainapps.activity.YllhBarChartActivity;
import  com.skyline.terraexplorer.mainapps.bean.BannerItem;
import  com.skyline.terraexplorer.mainapps.db.DbConfig;
import  com.skyline.terraexplorer.mainapps.db.User;
import  com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.mainapps.web.WebsActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private String TAG="HomeFragment";
    private Banner banner;
    private Context mContext;
    private List<BannerItem> mData = new ArrayList<>();
    private LinearLayout zhylLayout;
    private LinearLayout slfhLayout;
    private LinearLayout famousLayout;
    private LinearLayout lyzyLayout;
    private LinearLayout yllhLayout;
    private LinearLayout yhswLayout;
    private LinearLayout lmzmLayout;
    private LinearLayout ldzzLayout;
    private LinearLayout swdyxLayout;
    private LinearLayout lyxzLayout;
    private LinearLayout lxrLayout;
    private LinearLayout ltLayout;
    private SweetAlertDialog warndialog;
    private ServiceApkInstaller wpsInstaller;
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        banner = getView().findViewById(R.id.banner);
        mContext=getActivity();
        initData();
        initBanner();
        initView();
    }

    private void initView() {
        user = new DbConfig(getContext()).getUser();
        zhylLayout=((LinearLayout) getView().findViewById(R.id.zhly_layout));
        slfhLayout=((LinearLayout) getView().findViewById(R.id.slfh_layout));
        famousLayout=((LinearLayout) getView().findViewById(R.id.famous_layout));
        lyzyLayout=((LinearLayout) getView().findViewById(R.id.lyzy_layout));
        yllhLayout=((LinearLayout) getView().findViewById(R.id.yinhuanpaicha_layout));
        yhswLayout=((LinearLayout) getView().findViewById(R.id.yhsw_layout));
        lmzmLayout=((LinearLayout) getView().findViewById(R.id.lmzm_layout));
        ldzzLayout=((LinearLayout) getView().findViewById(R.id.ldzz_layout));
        swdyxLayout=((LinearLayout) getView().findViewById(R.id.swdyx_layout));
        lyxzLayout=((LinearLayout) getView().findViewById(R.id.lyxz_layout));
        lxrLayout=getView().findViewById(R.id.lxr_layout);
        ltLayout=getView().findViewById(R.id.lt_layout);
        RxViewAction.click(famousLayout)
                .subscribe(aVoid -> {
                    ///合并修改
                    Intent intent = new Intent(getContext(), FamousTreesActivity.class);
                    startActivity(intent);
                });
        RxViewAction.click(slfhLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        /*StringBuilder builder = new StringBuilder();
                        builder.append("wgh://start?username="+user.getFullName()+"&userpaswd="+user.getUserPasswd());
                        Log.e(TAG, "call: "+builder.toString());
                        warndialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                        warndialog.setTitleText("需要安装文件读取软件，请去设置中开启此权限");

                        wpsInstaller = new ServiceApkInstaller(getContext(), "qd_wgh1.2.2.apk");
                        if (!hasApplication("wgh://start")) {
                            setInstallPermission();
                        } else {
*//*                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            //前提：知道要跳转应用的包名、类名
                            ComponentName componentName = new ComponentName("com.skyline.terraexplorer", "com.skyline.terraexplorer.controllers.LaunchActivity");
                            intent.setComponent(componentName);
                            startActivity(intent);*//*
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(builder.toString()));
                            startActivity(intent);
                        }*/

                        ///合并修改
                        Intent intent = new Intent(getContext(), com.skyline.terraexplorer.controllers.LaunchActivity.class);
                        intent.putExtra("token",new DbConfig(getContext()).getUser().getToken());
                        startActivity(intent);
                    }
                });
        RxViewAction.click(zhylLayout)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        /*StringBuilder builder = new StringBuilder();
                        builder.append("zhyl://start?username="+user.getFullName()+"&userpaswd="+user.getUserPasswd());
                        Log.e(TAG, "call: "+builder.toString());
                        warndialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                        warndialog.setTitleText("需要安装文件读取软件，请去设置中开启此权限");

                        wpsInstaller = new ServiceApkInstaller(getContext(), "wisdomgarden1.0.7.apk");
                        if (!hasApplication("zhyl://start")) {
                            setInstallPermission();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(builder.toString()));
                            startActivity(intent);
                        }*/

                        ///合并修改
                        Intent intent = new Intent(getContext(), com.skyline.terraexplorer.wisdomgarden.ui.activity.LauncherActivity.class);
                        intent.putExtra("token",new DbConfig(getContext()).getUser().getToken());
                        startActivity(intent);
                    }
                });
        RxViewAction.click(lyzyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OtherBarChartActivity.class);
                        intent.putExtra("from","zyzy");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(yllhLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), YllhBarChartActivity.class);
                        intent.putExtra("from","yllh");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(yhswLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), YhswBarChartActivity.class);
                        intent.putExtra("from","yhsw");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(lmzmLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), LmzmBarChartActivity.class);
                        intent.putExtra("from","lmzm");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(ldzzLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), LdzzBarChartActivity.class);
                        intent.putExtra("from","ldzz");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(swdyxLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), SwdyxBarChartActivity.class);
                        intent.putExtra("from","swdyx");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(lyxzLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), LyxzajBarChartActivity.class);
                        intent.putExtra("from","Lyxzaj");
                        startActivity(intent);
                    }
                });
        RxViewAction.click(lxrLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), PhoneListActivity.class);
                        startActivity(intent);
                    }
                });
        RxViewAction.click(ltLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), WebsActivity.class);
                        //intent.putExtra("webUrl", "https://www.baidu.com");
                        User user = new DbConfig(getContext()).getUser();//https://tps-test.16u.cc/qt/ //https://www.qdsylj.com/garden_h5/
                        intent.putExtra("webUrl", "https://www.qdsylj.com/garden_h5/?userName="+user.getFullName()+"&userId="+user.getId()+"&userPost="+user.getJobTitle());
//                        intent.putExtra("webUrl", "https://www.baidu.com");
                        startActivity(intent);
                    }
                });
    }

    private void initData() {
        mData.add(new BannerItem(R.drawable.ic_park, ""));
        mData.add(new BannerItem(R.drawable.ic_park, ""));
        mData.add(new BannerItem(R.drawable.ic_park, ""));
        mData.add(new BannerItem(R.drawable.ic_park, ""));
        mData.add(new BannerItem(R.drawable.ic_park, ""));
    }
    private void initBanner() {
        // 1. 创建设置BannerLoader
        banner.setBannerLoader(new ImageLoader() {
            @Override
            public void loadView(Context context, BannerEntry entry, int position, View imageView) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_park)
                        .error(R.drawable.ic_park)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(context).load(entry.getBannerPath()).apply(requestOptions).into((ImageView) imageView);
            }
        });

        // 2. 设置页面点击事件
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                //Toast.makeText(getActivity(), "点击了：" + position, Toast.LENGTH_SHORT).show();
            }
        });

        // 3. 翻页事件
        banner.setBannerPagerChangedListener(new Banner.OnBannerSimplePagerListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("hsl777", "onPageSelected: ==>" + position);
            }
        });

        // 4. 最重要一步，加载数据
        banner.loadImagePaths(mData);
    }
    public void setInstallPermission(){
        boolean haveInstallPermission;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
            if(!haveInstallPermission){
                //弹框提示用户手动打开
                warndialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //此方法需要API>=26才能使用
                            toInstallPermissionSettingIntent();
                        }
                    }
                });
                warndialog.show();
                // toInstallPermissionSettingIntent();
            }else{
                warndialog.dismiss();
                wpsInstaller.install();
            }
        }
    }
    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:"+getActivity().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        startActivityForResult(intent, 10086);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10086) {
            warndialog.dismiss();
            wpsInstaller.install();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
        banner.stopAutoPlay();
    }
    private boolean hasApplication(String url) {
        PackageManager manager = mContext.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse(url));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }
}