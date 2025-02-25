package com.haohai.haohai.address.modle;

import org.xutils.db.annotation.Column;

/**
 * Created by geyang on 2019/9/16.
 */

public class AddressModel {

    private String Longitude;       //经度

    private String Latitude;        //纬度

    private String InsertTime;      //上传成功后为  更新时间          上传成功前  为获取经纬度时间


    public AddressModel(String longitude, String latitude, String insertTime) {
        Longitude = longitude;
        Latitude = latitude;
        InsertTime = insertTime;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getInsertTime() {
        return InsertTime;
    }

    public void setInsertTime(String insertTime) {
        InsertTime = insertTime;
    }
}
