package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by geyang on 2019/11/21.
 */

@Table(name = "resource")
public class Resource implements Serializable{
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    /**
     *  接口地址 ,
     */
    @Column(name = "apiurl ")
    private String apiUrl ;

    /**
     *  资源代码
     */
    @Column(name = "code")
    private String code;

    /**
     *  创建时间
     */
    @Column(name = "createtime")
    private String createTime;

    /**
     *  创建人 ,
     */
    @Column(name = "createuser")
    private String createUser;

    /**
     * 描述 ,
     */
    @Column(name = "description")
    private String description;

    /**
     *
     */
    @Column(name = "groupid")
    private String groupId;

    /**
     *   图标 ,
     */
    @Column(name = "iconfile")
    private String iconFile;

    /**
     *  是否在地图上显示 ,  状态是1的显示
     */
    @Column(name = "isdisplay")
    private String isDisplay;

    /**
     *  资源名称 ,
     */
    @Column(name = "name")
    private String name;

    /**
     * 状态 "DELETE";  "ACTIVE";
     */
    @Column(name = "state")
    private String state;

    /**
     *  文本颜色 ,
     */
    @Column(name = "textcolor")
    private String textColor;

    /**
     * 资源总数量 ,
     */
    @Column(name = "totalcount")
    private int totalCount;

    /**
     * 停用数量 ,
     */
    @Column(name = "unusecount")
    private int unuseCount;

    /**
     *  更新时间 ,
     */
    @Column(name = "updatetime")
    private String updateTime;

    /**
     * 更新人 ,
     */
    @Column(name = "updateuser")
    private String updateUser;

    /**
     * 在用数量
     */
    @Column(name = "usecount")
    private int useCount;

    /**
     * 在用数量
     */
    @Column(name = "ischoose")
    private boolean ischoose;

    public Resource() {
    }

    public Resource(String id, String apiUrl, String code, String createTime, String createUser, String description, String groupId, String iconFile, String isDisplay, String name, String state, String textColor, int totalCount, int unuseCount, String updateTime, String updateUser, int useCount) {
        this.id = id;
        this.apiUrl = apiUrl;
        this.code = code;
        this.createTime = createTime;
        this.createUser = createUser;
        this.description = description;
        this.groupId = groupId;
        this.iconFile = iconFile;
        this.isDisplay = isDisplay;
        this.name = name;
        this.state = state;
        this.textColor = textColor;
        this.totalCount = totalCount;
        this.unuseCount = unuseCount;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.useCount = useCount;
        this.ischoose = false;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getUnuseCount() {
        return unuseCount;
    }

    public void setUnuseCount(int unuseCount) {
        this.unuseCount = unuseCount;
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

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
}
