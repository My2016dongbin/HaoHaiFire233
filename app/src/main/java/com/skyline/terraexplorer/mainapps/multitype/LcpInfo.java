package com.skyline.terraexplorer.mainapps.multitype;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "lcpinfo")
public class LcpInfo {
    @Column(name = "lcpid",isId = true,autoGen = false)
    public String lcpid;
    @Column(name = "annualoutput")
    public double annualOutput;
    @Column(name = "area")
    public double area;
    @Column(name = "kind")
    public String kind;
    @Column(name = "remark")
    public String remark;
    @Column(name = "salesarea")
    public String salesArea;
    @Column(name = "salesway")
    public String salesWay;
    @Column(name = "variety")
    public String variety;
    @Column(name = "isSava")
    public boolean isSava;
    public LcpInfo() {
    }

    public String getLcpid() {
        return lcpid;
    }

    public void setLcpid(String lcpid) {
        this.lcpid = lcpid;
    }

    public boolean isSava() {
        return isSava;
    }

    public void setSava(boolean sava) {
        isSava = sava;
    }

    public double getAnnualOutput() {
        return annualOutput;
    }

    public void setAnnualOutput(double annualOutput) {
        this.annualOutput = annualOutput;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public String getSalesWay() {
        return salesWay;
    }

    public void setSalesWay(String salesWay) {
        this.salesWay = salesWay;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public LcpInfo(String lcpid,double annualOutput, double area, String kind, String remark, String salesArea, String salesWay, String variety) {
        this.isSava=false;
        this.lcpid = lcpid;
        this.annualOutput = annualOutput;
        this.area = area;
        this.kind = kind;
        this.remark = remark;
        this.salesArea = salesArea;
        this.salesWay = salesWay;
        this.variety = variety;
    }
}
