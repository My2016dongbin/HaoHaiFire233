package com.haohai.platform.platformmodel.ui.model;

/**
 * Created by Administrator on 2021/1/13.
 */

public class VideoId {
    private String videoId;
    private String videoName;
    private int videoPlayer;

    public VideoId(String videoId, String videoName, int videoPlayer) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoPlayer = videoPlayer;
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
}
