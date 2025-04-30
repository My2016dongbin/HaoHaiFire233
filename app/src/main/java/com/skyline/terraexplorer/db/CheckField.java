package com.skyline.terraexplorer.db;

import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by geyang on 2019/11/22.
 */

@Table(name = "checkfield")
public class CheckField implements Serializable {

    @Column(name = "id",isId = true,autoGen = false)
    public String id;

    /**
     * 检查字段 ,
     */
    @Column(name = "code")
    public String code;

    /**
     * 创建时间
     */
    @Column(name = "createtime")
    public String createTime;

    /**
     * 创建人 ,
     */
    @Column(name = "createuser")
    public String createUser;

    /**
     * 描述
     */
    @Column(name = "description")
    public String description;

    /**
     * 字段类型 ,
     */
    @Column(name = "fieldtype")
    public String fieldType;

    /**
     * 组织id ,
     */
    @Column(name = "groupid")
    public String groupId;

    /**
     *检查字段名称 ,
     */
    @Column(name = "name")
    public String name;

    /**
     * 资源类型 ,
     */
    @Column(name = "resourcetype")
    public String resourceType;

    /**
     * 更新时间 ,
     */
    @Column(name = "updatetime")
    public String updateTime;

    /**
     * 更新人
     */
    @Column(name = "updateuser")
    public String updateUser;

    /**
     * 选中状态  0未选中  1选中
     */
    @Column(name = "state")
    public int state;

    public CheckField() {
    }

    public CheckField(String id, String code, String createTime, String createUser, String description, String fieldType, String groupId, String name, String resourceType, String updateTime, String updateUser, int state) {
        this.id = id;
        this.code = code;
        this.createTime = createTime;
        this.createUser = createUser;
        this.description = description;
        this.fieldType = fieldType;
        this.groupId = groupId;
        this.name = name;
        this.resourceType = resourceType;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.state = state;
    }

    public CheckField(String id, String code, String createTime, String createUser, String description, String fieldType, String groupId, String name, String resourceType, String updateTime, String updateUser) {
        this.id = id;
        this.code = code;
        this.createTime = createTime;
        this.createUser = createUser;
        this.description = description;
        this.fieldType = fieldType;
        this.groupId = groupId;
        this.name = name;
        this.resourceType = resourceType;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    @Override
    public String toString() {
        return "CheckField{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", description='" + description + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", groupId='" + groupId + '\'' +
                ", name='" + name + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", state=" + state +
                '}';
    }
}
