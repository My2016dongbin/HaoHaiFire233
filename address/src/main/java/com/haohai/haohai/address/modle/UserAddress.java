package com.haohai.haohai.address.modle;

import com.haohai.haohai.address.db.model.Address;

import java.util.List;

/**
 * Created by geyang on 2019/9/16.
 */

public class UserAddress {
    public String UserId;
    public String UserName;
    public String PhoneNumber;
    public String DeviceNo;
    public List<AddressModel> list;

    public UserAddress(String userId, String userName, String phoneNumber, String deviceNo, List<AddressModel> list) {
        UserId = userId;
        UserName = userName;
        PhoneNumber = phoneNumber;
        DeviceNo = deviceNo;
        this.list = list;
    }

    public UserAddress() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
    }

    public List<AddressModel> getList() {
        return list;
    }

    public void setList(List<AddressModel> list) {
        this.list = list;
    }
}
