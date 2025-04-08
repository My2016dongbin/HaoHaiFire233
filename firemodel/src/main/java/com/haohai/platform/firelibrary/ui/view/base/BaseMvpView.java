package com.haohai.platform.firelibrary.ui.view.base;

/**
 * Created by geyang on 2020/7/8.
 */

public interface  BaseMvpView {
    //显示等待框
    void showLoding(String msg);
    //隐藏等待框
    void hideLoding();
    //错误信息
    void showerr(String err);
}
