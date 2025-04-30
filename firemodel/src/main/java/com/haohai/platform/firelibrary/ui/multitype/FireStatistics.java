package com.haohai.platform.firelibrary.ui.multitype;

/**
 * Created by geyang on 2021/3/12.
 */
public class FireStatistics {
    public String countyName;
    public int unHandleCount;
    public int handleCount;

    public FireStatistics() {
    }

    public FireStatistics(String countyName, int unHandleCount, int handleCount) {
        this.countyName = countyName;
        this.unHandleCount = unHandleCount;
        this.handleCount = handleCount;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getUnHandleCount() {
        return unHandleCount;
    }

    public void setUnHandleCount(int unHandleCount) {
        this.unHandleCount = unHandleCount;
    }

    public int getHandleCount() {
        return handleCount;
    }

    public void setHandleCount(int handleCount) {
        this.handleCount = handleCount;
    }
}