package com.skyline.test.places1;


import com.skyline.teapi.IPosition;

/**
 * Created by miao on 2017/6/2 9:05.
 */

public class Places1Info {
    private IPosition position;
    private String id;
    private String name;

    public Places1Info() {}

    public Places1Info(IPosition position, String id, String name) {
        this.position = position;
        this.id = id;
        this.name = name;
    }

    public IPosition getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
