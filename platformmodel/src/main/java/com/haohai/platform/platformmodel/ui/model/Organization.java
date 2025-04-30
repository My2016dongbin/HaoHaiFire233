package com.haohai.platform.platformmodel.ui.model;

/**
 * Created by geyang on 2020/7/2.
 */


public class Organization {
    public String id;
    public String org_code;             //父id
    public String name;
    public String unit_type;
    public String device_code;
    public String online_status;
    public String is_start;
    public String camera_type;
    public String category;
    public String device_type;      //0是文件夹  1是文件
    public String node_type;
    public String channel_seq;
    public String ip;
    public String longitude;
    public String latitude;
    public String place_code;
    public String install_addr;
    public String snapSwitch;

    public Organization() {
    }

    public Organization(String id, String org_code, String name, String unit_type, String device_code, String online_status, String is_start, String camera_type, String category, String device_type, String node_type, String channel_seq, String ip, String longitude, String latitude, String place_code, String install_addr, String snapSwitch) {
        this.id = id;
        this.org_code = org_code;
        this.name = name;
        this.unit_type = unit_type;
        this.device_code = device_code;
        this.online_status = online_status;
        this.is_start = is_start;
        this.camera_type = camera_type;
        this.category = category;
        this.device_type = device_type;
        this.node_type = node_type;
        this.channel_seq = channel_seq;
        this.ip = ip;
        this.longitude = longitude;
        this.latitude = latitude;
        this.place_code = place_code;
        this.install_addr = install_addr;
        this.snapSwitch = snapSwitch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit_type() {
        return unit_type;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getIs_start() {
        return is_start;
    }

    public void setIs_start(String is_start) {
        this.is_start = is_start;
    }

    public String getCamera_type() {
        return camera_type;
    }

    public void setCamera_type(String camera_type) {
        this.camera_type = camera_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getNode_type() {
        return node_type;
    }

    public void setNode_type(String node_type) {
        this.node_type = node_type;
    }

    public String getChannel_seq() {
        return channel_seq;
    }

    public void setChannel_seq(String channel_seq) {
        this.channel_seq = channel_seq;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlace_code() {
        return place_code;
    }

    public void setPlace_code(String place_code) {
        this.place_code = place_code;
    }

    public String getInstall_addr() {
        return install_addr;
    }

    public void setInstall_addr(String install_addr) {
        this.install_addr = install_addr;
    }

    public String getSnapSwitch() {
        return snapSwitch;
    }

    public void setSnapSwitch(String snapSwitch) {
        this.snapSwitch = snapSwitch;
    }
}

