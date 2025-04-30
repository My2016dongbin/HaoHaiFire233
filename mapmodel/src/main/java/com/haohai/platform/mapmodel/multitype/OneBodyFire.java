package com.haohai.platform.mapmodel.multitype;

/**
 * Created by geyang on 2020/12/3.
 */
public class OneBodyFire {

    private String groupId;
    /**
     * 火点id
     */
    private String id;
    /**
     * 监控点id
     */
    private String monitorId;
    /**
     * 所属网格ID
     */
    private String gridId;
    /**
     * 监控点类别
     */
    private String monitorType;
    /**
     * 水平角度
     */
    private Double horizonAngle;
    /**
     * 垂直角度
     */
    private Double verticalAngle;
    /**
     * 火点编号
     */
    private String fireNo;
    /**
     * 最高热值
     */
    private Double maxHeat;
    /**
     * 热值X
     */
    private Double heatX;
    /**
     * 热值Y
     */
    private Double heatY;

    /**
     * 火警经度
     */
    private Double alarmLongitude;
    /**
     * 火警纬度
     */
    private Double alarmLatitude;
    /**
     * 火警海拔
     */
    private Double alarmAltitude;
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
     * 县代码
     */
    private String countyCode;
    /**
     * 县名称
     */
    private String countyName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 火警时间
     */
    private String alarmDatetime;
    /**
     * 发现方式
     */
    private Integer findType;
    /**
     * 截图1
     */
    private String picPath1;
    /**
     * 截图2
     */
    private String picPath2;
    /**
     * 视频1
     */
    private String videoPath1;
    /**
     * 视频2
     */
    private String videoPath2;
    /**
     * 是否真实火点
     */
    private Integer isReal;
    /**
     * 是否Android上报真实火点
     */
    private Integer isRealAndroid;
    /**
     * 是否定位
     */
    private Integer isLocation;
    /**
     * 是否处理
     */
    private Integer isHandle;
    /**
     * 处理人
     */
    private String handlePerson;
    /**
     * 处理时间
     */
    private String handleTime;
    /**
     * 处理用户
     */
    private String handleUser;
    /**
     * 处理意见
     */
    private String handleOpinions;
    /**
     * 是否生成档案
     */
    private Integer isArchives;

    private String startTime;

    private String endTime;

    private String name;
    private Integer isDelegate;
    private Integer type;

    private Integer isPut;


    /**
     * 原始水平角
     */
    private Double originPanpos;
    /**
     * 原始俯仰角
     */
    private Double originTiltpos;
    /**
     * 是否为手动告警
     */
    private String isManual;
    /**
     * 误报类型（农户烟囱，工厂烟囱，车辆热源，反光，太阳，其他）
     */
    private Integer unrealType;
    /**
     * 村代码
     */
    private String villageCode;
    /**
     * 村名称
     */
    private String villageName;
    /**
     * 街道代码
     */
    private String streetCode;
    /**
     * 街道名称
     */
    private String streetName;

    /**
     * 状态
     */
    private String state;


    /**
     * 真实报警类型
     */
    private Integer trueAlarmType;

    /**
     * 是否真实备注
     */
    private String isTrueNote;

    /**
     * 是否扑灭备注
     */
    private String isPutOutNote;

    /**
     * 手动输入火点经度
     */
    private Double editLongitude;
    /**
     * 手动输入火点纬度
     */
    private Double editLatitude;

    /**
     * 网格员处理时经度
     */
    private Double dealLongitude;
    /**
     * 网格员处理时纬度
     */
    private Double dealLatitude;

    private String urlType;


    public OneBodyFire() {
    }

