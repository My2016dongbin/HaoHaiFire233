package com.haohai.haohai.address.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/9/16.
 */

@Table(name = "address")
public class Address {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;

    @Column(name = "longitude")
    private String longitude;       //经度

    @Column(name = "latitude")
    private String latitude;        //纬度

    @Column(name = "inserttime")
    private String insertTime;      //上传成功后为  更新时间          上传成功前  为获取经纬度时间

    @Column(name = "success")
    private String success;  //0失败  1成功

    public Address(int id, String longitude, String latitude, String insertTime, String success) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.insertTime = insertTime;
        this.success = success;
    }

    public Address(String longitude, String latitude, String insertTime, String success) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.insertTime = insertTime;
        this.success = success;
    }

    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
