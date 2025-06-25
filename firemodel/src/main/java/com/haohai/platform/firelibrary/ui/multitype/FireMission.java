package com.haohai.platform.firelibrary.ui.multitype;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.haohai.platform.firelibrary.ui.model.FirePosition;

/**
 * Created by geyang on 2020/12/2.
 */
public class FireMission {

    private String groupId;
    /**
     * 主键
     */

    private String id;
    /**
     * 发布人id
     */

    private String userId;
    /**
     * 任务的类型        //2  4  5   2：森林防火4：海域监控5：砂石盗采
     */
    private String taskType;

   // @ApiModelProperty(value = "任务状态，0未开始，1执行中，2已结束")
    private int status;

   // @ApiModelProperty(value = "火警类型")
    private String fireType;

  //  @ApiModelProperty(value = "位置")
    private FirePosition position;


    /**
     * 任务的内容
     */

    private String taskContent;
    /**
     * 任务内容附加的照片
     */
    private String taskImg;
    /**
     * 指派的任务成员 List<任务成员>
     */
    private String taskMember;
    /**
     * 任务开始的时间
     */
    private String taskStartTime;
    /**
     * 任务截至的时间
     */
    private String taskEndTime;
    /**
     * 任务优先级
     */
    private String priority;
    /**
     * 抄送人
     */
    private String ccId;
    /**
     * 工期（可选填）
     */
    private String duration;
    /**
     * 部门id
     */

    private String deptId;

 //   @ApiModelProperty(value = "任务执行人id")
    private String operatorId;

  //  @ApiModelProperty(value = "任务执行人姓名")
    private String operatorName;

 //   @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 储备字段
     */
//    @ApiModelProperty(value = "储备字段")
    private String reserve;

    /**
     * 火警id
     */
  //  @ApiModelProperty(value = "火警id")
    private String fireId;

    /**
     * 任务范围
     */
 //   @ApiModelProperty(value = "任务范围")
    private String taskRegion;

    public FireMission() {
    }


    public FireMission(String groupId, String id, String userId, String taskType, int status, String fireType, FirePosition position, String taskContent, String taskImg, String taskMember, String taskStartTime, String taskEndTime, String priority, String ccId, String duration, String deptId, String operatorId, String operatorName, String description, String reserve, String fireId, String taskRegion) {
        this.groupId = groupId;
        this.id = id;
        this.userId = userId;
        this.taskType = taskType;
        this.status = status;
        this.fireType = fireType;
        this.position = position;
        this.taskContent = taskContent;
        this.taskImg = taskImg;
        this.taskMember = taskMember;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.priority = priority;
        this.ccId = ccId;
        this.duration = duration;
        this.deptId = deptId;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.description = description;
        this.reserve = reserve;
        this.fireId = fireId;
        this.taskRegion = taskRegion;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFireType() {
        return fireType;
    }

    public void setFireType(String fireType) {
        this.fireType = fireType;
    }

    public FirePosition getPosition() {
        return position;
    }

    public void setPosition(FirePosition position) {
        this.position = position;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskImg() {
        return taskImg;
    }

    public void setTaskImg(String taskImg) {
        this.taskImg = taskImg;
    }

    public String getTaskMember() {
        return taskMember;
    }

    public void setTaskMember(String taskMember) {
        this.taskMember = taskMember;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCcId() {
        return ccId;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getFireId() {
        return fireId;
    }

    public void setFireId(String fireId) {
        this.fireId = fireId;
    }

    public String getTaskRegion() {
        return taskRegion;
    }

    public void setTaskRegion(String taskRegion) {
        this.taskRegion = taskRegion;
    }
}