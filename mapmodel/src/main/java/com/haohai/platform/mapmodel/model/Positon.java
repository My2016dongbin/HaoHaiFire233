package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/4.
 */

public class Positon {
    public double lng;
    public double lat;

    public Positon() {
    }

    public Positon(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
