package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/28.
 */

@Table(name = "dangerrecord")
public class DangerRecord {
    @Column(name = "id",isId = true,autoGen = true)
    public int id;


    @Column(name = "resourceid")
    public String resourceId;

    @Column(name = "resourcename")
    public String resourceName;

    @Column(name = "resourcetype")
    public String resourceType;

    @Column(name = "dangerdescription")
    public String dangerDescription;

    @Column(name = "dangertype")
    public String dangerType;

    @Column(name = "remark")
    public String remark;

    @Column(name = "status")
    public int status;

    @Column(name = "lat")
    public String lat;

    @Column(name = "lng")
    public String lng;


    @Column(name = "position")
    public String position;

    /**
     *
     */
    @Column(name = "pic1")
    public String pic1;

    /**
     *
     */
    @Column(name = "pic2")
    public String pic2;

    @Column(name = "gridid")
    public String gridId;

    @Column(name = "gridname")
    public String gridName;

    @Column(name = "gridno")
    public String gridNo;

    public DangerRecord() {
    }

    public DangerRecord(String resourceId, String resourceName, String resourceType, String dangerDescription, String dangerType, String remark, int status, String lat, String lng, String position, String pic1, String pic2, String gridId, String gridName, String gridNo) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.dangerDescription = dangerDescription;
        this.dangerType = dangerType;
        this.remark = remark;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.position = position;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.gridId = gridId;
        this.gridName = gridName;
        this.gridNo = gridNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDangerDescription() {
        return dangerDescription;
    }

    public void setDangerDescription(String dangerDescription) {
        this.dangerDescription = dangerDescription;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getDangerType() {
        return dangerType;
    }

    public void setDangerType(String dangerType) {
        this.dangerType = dangerType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }
}
