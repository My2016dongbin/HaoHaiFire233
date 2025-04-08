package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/12.
 */

public class CheckStationDTO {
    private String id;
    private String resourceType;


    /**
     * 类型，1检查站，2护林房，3管护站
     */
    private String type;

    private int isAllday;

    /**
     * 文本颜色
     */
    private String textColor;
    /**
     * 图标
     */
    private String iconFile;
    /**
     * 照片
     */
    private String picture;
    private String description;
    private String leaderName;

    private String leaderPhone;

    private String dataSnapshot;


    private int peopleCount;

    private int extinguisherCount;

    private int sawCount;

    private int truckCount;

    private String mountain;

    private String peopleName;

    private Integer windFireCount;

    private Integer waterPistolCount;

    private Integer twoToolCount;

    private String otherToolCount;

    private Integer hasMonitor;
    private String otherPic;

    /**
     * 检查状态：0未检查 1检查  默认状态0
     */
    private Integer checkState;
    private Positon position;

    public CheckStationDTO() {
    }

    public CheckStationDTO(String id, String resourceType, String type, int isAllday, String textColor, String iconFile, String picture, String description, String leaderName, String leaderPhone, String dataSnapshot, int peopleCount, int extinguisherCount, int sawCount, int truckCount, String mountain, String peopleName, Integer windFireCount, Integer waterPistolCount, Integer twoToolCount, String otherToolCount, Integer hasMonitor, String otherPic, Integer checkState, Positon position) {
        this.id = id;
        this.resourceType = resourceType;
        this.type = type;
        this.isAllday = isAllday;
        this.textColor = textColor;
        this.iconFile = iconFile;
        this.picture = picture;
        this.description = description;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.dataSnapshot = dataSnapshot;
        this.peopleCount = peopleCount;
        this.extinguisherCount = extinguisherCount;
        this.sawCount = sawCount;
        this.truckCount = truckCount;
        this.mountain = mountain;
        this.peopleName = peopleName;
        this.windFireCount = windFireCount;
        this.waterPistolCount = waterPistolCount;
        this.twoToolCount = twoToolCount;
        this.otherToolCount = otherToolCount;
        this.hasMonitor = hasMonitor;
        this.otherPic = otherPic;
        this.checkState = checkState;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(int isAllday) {
        this.isAllday = isAllday;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(String dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }



    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public int getExtinguisherCount() {
        return extinguisherCount;
    }

    public void setExtinguisherCount(int extinguisherCount) {
        this.extinguisherCount = extinguisherCount;
    }

    public int getSawCount() {
        return sawCount;
    }

    public void setSawCount(int sawCount) {
        this.sawCount = sawCount;
    }

    public int getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(int truckCount) {
        this.truckCount = truckCount;
    }

    public String getMountain() {
        return mountain;
    }

    public void setMountain(String mountain) {
        this.mountain = mountain;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public Integer getWindFireCount() {
        return windFireCount;
    }

    public void setWindFireCount(Integer windFireCount) {
        this.windFireCount = windFireCount;
    }

    public Integer getWaterPistolCount() {
        return waterPistolCount;
    }

    public void setWaterPistolCount(Integer waterPistolCount) {
        this.waterPistolCount = waterPistolCount;
    }

    public Integer getTwoToolCount() {
        return twoToolCount;
    }

    public void setTwoToolCount(Integer twoToolCount) {
        this.twoToolCount = twoToolCount;
    }

    public String getOtherToolCount() {
        return otherToolCount;
    }

    public void setOtherToolCount(String otherToolCount) {
        this.otherToolCount = otherToolCount;
    }

    public Integer getHasMonitor() {
        return hasMonitor;
    }

    public void setHasMonitor(Integer hasMonitor) {
        this.hasMonitor = hasMonitor;
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

    public Positon getPosition() {
        return position;
    }

    public void setPosition(Positon position) {
        this.position = position;
    }
}
