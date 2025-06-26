package com.skyline.terraexplorer.multitype;

public class Lcpbar {

    public double area;
    public double annualOutput;
    public String region;

    public Lcpbar(double area, double annualOutput, String region) {
        this.area = area;
        this.annualOutput = annualOutput;
        this.region = region;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getAnnualOutput() {
        return annualOutput;
    }

    public void setAnnualOutput(double annualOutput) {
        this.annualOutput = annualOutput;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
