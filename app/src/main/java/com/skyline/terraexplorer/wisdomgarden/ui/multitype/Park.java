package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

/**
 * Created by geyang on 2020/10/23.
 */
public class Park {
    public String id;
    public String name;
    public String position;
    public String address;
    public String area;
    public String finishTime;
    public String ownership;
    public String maintenanceUnit;
    public String maintenanceContent;
    public String people;
    public String phone;
    public String type;
    public String groupId;
    public String videoFile;
    public String imageFile;

    public Park() {
    }

    public Park(String id, String name, String position, String address, String area, String finishTime, String ownership, String maintenanceUnit, String maintenanceContent, String people, String phone, String type, String groupId, String videoFile, String imageFile) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.address = address;
        this.area = area;
        this.finishTime = finishTime;
        this.ownership = ownership;
        this.maintenanceUnit = maintenanceUnit;
        this.maintenanceContent = maintenanceContent;
        this.people = people;
        this.phone = phone;
        this.type = type;
        this.groupId = groupId;
        this.videoFile = videoFile;
        this.imageFile = imageFile;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getMaintenanceUnit() {
        return maintenanceUnit;
    }

    public void setMaintenanceUnit(String maintenanceUnit) {
        this.maintenanceUnit = maintenanceUnit;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}