package com.skyline.test.entity;


import java.util.UUID;

/**
 * Created by Yellow on 2017-06-09.
 */

public class FavoritePlace {
    public String id;
    public String name;
    public String desc;
    public boolean showOn3D;
    public int icon;
    public PointLatLng point;
    public FavoritePlace() {
        id = UUID.randomUUID().toString();  //自动生成主键
        name = "";
        icon = 0;
        desc = "";
    }
}
