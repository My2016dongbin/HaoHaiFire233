package com.haohai.platform.platformmodel.ui.model;

/**
 * Created by geyang on 2020/6/15.
 */

public class PositionModel {
    public String Longitude;
    public String Latitude;
    public String InsertTime;

    public PositionModel() {
    }

    public PositionModel(String longitude, String latitude, String insertTime) {
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