    public OneBodyFire(String groupId, String id, String monitorId, String gridId, String monitorType, Double horizonAngle, Double verticalAngle, String fireNo, Double maxHeat, Double heatX, Double heatY, Double alarmLongitude, Double alarmLatitude, Double alarmAltitude, String provinceCode, String provinceName, String cityCode, String cityName, String countyCode, String countyName, String address, String alarmDatetime, Integer findType, String picPath1, String picPath2, String videoPath1, String videoPath2, Integer isReal, Integer isRealAndroid, Integer isLocation, Integer isHandle, String handlePerson, String handleTime, String handleUser, String handleOpinions, Integer isArchives, String startTime, String endTime, String name, Integer isDelegate, Integer type, Integer isPut, Double originPanpos, Double originTiltpos, String isManual, Integer unrealType, String villageCode, String villageName, String streetCode, String streetName, String state, Integer trueAlarmType, String isTrueNote, String isPutOutNote, Double editLongitude, Double editLatitude, Double dealLongitude, Double dealLatitude, String urlType) {
        this.groupId = groupId;
        this.id = id;
        this.monitorId = monitorId;
        this.gridId = gridId;
        this.monitorType = monitorType;
        this.horizonAngle = horizonAngle;
        this.verticalAngle = verticalAngle;
        this.fireNo = fireNo;
        this.maxHeat = maxHeat;
        this.heatX = heatX;
        this.heatY = heatY;
        this.alarmLongitude = alarmLongitude;
        this.alarmLatitude = alarmLatitude;
        this.alarmAltitude = alarmAltitude;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.countyCode = countyCode;
        this.countyName = countyName;
        this.address = address;
        this.alarmDatetime = alarmDatetime;
        this.findType = findType;
        this.picPath1 = picPath1;
        this.picPath2 = picPath2;
        this.videoPath1 = videoPath1;
        this.videoPath2 = videoPath2;
        this.isReal = isReal;
        this.isRealAndroid = isRealAndroid;
        this.isLocation = isLocation;
        this.isHandle = isHandle;
        this.handlePerson = handlePerson;
        this.handleTime = handleTime;
        this.handleUser = handleUser;
        this.handleOpinions = handleOpinions;
        this.isArchives = isArchives;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.isDelegate = isDelegate;
        this.type = type;
        this.isPut = isPut;
        this.originPanpos = originPanpos;
        this.originTiltpos = originTiltpos;
        this.isManual = isManual;
        this.unrealType = unrealType;
        this.villageCode = villageCode;
        this.villageName = villageName;
        this.streetCode = streetCode;
        this.streetName = streetName;
        this.state = state;
        this.trueAlarmType = trueAlarmType;
        this.isTrueNote = isTrueNote;
        this.isPutOutNote = isPutOutNote;
        this.editLongitude = editLongitude;
        this.editLatitude = editLatitude;
        this.dealLongitude = dealLongitude;
        this.dealLatitude = dealLatitude;
        this.urlType = urlType;
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

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public Double getHorizonAngle() {
        return horizonAngle;
    }

    public void setHorizonAngle(Double horizonAngle) {
        this.horizonAngle = horizonAngle;
    }

    public Double getVerticalAngle() {
        return verticalAngle;
    }

    public void setVerticalAngle(Double verticalAngle) {
        this.verticalAngle = verticalAngle;
    }

    public String getFireNo() {
        return fireNo;
    }

    public void setFireNo(String fireNo) {
        this.fireNo = fireNo;
    }

    public Double getMaxHeat() {
        return maxHeat;
    }

    public void setMaxHeat(Double maxHeat) {
        this.maxHeat = maxHeat;
    }

    public Double getHeatX() {
        return heatX;
    }

    public void setHeatX(Double heatX) {
        this.heatX = heatX;
    }

    public Double getHeatY() {
        return heatY;
    }

    public void setHeatY(Double heatY) {
        this.heatY = heatY;
    }

    public Double getAlarmLongitude() {
        return alarmLongitude;
    }

    public void setAlarmLongitude(Double alarmLongitude) {
        this.alarmLongitude = alarmLongitude;
    }

    public Double getAlarmLatitude() {
        return alarmLatitude;
    }

    public void setAlarmLatitude(Double alarmLatitude) {
        this.alarmLatitude = alarmLatitude;
    }

    public Double getAlarmAltitude() {
        return alarmAltitude;
    }

    public void setAlarmAltitude(Double alarmAltitude) {
        this.alarmAltitude = alarmAltitude;
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

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlarmDatetime() {
        return alarmDatetime;
    }

    public void setAlarmDatetime(String alarmDatetime) {
        this.alarmDatetime = alarmDatetime;
    }

    public Integer getFindType() {
        return findType;
    }

    public void setFindType(Integer findType) {
        this.findType = findType;
    }

    public String getPicPath1() {
        return picPath1;
    }

    public void setPicPath1(String picPath1) {
        this.picPath1 = picPath1;
    }

    public String getPicPath2() {
        return picPath2;
    }

    public void setPicPath2(String picPath2) {
        this.picPath2 = picPath2;
    }

    public String getVideoPath1() {
        return videoPath1;
    }

    public void setVideoPath1(String videoPath1) {
        this.videoPath1 = videoPath1;
    }

    public String getVideoPath2() {
        return videoPath2;
    }

    public void setVideoPath2(String videoPath2) {
        this.videoPath2 = videoPath2;
    }

    public Integer getIsReal() {
        return isReal;
    }

    public void setIsReal(Integer isReal) {
        this.isReal = isReal;
    }

    public Integer getIsRealAndroid() {
        return isRealAndroid;
    }

    public void setIsRealAndroid(Integer isRealAndroid) {
        this.isRealAndroid = isRealAndroid;
    }

    public Integer getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Integer isLocation) {
        this.isLocation = isLocation;
    }

    public Integer getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(Integer isHandle) {
        this.isHandle = isHandle;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    public String getHandleOpinions() {
        return handleOpinions;
    }

    public void setHandleOpinions(String handleOpinions) {
        this.handleOpinions = handleOpinions;
    }

    public Integer getIsArchives() {
        return isArchives;
    }

    public void setIsArchives(Integer isArchives) {
        this.isArchives = isArchives;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(Integer isDelegate) {
        this.isDelegate = isDelegate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsPut() {
        return isPut;
    }

    public void setIsPut(Integer isPut) {
        this.isPut = isPut;
    }

    public Double getOriginPanpos() {
        return originPanpos;
    }

    public void setOriginPanpos(Double originPanpos) {
        this.originPanpos = originPanpos;
    }

    public Double getOriginTiltpos() {
        return originTiltpos;
    }

    public void setOriginTiltpos(Double originTiltpos) {
        this.originTiltpos = originTiltpos;
    }

    public String getIsManual() {
        return isManual;
    }

    public void setIsManual(String isManual) {
        this.isManual = isManual;
    }

    public Integer getUnrealType() {
        return unrealType;
    }

    public void setUnrealType(Integer unrealType) {
        this.unrealType = unrealType;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getTrueAlarmType() {
        return trueAlarmType;
    }

    public void setTrueAlarmType(Integer trueAlarmType) {
        this.trueAlarmType = trueAlarmType;
    }

    public String getIsTrueNote() {
        return isTrueNote;
    }

    public void setIsTrueNote(String isTrueNote) {
        this.isTrueNote = isTrueNote;
    }

    public String getIsPutOutNote() {
        return isPutOutNote;
    }

    public void setIsPutOutNote(String isPutOutNote) {
        this.isPutOutNote = isPutOutNote;
    }

    public Double getEditLongitude() {
        return editLongitude;
    }

    public void setEditLongitude(Double editLongitude) {
        this.editLongitude = editLongitude;
    }

    public Double getEditLatitude() {
        return editLatitude;
    }

    public void setEditLatitude(Double editLatitude) {
        this.editLatitude = editLatitude;
    }

    public Double getDealLongitude() {
        return dealLongitude;
    }

    public void setDealLongitude(Double dealLongitude) {
        this.dealLongitude = dealLongitude;
    }

    public Double getDealLatitude() {
        return dealLatitude;
    }

    public void setDealLatitude(Double dealLatitude) {
        this.dealLatitude = dealLatitude;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }
}