package com.haohai.platform.firelibrary.ui.back;

import java.util.List;

/**
 * Created by geyang on 2020/7/8.
 */

public interface CallBack2<T> {
    //成功返回
    void onSuccess(List<T> t);
    //失败返回
    void onFilure(String msg);
}
