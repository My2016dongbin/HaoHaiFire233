package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

/**
 * Created by geyang on 2020/12/8.
 */
public class ParkData {
    public String dataName;
    public String dataStr;
    public String dataJson;
    public boolean isCheck;

    public ParkData() {
    }

    public ParkData(String dataName, String dataStr, String dataJson,boolean isCheck) {
        this.dataName = dataName;
        this.dataStr = dataStr;
        this.dataJson = dataJson;
        this.isCheck = isCheck;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}