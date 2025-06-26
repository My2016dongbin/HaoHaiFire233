package com.skyline.terraexplorer.models;

import android.net.Uri;

public class ResourceCheck {
    private String id;
    private String title;
    private Uri uri;
    private int state;//0 未选 1 是  2 否

    public ResourceCheck(String id, String title, Uri uri, int state) {
        this.id = id;
        this.title = title;
        this.uri = uri;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
