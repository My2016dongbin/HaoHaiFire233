package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/21.
 *
 * 【资源】-视频监控点
 */

@Table(name = "monitordto")
public class MonitorDTO {
    /**
     * 主键
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "groupid")
    private String groupId;
    /**
     * 监控点名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 监控点编号
     */
    @Column(name = "monitorno")
    private String monitorNo;
    /**
     * 是否在线
     */
    @Column(name = "isonline")
    private String isOnline;
    /**
     * 经度
     */
    @Column(name = "longitude")
    private String longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private String latitude;
    /**
     * 位置地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 网格id
     */
    @Column(name = "gridid")
    private String gridId;
    /**
     * 网格编号
     */
    @Column(name = "gridno")
    private String gridNo;
    /**
     * 网格名称
     */
    @Column(name = "gridname")
    private String gridName;
    /**
     * 区编号
     */
    @Column(name = "districtno")
    private String districtNo;
    /**
     * 区市名称
     */
    @Column(name = "districtname")
    private String districtName;
    /**
     * 街道编号
     */
    @Column(name = "streetno")
    private String streetNo;
    /**
     * 街道名称
     */
    @Column(name = "streetname")
    private String streetName;
    /**
     * 监控面积
     */
    @Column(name = "monitorarea")
    private String monitorArea;
    /**
     * 地点名称
     */
    @Column(name = "placename")
    private String placeName;
    /**
     * 扫面区域
     */
    @Column(name = "sweeparea")
    private String sweepArea;
    /**
     * 海拔
     */
    @Column(name = "altitude")
    private String altitude;
    /**
     * 塔高
     */
    @Column(name = "towerheight")
    private String towerHeight;
    /**
     * 正北修正
     */
    @Column(name = "northcorrection")
    private String northCorrection;
    /**
     * 水平修正
     */
    @Column(name = "horizontalCorrection")
    private String horizontalCorrection;
    /**
     * 可视距离
     */
    @Column(name = "visualrange")
    private String visualRange;
    /**
     * 可视时长（小时）
     */
    @Column(name = "visualtime")
    private String visualTime;
    /**
     * 水平夹角
     */
    @Column(name = "horizontalangle")
    private String horizontalAngle;
    /**
     * 垂直夹角
     */
    @Column(name = "verticalangle")
    private String verticalAngle;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * "DELETE";  "ACTIVE"; 删除状态
     */

    @Column(name = "state")
    private String state;

    /**
     * 创建人
     */
    @Column(name = "createuser")
    private String createUser;
    /**
     * 更新人
     */
    @Column(name = "updateuser")
    private String updateUser;
    /**
     * 创建时间
     */
    @Column(name = "createtime")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "updatetime")
    private String updateTime;


    /**
     * 责任人名称
     */
    @Column(name = "leadername")
    private String leaderName;
    /**
     * 责任人电话
     */
    @Column(name = "leaderphone")
    private String leaderPhone;

    /**
     * 资源类型
     */
    @Column(name = "resourcetype")
    private String resourceType;

    @Column(name = "monitorRange")
    private String monitorRange; //监控范围

    @Column(name = "isNetworking")
    private String isNetworking; //是否联网 0否，1是

    @Column(name = "isIntelligentEntry")
    private String isIntelligentEntry; //是否智能卡口 0否，1是

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    @Column(name = "picture")
    private String picture;

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查


    public MonitorDTO() {
    }

    public MonitorDTO(String id, String groupId, String name, String monitorNo, String isOnline, String longitude, String latitude, String address, String gridId, String gridNo, String gridName, String districtNo, String districtName, String streetNo, String streetName, String monitorArea, String placeName, String sweepArea, String altitude, String towerHeight, String northCorrection, String horizontalCorrection, String visualRange, String visualTime, String horizontalAngle, String verticalAngle, String remark, String state, String createUser, String updateUser, String createTime, String updateTime, String leaderName, String leaderPhone, String resourceType, String monitorRange, String isNetworking, String isIntelligentEntry,String description,String picture) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.monitorNo = monitorNo;
        this.isOnline = isOnline;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.gridName = gridName;
        this.districtNo = districtNo;
        this.districtName = districtName;
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.monitorArea = monitorArea;
        this.placeName = placeName;
        this.sweepArea = sweepArea;
        this.altitude = altitude;
        this.towerHeight = towerHeight;
        this.northCorrection = northCorrection;
        this.horizontalCorrection = horizontalCorrection;
        this.visualRange = visualRange;
        this.visualTime = visualTime;
        this.horizontalAngle = horizontalAngle;
        this.verticalAngle = verticalAngle;
        this.remark = remark;
        this.state = state;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.resourceType = resourceType;
        this.monitorRange = monitorRange;
        this.isNetworking = isNetworking;
        this.isIntelligentEntry = isIntelligentEntry;
        this.description = description;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonitorRange() {
        return monitorRange;
    }

    public void setMonitorRange(String monitorRange) {
        this.monitorRange = monitorRange;
    }

    public String getIsNetworking() {
        return isNetworking;
    }

    public void setIsNetworking(String isNetworking) {
        this.isNetworking = isNetworking;
    }

    public String getIsIntelligentEntry() {
        return isIntelligentEntry;
    }

    public void setIsIntelligentEntry(String isIntelligentEntry) {
        this.isIntelligentEntry = isIntelligentEntry;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonitorNo() {
        return monitorNo;
    }

    public void setMonitorNo(String monitorNo) {
        this.monitorNo = monitorNo;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getMonitorArea() {
        return monitorArea;
    }

    public void setMonitorArea(String monitorArea) {
        this.monitorArea = monitorArea;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getSweepArea() {
        return sweepArea;
    }

    public void setSweepArea(String sweepArea) {
        this.sweepArea = sweepArea;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getTowerHeight() {
        return towerHeight;
    }

    public void setTowerHeight(String towerHeight) {
        this.towerHeight = towerHeight;
    }

    public String getNorthCorrection() {
        return northCorrection;
    }

    public void setNorthCorrection(String northCorrection) {
        this.northCorrection = northCorrection;
    }

    public String getHorizontalCorrection() {
        return horizontalCorrection;
    }

    public void setHorizontalCorrection(String horizontalCorrection) {
        this.horizontalCorrection = horizontalCorrection;
    }

    public String getVisualRange() {
        return visualRange;
    }

    public void setVisualRange(String visualRange) {
        this.visualRange = visualRange;
    }

    public String getVisualTime() {
        return visualTime;
    }

    public void setVisualTime(String visualTime) {
        this.visualTime = visualTime;
    }

    public String getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(String horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    public String getVerticalAngle() {
        return verticalAngle;
    }

    public void setVerticalAngle(String verticalAngle) {
        this.verticalAngle = verticalAngle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
