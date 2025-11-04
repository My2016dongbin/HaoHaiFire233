package com.hht.hsatellitemobile.ui.multitype;

/**
 * Created by dongbin on 2025/11/4.
 */

public class FeedBack {
    private String id;
    private String fire_id;
    private String longitude;
    private String latitude;
    private String is_real;
    private String address;
    private String pic_path1;
    private String pic_path2;
    private String pic_path3;
    private String operation_time;
    private String operation_user;

    public FeedBack() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPic_path3() {
        return pic_path3;
    }

    public void setPic_path3(String pic_path3) {
        this.pic_path3 = pic_path3;
    }

    public String getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(String operation_time) {
        this.operation_time = operation_time;
    }

    public String getOperation_user() {
        return operation_user;
    }

    public void setOperation_user(String operation_user) {
        this.operation_user = operation_user;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "id='" + id + '\'' +
                ", fire_id='" + fire_id + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", is_real='" + is_real + '\'' +
                ", address='" + address + '\'' +
                ", pic_path1='" + pic_path1 + '\'' +
                ", pic_path2='" + pic_path2 + '\'' +
                ", pic_path3='" + pic_path3 + '\'' +
                ", operation_time='" + operation_time + '\'' +
                ", operation_user='" + operation_user + '\'' +
                '}';
    }
}
