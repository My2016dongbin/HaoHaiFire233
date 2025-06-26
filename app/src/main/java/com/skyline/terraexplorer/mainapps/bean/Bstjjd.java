package com.skyline.terraexplorer.mainapps.bean;

public class Bstjjd {

    public String town;
    public int smallClassCount;
    public String county;
    public int treeCount;

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public int getSmallClassCount() {
        return smallClassCount;
    }

    public void setSmallClassCount(int smallClassCount) {
        this.smallClassCount = smallClassCount;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getTreeCount() {
        return treeCount;
    }

    public void setTreeCount(int treeCount) {
        this.treeCount = treeCount;
    }

    public Bstjjd(String town, int smallClassCount, String county, int treeCount) {
        this.town = town;
        this.smallClassCount = smallClassCount;
        this.county = county;
        this.treeCount = treeCount;
    }
}
