package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/12.
 */

public class CemeteryDTO {

    private String id;

    private String resourceType;

    /**
     * 编号
     */
    private String no;

    /**
     * 照片
     */
    private String picture;
    /**
     * 文本颜色
     */
    private String textColor;
    /**
     * 图标
     */
    private String iconFile;

    private String dataSnapshot;

    private CemeteryDTO dataSnapshotEntity;
    private String description;
    private String leaderName;
    private String leaderPhone;
    /**
     * 是否在林地内或林缘
     */
    private String isInForeast;
    /**
     * 是否有视频监控
     */
    private String isMonitor;
    /**
     * 监控是否能联网
     */
    private String isMonitorOnline;
    /**
     * 是否能够无火祭祀
     */
    private String isNoFire;
    /**
     * 墓地类型
     */
    private Integer type;
    private Integer graveCount;
    private String otherPic;
    private Positon position;
    /**
     * 检查状态：0未检查 1检查  默认状态0
     */
    private Integer checkState;

    public CemeteryDTO() {
    }

    public CemeteryDTO(String id, String resourceType, String no, String picture, String textColor, String iconFile, String dataSnapshot, CemeteryDTO dataSnapshotEntity, String description, String leaderName, String leaderPhone, String isInForeast, String isMonitor, String isMonitorOnline, String isNoFire, Integer type, Integer graveCount, String otherPic, Positon position, Integer checkState) {
        this.id = id;
        this.resourceType = resourceType;
        this.no = no;
        this.picture = picture;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.dataSnapshot = dataSnapshot;
        this.dataSnapshotEntity = dataSnapshotEntity;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.isInForeast = isInForeast;
        this.isMonitor = isMonitor;
        this.isMonitorOnline = isMonitorOnline;
        this.isNoFire = isNoFire;
        this.type = type;
        this.graveCount = graveCount;
        this.otherPic = otherPic;
        this.position = position;
        this.checkState = checkState;
    }

    public Positon getPosition() {
        return position;
    }

    public void setPosition(Positon position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(String dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public CemeteryDTO getDataSnapshotEntity() {
        return dataSnapshotEntity;
    }

    public void setDataSnapshotEntity(CemeteryDTO dataSnapshotEntity) {
        this.dataSnapshotEntity = dataSnapshotEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getIsInForeast() {
        return isInForeast;
    }

    public void setIsInForeast(String isInForeast) {
        this.isInForeast = isInForeast;
    }

    public String getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(String isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getIsMonitorOnline() {
        return isMonitorOnline;
    }

    public void setIsMonitorOnline(String isMonitorOnline) {
        this.isMonitorOnline = isMonitorOnline;
    }

    public String getIsNoFire() {
        return isNoFire;
    }

    public void setIsNoFire(String isNoFire) {
        this.isNoFire = isNoFire;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGraveCount() {
        return graveCount;
    }

    public void setGraveCount(Integer graveCount) {
        this.graveCount = graveCount;
    }

    public String getOtherPic() {
        return otherPic;
    }

    public void setOtherPic(String otherPic) {
        this.otherPic = otherPic;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }
}
