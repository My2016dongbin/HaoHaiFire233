package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/21.
 *
 * 【资源】-护林检查站
 */
@Table(name = "checkstationdto")
public class CheckStationDTO {
    /**
     * 主键
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "groupid")
    private String groupId;
    /**
     * 检查站名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 类型，1检查站，2护林房，3管护站
     */
    @Column(name = "type")
    private String type;
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
     * 所属网格ID
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
     * 负责人姓名
     */
    @Column(name = "leadername")
    private String leaderName;
    /**
     * 负责人电话
     */
    @Column(name = "leaderphone")
    private String leaderPhone;
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 文本颜色
     */
    @Column(name = "textcolor")
    private String textColor;
    /**
     * 图标
     */
    @Column(name = "iconfile")
    private String iconFile;
    /**
     * 照片
     */
    @Column(name = "picture")
    private String picture;

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
     * 资源类型
     */
    @Column(name = "resourcetype")
    private String resourceType;
    /**
     * 人员数量
     */
    @Column(name = "peoplecount")
    private String peopleCount;
    /**
     * 风力灭火器
     */
    @Column(name = "extinguishercount")
    private String extinguisherCount;
    /**
     * 油锯数量
     */
    @Column(name = "sawcount")
    private String sawCount;
    /**
     * 消防车数量
     */
    @Column(name = "truckcount")
    private String truckCount;
    /**
     * 数据快照 ,
     */
    @Column(name = "datasnapshot")
    private String dataSnapshot;

    /**
     * 是否24小时值班，0否，1是 ,
     */
    @Column(name = "isallday")
    private String isAllday;


    /**
     * 所属山系
     */
    @Column(name = "mountain")
    private String mountain;

    /**
     * 人员姓名
     */
    @Column(name = "peoplename")
    private String peopleName;

    /**
     * 灭火水枪数量
     */
    @Column(name = "waterPistolCount")
    private String waterPistolCount;

    /**
     *二号工具数量
     */
    @Column(name = "twotoolcount")
    private String twoToolCount;

    /**
     * 其他工具数量
     */
    @Column(name = "othertoolcount")
    private String otherToolCount;

    /**
     * 是否安装监控，0否，1是
     */
    @Column(name = "hasmonitor")
    private String hasMonitor;

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查


    public CheckStationDTO() {
    }

    public CheckStationDTO(String id, String groupId, String name, String type, String longitude, String latitude, String address, String gridId, String gridNo, String gridName, String districtNo, String districtName, String streetNo, String streetName, String leaderName, String leaderPhone, String description, String textColor, String iconFile, String picture, String state, String createUser, String updateUser, String createTime, String updateTime, String resourceType, String peopleCount, String extinguisherCount, String sawCount, String truckCount, String dataSnapshot, String isAllday, String mountain, String peopleName, String waterPistolCount, String twoToolCount, String otherToolCount, String hasMonitor) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.type = type;
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
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.description = description;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.picture = picture;
        this.state = state;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.resourceType = resourceType;
        this.peopleCount = peopleCount;
        this.extinguisherCount = extinguisherCount;
        this.sawCount = sawCount;
        this.truckCount = truckCount;
        this.dataSnapshot = dataSnapshot;
        this.isAllday = isAllday;
        this.mountain = mountain;
        this.peopleName = peopleName;
        this.waterPistolCount = waterPistolCount;
        this.twoToolCount = twoToolCount;
        this.otherToolCount = otherToolCount;
        this.hasMonitor = hasMonitor;
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

    public String getMountain() {
        return mountain;
    }

    public void setMountain(String mountain) {
        this.mountain = mountain;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getWaterPistolCount() {
        return waterPistolCount;
    }

    public void setWaterPistolCount(String waterPistolCount) {
        this.waterPistolCount = waterPistolCount;
    }

    public String getTwoToolCount() {
        return twoToolCount;
    }

    public void setTwoToolCount(String twoToolCount) {
        this.twoToolCount = twoToolCount;
    }

    public String getOtherToolCount() {
        return otherToolCount;
    }

    public void setOtherToolCount(String otherToolCount) {
        this.otherToolCount = otherToolCount;
    }

    public String getHasMonitor() {
        return hasMonitor;
    }

    public void setHasMonitor(String hasMonitor) {
        this.hasMonitor = hasMonitor;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getExtinguisherCount() {
        return extinguisherCount;
    }

    public void setExtinguisherCount(String extinguisherCount) {
        this.extinguisherCount = extinguisherCount;
    }

    public String getSawCount() {
        return sawCount;
    }

    public void setSawCount(String sawCount) {
        this.sawCount = sawCount;
    }

    public String getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(String truckCount) {
        this.truckCount = truckCount;
    }

    public String getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(String dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public String getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(String isAllday) {
        this.isAllday = isAllday;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

