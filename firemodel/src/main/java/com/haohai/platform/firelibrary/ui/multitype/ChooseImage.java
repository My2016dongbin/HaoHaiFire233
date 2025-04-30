package com.haohai.platform.firelibrary.ui.multitype;

import android.net.Uri;

import org.xutils.db.annotation.Column;

/**
 * Created by geyang on 2020/7/9.
 */
public class ChooseImage {
    public String uCheckId;

    public Uri uri;

    public Boolean isAdd;

    public ChooseImage() {
    }

    public ChooseImage(String uCheckId, Uri uri, Boolean isAdd) {
        this.uCheckId = uCheckId;
        this.uri = uri;
        this.isAdd = isAdd;
    }

    public String getuCheckId() {
        return uCheckId;
    }

    public void setuCheckId(String uCheckId) {
        this.uCheckId = uCheckId;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Boolean getAdd() {
        return isAdd;
    }

    public void setAdd(Boolean add) {
        isAdd = add;
    }
}