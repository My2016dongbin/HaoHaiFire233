package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/12/31.
 * 直升机机降点
 */

@Table(name = "helicopterpointdto")
public class HelicopterPointDTO {

    /**
     * 主键
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "address")
    private String address;
    /**
     * 名称
     */
    @Column(name = "createtime")
    private String createTime;

    @Column(name = "createuser")
    private String createUser;

    @Column(name = "description")
    private String description;

    @Column(name = "districtname")
    private String districtName;

    @Column(name = "districtno")
    private String districtNo;

    @Column(name = "gridid")
    private String gridId;

    @Column(name = "gridname")
    private String gridName;

    @Column(name = "gridno")
    private String gridNo;

    @Column(name = "groupid")
    private String groupId;

    @Column(name = "iconfile")
    private String iconFile;

    @Column(name = "leaderid")
    private String leaderId;

    @Column(name = "leadername")
    private String leaderName;

    @Column(name = "leaderphone")
    private String leaderPhone;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Column(name = "streetname")
    private String streetName;

    @Column(name = "streetno")
    private String streetNo;

    @Column(name = "textcolor")
    private String textColor;

    @Column(name = "updateyime")
    private String updateTime;

    @Column(name = "updateuser")
    private String updateUser;

    @Column(name = "watersource")
    private String waterSource;

    @Column(name = "resourcetype")
    private String resourceType;
    /**
     * "DELETE";  "ACTIVE"; 删除状态
     */
    @Column(name = "state")
    private String state;

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查



    public HelicopterPointDTO(String id, String address, String createTime, String createUser, String description, String districtName, String districtNo, String gridId, String gridName, String gridNo, String groupId, String iconFile, String leaderId, String leaderName, String leaderPhone, String name, String picture, String lat, String lng, String streetName, String streetNo, String textColor, String updateTime, String updateUser, String waterSource, String resourceType,String state) {
        this.id = id;
        this.address = address;
        this.createTime = createTime;
        this.createUser = createUser;
        this.description = description;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.gridId = gridId;
        this.gridName = gridName;
        this.gridNo = gridNo;
        this.groupId = groupId;
        this.iconFile = iconFile;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.name = name;
        this.picture = picture;
        this.lat = lat;
        this.lng = lng;
        this.streetName = streetName;
        this.streetNo = streetNo;
        this.textColor = textColor;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.waterSource = waterSource;
        this.resourceType = resourceType;
        this.state = state;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getOtherPic() {
        return otherPic;
    }

    public void setOtherPic(String otherPic) {
        this.otherPic = otherPic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public HelicopterPointDTO() {
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(String districtNo) {
        this.districtNo = districtNo;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }
}

