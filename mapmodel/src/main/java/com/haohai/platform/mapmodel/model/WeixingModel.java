package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2020/11/20.
 */

public class WeixingModel {
    private String id;
    private String groupId;
    private String longitude;
    private String latitude;
    private String observationFrequency;
    private String observationDatetime;
    private String strength;
    private String strengthLevel;
    private String landType;
    private double woodland;
    private double grassland;
    private double farmland;
    private double otherland;
    private double area;
    private double credibility;
    private double pixelArea;
    private int pixelNumber;
    private String country;
    private String countryCode;
    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;
    private String county;
    private String countyCode;
    private String formattedAddress;
    private String lightImageAddress;
    private String irImageAddress;
    private String satellite;
    private String datasourceFile;
    private String fireNo;
    private String districtNum;
    private String isReal;
    private String isHandle;
    private String isReport;
    private String handleUser;
    private String handlePerson;
    private String isArchives;
    private String handleDescription;
    private String startTime;
    private String endTime;
    private String isMajor;
    private String latelyTime;

    public boolean isShowTime;
    public boolean isShowLine;
    public int fireListType;        //1时间分类  2编号分类


    public WeixingModel() {
    }

    public WeixingModel(String id, String groupId, String longitude, String latitude, String observationFrequency, String observationDatetime, String strength, String strengthLevel, String landType, double woodland, double grassland, double farmland, double otherland, double area, double credibility, double pixelArea, int pixelNumber, String country, String countryCode, String province, String provinceCode, String city, String cityCode, String county, String countyCode, String formattedAddress, String lightImageAddress, String irImageAddress, String satellite, String datasourceFile, String fireNo, String districtNum, String isReal, String isHandle, String isReport, String handleUser, String handlePerson, String isArchives, String handleDescription, String startTime, String endTime, String isMajor, String latelyTime, boolean isShowTime, boolean isShowLine, int fireListType) {
        this.id = id;
        this.groupId = groupId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.observationFrequency = observationFrequency;
        this.observationDatetime = observationDatetime;
        this.strength = strength;
        this.strengthLevel = strengthLevel;
        this.landType = landType;
        this.woodland = woodland;
        this.grassland = grassland;
        this.farmland = farmland;
        this.otherland = otherland;
        this.area = area;
        this.credibility = credibility;
        this.pixelArea = pixelArea;
        this.pixelNumber = pixelNumber;
        this.country = country;
        this.countryCode = countryCode;
        this.province = province;
        this.provinceCode = provinceCode;
        this.city = city;
        this.cityCode = cityCode;
        this.county = county;
        this.countyCode = countyCode;
        this.formattedAddress = formattedAddress;
        this.lightImageAddress = lightImageAddress;
        this.irImageAddress = irImageAddress;
        this.satellite = satellite;
        this.datasourceFile = datasourceFile;
        this.fireNo = fireNo;
        this.districtNum = districtNum;
        this.isReal = isReal;
        this.isHandle = isHandle;
        this.isReport = isReport;
        this.handleUser = handleUser;
        this.handlePerson = handlePerson;
        this.isArchives = isArchives;
        this.handleDescription = handleDescription;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isMajor = isMajor;
        this.latelyTime = latelyTime;
        this.isShowTime = isShowTime;
        this.isShowLine = isShowLine;
        this.fireListType = fireListType;
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

    public String getObservationFrequency() {
        return observationFrequency;
    }

    public void setObservationFrequency(String observationFrequency) {
        this.observationFrequency = observationFrequency;
    }

    public String getObservationDatetime() {
        return observationDatetime;
    }

    public void setObservationDatetime(String observationDatetime) {
        this.observationDatetime = observationDatetime;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(String strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public double getWoodland() {
        return woodland;
    }

    public void setWoodland(double woodland) {
        this.woodland = woodland;
    }

    public double getGrassland() {
        return grassland;
    }

    public void setGrassland(double grassland) {
        this.grassland = grassland;
    }

    public double getFarmland() {
        return farmland;
    }

    public void setFarmland(double farmland) {
        this.farmland = farmland;
    }

    public double getOtherland() {
        return otherland;
    }

    public void setOtherland(double otherland) {
        this.otherland = otherland;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getCredibility() {
        return credibility;
    }

    public void setCredibility(double credibility) {
        this.credibility = credibility;
    }

    public double getPixelArea() {
        return pixelArea;
    }

    public void setPixelArea(double pixelArea) {
        this.pixelArea = pixelArea;
    }

    public int getPixelNumber() {
        return pixelNumber;
    }

    public void setPixelNumber(int pixelNumber) {
        this.pixelNumber = pixelNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getLightImageAddress() {
        return lightImageAddress;
    }

    public void setLightImageAddress(String lightImageAddress) {
        this.lightImageAddress = lightImageAddress;
    }

    public String getIrImageAddress() {
        return irImageAddress;
    }

    public void setIrImageAddress(String irImageAddress) {
        this.irImageAddress = irImageAddress;
    }

    public String getSatellite() {
        return satellite;
    }

    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }

    public String getDatasourceFile() {
        return datasourceFile;
    }

    public void setDatasourceFile(String datasourceFile) {
        this.datasourceFile = datasourceFile;
    }

    public String getFireNo() {
        return fireNo;
    }

    public void setFireNo(String fireNo) {
        this.fireNo = fireNo;
    }

    public String getDistrictNum() {
        return districtNum;
    }

    public void setDistrictNum(String districtNum) {
        this.districtNum = districtNum;
    }

    public String getIsReal() {
        return isReal;
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getIsReport() {
        return isReport;
    }

    public void setIsReport(String isReport) {
        this.isReport = isReport;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public String getIsArchives() {
        return isArchives;
    }

    public void setIsArchives(String isArchives) {
        this.isArchives = isArchives;
    }

    public String getHandleDescription() {
        return handleDescription;
    }

    public void setHandleDescription(String handleDescription) {
        this.handleDescription = handleDescription;
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

    public String getIsMajor() {
        return isMajor;
    }

    public void setIsMajor(String isMajor) {
        this.isMajor = isMajor;
    }

    public String getLatelyTime() {
        return latelyTime;
    }

    public void setLatelyTime(String latelyTime) {
        this.latelyTime = latelyTime;
    }

    public boolean isShowTime() {
        return isShowTime;
    }

    public void setShowTime(boolean showTime) {
        isShowTime = showTime;
    }

    public boolean isShowLine() {
        return isShowLine;
    }

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public int getFireListType() {
        return fireListType;
    }

    public void setFireListType(int fireListType) {
        this.fireListType = fireListType;
    }
}
