package com.haohai.haohai.address.multitype;

/**
 * Created by geyang on 2019/9/16.
 */
public class AddressMul {

    private String Longitude;       //经度

    private String Latitude;        //纬度

    private String InsertTime;      //上传成功后为  更新时间          上传成功前  为获取经纬度时间

    private String UploadTime;      // 本地获取GPS的时间


    public AddressMul(String longitude, String latitude, String insertTime, String uploadTime) {
        Longitude = longitude;
        Latitude = latitude;
        InsertTime = insertTime;
        UploadTime = uploadTime;
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

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }
}