package com.skyline.terraexplorer.mainapps.multitype;

import java.util.List;

public class YllhBarList {
        public String areaName;
        public double max;
        public double min;
        public double totalAcreages;
        public int totalCount;

    public YllhBarList(String areaName, double max, double min, double totalAcreages, int totalCount, List<ChildrenDataFirejd> childrenData) {
        this.areaName = areaName;
        this.max = max;
        this.min = min;
        this.totalAcreages = totalAcreages;
        this.totalCount = totalCount;
        this.childrenData = childrenData;
    }

    public YllhBarList() {

    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getTotalAcreages() {
        return totalAcreages;
    }

    public void setTotalAcreages(double totalAcreages) {
        this.totalAcreages = totalAcreages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ChildrenDataFirejd> getChildrenData() {
        return childrenData;
    }

    public void setChildrenData(List<ChildrenDataFirejd> childrenData) {
        this.childrenData = childrenData;
    }

    public List<ChildrenDataFirejd> childrenData;
        public static class ChildrenDataFirejd {
            public double sumPineAcreage;
            public double sumSurveyAcreage;

            public ChildrenDataFirejd(double sumPineAcreage, double sumSurveyAcreage, int sumSamplingNum, String eachAreaName, double eachAcreages, int eachCount, int sumPineDieNum, int famousCount, int oldCount) {
                this.sumPineAcreage = sumPineAcreage;
                this.sumSurveyAcreage = sumSurveyAcreage;
                this.sumSamplingNum = sumSamplingNum;
                this.eachAreaName = eachAreaName;
                this.eachAcreages = eachAcreages;
                this.eachCount = eachCount;
                this.sumPineDieNum = sumPineDieNum;
                this.famousCount = famousCount;
                this.oldCount = oldCount;
            }

            public int sumSamplingNum;
            public String eachAreaName;
            public double eachAcreages;
            public int eachCount;
            public int sumPineDieNum;
            public int famousCount;
            public int oldCount;

            public int getOldCount() {
                return oldCount;
            }

            public void setOldCount(int oldCount) {
                this.oldCount = oldCount;
            }

            public int getFamousCount() {
                return famousCount;
            }

            public void setFamousCount(int famousCount) {
                this.famousCount = famousCount;
            }

            public double getSumPineAcreage() {
                return sumPineAcreage;
            }

            public void setSumPineAcreage(double sumPineAcreage) {
                this.sumPineAcreage = sumPineAcreage;
            }

            public double getSumSurveyAcreage() {
                return sumSurveyAcreage;
            }

            public void setSumSurveyAcreage(double sumSurveyAcreage) {
                this.sumSurveyAcreage = sumSurveyAcreage;
            }

            public int getSumSamplingNum() {
                return sumSamplingNum;
            }

            public void setSumSamplingNum(int sumSamplingNum) {
                this.sumSamplingNum = sumSamplingNum;
            }

            public String getEachAreaName() {
                return eachAreaName;
            }

            public void setEachAreaName(String eachAreaName) {
                this.eachAreaName = eachAreaName;
            }

            public double getEachAcreages() {
                return eachAcreages;
            }

            public void setEachAcreages(double eachAcreages) {
                this.eachAcreages = eachAcreages;
            }

            public int getEachCount() {
                return eachCount;
            }

            public void setEachCount(int eachCount) {
                this.eachCount = eachCount;
            }

            public int getSumPineDieNum() {
                return sumPineDieNum;
            }

            public void setSumPineDieNum(int sumPineDieNum) {
                this.sumPineDieNum = sumPineDieNum;
            }
        }
}
