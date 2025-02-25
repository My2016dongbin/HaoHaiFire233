package com.haohai.haohai.address.db.model;

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

    @Column(name = "phone")
    private String phone;       //电话

    @Column(name = "ip")
    private String ip;              //上传ip

    @Column(name = "time")          //发送频次  秒
    private String time;

    @Column(name = "islogin")
    private String isLogin; //0未登陆  1已登陆

    @Column(name = "realtime")
    private String realTime; //0 不实时 1实时


    public User() {
    }

    public User(int id, String userId, String username, String password, String token, String phone, String ip, String time, String isLogin, String realTime) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.token = token;
        this.phone = phone;
        this.ip = ip;
        this.time = time;
        this.isLogin = isLogin;
        this.realTime = realTime;
    }


    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
