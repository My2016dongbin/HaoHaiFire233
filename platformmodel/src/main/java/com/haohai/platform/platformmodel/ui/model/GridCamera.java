package com.haohai.platform.platformmodel.ui.model;

public class GridCamera {
    String name;
    String monitorId;
    String deviceId;
    String id;
    String rtspUrl;
    String subRtspUrl;
    String state;
    int cameraType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public String getSubRtspUrl() {
        return subRtspUrl;
    }

    public void setSubRtspUrl(String subRtspUrl) {
        this.subRtspUrl = subRtspUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    @Override
    public String toString() {
        return "GridCamera{" +
                "name='" + name + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", id='" + id + '\'' +
                ", rtspUrl='" + rtspUrl + '\'' +
                ", subRtspUrl='" + subRtspUrl + '\'' +
                ", state='" + state + '\'' +
                ", cameraType=" + cameraType +
                '}';
    }
}
