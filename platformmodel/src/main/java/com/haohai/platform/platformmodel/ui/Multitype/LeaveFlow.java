package com.haohai.platform.platformmodel.ui.Multitype;

import java.io.Serializable;

/**
 * Created by geyang on 2020/6/18.
 */

/**
 * 第三步  创建对象 并进行数据绑定
 */
public class LeaveFlow implements Serializable{
    public String updateTime;
    public String id;
    public String processInstanceId;
    public String timeType;
    public String startTime;
    public String endTime;
    public String oneDayTime;
    public String reason;
    public String leaveDays;
    public String cc;
    public String submitId;
    public String submitName;
    public String processStart;
    public String deptId;
    public String reserve;


    public LeaveFlow() {
    }

    public LeaveFlow(String updateTime, String id, String processInstanceId, String timeType, String startTime, String endTime, String oneDayTime, String reason, String leaveDays, String cc, String submitId, String submitName, String processStart, String deptId, String reserve) {
        this.updateTime = updateTime;
        this.id = id;
        this.processInstanceId = processInstanceId;
        this.timeType = timeType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.oneDayTime = oneDayTime;
        this.reason = reason;
        this.leaveDays = leaveDays;
        this.cc = cc;
        this.submitId = submitId;
        this.submitName = submitName;
        this.processStart = processStart;
        this.deptId = deptId;
        this.reserve = reserve;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOneDayTime() {
        return oneDayTime;
    }

    public void setOneDayTime(String oneDayTime) {
        this.oneDayTime = oneDayTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public String getProcessStart() {
        return processStart;
    }

    public void setProcessStart(String processStart) {
        this.processStart = processStart;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}