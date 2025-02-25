package com.hht.hsatellitemobile.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 13589 on 2019/8/6.
 */


@Table(name = "user")
public class User {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "userid")
    private String userId;        //用户名


    @Column(name = "username")
    private String username;        //用户名

    @Column(name = "password")
    private String password;    //密码  可不要

    @Column(name = "token")
    private String token;       //token

    @Column(name = "companyname")
    private String companyName;       //


    @Column(name = "provincename")
    private String provinceName;       //t

    @Column(name = "provinceno")
    private String provinceNo;       //

    @Column(name = "cityno")
    private String cityNo;       //t

    @Column(name = "cityname")
    private String cityName;       //

    @Column(name = "pushtag")
    private String pushTag;       //

    @Column(name = "countyno")
    private String countyNo;       //t

    @Column(name = "countyname")
    private String countyName;       //

    @Column(name = "tagset")
    private String tagSet;       //

    @Column(name = "islogin")
    private String isLogin; //0未登陆  1已登陆

    @Column(name = "isyuyin")
    private int isyunyin; //0不播放  1播放

    @Column(name = "isjpush")
    private boolean isJpush;

    @Column(name = "jpushstr")
    private String jpushStr;

    public User() {
    }

    public User(int id, String userId, String username, String password, String token, String provinceName, String provinceNo, String cityNo, String cityName, String countyNo, String countyName, String isLogin) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.token = token;
        this.provinceName = provinceName;
        this.provinceNo = provinceNo;
        this.cityNo = cityNo;
        this.cityName = cityName;
        this.countyNo = countyNo;
        this.countyName = countyName;
        this.isLogin = isLogin;
        this.isyunyin = 0;
        this.isJpush = false;
        this.jpushStr = "";
    }

    public String getTagSet() {
        return tagSet;
    }

    public void setTagSet(String tagSet) {
        this.tagSet = tagSet;
    }

    public String getPushTag() {
        return pushTag;
    }

    public void setPushTag(String pushTag) {
        this.pushTag = pushTag;
    }

    public String getJpushStr() {
        return jpushStr;
    }

    public void setJpushStr(String jpushStr) {
        this.jpushStr = jpushStr;
    }

    public boolean isJpush() {
        return isJpush;
    }

    public void setJpush(boolean jpush) {
        isJpush = jpush;
    }

    public int getIsyunyin() {
        return isyunyin;
    }

    public void setIsyunyin(int isyunyin) {
        this.isyunyin = isyunyin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceNo() {
        return provinceNo;
    }

    public void setProvinceNo(String provinceNo) {
        this.provinceNo = provinceNo;
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyNo() {
        return countyNo;
    }

    public void setCountyNo(String countyNo) {
        this.countyNo = countyNo;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }
}
