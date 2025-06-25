package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2020/11/23.
 */

public class MapPosition {
    private double lng;
    private double lat;
    private double z;

    public MapPosition() {
    }

    public MapPosition(double lng, double lat, double z) {
        this.lng = lng;
        this.lat = lat;
        this.z = z;
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
