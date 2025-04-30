package com.haohai.platform.platformmodel.ui.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.haohai.platform.platformmodel.ui.fragment.base.HhBaseFragment;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ImageTextView;
import com.ruyiruyi.rylibrary.route.RouteUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;
//import com.tencent.android.tpush.XGPushManager;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;

/**
 * Created by geyang on 2020/7/2.
 */

public class MyFragment extends HhBaseFragment {
    private static final String TAG = MyFragment.class.getSimpleName();
    private TextView outButtonView;
    private ImageView touxiangImage;
    private ImageTextView touxiangView;
    private User user;
    private TextView nameView;
    private Switch weizhiSwitch;
    private Boolean isShangchuan = true;;
    private LinearLayout gengxinLayout;
    private LinearLayout guanyuLayout;
    private TextView versionCode;
    private String appversionNum;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);

        user = new DbConfig(getContext()).getUser();
        initView();

        bindView();
    }



    private void initView() {
        gengxinLayout = ((LinearLayout) getView().findViewById(R.id.gengxin_layout));
        guanyuLayout = ((LinearLayout) getView().findViewById(R.id.guanyu_layout));


        weizhiSwitch = ((Switch) getView().findViewById(R.id.weizhi_switch));
        outButtonView = ((TextView) getView().findViewById(R.id.out_login_button));

        touxiangImage = ((ImageView) getView().findViewById(R.id.touxiang_image));
        touxiangView = ((ImageTextView) getView().findViewById(R.id.touxiang_view));
        nameView = ((TextView) getView().findViewById(R.id.name_view));
        versionCode = ((TextView) getView().findViewById(R.id.version_code));
        if (user.getHeadUrl().equals("null")) {
            touxiangImage.setVisibility(View.GONE);
            touxiangView.setVisibility(View.VISIBLE);

            touxiangView.setName(user.getFullName());
        }else {
            touxiangImage.setVisibility(View.VISIBLE);
            touxiangView.setVisibility(View.GONE);
            Glide.with(getContext()).load(user.getHeadUrl()).transform(new GlideCircleTransform(getContext())).into(touxiangImage);
        }
        nameView.setText(user.getFullName());

        PackageManager pm = getContext().getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            appversionNum = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCode.setText("版本信息："+appversionNum);

        RxViewAction.clickNoDouble(gengxinLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(getContext(), "当前是最新版本", Toast.LENGTH_SHORT).show();
                    }
                });
        RxViewAction.clickNoDouble(guanyuLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(getContext(), "青岛浩海网络科技股份有限公司技术支持", Toast.LENGTH_SHORT).show();
                   }
                });

    }

    private void bindView() {
        weizhiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isShangchuan = true;

                    user.isShangchuan = true;
                    DbConfig dbConfig = new DbConfig(getContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else {
                    isShangchuan = false;
                    user.isShangchuan = false;
                    DbConfig dbConfig = new DbConfig(getContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        RxViewAction.clickNoDouble(outButtonView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        XGPushManager.unregisterPush(getContext());

                        DbConfig dbConfig = new DbConfig(getContext());
                        User user = dbConfig.getUser();
                        user.setIsLogin(0);
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        ARouter.getInstance().build(RouteUtils.OutLogin)
                                .navigation();
                        getActivity().finish();
                       // startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
