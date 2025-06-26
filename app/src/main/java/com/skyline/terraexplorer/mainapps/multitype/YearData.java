package com.skyline.terraexplorer.mainapps.multitype;

/**
 * Created by geyang on 2020/12/8.
 */
public class YearData {
    public String id;
    public String year;
    public boolean isCheck;

    public YearData() {
    }

    public YearData(String id, String year, boolean isCheck) {
        this.id = id;
        this.year = year;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}