package com.haohai.platform.platformmodel.ui.model;

/**
 * Created by Administrator on 2021/2/5.
 */

public class VideoId {
    private String monitorId;
    private String channelId;
    private String videoId;
    private String videoName;
    private int videoPlayer;

    public VideoId(String monitorId, String channelId, String videoId, String videoName, int videoPlayer) {
        this.monitorId = monitorId;
        this.channelId = channelId;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoPlayer = videoPlayer;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getVideoPlayer() {
        return videoPlayer;
    }

    public void setVideoPlayer(int videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    @Override
    public String toString() {
        return ",monitorId:" +  monitorId +
                ",channelId:" +  channelId +
                ",videoId:" +  videoId +
                ",videoName:" +  videoName +
                ",videoPlayer:" +  videoPlayer;
    }
}
