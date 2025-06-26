package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/21.
 *
 * 【资源】-物资库
 */
@Table(name = "materialrepositorydto")
public class MaterialRepositoryDTO {


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
     * 类型  0市级、1区市级、2街镇级
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
     * 灭火装备数量
     */
    @Column(name = "fireequipmentcount")
    private Integer fireEquipmentCount;
    /**
     * 安全防护装备数量
     */
    @Column(name = "safeequipmentcount")
    private Integer safeEquipmentCount;
    /**
     * 野外生存装备数量
     */
    @Column(name = "outdoorequipmentcount")
    private Integer outdoorEquipmentCount;
    /**
     * 通信设备数量
     */
    @Column(name = "communicateequipmentcount")
    private Integer communicateEquipmentCount;
    /**
     * 防火车辆数量
     */
    @Column(name = "carcount")
    private Integer carCount;
    /**
     * 工程机械数量
     */
    @Column(name = "machinecount")
    private Integer machineCount;

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
    private String updateTime;  //


    @Column(name = "windfirecount")
    private String windFireCount;  //风力灭火机数量

    @Column(name = "sprayfirecount")
    private String sprayFireCount;  //高压细水雾灭火机数量

    @Column(name = "waterpumpcount")
    private String waterPumpCount;  //高压水泵数量

    @Column(name = "twotoolcount")
    private String twoToolCount;  //二号工具数量

    @Column(name = "waterpistolxount")
    private String waterPistolCount;  //灭火水枪数量

    @Column(name = "chainsawcount")
    private String chainSawCount;  //油锯数量

    @Column(name = "bushcuttercount")
    private String bushCutterCount;  //割灌机数量

    @Column(name = "firecuttercount")
    private String fireCutterCount;  //火场切割机数量

    @Column(name = "fireproofclothescount")
    private String fireproofClothesCount;  //防火服数量

    @Column(name = "glovescount")
    private String glovesCount;  //防火手套数量

    @Column(name = "helmetcount")
    private String helmetCount;  //防火头盔数量

    @Column(name = "shoescount")
    private String shoesCount;  //防火鞋数量

    @Column(name = "waterbagcount")
    private String waterBagCount;  //水袋数量

    @Column(name = "watersaccount")
    private String waterSacCount;  //水囊数量

    @Column(name = "oildrumcount")
    private String oilDrumCount;  //油桶数量



    public MaterialRepositoryDTO() {
    }

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

    @Column(name = "otherpic")
    private String otherPic;    //    其他照片

    /**
     * 资源点校验状态
     */
    @Column(name = "checkstate")
    private String checkState;    //   0未检查 1检查




