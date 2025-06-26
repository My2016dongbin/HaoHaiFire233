package com.skyline.terraexplorer.multitype;

/**
 * Created by geyang on 2020/4/7.
 */
public class CheckList {

    public String checkName;
    public String checkTime;
    public String checkMen;
    public String id;

    public CheckList(String checkName, String checkTime, String checkMen, String id) {
        this.checkName = checkName;
        this.checkTime = checkTime;
        this.checkMen = checkMen;
        this.id = id;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckMen() {
        return checkMen;
    }

    public void setCheckMen(String checkMen) {
        this.checkMen = checkMen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}