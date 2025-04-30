package com.haohai.platform.platformmodel.ui.Multitype;

/**
 * Created by geyang on 2020/6/20.
 */
public class Video {
    public int position;
    public boolean hasVideo;
    public boolean isChoose;
    public String videoUrl;

    public Video(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Video(int position, boolean hasVideo, boolean isChoose, String videoUrl) {
        this.position = position;
        this.hasVideo = hasVideo;
        this.isChoose = isChoose;
        this.videoUrl = videoUrl;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }
}