package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/21.
 *
 * 资源 重大危险源
 */

@Table(name = "dangersourcedto")
public class DangerSourceDTO {

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
     * 类型
     */
    @Column(name = "type")
    private String type;
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
    private String streetName;

    @Column(name = "streetno")
    private String streetNo;


    @Column(name = "ismajorhazard")
    private String isMajorHazard;       //是否重大危险源  0否1 是

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查




    public DangerSourceDTO() {
    }

    public DangerSourceDTO(String id, String groupId, String name, String type, String no, String longitude, String latitude, String address, String gridId, String gridNo, String gridName, String leaderName, String leaderPhone, String picture, String textColor, String iconFile, String description, String state, String createUser, String updateUser, String createTime, String updateTime, String resourceType, String districtName, String districtNo, String streetName, String streetNo, String isMajorHazard) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.type = type;
        this.no = no;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.gridName = gridName;
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
        this.streetName = streetName;
        this.streetNo = streetNo;
        this.isMajorHazard = isMajorHazard;
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

    public String getIsMajorHazard() {
        return isMajorHazard;
    }

    public void setIsMajorHazard(String isMajorHazard) {
        this.isMajorHazard = isMajorHazard;
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