    public MaterialRepositoryDTO(String id, String groupId, String name, String type, String no, String longitude, String latitude, String address, String gridId, String gridNo, String gridName, Integer fireEquipmentCount, Integer safeEquipmentCount, Integer outdoorEquipmentCount, Integer communicateEquipmentCount, Integer carCount, Integer machineCount, String leaderName, String leaderPhone, String picture, String textColor, String iconFile, String description, String state, String createUser, String updateUser, String createTime, String updateTime, String windFireCount, String sprayFireCount, String waterPumpCount, String twoToolCount, String waterPistolCount, String chainSawCount, String bushCutterCount, String fireCutterCount, String fireproofClothesCount, String glovesCount, String helmetCount, String shoesCount, String waterBagCount, String waterSacCount, String oilDrumCount, String resourceType, String districtName, String districtNo, String streetName, String streetNo) {
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
        this.fireEquipmentCount = fireEquipmentCount;
        this.safeEquipmentCount = safeEquipmentCount;
        this.outdoorEquipmentCount = outdoorEquipmentCount;
        this.communicateEquipmentCount = communicateEquipmentCount;
        this.carCount = carCount;
        this.machineCount = machineCount;
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
        this.windFireCount = windFireCount;
        this.sprayFireCount = sprayFireCount;
        this.waterPumpCount = waterPumpCount;
        this.twoToolCount = twoToolCount;
        this.waterPistolCount = waterPistolCount;
        this.chainSawCount = chainSawCount;
        this.bushCutterCount = bushCutterCount;
        this.fireCutterCount = fireCutterCount;
        this.fireproofClothesCount = fireproofClothesCount;
        this.glovesCount = glovesCount;
        this.helmetCount = helmetCount;
        this.shoesCount = shoesCount;
        this.waterBagCount = waterBagCount;
        this.waterSacCount = waterSacCount;
        this.oilDrumCount = oilDrumCount;
        this.resourceType = resourceType;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetName = streetName;
        this.streetNo = streetNo;
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

    public String getWindFireCount() {
        return windFireCount;
    }

    public void setWindFireCount(String windFireCount) {
        this.windFireCount = windFireCount;
    }

    public String getSprayFireCount() {
        return sprayFireCount;
    }

    public void setSprayFireCount(String sprayFireCount) {
        this.sprayFireCount = sprayFireCount;
    }

    public String getWaterPumpCount() {
        return waterPumpCount;
    }

    public void setWaterPumpCount(String waterPumpCount) {
        this.waterPumpCount = waterPumpCount;
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

    public String getChainSawCount() {
        return chainSawCount;
    }

    public void setChainSawCount(String chainSawCount) {
        this.chainSawCount = chainSawCount;
    }

    public String getBushCutterCount() {
        return bushCutterCount;
    }

    public void setBushCutterCount(String bushCutterCount) {
        this.bushCutterCount = bushCutterCount;
    }

    public String getFireCutterCount() {
        return fireCutterCount;
    }

    public void setFireCutterCount(String fireCutterCount) {
        this.fireCutterCount = fireCutterCount;
    }

    public String getFireproofClothesCount() {
        return fireproofClothesCount;
    }

    public void setFireproofClothesCount(String fireproofClothesCount) {
        this.fireproofClothesCount = fireproofClothesCount;
    }

    public String getGlovesCount() {
        return glovesCount;
    }

    public void setGlovesCount(String glovesCount) {
        this.glovesCount = glovesCount;
    }

    public String getHelmetCount() {
        return helmetCount;
    }

    public void setHelmetCount(String helmetCount) {
        this.helmetCount = helmetCount;
    }

    public String getShoesCount() {
        return shoesCount;
    }

    public void setShoesCount(String shoesCount) {
        this.shoesCount = shoesCount;
    }

    public String getWaterBagCount() {
        return waterBagCount;
    }

    public void setWaterBagCount(String waterBagCount) {
        this.waterBagCount = waterBagCount;
    }

    public String getWaterSacCount() {
        return waterSacCount;
    }

    public void setWaterSacCount(String waterSacCount) {
        this.waterSacCount = waterSacCount;
    }

    public String getOilDrumCount() {
        return oilDrumCount;
    }

    public void setOilDrumCount(String oilDrumCount) {
        this.oilDrumCount = oilDrumCount;
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

    public Integer getFireEquipmentCount() {
        return fireEquipmentCount;
    }

    public void setFireEquipmentCount(Integer fireEquipmentCount) {
        this.fireEquipmentCount = fireEquipmentCount;
    }

    public Integer getSafeEquipmentCount() {
        return safeEquipmentCount;
    }

    public void setSafeEquipmentCount(Integer safeEquipmentCount) {
        this.safeEquipmentCount = safeEquipmentCount;
    }

    public Integer getOutdoorEquipmentCount() {
        return outdoorEquipmentCount;
    }

    public void setOutdoorEquipmentCount(Integer outdoorEquipmentCount) {
        this.outdoorEquipmentCount = outdoorEquipmentCount;
    }

    public Integer getCommunicateEquipmentCount() {
        return communicateEquipmentCount;
    }

    public void setCommunicateEquipmentCount(Integer communicateEquipmentCount) {
        this.communicateEquipmentCount = communicateEquipmentCount;
    }

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public Integer getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(Integer machineCount) {
        this.machineCount = machineCount;
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
