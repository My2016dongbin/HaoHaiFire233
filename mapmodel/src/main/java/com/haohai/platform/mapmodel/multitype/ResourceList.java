package com.haohai.platform.mapmodel.multitype;

/**
 * Created by Administrator on 2020/12/14.
 */

public class ResourceList {
    private String id;
    private String name;
    private String code;
    private String isDisplay;
    private String totalCount;
    private String useCount;
    private String unuseCount;
    private String apiUrl;
    private String checkApiUrl;
    private String textColor;
    private String iconFile;
    private String description;
    private String groupId;
    private String state;

    private boolean isCheck;

    public ResourceList() {
    }

    public ResourceList(String id, String name, String code, String isDisplay, String totalCount, String useCount, String unuseCount, String apiUrl, String checkApiUrl, String textColor, String iconFile, String description, String groupId, String state, boolean isCheck) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isDisplay = isDisplay;
        this.totalCount = totalCount;
        this.useCount = useCount;
        this.unuseCount = unuseCount;
        this.apiUrl = apiUrl;
        this.checkApiUrl = checkApiUrl;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.description = description;
        this.groupId = groupId;
        this.state = state;
        this.isCheck = isCheck;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    public String getUnuseCount() {
        return unuseCount;
    }

    public void setUnuseCount(String unuseCount) {
        this.unuseCount = unuseCount;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getCheckApiUrl() {
        return checkApiUrl;
    }

    public void setCheckApiUrl(String checkApiUrl) {
        this.checkApiUrl = checkApiUrl;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
