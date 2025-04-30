package com.haohai.platform.platformmodel.ui.model;

public class GridMonitor {
    String name;
    String address;
    String id;
    String gridId;
    String gridNo;
    String gridName;
    String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "GridMonitor{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", gridId='" + gridId + '\'' +
                ", gridNo='" + gridNo + '\'' +
                ", gridName='" + gridName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
