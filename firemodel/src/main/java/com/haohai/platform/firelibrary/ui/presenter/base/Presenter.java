package com.haohai.platform.firelibrary.ui.presenter.base;

/**
 * Created by geyang on 2020/7/8.
 */

interface Presenter<V> {
    void attachView(V mvpView);
    void deachView();
}
