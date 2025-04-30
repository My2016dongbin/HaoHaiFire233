package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by geyang on 2019/11/20.
 */

@Table(name = "user")
public class User {
    /**
     * 账号
     */
    @Column(name = "id",isId = true,autoGen = false)
    private String id;
    /**
     * 用户id
     */
    @Column(name = "usercode")
    private String userCode;
    /**
     * 用户id
     */
    @Column(name = "username")
    private String userName;
    /**
     * 密码
     */
    @Column(name = "userpasswd")
    private String userPasswd;
    /**
     * 显示名称
     */
    @Column(name = "fullname")
    private String fullName;
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 电话
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;
    /**
     * 入职时间
     */
    @Column(name = "entrytime")
    private String entryTime;
    /**
     * 生日
     */
    @Column(name = "birthday")
    private String birthday;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;
    /**
     * 是否组织管理员
     */
    @Column(name = "issuperadmin")
    private String isSuperAdmin;
    /**
     * 备注
     */
    @Column(name = "comment")
    private String comment;
    /**
     * 组织id
     */
    @Column(name = "groupid")
    private String groupId;


    /**
     * bkchar1网格编号
     */
    @Column(name = "gridno")
    private String gridNo;
    /**
     * bkchar2
     */
    @Column(name = "bkchar2")
    private String bkchar2;

    /**
     * 账户余额
     */
    @Column(name = "money")
    private String money;

    /**
     * 账户余额
     */
    @Column(name = "lockmoney")
    private String lockMoney;

    /**
     * 组织名称
     */
    @Column(name = "groupname")
    private String groupName;
    /**
     * 状态
     */
    @Column(name = "state")
    private String state;

    /**
     * //0未登陆  1已登陆
     */
    @Column(name = "islogin")
    private int isLogin;

    @Column(name = "token")
    public String token;

    @Column(name = "isdelete")
    public boolean isDelete;

    @Column(name = "isedit")
    public boolean isEdit;

    @Column(name = "isadd")
    public boolean isAdd;

    @Column(name = "dataischange")
    public boolean dataIsChange;

    public User() {
    }

    public User(String id, String userCode, String userPasswd, String fullName, String email, String phone, String sex, String entryTime, String birthday, String type, String isSuperAdmin, String comment, String groupId, String gridNo, String bkchar2, String money, String lockMoney, String groupName, String state, int isLogin, String token) {
        this.id = id;
        this.userCode = userCode;
        this.userPasswd = userPasswd;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.entryTime = entryTime;
        this.birthday = birthday;
        this.type = type;
        this.isSuperAdmin = isSuperAdmin;
        this.comment = comment;
        this.groupId = groupId;
        this.gridNo = gridNo;
        this.bkchar2 = bkchar2;
        this.money = money;
        this.lockMoney = lockMoney;
        this.groupName = groupName;
        this.state = state;
        this.isLogin = isLogin;
        this.token = token;
    }

    public User(String id, String userCode, String userName, String userPasswd, String fullName, String email, String phone, String sex, String entryTime, String birthday, String type, String isSuperAdmin, String comment, String groupId, String gridNo, String bkchar2, String money, String lockMoney, String groupName, String state, int isLogin, String token) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.userPasswd = userPasswd;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.entryTime = entryTime;
        this.birthday = birthday;
        this.type = type;
        this.isSuperAdmin = isSuperAdmin;
        this.comment = comment;
        this.groupId = groupId;
        this.gridNo = gridNo;
        this.bkchar2 = bkchar2;
        this.money = money;
        this.lockMoney = lockMoney;
        this.groupName = groupName;
        this.state = state;
        this.isLogin = isLogin;
        this.token = token;
    }

    public boolean isDataIsChange() {
        return dataIsChange;
    }

    public void setDataIsChange(boolean dataIsChange) {
        this.dataIsChange = dataIsChange;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(String isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getBkchar2() {
        return bkchar2;
    }

    public void setBkchar2(String bkchar2) {
        this.bkchar2 = bkchar2;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
