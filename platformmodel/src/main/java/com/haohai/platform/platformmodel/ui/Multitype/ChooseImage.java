package com.haohai.platform.platformmodel.ui.Multitype;

import android.net.Uri;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/2/6.
 */
public class ChooseImage {
    public String uCheckId;

    public Uri uri;

    public Boolean isAdd;

    public ChooseImage() {
        uCheckId = "";
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
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Boolean getAdd() {
        return this.isAdd;
    }

    public void setAdd(Boolean add) {
        this.isAdd = add;
    }
}