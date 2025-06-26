package com.skyline.terraexplorer.mainapps.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/1/19.
 */

@Table(name = "grid")
public class Grid {
    /**
     * 主键
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "createuser")
    private String createUser;

    @Column(name = "updateuser")
    private String updateUser;

    @Column(name = "createtime")
    private String createTime;

    @Column(name = "updatetime")
    private String updateTime;

    @Column(name = "resourcetype")
    private String resourceType;

    @Column(name = "groupid")
    private String groupId;

    @Column(name = "parentid")
    private String parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "parentname")
    private String parentName;

    @Column(name = "parentno")
    private String parentNo;

    @Column(name = "gridno")
    private String gridNo;

    @Column(name = "level")
    private String level;

    @Column(name = "position")
    private String position;

/*    @Column(name = "streetno")
    private String streetNo;

    @Column(name = "streetname")
    private String streetName;*/

    @Column(name = "state")
    private String state;

    @Column(name = "remark")
    private String remark;

    public Grid(String id, String createUser, String updateUser, String createTime, String updateTime, String resourceType, String groupId, String parentId, String name, String parentName, String parentNo, String gridNo, String level, String position, String state, String remark) {
        this.id = id;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.resourceType = resourceType;
        this.groupId = groupId;
        this.parentId = parentId;
        this.name = name;
        this.parentName = parentName;
        this.parentNo = parentNo;
        this.gridNo = gridNo;
        this.level = level;
        this.position = position;
        this.state = state;
        this.remark = remark;
    }

    public Grid() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getGroupId() {
        return groupId;
    }



    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
