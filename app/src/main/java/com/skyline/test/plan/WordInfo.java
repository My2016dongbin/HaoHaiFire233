package com.skyline.test.plan;

/**
 * Created by miao on 2017/5/17 10:02.
 */

public class WordInfo {
    private String name;
    private String path;

    public WordInfo(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
