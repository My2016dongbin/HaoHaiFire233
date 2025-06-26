package com.skyline.terraexplorer.models;


/**
 * Created by geyang on 2020/4/8.
 */

public class TrackModel {
    public String userName;
    public PositionModel position;

    public TrackModel() {
    }

    public TrackModel(String userName, PositionModel position) {
        this.userName = userName;
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PositionModel getPosition() {
        return position;
    }

    public void setPosition(PositionModel position) {
        this.position = position;
    }
}
