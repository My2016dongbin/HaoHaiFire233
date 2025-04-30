package com.haohai.platform.firelibrary.ui.model.imodel;

import com.haohai.platform.firelibrary.ui.back.CallBack;
import com.haohai.platform.firelibrary.ui.back.CallBack2;
import com.ruyiruyi.rylibrary.db.Area;

/**
 * Created by geyang on 2020/7/8.
 */

public interface IFireModel {
    void getAreaDate(CallBack2<Area> callBack2);


    void postData(String address, String beizhu, CallBack callBack);
}
