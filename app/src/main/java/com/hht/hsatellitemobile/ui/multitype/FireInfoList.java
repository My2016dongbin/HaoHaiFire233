package com.hht.hsatellitemobile.ui.multitype;

import java.util.List;

/**
 * Created by 13589 on 2019/8/8.
 */
public class FireInfoList {
    public int backgroundId; //0背景为白色  1背景为黑色
    public String fireTime;
    public List<FireInfo> fireInfoList;

    public FireInfoList(String fireTime, List<FireInfo> fireInfoList) {
        this.fireTime = fireTime;
        this.fireInfoList = fireInfoList;
    }

    public FireInfoList() {
    }

    public String getFireTime() {
        return fireTime;
    }

    public void setFireTime(String fireTime) {
        this.fireTime = fireTime;
    }

    public List<FireInfo> getFireInfoList() {
        return fireInfoList;
    }

    public void setFireInfoList(List<FireInfo> fireInfoList) {
        this.fireInfoList = fireInfoList;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public FireInfoList(int backgroundId, String fireTime, List<FireInfo> fireInfoList) {
        this.backgroundId = backgroundId;
        this.fireTime = fireTime;
        this.fireInfoList = fireInfoList;
    }
}