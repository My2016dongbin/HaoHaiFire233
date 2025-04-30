package com.skyline.terraexplorer.models;

/**
 * Created by geyang on 2020/3/25.
 */

public class CheckDetail {
    public String resourceName;
    public String description;
    public String pic1;
    public String pic2;
    public String pic3;

    public CheckDetail(String resourceName, String description, String pic1, String pic2, String pic3) {
        this.resourceName = resourceName;
        this.description = description;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
    }

    public CheckDetail() {
    }

    public CheckDetail(String resourceName, String description) {
        this.resourceName = resourceName;
        this.description = description;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }
}
