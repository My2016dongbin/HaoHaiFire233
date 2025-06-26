package com.skyline.terraexplorer.models;

import com.skyline.test.entity.PointLatLng;

import java.util.List;

/**
 * Created by geyang on 2019/12/13.
 */

public class Plot {
    public String PlotID;
    public String PlotUserID;
    public int PlotType;  //火苗：0(点)，   火区：1（面），    火线：2（线），  扑火线：3（线），   扑救力量：4，（点）   扑救力量行进方向：5（箭头）
    public String PlotInterTime;
    public String PlotLineColor;
    public String PlotName;
    public List<PointLatLng> pointLatLngList;

    public Plot() {
    }

    public Plot(String plotID, String plotUserID, int plotType, String plotInterTime, String plotLineColor, String plotName, List<PointLatLng> pointLatLngList) {
        PlotID = plotID;
        PlotUserID = plotUserID;
        PlotType = plotType;
        PlotInterTime = plotInterTime;
        PlotLineColor = plotLineColor;
        PlotName = plotName;
        this.pointLatLngList = pointLatLngList;
    }

    public String getPlotID() {
        return PlotID;
    }

    public void setPlotID(String plotID) {
        PlotID = plotID;
    }

    public String getPlotUserID() {
        return PlotUserID;
    }

    public void setPlotUserID(String plotUserID) {
        PlotUserID = plotUserID;
    }

    public int getPlotType() {
        return PlotType;
    }

    public void setPlotType(int plotType) {
        PlotType = plotType;
    }

    public String getPlotInterTime() {
        return PlotInterTime;
    }

    public void setPlotInterTime(String plotInterTime) {
        PlotInterTime = plotInterTime;
    }

    public String getPlotLineColor() {
        return PlotLineColor;
    }

    public void setPlotLineColor(String plotLineColor) {
        PlotLineColor = plotLineColor;
    }

    public String getPlotName() {
        return PlotName;
    }

    public void setPlotName(String plotName) {
        PlotName = plotName;
    }

    public List<PointLatLng> getPointLatLngList() {
        return pointLatLngList;
    }

    public void setPointLatLngList(List<PointLatLng> pointLatLngList) {
        this.pointLatLngList = pointLatLngList;
    }
}
