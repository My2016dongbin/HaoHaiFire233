package com.haohai.platform.platformmodel.ui.Multitype;

/**
 * Created by geyang on 2020/7/6.
 */
public class FlowApprove {
    public String taskId;
    public String proName;
    public String proAssignee;
    public String proAssigneeNext;
    public String proComment;
    public String proCommentTime;
    public String proMessageCode;
    public String proStart;
    public String proAttribute;

    public FlowApprove() {
    }

    public FlowApprove(String taskId, String proName, String proAssignee, String proAssigneeNext, String proComment, String proCommentTime, String proMessageCode, String proStart, String proAttribute) {
        this.taskId = taskId;
        this.proName = proName;
        this.proAssignee = proAssignee;
        this.proAssigneeNext = proAssigneeNext;
        this.proComment = proComment;
        this.proCommentTime = proCommentTime;
        this.proMessageCode = proMessageCode;
        this.proStart = proStart;
        this.proAttribute = proAttribute;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProAssignee() {
        return proAssignee;
    }

    public void setProAssignee(String proAssignee) {
        this.proAssignee = proAssignee;
    }

    public String getProAssigneeNext() {
        return proAssigneeNext;
    }

    public void setProAssigneeNext(String proAssigneeNext) {
        this.proAssigneeNext = proAssigneeNext;
    }

    public String getProComment() {
        return proComment;
    }

    public void setProComment(String proComment) {
        this.proComment = proComment;
    }

    public String getProCommentTime() {
        return proCommentTime;
    }

    public void setProCommentTime(String proCommentTime) {
        this.proCommentTime = proCommentTime;
    }

    public String getProMessageCode() {
        return proMessageCode;
    }

    public void setProMessageCode(String proMessageCode) {
        this.proMessageCode = proMessageCode;
    }

    public String getProStart() {
        return proStart;
    }

    public void setProStart(String proStart) {
        this.proStart = proStart;
    }

    public String getProAttribute() {
        return proAttribute;
    }

    public void setProAttribute(String proAttribute) {
        this.proAttribute = proAttribute;
    }
}