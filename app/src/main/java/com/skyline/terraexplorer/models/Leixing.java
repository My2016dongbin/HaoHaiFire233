package com.skyline.terraexplorer.models;

/**
 * Created by geyang on 2019/10/25.
 */

public class Leixing {
    public int id;
    public String name;

    public Leixing(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
