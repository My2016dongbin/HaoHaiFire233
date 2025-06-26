package com.skyline.terraexplorer.mainapps.multitype;

public class Scjd {
    public String id;
    public String head;
    public double lat;
    public double lng;
    public String name;
    public String phone;
    public String region;
    public String remark;
    public String address;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Scjd(String id,String head, int lat, int lng, String name, String phone, String region, String remark,String address) {
        this.head = head;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.remark = remark;
        this.address = address;
        this.id = id;
    }
}
