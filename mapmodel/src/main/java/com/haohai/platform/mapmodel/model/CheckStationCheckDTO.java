package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2021/3/12.
 */

public class CheckStationCheckDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 资源ID
     */
    private String resourceId;
    /**
     * 网格ID
     */
    private String gridId;
    /**
     * 网格编号
     */
    private String gridNo;
    /**
     * 网格名称
     */
    private String gridName;
    /**
     * 名称
     */
    private String name;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 检查时间
     */
    private String checkTime;
    /**
     * 检查人
     */
    private String checkUser;
    /**
     * 被检查单位
     */
    private String checkedUnit;
    /**
     * 审核人
     */
    private String auditUser;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 是否设立宣传标牌
     */
    private String isPropagandaSign;
    /**
     * 是否宣传标语
     */
    private String isPropagandaSlogan;
    /**
     * 是否张贴禁火通告
     */
    private String isNofireNotice;
    /**
     * 是否悬挂火险预警旗
     */
    private String isFireDangerFlag;
    /**
     * 人员是否在岗
     */
    private String isStaffOnduty;
    /**
     * 是否佩戴袖标
     */
    private String isWearArmband;
    /**
     * 人员车辆是否登记
     */
    private String isPeopleRegister;
    /**
     * 工作制度是否上墙
     */
    private String isInstitutionWall;
    /**
     * 责任网格是否上墙
     */
    private String isGridWall;
    /**
     * 是否吸烟
     */
    private String isSmoke;
    /**
     * 是否有烟头
     */
    private String isButt;
    /**
     * 是否收缴火种
     */
    private String isCollectFire;
    /**
     * 是否清理可燃物
     */
    private String isCombustiblesClear;
    /**
     * 是否保障取暖安全
     */
    private String isWarmSafety;
    /**
     * 装备是否充足
     */
    private String isEquipmentAdequate;
    /**
     * 护林房、检查站是否配备基本的灭火装备
     */
    private String isFireEquipment;
    /**
     * 灭火装备能否使用
     */
    private String isEquipmentUsable;
    /**
     * 护林员是否会正确使用防灭火装备
     */
    private String isPeopleUseEquipment;
    /**
     * 装备是否整齐有序
     */
    private String isEquipmentTidy;
    /**
     * 油料是否安全存放
     */
    private String isOilSafety;
    /**
     * 是否文明祭祀
     */
    private String isCivilizedSacrifice;
    /**
     * 检查人签名
     */
    private String signaturePic;

    private String signaturePic1;

    /**
     * 被检查人签名
     */
    private String checkedSignaturePic;
    /**
     * 图片1
     */
    private String pic1;
    /**
     * 图片2
     */
    private String pic2;
    /**
     * 图片3
     */
    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private String pic7;

    private String pic8;

    private String pic9;

    /**
     * 描述
     */
    private String description;

    private String groupId;

    private String StartTime;

    private String EndTime;
    private Positon position;

    public CheckStationCheckDTO(String id) {
        this.id = id;
    }

    public CheckStationCheckDTO(String id, String resourceId, String gridId, String gridNo, String gridName, String name, String status, String checkTime, String checkUser, String checkedUnit, String auditUser, String auditTime, String isPropagandaSign, String isPropagandaSlogan, String isNofireNotice, String isFireDangerFlag, String isStaffOnduty, String isWearArmband, String isPeopleRegister, String isInstitutionWall, String isGridWall, String isSmoke, String isButt, String isCollectFire, String isCombustiblesClear, String isWarmSafety, String isEquipmentAdequate, String isFireEquipment, String isEquipmentUsable, String isPeopleUseEquipment, String isEquipmentTidy, String isOilSafety, String isCivilizedSacrifice, String signaturePic, String signaturePic1, String checkedSignaturePic, String pic1, String pic2, String pic3, String pic4, String pic5, String pic6, String pic7, String pic8, String pic9, String description, String groupId, String startTime, String endTime, Positon position) {
        this.id = id;
        this.resourceId = resourceId;
        this.gridId = gridId;
        this.gridNo = gridNo;
        this.gridName = gridName;
        this.name = name;
        this.status = status;
        this.checkTime = checkTime;
        this.checkUser = checkUser;
        this.checkedUnit = checkedUnit;
        this.auditUser = auditUser;
        this.auditTime = auditTime;
        this.isPropagandaSign = isPropagandaSign;
        this.isPropagandaSlogan = isPropagandaSlogan;
        this.isNofireNotice = isNofireNotice;
        this.isFireDangerFlag = isFireDangerFlag;
        this.isStaffOnduty = isStaffOnduty;
        this.isWearArmband = isWearArmband;
        this.isPeopleRegister = isPeopleRegister;
        this.isInstitutionWall = isInstitutionWall;
        this.isGridWall = isGridWall;
        this.isSmoke = isSmoke;
        this.isButt = isButt;
        this.isCollectFire = isCollectFire;
        this.isCombustiblesClear = isCombustiblesClear;
        this.isWarmSafety = isWarmSafety;
        this.isEquipmentAdequate = isEquipmentAdequate;
        this.isFireEquipment = isFireEquipment;
        this.isEquipmentUsable = isEquipmentUsable;
        this.isPeopleUseEquipment = isPeopleUseEquipment;
        this.isEquipmentTidy = isEquipmentTidy;
        this.isOilSafety = isOilSafety;
        this.isCivilizedSacrifice = isCivilizedSacrifice;
        this.signaturePic = signaturePic;
        this.signaturePic1 = signaturePic1;
        this.checkedSignaturePic = checkedSignaturePic;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.pic5 = pic5;
        this.pic6 = pic6;
        this.pic7 = pic7;
        this.pic8 = pic8;
        this.pic9 = pic9;
        this.description = description;
        this.groupId = groupId;
        StartTime = startTime;
        EndTime = endTime;
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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
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

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckedUnit() {
        return checkedUnit;
    }

    public void setCheckedUnit(String checkedUnit) {
        this.checkedUnit = checkedUnit;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getIsPropagandaSign() {
        return isPropagandaSign;
    }

    public void setIsPropagandaSign(String isPropagandaSign) {
        this.isPropagandaSign = isPropagandaSign;
    }

    public String getIsPropagandaSlogan() {
        return isPropagandaSlogan;
    }

    public void setIsPropagandaSlogan(String isPropagandaSlogan) {
        this.isPropagandaSlogan = isPropagandaSlogan;
    }

    public String getIsNofireNotice() {
        return isNofireNotice;
    }

    public void setIsNofireNotice(String isNofireNotice) {
        this.isNofireNotice = isNofireNotice;
    }

    public String getIsFireDangerFlag() {
        return isFireDangerFlag;
    }

    public void setIsFireDangerFlag(String isFireDangerFlag) {
        this.isFireDangerFlag = isFireDangerFlag;
    }

    public String getIsStaffOnduty() {
        return isStaffOnduty;
    }

    public void setIsStaffOnduty(String isStaffOnduty) {
        this.isStaffOnduty = isStaffOnduty;
    }

    public String getIsWearArmband() {
        return isWearArmband;
    }

    public void setIsWearArmband(String isWearArmband) {
        this.isWearArmband = isWearArmband;
    }

    public String getIsPeopleRegister() {
        return isPeopleRegister;
    }

    public void setIsPeopleRegister(String isPeopleRegister) {
        this.isPeopleRegister = isPeopleRegister;
    }

    public String getIsInstitutionWall() {
        return isInstitutionWall;
    }

    public void setIsInstitutionWall(String isInstitutionWall) {
        this.isInstitutionWall = isInstitutionWall;
    }

    public String getIsGridWall() {
        return isGridWall;
    }

    public void setIsGridWall(String isGridWall) {
        this.isGridWall = isGridWall;
    }

    public String getIsSmoke() {
        return isSmoke;
    }

    public void setIsSmoke(String isSmoke) {
        this.isSmoke = isSmoke;
    }

    public String getIsButt() {
        return isButt;
    }

    public void setIsButt(String isButt) {
        this.isButt = isButt;
    }

    public String getIsCollectFire() {
        return isCollectFire;
    }

    public void setIsCollectFire(String isCollectFire) {
        this.isCollectFire = isCollectFire;
    }

    public String getIsCombustiblesClear() {
        return isCombustiblesClear;
    }

    public void setIsCombustiblesClear(String isCombustiblesClear) {
        this.isCombustiblesClear = isCombustiblesClear;
    }

    public String getIsWarmSafety() {
        return isWarmSafety;
    }

    public void setIsWarmSafety(String isWarmSafety) {
        this.isWarmSafety = isWarmSafety;
    }

    public String getIsEquipmentAdequate() {
        return isEquipmentAdequate;
    }

    public void setIsEquipmentAdequate(String isEquipmentAdequate) {
        this.isEquipmentAdequate = isEquipmentAdequate;
    }

    public String getIsFireEquipment() {
        return isFireEquipment;
    }

    public void setIsFireEquipment(String isFireEquipment) {
        this.isFireEquipment = isFireEquipment;
    }

    public String getIsEquipmentUsable() {
        return isEquipmentUsable;
    }

    public void setIsEquipmentUsable(String isEquipmentUsable) {
        this.isEquipmentUsable = isEquipmentUsable;
    }

    public String getIsPeopleUseEquipment() {
        return isPeopleUseEquipment;
    }

    public void setIsPeopleUseEquipment(String isPeopleUseEquipment) {
        this.isPeopleUseEquipment = isPeopleUseEquipment;
    }

    public String getIsEquipmentTidy() {
        return isEquipmentTidy;
    }

    public void setIsEquipmentTidy(String isEquipmentTidy) {
        this.isEquipmentTidy = isEquipmentTidy;
    }

    public String getIsOilSafety() {
        return isOilSafety;
    }

    public void setIsOilSafety(String isOilSafety) {
        this.isOilSafety = isOilSafety;
    }

    public String getIsCivilizedSacrifice() {
        return isCivilizedSacrifice;
    }

    public void setIsCivilizedSacrifice(String isCivilizedSacrifice) {
        this.isCivilizedSacrifice = isCivilizedSacrifice;
    }

    public String getSignaturePic() {
        return signaturePic;
    }

    public void setSignaturePic(String signaturePic) {
        this.signaturePic = signaturePic;
    }

    public String getSignaturePic1() {
        return signaturePic1;
    }

    public void setSignaturePic1(String signaturePic1) {
        this.signaturePic1 = signaturePic1;
    }

    public String getCheckedSignaturePic() {
        return checkedSignaturePic;
    }

    public void setCheckedSignaturePic(String checkedSignaturePic) {
        this.checkedSignaturePic = checkedSignaturePic;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public String getPic6() {
        return pic6;
    }

    public void setPic6(String pic6) {
        this.pic6 = pic6;
    }

    public String getPic7() {
        return pic7;
    }

    public void setPic7(String pic7) {
        this.pic7 = pic7;
    }

    public String getPic8() {
        return pic8;
    }

    public void setPic8(String pic8) {
        this.pic8 = pic8;
    }

    public String getPic9() {
        return pic9;
    }

    public void setPic9(String pic9) {
        this.pic9 = pic9;
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

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
