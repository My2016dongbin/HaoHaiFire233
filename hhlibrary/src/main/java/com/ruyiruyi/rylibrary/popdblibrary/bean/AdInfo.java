package com.ruyiruyi.rylibrary.popdblibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 广告弹窗实体类
 */
public class AdInfo implements Parcelable {

    private String adId = null;
    private String title = null;
    private String description = null;//活动文字介绍
    private boolean canShare;//点击弹窗后内部页面是否可分享
    private boolean canClick;//弹窗是否可点击
    private String url = null;//点击弹窗对应链接
    private String shareUrl = null;//分享链接
    private String activityImg = null;//弹窗轮播图url
    private int activityImgId = -1;//弹窗轮播图资源id

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCanShare() {
        return canShare;
    }

    public void setCanShare(boolean canShare) {
        this.canShare = canShare;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public int getActivityImgId() {
        return activityImgId;
    }

    public void setActivityImgId(int activityImgId) {
        this.activityImgId = activityImgId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.activityImg);
        dest.writeInt(this.activityImgId);
    }

    public AdInfo() {
    }

    protected AdInfo(Parcel in) {
        this.adId = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.activityImg = in.readString();
        this.activityImgId = in.readInt();
    }

    public static final Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
        @Override
        public AdInfo createFromParcel(Parcel source) {
            return new AdInfo(source);
        }

        @Override
        public AdInfo[] newArray(int size) {
            return new AdInfo[size];
        }
    };
}
