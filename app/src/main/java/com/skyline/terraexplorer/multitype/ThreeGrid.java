package com.skyline.terraexplorer.multitype;

/**
 * Created by geyang on 2020/1/16.
 */
public class ThreeGrid {
    public String id;
    public String text1;
    public String text2;
    public String text3;
    public Double lat;
    public Double lng;
    public boolean hasBottomView;
    public boolean isChoose;
    public double distance;



    public ThreeGrid(String id,String text1, String text2, String text3,Double lat,Double lng, boolean hasBottomView) {
        this.id = id;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.lat = lat;
        this.lng = lng;
        this.hasBottomView = hasBottomView;
        this.isChoose = false;
        this.distance = 0;
    }

    public ThreeGrid(String id, String text1, String text2, String text3, Double lat, Double lng, boolean hasBottomView, boolean isChoose, double distance) {
        this.id = id;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.lat = lat;
        this.lng = lng;
        this.hasBottomView = hasBottomView;
        this.isChoose = isChoose;
        this.distance = distance;
    }

    public ThreeGrid() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ThreeGrid(String text1, String text2, String text3, boolean hasBottomView, boolean isChoose, double distance) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.hasBottomView = hasBottomView;
        this.isChoose = isChoose;
        this.distance = distance;
    }

    public boolean isHasBottomView() {
        return hasBottomView;
    }

    public void setHasBottomView(boolean hasBottomView) {
        this.hasBottomView = hasBottomView;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }
}