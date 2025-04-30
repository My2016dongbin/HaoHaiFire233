package com.skyline.terraexplorer.models;

import org.xutils.db.annotation.Column;

/**
 * Created by geyang on 2019/11/23.
 */

public class ResourceModel {
    public String address;
    public String updateTime;
    public String id;
    public String lng;
    public String lat;
    public String name;
    public String description;
    public String leaderName;
    public String leaderPhone;
    public String resourceType;
    public String gridName;
    public String gridNo;
    public String gridId;
    public String wanggeStr;
    public String peopleCount;
    public String extinguisherCount;
    public String sawCount;
    public String truckCount;//消防车数量
    public String dataSnapshot;
    public String isAllday;
    public String picture;


    //检查站
    private String mountain; // 所属山系
    private String peopleName; //人员姓名
    private String waterPistolCount;  //灭火水枪数量
    private String twoToolCount; //二号工具数量
    private String otherToolCount; //其他工具数量
    private String hasMonitor; //是否安装监控，0否，1是

    //水源地
    public String waterCapacity;
    private String isHelicopterWater; //是否直升机取水点，0否，1是

    //专业队
    private String teamCount;  //队伍人数
    private String phone; //值班电话
    private String truckCountTeam;
    private String troopCarrierCount;  //运兵车数量
    private String commandCarCount; //指挥车数量
    private String waterPumpCount; //高压水泵数量
    private String windFireCount; //风力灭火机数量
    private String twoToolCountTeam;; //二号工具数量
    private String waterPistolCountTeam;; //灭火水枪数量
    private String intercomCount; //对讲机数量
    private String equipmentTruckCount; //装备运输车数量
    private String barracksMeasure; //营房面积
    private String waterCarCount; //营房面积

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

    public ResourceModel(String address,String updateTime,String id, String lng, String lat, String name, String description, String leaderName, String leaderPhone, String resourceType) {
        this.id = id;
        this.updateTime = updateTime;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.resourceType = resourceType;
        this.address = address;
    }

    public ResourceModel(String address,String updateTime,String id, String lng, String lat, String name, String description, String leaderName, String leaderPhone, String resourceType, String gridName, String gridNo, String gridId,String wanggeStr,String picture) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.resourceType = resourceType;
        this.gridName = gridName;
        this.gridNo = gridNo;
        this.gridId = gridId;
        this.wanggeStr = wanggeStr;
        this.picture = picture;
        this.address = address;
        this.updateTime = updateTime;
    }
    public ResourceModel(String address,String updateTime,String id, String lng, String lat, String name, String description, String leaderName, String leaderPhone, String resourceType, String gridName, String gridNo, String gridId,String wanggeStr,String picture,String waterCapacity) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.resourceType = resourceType;
        this.gridName = gridName;
        this.gridNo = gridNo;
        this.gridId = gridId;
        this.wanggeStr = wanggeStr;
        this.picture = picture;
        this.waterCapacity = waterCapacity;
        this.address = address;
        this.updateTime = updateTime;
    }

    public ResourceModel(String address,String updateTime,String id, String lng, String lat, String name, String description, String leaderName, String leaderPhone, String resourceType, String gridName, String gridNo, String gridId, String wanggeStr, String peopleCount, String extinguisherCount, String sawCount, String truckCount, String dataSnapshot, String isAllday,String picture) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.resourceType = resourceType;
        this.gridName = gridName;
        this.gridNo = gridNo;
        this.gridId = gridId;
        this.wanggeStr = wanggeStr;
        this.peopleCount = peopleCount;
        this.extinguisherCount = extinguisherCount;
        this.sawCount = sawCount;
        this.truckCount = truckCount;
        this.dataSnapshot = dataSnapshot;
        this.isAllday = isAllday;
        this.picture = picture;
        this.address = address;
        this.updateTime = updateTime;
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

    public String getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(String waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getWanggeStr() {
        return wanggeStr;
    }

    public void setWanggeStr(String wanggeStr) {
        this.wanggeStr = wanggeStr;
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
