package com.skyline.test.plan;

/**
 * Created by miao on 2017/5/15 15:14.
 */

public class OnlineInfo1 {

    private String name;
    private String downloadUrl;
    private boolean isDownload;

    public OnlineInfo1(String name, String downloadUrl, boolean isDownload) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.isDownload = isDownload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }



    @Override
    public String toString() {
        return "OnlineInfo1{" +
                "name='" + name + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", isDownload=" + isDownload +
                '}';
    }
}
