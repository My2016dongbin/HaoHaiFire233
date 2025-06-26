package com.skyline.terraexplorer.models;

import java.io.Serializable;

/**
 * 地图上显示的点位图标
 */
public class ImageLabel  implements Serializable {


    //生成的Label对象的id
    public  String objectId;


    public String name;//资源名称

    //经度
    public double longitude;

    //纬度
    public double latitude;

    //资源点类型    检查站类型，1检查站，2护林房，3管护站
    public String resourceType;

    public String uuId;
    public String description;  //资源描述
    public String leaderName;   //责任人姓名
    public String leaderPhone;  //责任人到卖家
    public String code;         //网格英文
    public String apiUrl;
    public String gridName;
    public String gridId;
    public String gridNo;
    public String wanggeStr;
    public String picture;  //照片
    public String address;
    public String updateTime;


    //检查站新增数据
    public String peopleCount;  //人员数量
    public String extinguisherCount;  //灭火器数量
    public String sawCount;     //油锯数量
    public String truckCount;   //消防车数量
    public String dataSnapshot; //数据快照
    public String isAllday;  // 是否24小时值班，0否，1是 ,
    private String mountain; // 所属山系
    private String peopleName; //人员姓名
    private String waterPistolCount;  //灭火水枪数量
    private String twoToolCount; //二号工具数量
    private String otherToolCount; //其他工具数量
    private String hasMonitor; //是否安装监控，0否，1是


    //水源地
    public String waterCapacity;  // 水源地 蓄水量
    public String isHelicopterWater; //是否直升机取水点，0否，1是


    //专业队
    private String teamCount;  //队伍人数
    private String phone; //值班电话
    private String truckCountTeam;//消防车数量
    private String troopCarrierCount;  //运兵车数量
    private String commandCarCount; //指挥车数量
    private String waterPumpCount; //高压水泵数量
    private String windFireCount; //风力灭火机数量
    private String twoToolCountTeam;; //二号工具数量
    private String waterPistolCountTeam;; //灭火水枪数量
    private String intercomCount; //对讲机数量
    private String equipmentTruckCount; //装备运输车数量
    private String barracksMeasure; //营房面积
    private String waterCarCount; //水罐车水量

    //物资库
    private String windFireCountMR;  //风力灭火机数量
    private String sprayFireCountMR;  //高压细水雾灭火机数量
    private String waterPumpCountMR;  //高压水泵数量
    private String twoToolCountMR;  //二号工具数量
    private String waterPistolCountMR;  //灭火水枪数量
    private String chainSawCountMR;  //油锯数量
    private String bushCutterCountMR;  //割灌机数量
    private String fireCutterCountMR;  //火场切割机数量
    private String fireproofClothesCountMR;  //防火服数量
    private String glovesCountMR;  //防火手套数量
    private String helmetCountMR;  //防火头盔数量
    private String shoesCountMR;  //防火鞋数量
    private String waterBagCountMR;  //水袋数量
    private String waterSacCountMR;  //水囊数量
    private String oilDrumCountMR;  //油桶数量

    //6、瞭望
    private String watchRange;  //观测范围

    //视频监控点
    private String monitorRange; //监控范围
    private String isNetworking; //是否联网 0否，1是
    private String isIntelligentEntry; //是否智能卡口 0否，1是

    //墓地
    private String graveCount;  //坟头数量


    //危险源
    private String isMajorHazard; //是否重大危险源 0否，1是

    private String otherPic;    //    其他照片
    private String checkState;    //   0未检查 1检查

    public ImageLabel(String objectId, double longitude, double latitude, String name, String resourceType,String uuId) {
        this.objectId = objectId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resourceType = resourceType;
        this.uuId = uuId;
    }

    public ImageLabel(String objectId, double longitude, double latitude, String name, String resourceType,String uuId, String description, String leaderName, String leaderPhone) {
        this.objectId = objectId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resourceType = resourceType;
        this.uuId = uuId;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
    }



