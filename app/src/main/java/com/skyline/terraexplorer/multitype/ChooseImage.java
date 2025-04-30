package com.skyline.terraexplorer.multitype;

import android.net.Uri;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/2/6.
 */
@Table(name = "checkcomimage")
public class ChooseImage {
    @Column(name = "id",isId = true,autoGen = false)
    public String uCheckId;

    @Column(name = "uri")
    public Uri uri;

    @Column(name = "add")
    public Boolean isAdd;

    @Column(name = "picstr")
    public String picStr;

    @Column(name = "isnetpic")
    public boolean isNetPic;

    public ChooseImage() {
        uCheckId = "";
        isAdd = false;
        this.isNetPic = false;
    }

    public ChooseImage(String uCheckId, Uri uri, Boolean isAdd) {
        this.uCheckId = uCheckId;
        this.uri = uri;
        this.isAdd = isAdd;
        this.isNetPic = false;
    }

    public ChooseImage(String uCheckId, Uri uri, Boolean isAdd, String picStr) {
        this.uCheckId = uCheckId;
        this.uri = uri;
        this.isAdd = isAdd;
        this.picStr = picStr;
        this.isNetPic = false;
    }

    public boolean isNetPic() {
        return isNetPic;
    }

    public void setNetPic(boolean netPic) {
        isNetPic = netPic;
    }

    public String getPicStr() {
        return picStr;
    }

    public void setPicStr(String picStr) {
        this.picStr = picStr;
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