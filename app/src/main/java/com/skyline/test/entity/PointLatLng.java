package com.skyline.test.entity;

/**
 * Created by Yellow on 2017-05-08.
 */

public class PointLatLng {
    private double lng;
    private double lat;

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
    public PointLatLng(double lat, double lng)
    {
        this.lat=lat;
        this.lng=lng;
    }
}
