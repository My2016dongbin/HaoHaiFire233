package com.skyline.terraexplorer.models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by geyang on 2019/12/26.
 */
@Table(name = "resourceposition")
public class ResourcePosition implements Serializable {
    @Column(name = "lng")
    public double lng;
    @Column(name = "lat")
    public double lat;

    public ResourcePosition(double lng, double lat) {
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
