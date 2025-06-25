package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/12.
 */

public class WaterSourceDTO {

    private String id;

    //@ApiModelProperty(value = "资源类型")
    private String resourceType;

    /**
     * 类型，1水囊2水罐 3水池 4水坝
     */
    private Integer type;
    /**
     * 编号
     */
    private String no;

    /**
     * 蓄水量
     */
    private String waterCapacity;

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
    private String leaderName;

    private String leaderPhone;
    private String dataSnapshot;
    private String description;
    private WaterSourceDTO dataSnapshotEntity;
    private Integer isHelicopterWater;
    private String otherPic;

    /**
     * 检查状态：0未检查 1检查  默认状态0
     */
    private Integer checkState;

    private Positon position;

    public WaterSourceDTO() {
    }

    public WaterSourceDTO(String id, String resourceType, Integer type, String no, String waterCapacity, String picture, String textColor, String iconFile, String leaderName, String leaderPhone, String dataSnapshot, String description, WaterSourceDTO dataSnapshotEntity, Integer isHelicopterWater, String otherPic, Integer checkState, Positon position) {
        this.id = id;
        this.resourceType = resourceType;
        this.type = type;
        this.no = no;
        this.waterCapacity = waterCapacity;
        this.picture = picture;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.dataSnapshot = dataSnapshot;
        this.description = description;
        this.dataSnapshotEntity = dataSnapshotEntity;
        this.isHelicopterWater = isHelicopterWater;
        this.otherPic = otherPic;
        this.checkState = checkState;
        this.position = position;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(String waterCapacity) {
        this.waterCapacity = waterCapacity;
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

    public String getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(String dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WaterSourceDTO getDataSnapshotEntity() {
        return dataSnapshotEntity;
    }

    public void setDataSnapshotEntity(WaterSourceDTO dataSnapshotEntity) {
        this.dataSnapshotEntity = dataSnapshotEntity;
    }

    public Integer getIsHelicopterWater() {
        return isHelicopterWater;
    }

    public void setIsHelicopterWater(Integer isHelicopterWater) {
        this.isHelicopterWater = isHelicopterWater;
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
