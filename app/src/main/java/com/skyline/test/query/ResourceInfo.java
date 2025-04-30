package com.skyline.test.query;

import com.skyline.teapi.IPosition;

/**
 * Created by miao on 2017/5/31 16:12.
 */

public class ResourceInfo {
    private String name;
    private IPosition position;
    private String desc;
    private int icon;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public ResourceInfo(String name, IPosition position) {
        this.name = name;
        this.position = position;
    }
    public ResourceInfo(String name,IPosition position,String desc,int icon)
    {
        this.name=name;
        this.position=position;
        this.desc=desc;
        this.icon=icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPosition getPosition() {
        return position;
    }

    public void setPosition(IPosition position) {
        this.position = position;
    }
}
