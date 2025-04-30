package com.haohai.platform.platformmodel.ui.Multitype;

/**
 * Created by geyang on 2020/6/6.
 */
public class WorkReport {
    public String groupId;
    public String id;
    public String userId;
    public String typeName;
    public String reportType;
    public String commitReportTime;
    public String dailyReportTime;
    public String weekReportTime;
    public String todaySummary;
    public String tomorrowPlan;
    public String unfinishedWork;
    public String measuresPlan;
    public String img;
    public String deptId;

    public WorkReport() {
    }

    public WorkReport(String groupId, String id, String userId, String typeName, String reportType, String commitReportTime, String dailyReportTime, String weekReportTime, String todaySummary, String tomorrowPlan, String unfinishedWork, String measuresPlan, String img, String deptId) {
        this.groupId = groupId;
        this.id = id;
        this.userId = userId;
        this.typeName = typeName;
        this.reportType = reportType;
        this.commitReportTime = commitReportTime;
        this.dailyReportTime = dailyReportTime;
        this.weekReportTime = weekReportTime;
        this.todaySummary = todaySummary;
        this.tomorrowPlan = tomorrowPlan;
        this.unfinishedWork = unfinishedWork;
        this.measuresPlan = measuresPlan;
        this.img = img;
        this.deptId = deptId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getCommitReportTime() {
        return commitReportTime;
    }

    public void setCommitReportTime(String commitReportTime) {
        this.commitReportTime = commitReportTime;
    }

    public String getDailyReportTime() {
        return dailyReportTime;
    }

    public void setDailyReportTime(String dailyReportTime) {
        this.dailyReportTime = dailyReportTime;
    }

    public String getWeekReportTime() {
        return weekReportTime;
    }

    public void setWeekReportTime(String weekReportTime) {
        this.weekReportTime = weekReportTime;
    }

    public String getTodaySummary() {
        return todaySummary;
    }

    public void setTodaySummary(String todaySummary) {
        this.todaySummary = todaySummary;
    }

    public String getTomorrowPlan() {
        return tomorrowPlan;
    }

    public void setTomorrowPlan(String tomorrowPlan) {
        this.tomorrowPlan = tomorrowPlan;
    }

    public String getUnfinishedWork() {
        return unfinishedWork;
    }

    public void setUnfinishedWork(String unfinishedWork) {
        this.unfinishedWork = unfinishedWork;
    }

    public String getMeasuresPlan() {
        return measuresPlan;
    }

    public void setMeasuresPlan(String measuresPlan) {
        this.measuresPlan = measuresPlan;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}