package com.haohai.platform.firelibrary.ui.multitype;

/**
 * Created by geyang on 2021/3/12.
 */
public class FireStatistics {
    public String countyName;
    public int unHandleCount;
    public int handleCount;

    public int allTaskCount = 0;
    public int completeCount = 0;
    public int notStartCount = 0;
    public int executingCount = 0;

    public FireStatistics() {
    }

    public FireStatistics(String countyName, int unHandleCount, int handleCount) {
        this.countyName = countyName;
        this.unHandleCount = unHandleCount;
        this.handleCount = handleCount;
    }

    public FireStatistics(String countyName, int unHandleCount, int handleCount, int allTaskCount, int executingCount, int completeCount, int notStartCount) {
        this.countyName = countyName;
        this.unHandleCount = unHandleCount;
        this.handleCount = handleCount;
        this.allTaskCount = allTaskCount;
        this.completeCount = completeCount;
        this.executingCount = executingCount;
        this.notStartCount = notStartCount;
    }

    public int getTaskAll() {
        return allTaskCount;
    }

    public void setTaskAll(int allTaskCount) {
        this.allTaskCount = allTaskCount;
    }

    public int getTaskIng() {
        return executingCount;
    }

    public void setTaskIng(int executingCount) {
        this.executingCount = executingCount;
    }

    public int getTaskEnd() {
        return completeCount;
    }

    public void setTaskEnd(int completeCount) {
        this.completeCount = completeCount;
    }

    public int getTaskWait() {
        return notStartCount;
    }

    public void setTaskWait(int notStartCount) {
        this.notStartCount = notStartCount;
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