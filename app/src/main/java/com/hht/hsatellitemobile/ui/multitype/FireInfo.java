package com.hht.hsatellitemobile.ui.multitype;

/**
 * Created by 13589 on 2019/8/7.
 */
public class FireInfo {

    public String id;
    public String Longitude;   //精度
    public String Latitude;   //维度
    public int observationFrequency;   //观测频次
    public String observationDateTime;   //观测时间
    public int strength;       //
    public int strengthLevel;     //
    public String districtNum;     //
    public double woodland;       //  林地比例
    public double grassland;      // 草原比例
    public double farmland;       // 农田比例
    public double otherland;     // 其他比例
    public double area;           //明火面积
    public double credibility;   //可信度
    public double pixelArea;     //
    public int pixelNumber;   //
    public String country;       //国家
    public String countryCode;   //国家编号
    public String province;      //省份
    public String provinceCode;   //省份编号
    public String city;           //城市
    public String cityCode;       //城市
    public String county;        //区
    public String countyCode;   //区编号
    public String formattedAddress;   //发生地址
    public String visibleLightImageAddress;   //
    public String iRImageAddress;   //
    public String satellite;   // 发现卫星
    public String putStorageTime;   //提交时间
    public String dataSourceFile;   //
    public String fireNo;   //火点编号



    public boolean isShowTime;
    public boolean isShowLine;
    public int fireListType;        //1时间分类  2编号分类

    public FireInfo() {
    }

    public FireInfo(String id, String longitude, String latitude, int observationFrequency, String observationDateTime, int strength, int strengthLevel, double woodland, double grassland, double farmland, double otherland, double area, double credibility, double pixelArea, int pixelNumber, String country, String countryCode, String province, String provinceCode, String city, String cityCode, String county, String countyCode, String formattedAddress, String visibleLightImageAddress, String iRImageAddress, String satellite, String putStorageTime, String dataSourceFile, String fireNo,String districtNum) {
        this.id = id;
        Longitude = longitude;
        Latitude = latitude;
        this.observationFrequency = observationFrequency;
        this.observationDateTime = observationDateTime;
        this.strength = strength;
        this.strengthLevel = strengthLevel;
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
        this.visibleLightImageAddress = visibleLightImageAddress;
        this.iRImageAddress = iRImageAddress;
        this.satellite = satellite;
        this.putStorageTime = putStorageTime;
        this.dataSourceFile = dataSourceFile;
        this.fireNo = fireNo;
        this.fireListType = 1;
        this.districtNum = districtNum;
    }

    public String getDistrictNum() {
        return districtNum;
    }

    public void setDistrictNum(String districtNum) {
        this.districtNum = districtNum;
    }

    public int getFireListType() {
        return fireListType;
    }

    public void setFireListType(int fireListType) {
        this.fireListType = fireListType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public int getObservationFrequency() {
        return observationFrequency;
    }

    public void setObservationFrequency(int observationFrequency) {
        this.observationFrequency = observationFrequency;
    }

    public String getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(String observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
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

    public String getVisibleLightImageAddress() {
        return visibleLightImageAddress;
    }

    public void setVisibleLightImageAddress(String visibleLightImageAddress) {
        this.visibleLightImageAddress = visibleLightImageAddress;
    }

    public String getiRImageAddress() {
        return iRImageAddress;
    }

    public void setiRImageAddress(String iRImageAddress) {
        this.iRImageAddress = iRImageAddress;
    }

    public String getSatellite() {
        return satellite;
    }

    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }

    public String getPutStorageTime() {
        return putStorageTime;
    }

    public void setPutStorageTime(String putStorageTime) {
        this.putStorageTime = putStorageTime;
    }

    public String getDataSourceFile() {
        return dataSourceFile;
    }

    public void setDataSourceFile(String dataSourceFile) {
        this.dataSourceFile = dataSourceFile;
    }

    public String getFireNo() {
        return fireNo;
    }

    public void setFireNo(String fireNo) {
        this.fireNo = fireNo;
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
}