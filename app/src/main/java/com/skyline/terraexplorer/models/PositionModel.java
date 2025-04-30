package com.skyline.terraexplorer.models;

/**
 * Created by geyang on 2020/4/8.
 */

public class PositionModel {
    public String lng;
    private String lat;

    public PositionModel(String lng, String lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public PositionModel() {
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
