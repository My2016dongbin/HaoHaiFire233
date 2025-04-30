package com.haohai.platform.firelibrary.ui.presenter;

import android.util.Log;

import com.haohai.platform.firelibrary.ui.back.CallBack;
import com.haohai.platform.firelibrary.ui.back.CallBack2;
import com.haohai.platform.firelibrary.ui.model.FireModel;
import com.haohai.platform.firelibrary.ui.presenter.base.BaseMvpPresenter;
import com.haohai.platform.firelibrary.ui.presenter.ipresenter.IFirePresenter;
import com.haohai.platform.firelibrary.ui.view.FireView;
import com.ruyiruyi.rylibrary.db.Area;

import java.util.List;

/**
 * Created by geyang on 2020/7/8.
 */

public class FirePresenter extends BaseMvpPresenter<FireView> implements IFirePresenter {
    private static final String TAG = FirePresenter.class.getSimpleName();
    private FireModel fireModel;

    public FirePresenter(FireModel fireModel) {
        this.fireModel = fireModel;
    }

    @Override
    public void fireData(String address, String beizhu) {




        final FireView fireView = getMvpView();
        if (address.length() == 0){
            fireView.showToast("地址不能为空");
        }else {
            postFireToService(address,beizhu);

          /*  fireView.showLoding("数据提交中");
            fireModel.postData(address,beizhu, new CallBack() {
                @Override
                public void onSuccess() {
                    //进行网络请求
                    fireView.hideLoding();
                    fireView.showToast("登陆成功");
                }

                @Override
                public void onFilure(String msg) {
                    fireView.hideLoding();
                    fireView.showerr(msg);
                }
            });*/
        }
    }

    @Override
    public void getAreaData() {
        Log.e(TAG, "call: 2");
        checkViewAttach();
        final FireView mvpView = getMvpView();
        fireModel.getAreaDate(new CallBack2<Area>() {
            @Override
            public void onSuccess(List<Area> areaList) {
                Log.e(TAG, "onSuccess: 5" +areaList.size() );
                mvpView.showAreaList(areaList);
            }

            @Override
            public void onFilure(String msg) {

            }
        });
    }

    private void postFireToService(String address, String beizhu) {

    }
}
