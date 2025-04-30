package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/4.
 */

public class MonitorDTO {

    private String id;
    private String resourceType;

    private String description;


    private String monitorNo;

    private String manufacturer;
    private String leaderName;

    private String leaderPhone;
    private Positon position;

    /**
     * 是否在线
     */
    private String isOnline;

    /**
     * 监控面积
     */
    private Float monitorArea;
    /**
     * 地点名称
     */
    private String placeName;
    /**
     * 扫面区域
     */
    private String sweepArea;
    /**
     * 海拔
     */
    private Float altitude;
    /**
     * 塔高
     */
    private Float towerHeight;
    /**
     * 正北修正
     */
    private Float northCorrection;
    /**
     * 水平修正
     */
    private Float horizontalCorrection;
    /**
     * 可视距离
     */
    private Float visualRange;
    /**
     * 可视时长（小时）
     */
    private Float visualTime;
    /**
     * 水平夹角
     */
    private Float horizontalAngle;
    /**
     * 垂直夹角
     */
    private Float verticalAngle;
    /**
     * 备注
     */
    private String remark;

    private Integer irAlarmValue;

    private String deptId;

    private Integer type;
    private Float monitorRange;
    private Integer isNetworking;
    private Integer isIntelligentEntry;

    private String otherPic;

    /**
     * 检查状态：0未检查 1检查  默认状态0
     */
    private Integer checkState;

    private Integer monitorType;
    /**
     * 省代码
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市代码
     */
    private String cityCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 区县名称
     */
    private String countyName;
    /**
     * 区县代码
     */
    private String countyCode;
    /**
     * 街道名称
     */
    private String townName;
    /**
     * 街道代码
     */
    private String townCode;
    /**
     * 村名称
     */
    private String villageName;
    /**
     * 村代码
     */
    private String villageCode;

    public MonitorDTO() {
    }

    public MonitorDTO(String id, String resourceType, String description, String monitorNo, String manufacturer, String leaderName, String leaderPhone, Positon position, String isOnline, Float monitorArea, String placeName, String sweepArea, Float altitude, Float towerHeight, Float northCorrection, Float horizontalCorrection, Float visualRange, Float visualTime, Float horizontalAngle, Float verticalAngle, String remark, Integer irAlarmValue, String deptId, Integer type, Float monitorRange, Integer isNetworking, Integer isIntelligentEntry, String otherPic, Integer checkState, Integer monitorType, String provinceCode, String provinceName, String cityCode, String cityName, String countyName, String countyCode, String townName, String townCode, String villageName, String villageCode) {
        this.id = id;
        this.resourceType = resourceType;
        this.description = description;
        this.monitorNo = monitorNo;
        this.manufacturer = manufacturer;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.position = position;
        this.isOnline = isOnline;
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
        this.irAlarmValue = irAlarmValue;
        this.deptId = deptId;
        this.type = type;
        this.monitorRange = monitorRange;
        this.isNetworking = isNetworking;
        this.isIntelligentEntry = isIntelligentEntry;
        this.otherPic = otherPic;
        this.checkState = checkState;
        this.monitorType = monitorType;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.countyName = countyName;
        this.countyCode = countyCode;
        this.townName = townName;
        this.townCode = townCode;
        this.villageName = villageName;
        this.villageCode = villageCode;
    }

    public Positon getPosition() {
        return position;
    }

    public void setPosition(Positon position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonitorNo() {
        return monitorNo;
    }

    public void setMonitorNo(String monitorNo) {
        this.monitorNo = monitorNo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public Float getMonitorArea() {
        return monitorArea;
    }

    public void setMonitorArea(Float monitorArea) {
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

    public Float getAltitude() {
        return altitude;
    }

    public void setAltitude(Float altitude) {
        this.altitude = altitude;
    }

    public Float getTowerHeight() {
        return towerHeight;
    }

    public void setTowerHeight(Float towerHeight) {
        this.towerHeight = towerHeight;
    }

    public Float getNorthCorrection() {
        return northCorrection;
    }

    public void setNorthCorrection(Float northCorrection) {
        this.northCorrection = northCorrection;
    }

    public Float getHorizontalCorrection() {
        return horizontalCorrection;
    }

    public void setHorizontalCorrection(Float horizontalCorrection) {
        this.horizontalCorrection = horizontalCorrection;
    }

    public Float getVisualRange() {
        return visualRange;
    }

    public void setVisualRange(Float visualRange) {
        this.visualRange = visualRange;
    }

    public Float getVisualTime() {
        return visualTime;
    }

    public void setVisualTime(Float visualTime) {
        this.visualTime = visualTime;
    }

    public Float getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(Float horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    public Float getVerticalAngle() {
        return verticalAngle;
    }

    public void setVerticalAngle(Float verticalAngle) {
        this.verticalAngle = verticalAngle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIrAlarmValue() {
        return irAlarmValue;
    }

    public void setIrAlarmValue(Integer irAlarmValue) {
        this.irAlarmValue = irAlarmValue;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getMonitorRange() {
        return monitorRange;
    }

    public void setMonitorRange(Float monitorRange) {
        this.monitorRange = monitorRange;
    }

    public Integer getIsNetworking() {
        return isNetworking;
    }

    public void setIsNetworking(Integer isNetworking) {
        this.isNetworking = isNetworking;
    }

    public Integer getIsIntelligentEntry() {
        return isIntelligentEntry;
    }

    public void setIsIntelligentEntry(Integer isIntelligentEntry) {
        this.isIntelligentEntry = isIntelligentEntry;
    }

    public String getOtherPic() {
        return otherPic;
    }

    public void setOtherPic(String otherPic) {
        this.otherPic = otherPic;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public Integer getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }
}
