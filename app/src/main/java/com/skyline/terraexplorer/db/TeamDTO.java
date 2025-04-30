package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/21.
 *
 * 【资源】-专业队伍
 */

@Table(name = "teamdto")
public class TeamDTO {
    /**
     * 主键
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "groupid")
    private String groupId;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 编号
     */
    @Column(name = "no")
    private String no;
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
     * 位置名称
     */
    @Column(name = "address")
    private String address;


    /**
     * 网格ID
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
     * 类别  0区市级专业队    1 街镇级专业队
     */
    @Column(name = "type")
    private String type;
    /**
     * 人员数量
     */
    @Column(name = "peoplenumber")
    private String peopleNumber;

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
     * 照片
     */
    @Column(name = "picture")
    private String picture;
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
     * 描述
     */
    @Column(name = "description")
    private String description;

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

    @Column(name = "districtname")
    private String districtName;

    @Column(name = "districtno")
    private String districtNo;

    @Column(name = "streetname")
    private String streetname;

    @Column(name = "streetno")
    private String streetNo;

    /**
     * 队伍人数
     */
    @Column(name = "teamcount")
    private String teamCount;

    /**
     * 值班电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 消防车数量
     */
    @Column(name = "truckcount")
    private String truckCount;

    /**
     * 运兵车数量
     */
    @Column(name = "troopcarriercount")
    private String troopCarrierCount;

    /**
     * 指挥车数量
     */
    @Column(name = "commandcarcount")
    private String commandCarCount;

    /**
     * 高压水泵数量
     */
    @Column(name = "waterpumpcount")
    private String waterPumpCount;

    /**
     * 风力灭火机数量
     */
    @Column(name = "windfirexount")
    private String windFireCount;

    /**
     * 二号工具数量
     */
    @Column(name = "twotoolcount")
    private String twoToolCount;

    /**
     * 灭火水枪数量
     */
    @Column(name = "waterpistolcount")
    private String waterPistolCount;

    /**
     * 对讲机数量
     */
    @Column(name = "intercomcount")
    private String intercomCount;

    /**
     * 装备运输车数量
     */
    @Column(name = "equipmenttruckcount")
    private String equipmentTruckCount;
    /**
     * 营房面积
     */
    @Column(name = "barracksmeasure")
    private String barracksMeasure;

    /**
     * 水罐车水量
     */
    @Column(name = "watercarcount")
    private String waterCarCount;

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查


    public TeamDTO() {
    }

    public TeamDTO(String id, String groupId, String name, String no, String longitude, String latitude, String address, String gridId, String gridNo, String gridName, String type, String peopleNumber, String leaderName, String leaderPhone, String picture, String textColor, String iconFile, String description, String state, String createUser, String updateUser, String createTime, String updateTime, String resourceType, String districtName, String districtNo, String streetname, String streetNo, String teamCount, String phone, String truckCount, String troopCarrierCount, String commandCarCount, String waterPumpCount, String windFireCount, String twoToolCount, String waterPistolCount, String intercomCount, String equipmentTruckCount, String barracksMeasure, String waterCarCount) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.no = no;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.gridName = gridName;
        this.type = type;
        this.peopleNumber = peopleNumber;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.picture = picture;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.description = description;
        this.state = state;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.resourceType = resourceType;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetname = streetname;
        this.streetNo = streetNo;
        this.teamCount = teamCount;
        this.phone = phone;
        this.truckCount = truckCount;
        this.troopCarrierCount = troopCarrierCount;
        this.commandCarCount = commandCarCount;
        this.waterPumpCount = waterPumpCount;
        this.windFireCount = windFireCount;
        this.twoToolCount = twoToolCount;
        this.waterPistolCount = waterPistolCount;
        this.intercomCount = intercomCount;
        this.equipmentTruckCount = equipmentTruckCount;
        this.barracksMeasure = barracksMeasure;
        this.waterCarCount = waterCarCount;
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

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(String truckCount) {
        this.truckCount = truckCount;
    }

    public String getTroopCarrierCount() {
        return troopCarrierCount;
    }

    public void setTroopCarrierCount(String troopCarrierCount) {
        this.troopCarrierCount = troopCarrierCount;
    }

    public String getCommandCarCount() {
        return commandCarCount;
    }

    public void setCommandCarCount(String commandCarCount) {
        this.commandCarCount = commandCarCount;
    }

    public String getWaterPumpCount() {
        return waterPumpCount;
    }

    public void setWaterPumpCount(String waterPumpCount) {
        this.waterPumpCount = waterPumpCount;
    }

    public String getWindFireCount() {
        return windFireCount;
    }

    public void setWindFireCount(String windFireCount) {
        this.windFireCount = windFireCount;
    }

    public String getTwoToolCount() {
        return twoToolCount;
    }

    public void setTwoToolCount(String twoToolCount) {
        this.twoToolCount = twoToolCount;
    }

    public String getWaterPistolCount() {
        return waterPistolCount;
    }

    public void setWaterPistolCount(String waterPistolCount) {
        this.waterPistolCount = waterPistolCount;
    }

    public String getIntercomCount() {
        return intercomCount;
    }

    public void setIntercomCount(String intercomCount) {
        this.intercomCount = intercomCount;
    }

    public String getEquipmentTruckCount() {
        return equipmentTruckCount;
    }

    public void setEquipmentTruckCount(String equipmentTruckCount) {
        this.equipmentTruckCount = equipmentTruckCount;
    }

    public String getBarracksMeasure() {
        return barracksMeasure;
    }

    public void setBarracksMeasure(String barracksMeasure) {
        this.barracksMeasure = barracksMeasure;
    }

    public String getWaterCarCount() {
        return waterCarCount;
    }

    public void setWaterCarCount(String waterCarCount) {
        this.waterCarCount = waterCarCount;
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

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
