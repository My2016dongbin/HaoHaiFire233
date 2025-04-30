package com.ruyiruyi.rylibrary.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/7/13.
 *
 * 部门
 */
@Table(name = "department")
public class Department {
    @Column(name = "id",isId = true,autoGen = false)
    public String id;

    @Column(name = "description")
    public String description;  //描述

    @Column(name = "groupid")
    public String groupId;      //组织id

    @Column(name = "leaderid")
    public String leaderId;         //部门经理id

    @Column(name = "leadername")
    public String leaderName;       //部门经理名称

    @Column(name = "name")
    public String name;             //部门名称

    @Column(name = "parentid")
    public String parentId;     //父级id

    @Column(name = "parentname")
    public String parentName;       //父级名称

    @Column(name = "updatetime")
    public String updateTime;

    @Column(name = "updateuser")
    public String updateUser;

    public Department() {
    }

    public Department(String description, String groupId, String id, String leaderId, String leaderName, String name, String parentId, String parentName, String updateTime, String updateUser) {
        this.description = description;
        this.groupId = groupId;
        this.id = id;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
