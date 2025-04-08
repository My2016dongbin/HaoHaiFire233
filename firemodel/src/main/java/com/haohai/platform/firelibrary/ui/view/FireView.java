package com.haohai.platform.firelibrary.ui.view;

import com.haohai.platform.firelibrary.ui.view.base.BaseMvpView;
import com.ruyiruyi.rylibrary.db.Area;

import java.util.List;

/**
 * Created by geyang on 2020/7/8.
 */

public interface FireView extends BaseMvpView {
    //成功返回信息
    void showResult(String msg);
    //Toast信息
    void showToast(String msg);

    void showAreaList(List<Area> areaList);
}
