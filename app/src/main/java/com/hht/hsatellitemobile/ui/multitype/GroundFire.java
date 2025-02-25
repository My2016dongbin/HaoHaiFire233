package com.hht.hsatellitemobile.ui.multitype;

/**
 * Created by geyang on 2019/12/16.
 */
public class GroundFire {

    public String AlarmID;
    public String AlarmLongitude;
    public String AlarmLatitude;
    public String AlarmDateTime;
    public String PicPath1;
    public String PicPath2;
    public String IsDispose;

    public GroundFire(String alarmID, String alarmLongitude, String alarmLatitude, String alarmDateTime, String picPath1, String picPath2, String isDispose) {
        AlarmID = alarmID;
        AlarmLongitude = alarmLongitude;
        AlarmLatitude = alarmLatitude;
        AlarmDateTime = alarmDateTime;
        PicPath1 = picPath1;
        PicPath2 = picPath2;
        IsDispose = isDispose;
    }

    public GroundFire() {
    }

    public String getAlarmID() {
        return AlarmID;
    }

    public void setAlarmID(String alarmID) {
        AlarmID = alarmID;
    }

    public String getAlarmLongitude() {
        return AlarmLongitude;
    }

    public void setAlarmLongitude(String alarmLongitude) {
        AlarmLongitude = alarmLongitude;
    }

    public String getAlarmLatitude() {
        return AlarmLatitude;
    }

    public void setAlarmLatitude(String alarmLatitude) {
        AlarmLatitude = alarmLatitude;
    }

    public String getAlarmDateTime() {
        return AlarmDateTime;
    }

    public void setAlarmDateTime(String alarmDateTime) {
        AlarmDateTime = alarmDateTime;
    }

    public String getPicPath1() {
        return PicPath1;
    }

    public void setPicPath1(String picPath1) {
        PicPath1 = picPath1;
    }

    public String getPicPath2() {
        return PicPath2;
    }

    public void setPicPath2(String picPath2) {
        PicPath2 = picPath2;
    }

    public String getIsDispose() {
        return IsDispose;
    }

    public void setIsDispose(String isDispose) {
        IsDispose = isDispose;
    }
}