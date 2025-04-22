package com.hht.hsatellitemobile.ui.model;

/**
 * @作者 qc
 * eml.dongbinjava@163.com
 * @创建日期 2023/5/29 10:14
 */


/**
 *
 jsonObject.put("FireAlarmId",id);
 jsonObject.put("fire_id",id);
 jsonObject.put("Longitude",longitude);
 jsonObject.put("longitude",longitude);
 jsonObject.put("Latitude",latitude);
 jsonObject.put("latitude",latitude);
 jsonObject.put("IsReal",currentFireState);
 jsonObject.put("is_real",currentFireState);
 jsonObject.put("Address",dizhiEdit.getText().toString());
 jsonObject.put("address",dizhiEdit.getText().toString());
 jsonObject.put("description",huoqingEdit.getText().toString());
 jsonObject.put("pic_path3",huoqingEdit.getText().toString());
 jsonObject.put("UserName",new DbConfig(this).getUser().getUsername());
 jsonObject.put("operation_user",new DbConfig(this).getUser().getUsername());
 */
public class FeedBack {
    private String fire_id;
    private String longitude;
    private String latitude;
    private int is_real;
    private String address;
    private String pic_path3;//描述临时使用该字段
    private String operation_user;
    private String pic_path1;
    private String pic_path2;

    public FeedBack() {
    }

    public FeedBack(String fire_id, String longitude, String latitude, int is_real, String address, String pic_path3, String operation_user, String pic_path1, String pic_path2) {
        this.fire_id = fire_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.is_real = is_real;
        this.address = address;
        this.pic_path3 = pic_path3;
        this.operation_user = operation_user;
        this.pic_path1 = pic_path1;
        this.pic_path2 = pic_path2;
    }

    public String getPic_path1() {
        return pic_path1;
    }

    public void setPic_path1(String pic_path1) {
        this.pic_path1 = pic_path1;
    }

    public String getPic_path2() {
        return pic_path2;
    }

    public void setPic_path2(String pic_path2) {
        this.pic_path2 = pic_path2;
    }

    public String getFire_id() {
        return fire_id;
    }

    public void setFire_id(String fire_id) {
        this.fire_id = fire_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getIs_real() {
        return is_real;
    }

    public void setIs_real(int is_real) {
        this.is_real = is_real;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic_path3() {
        return pic_path3;
    }

    public void setPic_path3(String pic_path3) {
        this.pic_path3 = pic_path3;
    }

    public String getOperation_user() {
        return operation_user;
    }

    public void setOperation_user(String operation_user) {
        this.operation_user = operation_user;
    }
}