    public ImageLabel(String objectId, double longitude, double latitude, String name, String resourceType,String uuId, String description, String leaderName, String leaderPhone,String code,String apiUrl,String gridName,String gridId,String gridNo,String wanggeStr) {
        this.objectId = objectId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resourceType = resourceType;
        this.uuId = uuId;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.code = code;
        this.apiUrl = apiUrl;
        this.gridName = gridName;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.wanggeStr = wanggeStr;
    }
    public ImageLabel(String objectId, double longitude, double latitude, String name, String resourceType,String uuId, String description, String leaderName, String leaderPhone,String code,String apiUrl,String gridName,String gridId,String gridNo,String wanggeStr, String peopleCount, String extinguisherCount, String sawCount, String truckCount, String dataSnapshot, String isAllday) {
        this.objectId = objectId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resourceType = resourceType;
        this.uuId = uuId;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.code = code;
        this.apiUrl = apiUrl;
        this.gridName = gridName;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.wanggeStr = wanggeStr;
        this.peopleCount = peopleCount;
        this.extinguisherCount = extinguisherCount;
        this.sawCount = sawCount;
        this.truckCount = truckCount;
        this.dataSnapshot = dataSnapshot;
        this.isAllday = isAllday;
    }
    public ImageLabel(String address ,String updateTime,String objectId, double longitude, double latitude, String name, String resourceType,String uuId, String description, String leaderName, String leaderPhone,String code,String apiUrl,String gridName,String gridId,String gridNo,String wanggeStr, String peopleCount, String extinguisherCount, String sawCount, String truckCount, String dataSnapshot, String isAllday,
                      String waterCapacity, String picture) {
        this.address = address;
        this.updateTime = updateTime;
        this.objectId = objectId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resourceType = resourceType;
        this.uuId = uuId;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.code = code;
        this.apiUrl = apiUrl;
        this.gridName = gridName;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.wanggeStr = wanggeStr;
        this.peopleCount = peopleCount;
        this.extinguisherCount = extinguisherCount;
        this.sawCount = sawCount;
        this.truckCount = truckCount;
        this.dataSnapshot = dataSnapshot;
        this.isAllday = isAllday;
        this.waterCapacity = waterCapacity;
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

    public String getIsMajorHazard() {
        return isMajorHazard;
    }

    public void setIsMajorHazard(String isMajorHazard) {
        this.isMajorHazard = isMajorHazard;
    }

    public String getGraveCount() {
        return graveCount;
    }

    public void setGraveCount(String graveCount) {
        this.graveCount = graveCount;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    public String getWatchRange() {
        return watchRange;
    }

    public void setWatchRange(String watchRange) {
        this.watchRange = watchRange;
    }

    public String getWindFireCountMR() {
        return windFireCountMR;
    }

    public void setWindFireCountMR(String windFireCountMR) {
        this.windFireCountMR = windFireCountMR;
    }

    public String getSprayFireCountMR() {
        return sprayFireCountMR;
    }

    public void setSprayFireCountMR(String sprayFireCountMR) {
        this.sprayFireCountMR = sprayFireCountMR;
    }

    public String getWaterPumpCountMR() {
        return waterPumpCountMR;
    }

    public void setWaterPumpCountMR(String waterPumpCountMR) {
        this.waterPumpCountMR = waterPumpCountMR;
    }

    public String getTwoToolCountMR() {
        return twoToolCountMR;
    }

    public void setTwoToolCountMR(String twoToolCountMR) {
        this.twoToolCountMR = twoToolCountMR;
    }

    public String getWaterPistolCountMR() {
        return waterPistolCountMR;
    }

    public void setWaterPistolCountMR(String waterPistolCountMR) {
        this.waterPistolCountMR = waterPistolCountMR;
    }

    public String getChainSawCountMR() {
        return chainSawCountMR;
    }

    public void setChainSawCountMR(String chainSawCountMR) {
        this.chainSawCountMR = chainSawCountMR;
    }

    public String getBushCutterCountMR() {
        return bushCutterCountMR;
    }

    public void setBushCutterCountMR(String bushCutterCountMR) {
        this.bushCutterCountMR = bushCutterCountMR;
    }

    public String getFireCutterCountMR() {
        return fireCutterCountMR;
    }

    public void setFireCutterCountMR(String fireCutterCountMR) {
        this.fireCutterCountMR = fireCutterCountMR;
    }

    public String getFireproofClothesCountMR() {
        return fireproofClothesCountMR;
    }

    public void setFireproofClothesCountMR(String fireproofClothesCountMR) {
        this.fireproofClothesCountMR = fireproofClothesCountMR;
    }

    public String getGlovesCountMR() {
        return glovesCountMR;
    }

    public void setGlovesCountMR(String glovesCountMR) {
        this.glovesCountMR = glovesCountMR;
    }

    public String getHelmetCountMR() {
        return helmetCountMR;
    }

    public void setHelmetCountMR(String helmetCountMR) {
        this.helmetCountMR = helmetCountMR;
    }

    public String getShoesCountMR() {
        return shoesCountMR;
    }

    public void setShoesCountMR(String shoesCountMR) {
        this.shoesCountMR = shoesCountMR;
    }

    public String getWaterBagCountMR() {
        return waterBagCountMR;
    }

    public void setWaterBagCountMR(String waterBagCountMR) {
        this.waterBagCountMR = waterBagCountMR;
    }

    public String getWaterSacCountMR() {
        return waterSacCountMR;
    }

    public void setWaterSacCountMR(String waterSacCountMR) {
        this.waterSacCountMR = waterSacCountMR;
    }

    public String getOilDrumCountMR() {
        return oilDrumCountMR;
    }

    public void setOilDrumCountMR(String oilDrumCountMR) {
        this.oilDrumCountMR = oilDrumCountMR;
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

    public String getTruckCountTeam() {
        return truckCountTeam;
    }

    public void setTruckCountTeam(String truckCountTeam) {
        this.truckCountTeam = truckCountTeam;
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

    public String getTwoToolCountTeam() {
        return twoToolCountTeam;
    }

    public void setTwoToolCountTeam(String twoToolCountTeam) {
        this.twoToolCountTeam = twoToolCountTeam;
    }

    public String getWaterPistolCountTeam() {
        return waterPistolCountTeam;
    }

    public void setWaterPistolCountTeam(String waterPistolCountTeam) {
        this.waterPistolCountTeam = waterPistolCountTeam;
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

    public String getIsHelicopterWater() {
        return isHelicopterWater;
    }

    public void setIsHelicopterWater(String isHelicopterWater) {
        this.isHelicopterWater = isHelicopterWater;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(String waterCapacity) {
        this.waterCapacity = waterCapacity;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
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

    public String getWanggeStr() {
        return wanggeStr;
    }

    public void setWanggeStr(String wanggeStr) {
        this.wanggeStr = wanggeStr;
    }

    public ImageLabel(String objId, double lon, double lat, String n){
        objectId=objId;
        longitude=lon;
        latitude=lat;
        name=n;
    }

    @Override
    public String toString() {
        return "ImageLabel{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", resourceType='" + resourceType + '\'' +
                ", uuId='" + uuId + '\'' +
                ", description='" + description + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", leaderPhone='" + leaderPhone + '\'' +
                ", code='" + code + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", gridName='" + gridName + '\'' +
                ", gridId='" + gridId + '\'' +
                ", gridNo='" + gridNo + '\'' +
                ", wanggeStr='" + wanggeStr + '\'' +
                ", picture='" + picture + '\'' +
                ", address='" + address + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", peopleCount='" + peopleCount + '\'' +
                ", extinguisherCount='" + extinguisherCount + '\'' +
                ", sawCount='" + sawCount + '\'' +
                ", truckCount='" + truckCount + '\'' +
                ", dataSnapshot='" + dataSnapshot + '\'' +
                ", isAllday='" + isAllday + '\'' +
                ", mountain='" + mountain + '\'' +
                ", peopleName='" + peopleName + '\'' +
                ", waterPistolCount='" + waterPistolCount + '\'' +
                ", twoToolCount='" + twoToolCount + '\'' +
                ", otherToolCount='" + otherToolCount + '\'' +
                ", hasMonitor='" + hasMonitor + '\'' +
                ", waterCapacity='" + waterCapacity + '\'' +
                ", isHelicopterWater='" + isHelicopterWater + '\'' +
                ", teamCount='" + teamCount + '\'' +
                ", phone='" + phone + '\'' +
                ", truckCountTeam='" + truckCountTeam + '\'' +
                ", troopCarrierCount='" + troopCarrierCount + '\'' +
                ", commandCarCount='" + commandCarCount + '\'' +
                ", waterPumpCount='" + waterPumpCount + '\'' +
                ", windFireCount='" + windFireCount + '\'' +
                ", twoToolCountTeam='" + twoToolCountTeam + '\'' +
                ", waterPistolCountTeam='" + waterPistolCountTeam + '\'' +
                ", intercomCount='" + intercomCount + '\'' +
                ", equipmentTruckCount='" + equipmentTruckCount + '\'' +
                ", barracksMeasure='" + barracksMeasure + '\'' +
                ", waterCarCount='" + waterCarCount + '\'' +
                ", windFireCountMR='" + windFireCountMR + '\'' +
                ", sprayFireCountMR='" + sprayFireCountMR + '\'' +
                ", waterPumpCountMR='" + waterPumpCountMR + '\'' +
                ", twoToolCountMR='" + twoToolCountMR + '\'' +
                ", waterPistolCountMR='" + waterPistolCountMR + '\'' +
                ", chainSawCountMR='" + chainSawCountMR + '\'' +
                ", bushCutterCountMR='" + bushCutterCountMR + '\'' +
                ", fireCutterCountMR='" + fireCutterCountMR + '\'' +
                ", fireproofClothesCountMR='" + fireproofClothesCountMR + '\'' +
                ", glovesCountMR='" + glovesCountMR + '\'' +
                ", helmetCountMR='" + helmetCountMR + '\'' +
                ", shoesCountMR='" + shoesCountMR + '\'' +
                ", waterBagCountMR='" + waterBagCountMR + '\'' +
                ", waterSacCountMR='" + waterSacCountMR + '\'' +
                ", oilDrumCountMR='" + oilDrumCountMR + '\'' +
                ", watchRange='" + watchRange + '\'' +
                ", monitorRange='" + monitorRange + '\'' +
                ", isNetworking='" + isNetworking + '\'' +
                ", isIntelligentEntry='" + isIntelligentEntry + '\'' +
                ", graveCount='" + graveCount + '\'' +
                ", isMajorHazard='" + isMajorHazard + '\'' +
                ", otherPic='" + otherPic + '\'' +
                ", checkState='" + checkState + '\'' +
                '}';
    }
}