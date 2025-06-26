package com.skyline.terraexplorer.multitype;


import com.skyline.terraexplorer.models.ResourcePosition;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by geyang on 2019/12/26.
 */
@Table(name = "plan")
public class Plan implements Serializable{
    @Column(name = "id",isId = true,autoGen = false)
    public String id;

    @Column(name = "createuser")
    public String createUser;

    @Column(name = "updateuser")
    public String updateUser;

    @Column(name = "createtime")
    public String createTime;

    @Column(name = "updatetime")
    public String updateTime;

    @Column(name = "groupid")
    public String groupId;

    @Column(name = "planid")
    public String planId;

    @Column(name = "resourceid")
    public String resourceId;

    @Column(name = "resourcetype")
    public String resourceType;

    @Column(name = "resourcename")
    public String resourceName;

    @Column(name = "districtno")
    public String districtNo;

    @Column(name = "districtname")
    public String districtName;

    @Column(name = "streetno")
    public String streetNo;

    @Column(name = "streetname")
    public String streetName;

    @Column(name = "gridid")
    public String gridId;

    @Column(name = "gridno")
    public String gridNo;

    @Column(name = "gridname")
    public String gridName;

    @Column(name = "orderno")
    public int orderNo;

    @Column(name = "resourceposition")
    public ResourcePosition resourcePosition;

    @Column(name = "description")
    public String description;

    @Column(name = "lng")
    public double lng;

    @Column(name = "lat")
    public double lat;


    public Plan() {
    }

    public Plan(String createUser, String updateUser, String createTime, String updateTime, String groupId, String id, String planId, String resourceId, String resourceType, String resourceName, String districtNo, String districtName, String streetNo, String streetName, String gridId, String gridNo, String gridName, int orderNo, ResourcePosition resourcePosition, String description) {
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.groupId = groupId;
        this.id = id;
        this.planId = planId;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.districtNo = districtNo;
        this.districtName = districtName;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.gridName = gridName;
        this.orderNo = orderNo;
        this.resourcePosition = resourcePosition;
        this.description = description;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(String districtNo) {
        this.districtNo = districtNo;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public ResourcePosition getResourcePosition() {
        return resourcePosition;
    }

    public void setResourcePosition(ResourcePosition resourcePosition) {
        this.resourcePosition = resourcePosition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", groupId='" + groupId + '\'' +
                ", id='" + id + '\'' +
                ", planId='" + planId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", districtNo='" + districtNo + '\'' +
                ", districtName='" + districtName + '\'' +
                ", streetNo='" + streetNo + '\'' +
                ", streetName='" + streetName + '\'' +
                ", gridId='" + gridId + '\'' +
                ", gridNo='" + gridNo + '\'' +
                ", gridName='" + gridName + '\'' +
                ", orderNo=" + orderNo +
                ", resourcePosition=" + resourcePosition +
                ", description='" + description + '\'' +
                '}';
    }
}