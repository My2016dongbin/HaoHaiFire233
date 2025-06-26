package com.skyline.terraexplorer.multitype;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/12/26.
 */
@Table(name = "planorder")
public class PlanOrder {
    @Column(name = "id",isId = true,autoGen = false)
    public String id;

    @Column(name = "createuser")
    public String createUser;

    @Column(name = "updateuser")
    public String updateUser;

    @Column(name = "createtime")
    public String createTime;

    @Column(name = "updatetime")
    public String updateTime;

    @Column(name = "groupid")
    public String groupId;

    @Column(name = "name")
    public String name;

    @Column(name = "status")
    public String status;

    @Column(name = "districtname")
    public String districtName;

    @Column(name = "districtno")
    public String districtNo;

    @Column(name = "streetname")
    public String streetName;

    @Column(name = "streetno")
    public String streetNo;

    @Column(name = "checkstationcount")
    public int checkStationCount;

    @Column(name = "teamcount")
    public int teamCount;

    @Column(name = "repositorycount")
    public int repositoryCount;

    @Column(name = "starttime")
    public String startTime;

    @Column(name = "endtime")
    public String endTime;

    @Column(name = "deadline")
    public String deadline;

    @Column(name = "executeuser")
    public String executeUser;

    @Column(name = "description")
    public String description;

    public PlanOrder() {
    }

    public PlanOrder(String createUser, String updateUser, String createTime, String updateTime, String groupId, String id, String name, String status, String districtName, String districtNo, String streetName, String streetNo, int checkStationCount, int teamCount, int repositoryCount, String startTime, String endTime, String deadline, String executeUser, String description) {
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.groupId = groupId;
        this.id = id;
        this.name = name;
        this.status = status;
        this.districtName = districtName;
        this.districtNo = districtNo;
        this.streetName = streetName;
        this.streetNo = streetNo;
        this.checkStationCount = checkStationCount;
        this.teamCount = teamCount;
        this.repositoryCount = repositoryCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.executeUser = executeUser;
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getCheckStationCount() {
        return checkStationCount;
    }

    public void setCheckStationCount(int checkStationCount) {
        this.checkStationCount = checkStationCount;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(int repositoryCount) {
        this.repositoryCount = repositoryCount;
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

    public String getExecuteUser() {
        return executeUser;
    }

    public void setExecuteUser(String executeUser) {
        this.executeUser = executeUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PlanOrder{" +
                "name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}