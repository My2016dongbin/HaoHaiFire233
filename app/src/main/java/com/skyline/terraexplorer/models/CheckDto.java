package com.skyline.terraexplorer.models;

/**
 * Created by geyang on 2020/3/25.
 */

public class CheckDto {
    public String name;
    public String description;
    public String dangerDescription;
    public String renovationDescription;
    public String checkUser;
    public String checkTime;
    public String pic1;
    public String pic2;
    public String pic3;
    public String pic4;
    public String pic5;
    public String pic6;
    public String pic7;
    public String pic8;
    public String pic9;
    public String districtName;
    public String districtNo;
    public String streetName;
    public String streetNo;
    public String id;

    public CheckDto(String name, String description, String dangerDescription, String renovationDescription, String checkUser, String checkTime, String pic1, String pic2, String pic3, String pic4, String pic5, String pic6, String pic7, String pic8, String pic9, String districtName, String districtNo, String streetName, String streetNo, String id) {
        this.name = name;
        this.description = description;
        this.dangerDescription = dangerDescription;
        this.renovationDescription = renovationDescription;
        this.checkUser = checkUser;
        this.checkTime = checkTime;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.pic5 = pic5;
        this.pic6 = pic6;
        this.pic7 = pic7;
        this.pic8 = pic8;
        this.pic9 = pic9;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetName = streetName;
        this.streetNo = streetNo;
        this.id = id;
    }

    public CheckDto() {
    }

    public CheckDto(String name, String description, String dangerDescription, String renovationDescription, String checkUser, String checkTime, String districtName, String districtNo, String streetName, String streetNo) {
        this.name = name;
        this.description = description;
        this.dangerDescription = dangerDescription;
        this.renovationDescription = renovationDescription;
        this.checkUser = checkUser;
        this.checkTime = checkTime;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetName = streetName;
        this.streetNo = streetNo;
    }

    public CheckDto(String name, String description, String dangerDescription, String renovationDescription, String checkUser, String checkTime, String pic1, String pic2, String pic3, String pic4, String pic5, String pic6, String pic7, String pic8, String pic9, String districtName, String districtNo, String streetName, String streetNo) {
        this.name = name;
        this.description = description;
        this.dangerDescription = dangerDescription;
        this.renovationDescription = renovationDescription;
        this.checkUser = checkUser;
        this.checkTime = checkTime;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.pic5 = pic5;
        this.pic6 = pic6;
        this.pic7 = pic7;
        this.pic8 = pic8;
        this.pic9 = pic9;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetName = streetName;
        this.streetNo = streetNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDangerDescription() {
        return dangerDescription;
    }

    public void setDangerDescription(String dangerDescription) {
        this.dangerDescription = dangerDescription;
    }

    public String getRenovationDescription() {
        return renovationDescription;
    }

    public void setRenovationDescription(String renovationDescription) {
        this.renovationDescription = renovationDescription;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public String getPic6() {
        return pic6;
    }

    public void setPic6(String pic6) {
        this.pic6 = pic6;
    }

    public String getPic7() {
        return pic7;
    }

    public void setPic7(String pic7) {
        this.pic7 = pic7;
    }

    public String getPic8() {
        return pic8;
    }

    public void setPic8(String pic8) {
        this.pic8 = pic8;
    }

    public String getPic9() {
        return pic9;
    }

    public void setPic9(String pic9) {
        this.pic9 = pic9;
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
}
