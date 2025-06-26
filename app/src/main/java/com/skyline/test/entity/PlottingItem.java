package com.skyline.test.entity;

import java.util.List;

/**
 * Created by HHT on 2018-01-12.
 */

public class PlottingItem {
    /**
     * 编号
     */
    private  String id;

    /**
     * 对象类型
     */
    private  String type;
    /**
     * 关键点
     */
    private List<PointLatLng> points;
    /**
     * 边缘颜色
     */
    private String lineColor;
    /**
     * 填充颜色
     */
    private String fillColor;

    /**
     * 线的类型
     */
    private String lineStyle;
    /**
     * 名称
     */
    private String name;
    /**
     * 标签内容
     */
    private String lableContent;

    public String getLableContent() {
        return lableContent;
    }

    public void setLableContent(String lableContent) {
        this.lableContent = lableContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PointLatLng> getPoints() {
        return points;
    }

    public void setPoints(List<PointLatLng> points) {
        this.points = points;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
